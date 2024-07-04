package hsos.de.swa.security.boundary;

import hsos.de.swa.authorAdministration.boundary.rest.AuthorResource;
import hsos.de.swa.security.boundary.dto.LoginDTO;
import hsos.de.swa.security.control.AuthManagement;

import hsos.de.swa.security.entity.User;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    private static final Logger LOG = Logger.getLogger(AuthResource.class.getName());

    @Inject
    AuthManagement authManagement;

    /*
    **Biete Login-Funktionaltät und überprüfe die eingegebenen Login-Daten
     */
    @POST
    @Path("/login")
    public Response login(LoginDTO loginDTO) {
        LOG.info("Biete Login-Funktionalitaet");
        if (authManagement.authenticate(loginDTO.username, loginDTO.password)) {
            User user = User.find("username", loginDTO.username).firstResult();
            String token = authManagement.generateToken(user.username);
            LOG.info("Login erfolgreich");
            return Response.ok().entity(token).build();
        } else {
            LOG.info("Login nicht erfolgreich");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
