package hsos.de.swa.bookFairAdministration.boundary.web;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.control.IBookFairManagement;
import hsos.de.swa.bookFairAdministration.boundary.dto.BookFairDTO;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import hsos.de.swa.bookFairAdministration.entity.BookFairForm;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
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
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

@Path("/api/v1/web/bookFair")
@Produces(MediaType.TEXT_HTML)
@ApplicationScoped
public class BookFairResource {
    private static final Logger LOG = Logger.getLogger(BookFairResource.class.getName());

    @Inject
    private IBookFairManagement bookFairManagement;

    @Inject
    @Location("BookFairResource/bookfairForm.html")
    Template bookfairForm;

    @Inject
    @Location("BookFairResource/bookfairs.html")
    Template bookfairs;

    //@Inject
    //Principal principal;

    @GET
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Transactional
   // @PermitAll
    @Authenticated
    public TemplateInstance getBookFairs(@QueryParam("id") Long id, @QueryParam("name") String name, @QueryParam("location") String location) {
        LOG.info("Suche Buchmessen");
        Collection<BookFair> bookFairs;
        if (id != null) {
            LOG.info("Suche Buchmesse mit ID: " + id);
            BookFair bookFair = this.bookFairManagement.getBookFairById(id);
            if (bookFair != null) {
                bookFairs = Collections.singletonList(bookFair);
            } else {
                bookFairs = Collections.emptyList();
            }
        } else if (name != null && !name.trim().isEmpty()) {
            LOG.info("Suche Buchmessen mit Namen: " + name);
            bookFairs = this.bookFairManagement.getBookFairByName(name);
        } else if (location != null && !location.trim().isEmpty()) {
            LOG.info("Suche Buchmessen mit Location: " + location);
            bookFairs = this.bookFairManagement.getBookFairByLocation(location);
        } else {
            LOG.info("Hole alle Buchmessen");
            bookFairs = this.bookFairManagement.getAllBookFairs();
        }
        return bookfairs.data("bookFairs", bookFairs)
                .data("isParticipantView", false);
    }

    @GET
    @Path("/add")
    @RolesAllowed("admin")
    public TemplateInstance showAddForm() {
        return bookfairForm.data("update", false);
    }

    @GET
    @Path("/{id}/edit")
    @RolesAllowed("admin")
    public TemplateInstance showEditForm(@PathParam("id") long id) {

        BookFair bookFair = bookFairManagement.getBookFairById(id);
        if (bookFair == null) {
            return bookfairs.data("error", "BookFair not found");
        }
        return bookfairForm.data("bookFair", bookFair)
                .data("update", true);
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    @RolesAllowed("admin")
    public Response addBookFair(@BeanParam BookFairForm form) {

        BookFairDTO dto = form.toDTO();
        LOG.info("Fuege Buchmesse hinzu:");
        BookFair bookFair = bookFairManagement.addBookFair(dto);
        if (bookFair != null) {
            LOG.info("Hinzufuegen erfolgreich");
            return Response.seeOther(URI.create("/api/v1/web/bookFair")).build();
        } else {
            LOG.info("Hinzufuegen fehlgeschlagen");
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/{id}/edit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    @RolesAllowed("admin")
    public Response editBookFair(@PathParam("id") long id, @BeanParam BookFairForm form) {

        LOG.info("Bearbeite Buchmesse:");
        BookFairDTO dto = form.toDTO();

        BookFair bookFair = bookFairManagement.editBookFair(id, dto);
        if (bookFair != null && !dto.getDate().isBefore(LocalDate.now())) {
            LOG.info("Bearbeitung erfolgreich");
            return Response.seeOther(URI.create("/api/v1/web/bookFair")).build();
        } else {
            LOG.info("Bearbeiten fehlgeschlagen");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/{id}/delete")
    @Transactional
    @RolesAllowed("admin")
    public Response deleteBookFair(@PathParam("id") long id) {

        LOG.info("Loesche Buchmesse:");
        boolean deleted = bookFairManagement.deleteBookFair(id);
        if (deleted) {
            LOG.info("Loeschen war erfolgreich");
            return Response.seeOther(URI.create("/api/v1/web/bookFair")).build();
        } else {
            LOG.info("Loeschen fehlgeschlagen");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/participants")
    @Transactional
    @RolesAllowed({"admin", "author"})
    public TemplateInstance getParticipantsByBookFairId(@PathParam("id") long bookFairId) {

        BookFair bookFair = bookFairManagement.getBookFairById(bookFairId);
        if (bookFair != null) {
            bookFair.loadParticipants();
        }
        Collection<Author> participants = bookFair != null ? bookFair.getParticipants() : Collections.emptyList();
        return bookfairs.data("bookFair", bookFair)
                .data("participants", participants)
                .data("isParticipantView", true);
    }



}
