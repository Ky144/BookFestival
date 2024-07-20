package hsos.de.swa;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
/*
Testet alle Methoden der „WaitlistResource“
 */
@QuarkusTest
public class WaitlistResourceTest {

    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testGetWaitlist() {
        given()
                .when().get("/api/v1/waitlist/3")  // Bologna Children BookFair
                .then()
                .statusCode(200);

    }



    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testRemoveFromWaitlist() {
        // Zuerst überprüfen, dass der Autor in der Warteliste ist
        given()
                .when().get("/api/v1/waitlist/3")  // Bologna Children BookFair
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("[0].authorId", is(2));  // Jane Smith

        // Dann entferne den Autor aus der Warteliste
        given()
                .when().delete("/api/v1/waitlist/3/remove/2")  // Entferne Jane Smith (ID 2) aus der Warteliste für Bologna Children (ID 3)
                .then()
                .statusCode(204);  // No Content

        // überprüfe, dass der Autor nicht mehr in der Warteliste ist
        given()
                .when().get("/api/v1/waitlist/3")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].authorId", is(3));  // Nur noch Hanna Meier sollte in der Warteliste sein
    }

    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testRemoveFromWaitlistNotFound() {
        given()
                .when().delete("/api/v1/waitlist/1/remove/999")  // Versuche, einen nicht existierenden Autor zu entfernen
                .then()
                .statusCode(404)
                .body(is("Author not found in waitlist"));
    }
}
