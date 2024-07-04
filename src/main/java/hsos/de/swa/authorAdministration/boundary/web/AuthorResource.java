package hsos.de.swa.authorAdministration.boundary.web;

import hsos.de.swa.authorAdministration.control.IAuthorManagement;
import hsos.de.swa.authorAdministration.entity.Author;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Retry;

import java.util.Collection;
import java.util.logging.Logger;

//@Path("/web/author")
//@ApplicationScoped
public class AuthorResource {
   /* @Inject
    IAuthorManagement management;
    private static final Logger LOG = Logger.getLogger(AuthorResource.class.getName());

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance authors(Collection<Author> authors);
    }


    @GET
    @Retry(maxRetries = 4)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getAuthors() {
        LOG.info("Liste alle Authoren auf ");
        Collection<Author> authors=this.management.getAll();
        if(authors==null){
            LOG.info("Liste der Authoren ist leer ");
            throw new WebApplicationException(403);
        }
        LOG.info("Erfolgreiches Abrufen der Authorenliste ");
        return Templates.authors(authors);
    }

    @GET
    @Retry(maxRetries = 4)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getAuthor(@PathParam("id") long id) {

    }


*/
}
