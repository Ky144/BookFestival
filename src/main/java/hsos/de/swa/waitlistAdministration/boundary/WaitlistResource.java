package hsos.de.swa.waitlistAdministration.boundary;

import hsos.de.swa.authorAdministration.entity.Author;
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

@RequestScoped
@Path("/waitlist")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WaitlistResource {
    private static final Logger LOG = Logger.getLogger(WaitlistResource.class.getName());

    @Inject
    WaitlistManagement waitlistManagement;

    @GET
    @Path("/{bookFairId}")
    public Response getWaitlist(@PathParam("bookFairId") long bookFairId) {
        List<WaitlistEntry> waitlist = waitlistManagement.getWaitlistByBookFairId(bookFairId);
        if (waitlist.isEmpty()) {
            return Response.noContent().build();
        }
        return Response.ok(waitlist).build();
    }

    @POST
    @Path("/{bookFairId}/add")
    @Transactional
    public Response addToWaitlist(@PathParam("bookFairId") long bookFairId, Author author) {
        WaitlistEntry entry = waitlistManagement.addToWaitlist(bookFairId, author);
        if (entry == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to add to waitlist").build();
        }
        return Response.status(Response.Status.CREATED).entity(entry).build();
    }

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
}
