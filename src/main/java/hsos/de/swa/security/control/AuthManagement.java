package hsos.de.swa.security.control;

import hsos.de.swa.security.entity.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import io.smallrye.jwt.build.Jwt;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;

@ApplicationScoped
public class AuthManagement implements IAuthManagement {

    @Override
    public boolean authenticate(String username, String password) {
        User user = User.find("username", username).firstResult();
        if (user != null) {
            return BcryptUtil.matches(password, user.password);
        }
        return false;
    }
//Quelle: https://quarkus.io/guides/security-jwt
public String generateToken(String username) {
    return Jwt.issuer("https://example.com/issuer")
            .subject(username)
            .expiresIn(Duration.ofHours(1))
            .sign();
}

}
