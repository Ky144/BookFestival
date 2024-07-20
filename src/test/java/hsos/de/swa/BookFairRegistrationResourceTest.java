package hsos.de.swa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

/*
Testet alle Methoden der „BookFairRegistration-Resource“
 */
@QuarkusTest
public class BookFairRegistrationResourceTest {

    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testSignIn() {
        given()
                .contentType(ContentType.JSON)
                .when().post("/api/v1/registration/4/authors/5")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testSignOut() {

        // Now, sign out
        given()
                .contentType(ContentType.JSON)
                .when().delete("/api/v1/registration/2/authors/3")
                .then()
                .statusCode(200);


    }
}

