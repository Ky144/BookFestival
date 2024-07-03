package hsos.de.swa.bookFairAdministration.boundary.rest;

import hsos.de.swa.bookFairAdministration.control.IBookFairManagement;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import hsos.de.swa.bookFairAdministration.gateway.RegistrationResult;
import hsos.de.swa.bookFairAdministration.gateway.SignOutResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import java.util.Collection;
import java.util.logging.Logger;

/**
 * Methoden für das An- und Abmelden bei Buchmessen,
 * sowie Anzeige für die Buchmessen, an denen ein Author teilnimmt
 */
@Path("/registration")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class BookFairRegistrationResource {
    private static final Logger LOG = Logger.getLogger(BookFairResource.class.getName());
    @Inject
    private IBookFairManagement bookFairManagement;

    /**
     * Meldet Author bei einer Buchmesse an, falls die Teilnehmeranzahl voll ist,
     * wird Author auf die Warteliste gesetzt
     * @param bookFairId
     * @param authorId
     * @return
     */
    @POST
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("/{bookFairId}/authors/{authorId}")
    public Response signIn(@PathParam("bookFairId") long bookFairId, @PathParam("authorId") long authorId) {
        LOG.info("Melde Author mit id: "+ authorId + " zur Buchmesse mit id: " + bookFairId + "an");
        RegistrationResult response = this.bookFairManagement.signIn(bookFairId, authorId);

        if (response==RegistrationResult.REGISTERED) {
            LOG.info("Hinzufügen des Authors mit der id: " + authorId +
                    "zur Buchmesse mit der id: " + bookFairId + "war erfolgreich");
            return Response.ok().build();
        } else if(response==RegistrationResult.WAITLISTED){
            LOG.info("Author steht mit der ID"+ authorId + "steht " +
                    "auf der Warteliste der Buchmesse mit der ID " + bookFairId);
            return Response.ok().build();

        } else if(response==RegistrationResult.ALREADY_SIGNEDIN){
        LOG.info("Author ist bereits angemeldet zur Buchmesse mit der ID " + bookFairId);
        return Response.ok().build();
         }
        else {
            LOG.info("Anmelden ist fehlgeschlagen");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Meldet Author von der Buchmesse ab
     * @param bookFairId
     * @param authorId
     * @return
     */
    @DELETE
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("/{bookFairId}/authors/{authorId}")
    public Response signOut(@PathParam("bookFairId") long bookFairId, @PathParam("authorId") long authorId) {
        LOG.info("Melde Author mit id: "+ authorId + " zur Buchmesse mit id: " + bookFairId + "ab");
        SignOutResult response = bookFairManagement.signOut(bookFairId, authorId);
        if (response==SignOutResult.SIGNED_OUT) {
            LOG.info("Abmelden des Authors mit der id: " + authorId +
                    "von der Buchmesse mit der id: " + bookFairId + "war erfolgreich");
            return Response.ok().build();
        } else if(response==SignOutResult.NOT_SIGNED_IN){
            LOG.info("Author mit der ID"+ authorId + "ist nicht angemeldet und kann sich " +
                    "von der Buchmesse mit der ID " + bookFairId + " nicht abmelden");
            return Response.ok().build();

        }else {
            LOG.info("Abmelden ist fehlgeschlagen");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * Gebe alle Buchmessen aus an denen Author teilnimmt
     * @param authorId
     * @return
     */
    @GET
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("/authors/{authorId}")
    public Response getBookFairsOfAuthor(@PathParam("authorId") long authorId) {
        LOG.info("Information über die Buchmessen an denen der AUthor mit der ID: " + authorId+ " teilnimmt");
        Collection<BookFair> bookFairs= this.bookFairManagement.getBookFairsOfAuthor(authorId);
        if (bookFairs.isEmpty()) {
            LOG.info("Keine Messen für den AUthor: " + authorId + "gefunden");
            return Response.noContent().build();
        }
        LOG.info("Teilnahme an: " + bookFairs.size() + " Buchmessen");
        return Response.ok(bookFairs).build();
    }
}
