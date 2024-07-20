package hsos.de.swa;
import hsos.de.swa.authorAdministration.boundary.dto.AuthorDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

/*
Testet alle Methoden der „AuthorResource“
 */
@QuarkusTest
public class AuthorResourceTest {

    @Test
    //https://stackoverflow.com/questions/71530382/how-to-use-dynamic-roles-with-testsecurity
    @TestSecurity(user = "admin", roles = "admin")
    public void testGetAuthors() {
        given()
                .when().get("/api/v1/author")
                .then()
                .statusCode(200);

    }

    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testGetAuthor() {
        given()
                .when().get("/api/v1/author/2")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testAddAuthor() {
        AuthorDTO newAuthor = new AuthorDTO("John", "Doe");

        given()
                .contentType(ContentType.JSON)
                .body(newAuthor)
                .when().post("/api/v1/author")
                .then()
                .statusCode(200)
                .body("firstname", is("John"))
                .body("lastname", is("Doe"));
    }

    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testEditAuthor() {
        AuthorDTO updatedAuthor = new AuthorDTO("Jane", "Smith");

        given()
                .contentType(ContentType.JSON)
                .body(updatedAuthor)
                .when().put("/api/v1/author/2")
                .then()
                .statusCode(200);

    }

    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testDeleteAuthor() {
        given()
                .when().delete("/api/v1/author/1")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testGetBookFairsOfAuthor() {
        given()
                .when().get("/api/v1/author/authors/2")
                .then()
                .statusCode(200);

    }
}
