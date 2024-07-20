package hsos.de.swa.waitlistAdministration.boundary.web;

import hsos.de.swa.waitlistAdministration.control.WaitlistManagement;
import hsos.de.swa.waitlistAdministration.entity.WaitlistEntry;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

@Path("/api/v1/web/waitlist")
@Produces(MediaType.TEXT_HTML)
@RequestScoped
public class WaitlistResource {
    private static final Logger LOG = Logger.getLogger(WaitlistResource.class.getName());

    @Inject
    WaitlistManagement waitlistManagement;

    @Inject
    @Location("WaitlistResource/waitlistResource.html")
    Template waitlistTemplate;

    @GET
    @Path("/{bookFairId}")
    @RolesAllowed({"admin", "author"})
    public TemplateInstance getWaitlist(@PathParam("bookFairId") long bookFairId) {
        LOG.info("Gebe Warteliste der Buchmesse mit der ID: " + bookFairId + " aus");
        List<WaitlistEntry> waitlist = waitlistManagement.getWaitlistByBookFairId(bookFairId);
        if (waitlist.isEmpty()) {
            LOG.info("Warteliste der Buchmesse mit der ID: " + bookFairId + " ist leer");
            return waitlistTemplate.data("waitlist", waitlist).data("message", "Keine Einträge in der Warteliste.");
        }
        LOG.info("Warteliste der Buchmesse mit der ID: " + bookFairId + " existiert");

        return waitlistTemplate.data("waitlist", waitlist).data("message", "");
    }

    @POST
    @Path("/{bookFairId}/remove/{authorId}")
    @RolesAllowed({"admin", "author"})
    @Transactional
    public Response removeFromWaitlist(@PathParam("bookFairId") long bookFairId, @PathParam("authorId") long authorId) {
        LOG.info("Entferne Author mit der ID: " + authorId + " aus der Warteliste der Buchmesse mit der ID: " + bookFairId);
        boolean removed = waitlistManagement.removeFromWaitlistByAuthorId(bookFairId, authorId);
        String message;
        if (!removed) {
            LOG.info("Author mit der ID: " + authorId + " konnte nicht aus der Warteliste der Buchmesse mit der ID: " + bookFairId + " entfernt werden");
            message = "Author konnte nicht entfernt werden";
        } else {
            LOG.info("Author mit der ID: " + authorId + " wurde aus der Warteliste der Buchmesse mit der ID: " + bookFairId + " entfernt");
            message = "Author erfolgreich entfernt";
        }
        try {
            //Quelle:https://docs.oracle.com/javase/8/docs/api/java/net/URLEncoder.html
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
            return Response.seeOther(URI.create("/api/v1/web/waitlist/" + bookFairId + "?message=" + encodedMessage)).build();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported", e);
        }
    }


}
