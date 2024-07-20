package hsos.de.swa.waitlistAdministration.boundary.rest;

import hsos.de.swa.waitlistAdministration.control.WaitlistManagement;
import hsos.de.swa.waitlistAdministration.entity.WaitlistEntry;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
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
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.logging.Logger;
/*
Stellt Methoden zur Verfügung, die die Warteliste einer Buchmesse ausgibt
und Ermöglichung des Austragens aus der Warteliste
 */
@RequestScoped
@Path("/api/v1/waitlist")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Warteliste", description = "Verwaltung von Wartelisten für Buchmessen")
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
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("/{bookFairId}")
    @RolesAllowed({"admin", "author"})
    @Operation(summary = "Warteliste einer Buchmesse abrufen",
            description = "Gibt die Warteliste für eine bestimmte Buchmesse zurück")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Erfolgreiche Operation"),
            @APIResponse(responseCode = "204", description = "Keine Einträge in der Warteliste")
    })
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

    /**
     * Entfernt Author aus der Warteliste
     * @param bookFairId
     * @param authorId
     * @return
     */
    @DELETE
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("/{bookFairId}/remove/{authorId}")
    @RolesAllowed({"admin", "author"})
    @Transactional
    @Operation(summary = "Autor aus Warteliste entfernen",
            description = "Entfernt einen Autor aus der Warteliste einer bestimmten Buchmesse")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Autor erfolgreich aus der Warteliste entfernt"),
            @APIResponse(responseCode = "404", description = "Autor nicht in der Warteliste gefunden")
    })
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
