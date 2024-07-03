package hsos.de.swa.bookFairAdministration.boundary.rest;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.control.IBookFairManagement;
import hsos.de.swa.bookFairAdministration.control.dto.BookFairDTO;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import java.util.Collection;
import java.util.logging.Logger;

@Path("/bookFair")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped

public class BookFairResource {
    private static final Logger LOG = Logger.getLogger(BookFairResource.class.getName());
    @Inject
    private IBookFairManagement bookFairManagement;

    /**
     * Ermittelt alle vorhandenen Buchmessen aus
     * @return gibt vorhandene Buchmessen zurück
     */
    @GET
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Transactional
    public Response getAll(){
        LOG.info("Gebe alle Buchmessen aus: ");
        Collection<BookFair> bookFairs= this.bookFairManagement.getAllBookFairs();
        if(bookFairs==null){
            LOG.info("Es wurden keine Buchmessen gefunden");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOG.info("Es wurden " + bookFairs.size() + " Buchmessen gefunden");
        return Response.ok(bookFairs).build();
    }
    /**
     *Ermittelt Buchmesse anhand der ID
     * @param bookFair_id
     * @return gibt Buchmesse mit übergebenen id zurück
     */
    @GET
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("{id}")
    @Transactional
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
     * ERmittelt alle Buchmessen, die mit dem Suchbegriff übereinstimmen
     * @param name
     * @return
     */
    @GET
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("/name")
    @Transactional
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
     * ERstellung einer neuen Buchmesse
     * @param bookFairDTO
     * @return neue Buchmesse
     */
    @POST
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Transactional
    public Response addBookFair(BookFairDTO bookFairDTO){
        LOG.info("Füge Buchmesse hinzu: ");
        BookFair bookFair= this.bookFairManagement.addBookFair(bookFairDTO);
        if(bookFair==null){
            LOG.info("Es konnte keine Buchmesse hinzugefügt werden");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        LOG.info("Neue Buchmesse wurde mit folgender ID: " + bookFair.getId()+ " erstellt");
        return Response.ok(bookFair).build();
    }

    /**
     * Bearbietung einer Buchmesse
     * @param bookFair_id
     * @param bookFairDTO
     * @return bearbeitete Buchmesse
     */
    @PUT
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("{id}")
    @Transactional
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
     * Löschen einer Buchmesse
     * @param bookFair_id
     * @return Gibt Wahrheitswert zurück, ob Löschen funktioniert hat
     */

    @DELETE
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") long bookFair_id){
        LOG.info("Lösche Buchmesse mit diese ID: " + bookFair_id);
        boolean deleted = this.bookFairManagement.deleteBookFair(bookFair_id);
        if(deleted){
            LOG.info("Buchmesse mit dieser id: " + bookFair_id+ " wurde gelöscht");
            return Response.status(Response.Status.OK).build();
        }
        LOG.info("Löschen der Buchmesse mit die id: " + bookFair_id + "ist fehlgeschlagen");
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("/{id}/participants")
    public Response getParticipantsByBookFairId(@PathParam("id") long bookFair_id) {
        Collection<Author> participants = bookFairManagement.getParticipantsByBookFairId(bookFair_id);
        if (participants.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(participants).build();
    }


}
