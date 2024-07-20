package hsos.de.swa.authorAdministration.boundary.web;

import hsos.de.swa.authorAdministration.control.IAuthorManagement;
import hsos.de.swa.authorAdministration.boundary.dto.AuthorDTO;
import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.authorAdministration.entity.AuthorForm;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
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

import java.net.URI;
import java.util.Collection;
import java.util.logging.Logger;

@Path("/api/v1/web/author")
@Produces(MediaType.TEXT_HTML)
@ApplicationScoped
public class AuthorResource {
    private static final Logger LOG = Logger.getLogger(AuthorResource.class.getName());

    @Inject
    private IAuthorManagement authorManagement;

    @Inject
    @Location("AuthorResource/authorBookFairs.html")
    Template authorBookFairsTemplate;

    @Inject
    @Location("AuthorResource/authorForm.html")
    Template authorForm;

    @Inject
    @Location("AuthorResource/authors.html")
    Template authors;

    /**
     * Endpoint zur Anzeige der Autorenliste.
     * @param id Die optionale ID des Autors, der angezeigt werden soll
     * @param name Der optionale Name des Autors, der angezeigt werden soll
     * @return TemplateInstance für die Autorenliste
     */
    @GET
    @Retry(maxRetries = 3, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 1000)
    @Transactional
    @RolesAllowed({"author", "admin"})
    public TemplateInstance getAuthors(@QueryParam("id") Long id, @QueryParam("name") String name) {
        LOG.info("Suche Autoren");
        Collection<Author> authorList;

        if (id != null) {
            LOG.info("Suche Autor mit ID: " + id);
            Author author = this.authorManagement.get(id);
            if (author != null) {
                authorList = java.util.Collections.singletonList(author);
            } else {
                authorList = java.util.Collections.emptyList();
            }
        } else {
            LOG.info("Hole alle Autoren");
            authorList = this.authorManagement.getAll();
        }

        LOG.info("Anzahl der gefundenen Autoren: " + authorList.size());
        return authors.data("authors", authorList);
    }

    /**
     * Endpoint zur Anzeige des Formulars zum Hinzufügen eines neuen Autors.
     * @return TemplateInstance für das Hinzufügen-Formular
     */
    @GET
    @Path("/add")
    @RolesAllowed({"admin"})
    public TemplateInstance showAddForm() {
        LOG.info("Zeige Formular zum Hinzufügen eines Autors");
        return authorForm.data("update", false);
    }

    /**
     * Endpoint zur Anzeige des Formulars zum Bearbeiten eines vorhandenen Autors.
     * @param id Die ID des zu bearbeitenden Autors
     * @return TemplateInstance für das Bearbeiten-Formular oder Fehlermeldung, falls Autor nicht gefunden
     */
    @GET
    @Path("/{id}/edit")
    @RolesAllowed({"admin"})
    public TemplateInstance showEditForm(@PathParam("id") long id) {
        LOG.info("Zeige Bearbeitungsformular für Autor mit ID: " + id);
        Author author = authorManagement.get(id);
        if (author == null) {
            LOG.warning("Autor mit ID " + id + " nicht gefunden");
            return authors.data("error", "Autor nicht gefunden");
        }
        LOG.info("Autor für Bearbeitung gefunden: " + author);
        return authorForm.data("author", author)
                .data("update", true);
    }

    /**
     * Endpoint zum Hinzufügen eines neuen Autors.
     * @param form Das Formular mit den Daten des neuen Autors
     * @return Response mit Weiterleitung zur Autorenliste oder Serverfehlermeldung
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    @RolesAllowed({"admin"})
    public Response addAuthor(@BeanParam AuthorForm form) {
        LOG.info("Füge neuen Autor hinzu");
        LOG.info("Empfangenes Formular: " + form);
        AuthorDTO dto = form.toDTO();
        Author author = authorManagement.add(dto);
        if (author != null) {
            LOG.info("Autor erfolgreich hinzugefügt: " + author);
            return Response.seeOther(URI.create("/api/v1/web/author")).build();
        } else {
            LOG.severe("Fehler beim Hinzufügen des Autors");
            return Response.serverError().build();
        }
    }

    /**
     * Endpoint zur Bearbeitung eines vorhandenen Autors.
     * @param id Die ID des zu bearbeitenden Autors
     * @param form Das Formular mit den aktualisierten Daten des Autors
     * @return Response mit Weiterleitung zur Autorenliste oder Fehlermeldung, falls Autor nicht gefunden
     */
    @POST
    @Path("/{id}/edit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    @RolesAllowed({"admin"})
    public Response editAuthor(@PathParam("id") long id, @BeanParam AuthorForm form) {
        LOG.info("Bearbeite Autor mit ID: " + id);
        LOG.info("Empfangenes Formular: " + form);
        AuthorDTO dto = form.toDTO();
        Author author = authorManagement.edit(id, dto);
        if (author != null) {
            LOG.info("Autor erfolgreich aktualisiert: " + author);
            return Response.seeOther(URI.create("/api/v1/web/author")).build();
        } else {
            LOG.warning("Autor mit ID " + id + " nicht für Bearbeitung gefunden");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Endpoint zum Löschen eines Autors.
     * @param id Die ID des zu löschenden Autors
     * @return Response mit Weiterleitung zur Autorenliste oder Fehlermeldung, falls Autor nicht gefunden
     */
    @POST
    @Path("/{id}/delete")
    @Transactional
    @RolesAllowed({"admin"})
    public Response deleteAuthor(@PathParam("id") long id) {
        LOG.info("Lösche Autor mit ID: " + id);
        boolean deleted = authorManagement.delete(id);
        if (deleted) {
            LOG.info("Autor mit ID " + id + " erfolgreich gelöscht");
            return Response.seeOther(URI.create("/api/v1/web/author")).build();
        } else {
            LOG.warning("Autor mit ID " + id + " nicht zum Löschen gefunden");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Endpoint zur Anzeige der Buchmessen, an denen ein Autor teilnimmt.
     * @param authorId Die ID des Autors
     * @return TemplateInstance für die Anzeige der Buchmessen des Autors
     */
    @GET
    @Path("/{id}/bookfairs")
    @RolesAllowed({"admin"})
    public TemplateInstance getBookFairsOfAuthor(@PathParam("id") long authorId) {
        LOG.info("Gebe Buchmessen aus, an denen der Autor mit der ID: " + authorId + " teilnimmt");
        Author author = authorManagement.get(authorId);
        Collection<BookFair> bookFairs = authorManagement.getBookFairsOfAuthor(authorId);
        return authorBookFairsTemplate.data("author", author)
                .data("bookFairs", bookFairs);
    }
}
