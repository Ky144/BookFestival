package hsos.de.swa.bookFairAdministration.boundary.web;

import hsos.de.swa.bookFairAdministration.control.IBookFairManagement;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import hsos.de.swa.bookFairAdministration.gateway.RegistrationResult;
import hsos.de.swa.bookFairAdministration.gateway.SignOutResult;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;



import java.util.Collection;
import java.util.logging.Logger;

@Path("/api/v1/web/registration")
@Produces(MediaType.TEXT_HTML)
@ApplicationScoped
public class BookFairRegistrationResource {
    private static final Logger LOG = Logger.getLogger(BookFairRegistrationResource.class.getName());

    @Inject
    private IBookFairManagement bookFairManagement;

    @Inject
    @Location("BookFairResource/BookFairRegistration.html")
    Template registrationForm;

    @Inject
    @Location("BookFairResource/bookFairsFromAuthor.html")
    Template bookFairs;

    @GET
    //@Path("/form")
    public TemplateInstance showRegistrationForm() {
        return registrationForm.data("message", "");
    }

    @POST
    @Path("/signIn")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed({"admin", "author"})
    @Transactional
    public TemplateInstance signIn(@FormParam("bookFairId") long bookFairId, @FormParam("authorId") long authorId) {
        LOG.info("Melde Author mit id: " + authorId + " zur Buchmesse mit id: " + bookFairId + " an");
        RegistrationResult response = this.bookFairManagement.signIn(bookFairId, authorId);

        if (response == RegistrationResult.REGISTERED) {
            LOG.info("Hinzufügen des Authors mit der id: " + authorId +
                    "zur Buchmesse mit der id: " + bookFairId + " war erfolgreich");
            return registrationForm.data("message", "Anmeldung erfolgreich!");
        } else if (response == RegistrationResult.WAITLISTED) {
            LOG.info("Author steht mit der ID" + authorId + " steht " +
                    "auf der Warteliste der Buchmesse mit der ID " + bookFairId);
            return registrationForm.data("message", "Author wurde auf die Warteliste gesetzt.");
        } else if (response == RegistrationResult.ALREADY_SIGNEDIN) {
            LOG.info("Author ist bereits angemeldet zur Buchmesse mit der ID " + bookFairId);
            return registrationForm.data("message", "Author ist bereits angemeldet.");
        } else {
            LOG.info("Anmelden ist fehlgeschlagen");
            return registrationForm.data("message", "Anmelden ist fehlgeschlagen.");
        }
    }

    @POST
    @Path("/signOut")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed({"admin", "author"})
    @Transactional
    public TemplateInstance signOut(@FormParam("bookFairId") long bookFairId, @FormParam("authorId") long authorId) {
        LOG.info("Melde Author mit id: " + authorId + " von der Buchmesse mit id: " + bookFairId + " ab");
        SignOutResult response = bookFairManagement.signOut(bookFairId, authorId);
        if (response == SignOutResult.SIGNED_OUT) {
            LOG.info("Abmelden des Authors mit der id: " + authorId +
                    "von der Buchmesse mit der id: " + bookFairId + " war erfolgreich");
            return registrationForm.data("message", "Abmeldung erfolgreich!");
        } else if (response == SignOutResult.NOT_SIGNED_IN) {
            LOG.info("Author mit der ID" + authorId + " ist nicht angemeldet und kann sich " +
                    "von der Buchmesse mit der ID " + bookFairId + " nicht abmelden");
            return registrationForm.data("message", "Author ist nicht angemeldet.");
        } else {
            LOG.info("Abmelden ist fehlgeschlagen");
            return registrationForm.data("message", "Abmelden ist fehlgeschlagen.");
        }
    }


}
