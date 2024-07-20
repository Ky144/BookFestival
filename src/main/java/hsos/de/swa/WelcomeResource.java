package hsos.de.swa;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/web/welcome")
public class WelcomeResource {
    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance welcome();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance loadPage(){
        return Templates.welcome();
    }
}
