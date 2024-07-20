package hsos.de.swa.bookFairAdministration.boundary.rest;

import hsos.de.swa.bookFairAdministration.control.IBookFairManagement;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import hsos.de.swa.bookFairAdministration.gateway.RegistrationResult;
import hsos.de.swa.bookFairAdministration.gateway.SignOutResult;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

import java.util.Collection;
import java.util.logging.Logger;

/**
 * REST resource fürs Handeln von author registration zu  bookfairs und sign-out operationen.
 */
@Path("/api/v1/registration")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Buchmesse-Registrierung", description = "Verwaltung von Autor-Registrierungen für Buchmessen")
@ApplicationScoped
public class BookFairRegistrationResource {
    private static final Logger LOG = Logger.getLogger(BookFairResource.class.getName());

    @Inject
    private IBookFairManagement bookFairManagement;

    /**
     * Meldet einen Autor für eine Buchmesse an oder setzt ihn auf die Warteliste.
     * @param bookFairId Die ID der Buchmesse
     * @param authorId Die ID des Autors
     * @return Response mit dem Ergebnis der Anmeldung
     */
    @POST
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @RolesAllowed({"admin", "author"})
    @Path("/{bookFairId}/authors/{authorId}")
    @Operation(summary = "Autor für Buchmesse anmelden",
            description = "Meldet einen Autor für eine Buchmesse an oder setzt ihn auf die Warteliste, falls die Messe voll ist")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Anmeldung erfolgreich oder Autor auf Warteliste gesetzt"),
            @APIResponse(responseCode = "404", description = "Buchmesse oder Autor nicht gefunden")
    })
    public Response signIn(@PathParam("bookFairId") long bookFairId, @PathParam("authorId") long authorId) {
        LOG.info("Signing up author with ID: " + authorId + " for book fair with ID: " + bookFairId);
        RegistrationResult response = this.bookFairManagement.signIn(bookFairId, authorId);

        if (response == RegistrationResult.REGISTERED) {
            LOG.info("Author with ID: " + authorId + " successfully signed up for book fair with ID: " + bookFairId);
            return Response.ok().build();
        } else if (response == RegistrationResult.WAITLISTED) {
            LOG.info("Author with ID: " + authorId + " placed on waitlist for book fair with ID: " + bookFairId);
            return Response.ok().build();
        } else if (response == RegistrationResult.ALREADY_SIGNEDIN) {
            LOG.info("Author with ID: " + authorId + " is already signed up for book fair with ID: " + bookFairId);
            return Response.ok().build();
        } else {
            LOG.info("Failed to sign up author with ID: " + authorId + " for book fair with ID: " + bookFairId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Meldet einen Autor von einer Buchmesse ab.
     * @param bookFairId Die ID der Buchmesse
     * @param authorId Die ID des Autors
     * @return Response mit dem Ergebnis der Abmeldung
     */
    @DELETE
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @RolesAllowed({"admin", "author"})
    @Path("/{bookFairId}/authors/{authorId}")
    @Operation(summary = "Autor von Buchmesse abmelden",
            description = "Meldet einen Autor von einer Buchmesse ab")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Abmeldung erfolgreich oder Autor war nicht angemeldet"),
            @APIResponse(responseCode = "400", description = "Fehler bei der Abmeldung")
    })
    public Response signOut(@PathParam("bookFairId") long bookFairId, @PathParam("authorId") long authorId) {
        LOG.info("Signing out author with ID: " + authorId + " from book fair with ID: " + bookFairId);
        SignOutResult response = bookFairManagement.signOut(bookFairId, authorId);

        if (response == SignOutResult.SIGNED_OUT) {
            LOG.info("Author with ID: " + authorId + " successfully signed out from book fair with ID: " + bookFairId);
            return Response.ok().build();
        } else if (response == SignOutResult.NOT_SIGNED_IN) {
            LOG.info("Author with ID: " + authorId + " is not signed in and cannot sign out from book fair with ID: " + bookFairId);
            return Response.ok().build();
        } else {
            LOG.info("Failed to sign out author with ID: " + authorId + " from book fair with ID: " + bookFairId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
