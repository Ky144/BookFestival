package hsos.de.swa.waitlistAdministration.boundary.rest;

import hsos.de.swa.waitlistAdministration.control.WaitlistManagement;
import hsos.de.swa.waitlistAdministration.entity.WaitlistEntry;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.logging.Logger;
/*
Stellt Methoden zur Verfügung, die die Warteliste einer Buchmesse ausgibt
und Ermöglichung des Austragens aus der Warteliste
 */
@RequestScoped
@Path("/waitlist")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WaitlistResource {
    private static final Logger LOG = Logger.getLogger(WaitlistResource.class.getName());

    @Inject
    WaitlistManagement waitlistManagement;
    /**
     * Gibt Warteliste der jeweiligen Buchmesse aus
     * @param bookFairId
     * @param bookFairId
     * @return
     */
    @GET
    @Path("/{bookFairId}")
    public Response getWaitlist(@PathParam("bookFairId") long bookFairId) {
        LOG.info("Gebe Warteliste der Buchmesse mit der ID: " + bookFairId + " aus");
        List<WaitlistEntry> waitlist = waitlistManagement.getWaitlistByBookFairId(bookFairId);
        if (waitlist.isEmpty()) {
            LOG.info("Warteliste der Buchmesse mit der ID: " + bookFairId + " ist leer");
            return Response.noContent().build();
        }
        LOG.info("Warteliste der Buchmesse mit der ID: " + bookFairId + " existiert");
        return Response.ok(waitlist).build();
    }
/*
    @POST
    @Path("/{bookFairId}/add")
    @Transactional
    public Response addToWaitlist(@PathParam("bookFairId") long bookFairId, Author author) {
        WaitlistEntry entry = waitlistManagement.addToWaitlist(bookFairId, author);
        if (entry == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to add to waitlist").build();
        }
        return Response.status(Response.Status.CREATED).entity(entry).build();
    }*/
/*
    @DELETE
    @Path("/{bookFairId}/remove")
    @Transactional
    public Response removeFromWaitlist(@PathParam("bookFairId") long bookFairId, Author author) {
        boolean removed = waitlistManagement.removeFromWaitlist(bookFairId, author);
        if (!removed) {
            return Response.status(Response.Status.NOT_FOUND).entity("Author not found in waitlist").build();
        }
        return Response.noContent().build();
    }
*/
    /**
     * Entfernt Author aus der Warteliste
     * @param bookFairId
     * @param authorId
     * @return
     */
    @DELETE
    @Path("/{bookFairId}/remove/{authorId}")
    @Transactional
    public Response removeFromWaitlistByAuthorId(@PathParam("bookFairId") long bookFairId, @PathParam("authorId") long authorId) {
        LOG.info("Entferne Author mit der ID: " + authorId + " aus der Warteliste der Buchmesse mit der ID: " + bookFairId);
        boolean removed = waitlistManagement.removeFromWaitlistByAuthorId(bookFairId, authorId);
        if (!removed) {
            LOG.info("Author mit der ID: " + authorId + " konnte nicht aus der Warteliste der Buchmesse mit der ID: "
                    + bookFairId +" entfernt werden");
            return Response.status(Response.Status.NOT_FOUND).entity("Author not found in waitlist").build();
        }
        LOG.info("Author mit der ID: " + authorId + " wurde aus der Warteliste der Buchmesse mit der ID: "
                + bookFairId + " entfernt");
        return Response.noContent().build();
    }
}
