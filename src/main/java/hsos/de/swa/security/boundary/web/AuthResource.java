package hsos.de.swa.security.boundary.web;


import hsos.de.swa.security.control.IAuthManagement;
import hsos.de.swa.security.entity.User;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.logging.Logger;

@Path("/auth")
public class AuthResource {
    private static final Logger LOG= Logger.getLogger(AuthResource.class.getName());
    @Inject
    IAuthManagement authManagement;

    @Inject
    @Location("LoginResource/login.html")
    Template login;
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(
            @FormParam("username") String username,
            @FormParam("password") String password) {
        LOG.info("Biete Login-Funktionalitaet");

        User user = User.find("username", username).firstResult();

        if (user == null) {
            LOG.info("Benutzername nicht gefunden");
            return Response.seeOther(URI.create("/auth/login?error=username"))
                    .build();
        }

        if (authManagement.authenticate(username, password)) {
            String token = authManagement.generateToken(username);
            LOG.info("Login erfolgreich");

            NewCookie tokenCookie = new NewCookie("token", token, "/", null, null, -1, false);

            return Response.seeOther(URI.create("/main"))
                    .cookie(tokenCookie)
                    .build();
        } else {
            LOG.info("Falsches Passwort");
            return Response.seeOther(URI.create("/auth/login?error=password"))
                    .build();
        }
    }

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance showLoginForm(@QueryParam("error") String error) {
        String errorMessage = null;
        if ("username".equals(error)) {
            errorMessage = "Der eingegebene Benutzername existiert nicht.";
        } else if ("password".equals(error)) {
            errorMessage = "Das eingegebene Passwort ist falsch.";
        }
        return login.data("error", errorMessage);
    }

}
