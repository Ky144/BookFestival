package hsos.de.swa.authorAdministration.boundary.rest;

import hsos.de.swa.authorAdministration.control.IAuthorManagement;
import hsos.de.swa.authorAdministration.control.dto.AuthorDTO;
import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
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

@Path("/author")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private static final Logger LOG = Logger.getLogger(AuthorResource.class.getName());

    @Inject
    IAuthorManagement authorManagement;

    /**
     * Gibt alle existierenden Authoren aus
     * @return ggf. Collection mit den AUthoren
     */
    @GET
    @Retry(maxRetries = 4)
    public Response getAuthors() {
        LOG.info("Liste alle Authoren auf");
        //Collection<BookFair> bookFairs= this.bookFairManagement.getAllBookFairs();
        Collection<Author> authors =this.authorManagement.getAll();
        //if(authors.isEmpty()) {
        if(authors==null){
            LOG.info("Liste der Authoren ist leer");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOG.info("Erfolgreiches Abrufen der Authorenliste");
        return Response.ok(authors).build();
    }

    /**
     * Ermittelt einen AUthor anhand der ID
     * @param id
     * @return das AnfrageErgebnis ggf. mit gefundenem Author
     */
    @GET
    @Path("/{id}")
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    public Response getAuthor(@PathParam("id") long id) {
        LOG.info("Suche nach Author mit dieser id: " + id);
        Author author = this.authorManagement.get(id);
        if (author == null) {
            LOG.info("Author mit dieser id: " + id + " existiert nicht");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOG.info("Erfolgreiches Abrufen der Authorenliste");
        return Response.ok(author).build();
    }

    /**
     * Erstellt und fügt neuen Author zur DB hinzu
     * @param authorDTO die Daten des Authors
     * @return ggf. neuer Author
     */
    @POST
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Transactional
    public Response add(AuthorDTO authorDTO) {
        LOG.info("Füge einen Author zur Datenbank hinzu");
        Author author = this.authorManagement.add(authorDTO);
        if (author == null) {
            LOG.info("Hinzufügen eines Authoren ist fehlgeschlagen");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        LOG.info("Erfolgreiches Hinzufügen eines Authoren mit folgender ID " + author.getId());
        //return Response.status(Response.Status.CREATED).entity(author).build();
        return Response.ok(author).build();
    }

    /**
     * Bearbeitung eines bereits existierenden AUthors
     * @param id
     * @param authorDTO
     * @return ggf. aktualisierter Author
     */
    @PUT
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @Path("/{id}")
    @Transactional
    public Response edit(@PathParam("id") long id, AuthorDTO authorDTO) {
        LOG.info("Bearbeite Author mit dieser id: " + id);
        Author author = this.authorManagement.edit(id, authorDTO);
        if(author == null) {
            LOG.info("Author mit dieser id: " + id + " existiert nicht");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOG.info("Erfolgreiches bearbeiten des Authors mit dieser id: " + id);
        return Response.ok(author).build();
    }
    @Path("{id}")
    @Retry(maxRetries = 4, delay = 200)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 200)
    @DELETE
    @Transactional
    public Response delete(@PathParam("id") long id) {
        LOG.info("Lösche AUthor mit dieser id: " + id);
        boolean exist = this.authorManagement.delete(id);
        if(exist) {
            LOG.info("Erfolgreiches Löschen des Authors mit der id: " + id);
            return Response.ok().build();
        }
        LOG.info("Löschen des AUthors mit der ID " + id + " ist fehlgeschlagen");
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
