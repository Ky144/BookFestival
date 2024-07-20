package hsos.de.swa.authorAdministration.boundary.rest;

import hsos.de.swa.authorAdministration.control.IAuthorManagement;
import hsos.de.swa.authorAdministration.boundary.dto.AuthorDTO;
import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.Collection;
import java.util.logging.Logger;

@Path("/api/v1/author")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private static final Logger LOG = Logger.getLogger(AuthorResource.class.getName());

    @Inject
    IAuthorManagement authorManagement;

    /**
     * Endpoint zum Abrufen aller Autoren.
     * @return Response mit einer möglichen Collection von Autoren
     */
    @GET
    @Retry(maxRetries = 4)
    @RolesAllowed({"author", "admin"})

    @Operation(summary = "Alle Autoren abrufen", description = "Ruft eine Liste aller registrierten Autoren ab")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Erfolgreiche Operation"),
            @APIResponse(responseCode = "404", description = "Keine Autoren gefunden")
    })
    public Response getAuthors() {
        LOG.info("Liste alle Autoren auf");
        Collection<Author> authors = this.authorManagement.getAll();
        if (authors == null) {
            LOG.info("Liste der Autoren ist leer");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOG.info("Erfolgreiches Abrufen der Autorenliste");
        return Response.ok(authors).build();
    }

    /**
     * Endpoint zum Abrufen eines Autors anhand seiner ID.
     * @param id Die ID des Autors, der abgerufen werden soll
     * @return Response mit dem gefundenen Autor oder Status 404, falls nicht gefunden
     */
    @GET
    @Path("/{id}")
    @RolesAllowed({"author", "admin"})
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Operation(summary = "Autor nach ID abrufen", description = "Ruft einen spezifischen Autor anhand seiner ID ab")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Erfolgreiche Operation"),
            @APIResponse(responseCode = "404", description = "Autor nicht gefunden")
    })
    public Response getAuthor(@PathParam("id") long id) {
        LOG.info("Suche nach Autor mit ID: " + id);
        Author author = this.authorManagement.get(id);
        if (author == null) {
            LOG.info("Autor mit ID " + id + " existiert nicht");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOG.info("Erfolgreiches Abrufen des Autors mit ID " + id);
        return Response.ok(author).build();
    }

    /**
     * Endpoint zum Hinzufügen eines neuen Autors.
     * @param authorDTO Die Daten des neuen Autors
     * @return Response mit dem neu hinzugefügten Autor oder Status 400 bei Fehlschlag
     */
    @POST
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Transactional
    @RolesAllowed({"admin"})
    @Operation(summary = "Neuen Autor hinzufügen", description = "Fügt einen neuen Autor zur Datenbank hinzu")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Autor erfolgreich hinzugefügt"),
            @APIResponse(responseCode = "400", description = "Ungültige Eingabe")
    })
    public Response add(AuthorDTO authorDTO) {
        LOG.info("Füge neuen Autor hinzu");
        Author author = this.authorManagement.add(authorDTO);
        if (author == null) {
            LOG.info("Hinzufügen des Autors ist fehlgeschlagen");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        LOG.info("Erfolgreich hinzugefügter Autor mit ID " + author.getId());
        return Response.ok(author).build();
    }

    /**
     * Endpoint zur Bearbeitung eines vorhandenen Autors.
     * @param id Die ID des zu bearbeitenden Autors
     * @param authorDTO Die aktualisierten Daten des Autors
     * @return Response mit dem aktualisierten Autor oder Status 404, falls nicht gefunden
     */
    @PUT
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"admin"})
    @Operation(summary = "Autor bearbeiten", description = "Aktualisiert die Informationen eines bestehenden Autors")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Autor erfolgreich aktualisiert"),
            @APIResponse(responseCode = "404", description = "Autor nicht gefunden")
    })
    public Response edit(@PathParam("id") long id, AuthorDTO authorDTO) {
        LOG.info("Bearbeite Autor mit ID: " + id);
        Author author = this.authorManagement.edit(id, authorDTO);
        if (author == null) {
            LOG.info("Autor mit ID " + id + " existiert nicht");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOG.info("Erfolgreich bearbeiteter Autor mit ID: " + id);
        return Response.ok(AuthorDTO.fromEntity(author)).build();
    }

    /**
     * Endpoint zum Löschen eines Autors anhand seiner ID.
     * @param id Die ID des zu löschenden Autors
     * @return Response mit Status OK bei Erfolg oder Status 404, falls nicht gefunden
     */
    @DELETE
    @Path("/{id}")
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Transactional
    @RolesAllowed({"admin"})
    @Operation(summary = "Autor löschen", description = "Löscht einen Autor aus der Datenbank")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Autor erfolgreich gelöscht"),
            @APIResponse(responseCode = "404", description = "Autor nicht gefunden")
    })
    public Response delete(@PathParam("id") long id) {
        LOG.info("Lösche Autor mit ID: " + id);
        boolean deleted = this.authorManagement.delete(id);
        if (deleted) {
            LOG.info("Erfolgreich gelöschter Autor mit ID: " + id);
            return Response.ok().build();
        }
        LOG.info("Löschen des Autors mit ID " + id + " ist fehlgeschlagen");
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Endpoint zum Abrufen aller Buchmessen, an denen ein Autor teilnimmt.
     * @param authorId Die ID des Autors
     * @return Response mit einer möglichen Collection von Buchmessen oder Status 204, falls keine gefunden
     */
    @GET
    @Path("/authors/{authorId}")
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @RolesAllowed({"admin"})
    @Operation(summary = "Buchmessen eines Autors abrufen", description = "Ruft alle Buchmessen ab, an denen ein bestimmter Autor teilnimmt")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Erfolgreiche Operation"),
            @APIResponse(responseCode = "204", description = "Keine Buchmessen gefunden")
    })

    public Response getBookFairsOfAuthor(@PathParam("authorId") long authorId) {
        LOG.info("Abruf der Buchmessen für Autor mit ID: " + authorId);
        Collection<BookFair> bookFairs = this.authorManagement.getBookFairsOfAuthor(authorId);
        if (bookFairs.isEmpty()) {
            LOG.info("Keine Buchmessen für Autor mit ID: " + authorId + " gefunden");
            return Response.noContent().build();
        }
        LOG.info("Teilnahme an " + bookFairs.size() + " Buchmessen");
        return Response.ok(bookFairs).build();
    }
}
