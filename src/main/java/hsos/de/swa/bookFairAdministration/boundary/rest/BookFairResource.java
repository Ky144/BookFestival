package hsos.de.swa.bookFairAdministration.boundary.rest;

import hsos.de.swa.bookFairAdministration.control.IBookFairManagement;
import hsos.de.swa.bookFairAdministration.boundary.dto.BookFairDTO;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
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
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Collection;
import java.util.logging.Logger;
/**
 * REST-Ressource zur Verwaltung von Buchmessen.
 * Diese Klasse stellt Endpunkte für CRUD-Operationen auf Autor-Entitäten bereit.
 */
@Path("/api/v1/bookFair")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Tag(name = "Buchmesse", description = "Verwaltung von Buchmessen")
public class BookFairResource {
    private static final Logger LOG = Logger.getLogger(BookFairResource.class.getName());
    @Inject
    private IBookFairManagement bookFairManagement;

    /**
     * Ruft alle Buchmessen ab.
     * @return Response mit einer Collection von Buchmessen oder 404, falls keine gefunden wurden.
     */

    @GET
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Transactional
    @Operation(summary = "Alle Buchmessen abrufen", description = "Gibt eine Liste aller Buchmessen zurück")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Erfolgreiche Operation"),
            @APIResponse(responseCode = "404", description = "Keine Buchmessen gefunden")
    })
    @RolesAllowed({"author", "admin"})
    public Response getAll(){
        LOG.info("Gebe alle Buchmessen aus: ");
        Collection<BookFair> bookFairs = this.bookFairManagement.getAllBookFairs();
        if(bookFairs == null){
            LOG.info("Es wurden keine Buchmessen gefunden");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOG.info("Es wurden " + bookFairs.size() + " Buchmessen gefunden");
        return Response.ok(bookFairs).build();
    }
    /**
     * Ruft eine spezifischen Buchmesse anhand seiner ID ab.
     * @param {id} Die ID der abzurufenden Buchmesse
     * @return Response mit der Buchmesse oder 404, falls nicht gefunden
     */
    @GET
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("{id}")
    @Transactional
    @RolesAllowed({"author", "admin"})
    @Operation(summary = "Buchmesse nach ID abrufen", description = "Gibt eine spezifische Buchmesse anhand ihrer ID zurück")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Erfolgreiche Operation"),
            @APIResponse(responseCode = "404", description = "Buchmesse nicht gefunden")
    })
    public Response getBookFair(@PathParam("id") long bookFair_id){

        LOG.info("Gebe Buchmesse aus mit ID: " + bookFair_id);
        BookFair bookFair = this.bookFairManagement.getBookFairById(bookFair_id);
        if(bookFair == null){
            LOG.info("Es wurden keine Buchmesse mit der ID: " + bookFair_id + " gefunden");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOG.info("Es wurde eine Buchmesse mit der ID: " + bookFair.getId() + " gefunden");
        return Response.ok(bookFair).build();
    }
    /**
     *
     * Ruft eine spezifische Buchmesse anhand des Namens ab.
     * @param name Der Name der abzurufenden Buchmesse
     * @return Response mit dem Buchmesse oder 404, falls nicht gefunden
     */
    @GET
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("/name")
    @Transactional
    @RolesAllowed({"author", "admin"})
    @Operation(summary = "Buchmessen nach Namen suchen", description = "Gibt eine Liste von Buchmessen zurück, die dem angegebenen Namen entsprechen")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Erfolgreiche Operation"),
            @APIResponse(responseCode = "404", description = "Keine Buchmessen gefunden")
    })
    public Response getBookFairName(@QueryParam("name") String name){
        LOG.info("Gebe alle Buchmessen mit dem Namen: " + " aus: ");
        Collection<BookFair> bookFairs= this.bookFairManagement.getBookFairByName(name);
        if(bookFairs.isEmpty()){
            LOG.info("Es wurden keine Buchmessen gefunden");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOG.info("Es wurden folgende Buchmessen mit dem Namen: " + name + " gefunden");
        return Response.ok(bookFairs).build();
    }

    /**
     *
     * Ruft einen spezifische Buchmesse anhand seines Location ab.
     * @param location Der Name de abzurufenden Buchmesse
     * @return Response mit der Buchmesse oder 404, falls nicht gefunden
     */
    @GET
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("/location")
    @Transactional
    @RolesAllowed({"author", "admin"})
    @Operation(summary = "Buchmessen nach Ort suchen", description = "Gibt eine Liste von Buchmessen zurück, die am angegebenen Ort stattfinden")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Erfolgreiche Operation"),
            @APIResponse(responseCode = "404", description = "Keine Buchmessen gefunden")
    })
    public Response getBookFairLocation(@QueryParam("location") String location){

        LOG.info("Gebe alle Buchmessen, die in diesem Ort" + location +" stattfinden aus: ");
        Collection<BookFair> bookFairs= this.bookFairManagement.getBookFairByLocation(location);
        if(bookFairs.isEmpty()){
            LOG.info("Es wurden keine Buchmessen, die in diesem Ort stattfinden, gefunden");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOG.info("Es wurden folgende Buchmessen, die in diesem Ort stattfinden: " + location + " gefunden");
        return Response.ok(bookFairs).build();
    }
    /**
     * Fügt einen neue Buchmesse hinzu.
     * @param bookFairDTO Die Daten der neuen Buchmesse
     * @return Response mit der hinzugefügten Buchmesse oder 400 bei Fehlschlag
     */
    @POST
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Transactional
   @RolesAllowed("admin")
    @Operation(summary = "Neue Buchmesse hinzufügen", description = "Fügt eine neue Buchmesse hinzu")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Buchmesse erfolgreich hinzugefügt"),
            @APIResponse(responseCode = "500", description = "Interner Serverfehler")
    })
    public Response addBookFair(BookFairDTO bookFairDTO){
        LOG.info("Füge Buchmesse hinzu: ");
        BookFair bookFair = this.bookFairManagement.addBookFair(bookFairDTO);
        if(bookFair == null){
            LOG.info("Es konnte keine Buchmesse hinzugefügt werden");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        LOG.info("Neue Buchmesse wurde mit folgender ID: " + bookFair.getId()+ " erstellt");
        return Response.ok(bookFair).build();
    }

    /**
     * Bearbeitet eine Buchmesse anhand seiner ID.
     * @param bookFair_id Die ID der zu bearbeitenden Buchmesse
     * @return Response mit Status OK bei Erfolg oder 404, falls nicht gefunden
     */

    @PUT
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("{id}")
    @Transactional
    @RolesAllowed("admin")
    @Operation(summary = "Buchmesse bearbeiten", description = "Aktualisiert die Informationen einer bestehenden Buchmesse")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Buchmesse erfolgreich aktualisiert"),
            @APIResponse(responseCode = "404", description = "Buchmesse nicht gefunden")
    })
    public Response edit(@PathParam("id") long bookFair_id, BookFairDTO bookFairDTO){

        LOG.info("Bearbeitung der Buchmesse mit dieser ID: " + bookFair_id);
        BookFair bookFair = this.bookFairManagement.editBookFair(bookFair_id, bookFairDTO);
        if(bookFair == null){
            LOG.info("Buchmesse mit ID: " + bookFair_id + " nicht gefunden");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOG.info("Buchmesse mit ID: " + bookFair_id + " wurde bearbeitet");
        return Response.ok(bookFair).build();
    }

    /**
     * Löscht eine Buchmesse anhand seiner ID.
     * @param bookFair_id Die ID der zu löschenden Buchmesse
     * @return Response mit Status OK bei Erfolg oder 404, falls nicht gefunden
     */
    @DELETE
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("{id}")
    @Transactional
    @RolesAllowed("admin")
    @Operation(summary = "Buchmesse löschen", description = "Löscht eine Buchmesse anhand ihrer ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Buchmesse erfolgreich gelöscht"),
            @APIResponse(responseCode = "404", description = "Buchmesse nicht gefunden")
    })
    public Response deleteBookFair(@PathParam("id") long bookFair_id){

        LOG.info("Lösche Buchmesse mit dieser ID: " + bookFair_id);
        if(!this.bookFairManagement.deleteBookFair(bookFair_id)){
            LOG.info("Buchmesse konnte nicht gelöscht werden");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOG.info("Buchmesse mit ID: " + bookFair_id + " wurde gelöscht");
        return Response.ok().build();
    }


    /**
     * Ruft alle Buchmessen ab, an denen ein Autor teilnimmt.
     * @param bookFair_id Die ID der Buchmesse
     * @return Response mit einer Collection von Buchmessen oder 204, falls keine gefunden
     */
    @GET
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("{bookFair_id}/participants")
    @Transactional
    @RolesAllowed({"author", "admin"})
    @Operation(summary = "Teilnehmer einer Buchmesse abrufen", description = "Gibt eine Liste aller Teilnehmer einer bestimmten Buchmesse zurück")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Erfolgreiche Operation"),
            @APIResponse(responseCode = "404", description = "Buchmesse nicht gefunden")
    })
    public Response getParticipants(@PathParam("bookFair_id") long bookFair_id){

        LOG.info("Gebe Teilnehmer der Buchmesse aus: ");
        BookFair bookFair = this.bookFairManagement.getBookFairById(bookFair_id);
        if (bookFair == null) {
            LOG.info("Es wurden keine Buchmesse mit der ID: " + bookFair_id + " gefunden");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(bookFair.getParticipants()).build();
    }

}
