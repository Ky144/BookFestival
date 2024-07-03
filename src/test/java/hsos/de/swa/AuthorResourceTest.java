package hsos.de.swa;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class AuthorResourceTest {

    @BeforeEach
    @Transactional
    public void setUp() {
        // Clear the database or set up initial state
        given()
                .when().delete("/authors")
                .then().statusCode(204);
    }

    @Test
    public void testAddAuthor() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"firstName\": \"Jana\",\"lastName\":\"Goedert\"}")
                .when()
                .post("/authors")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("firstName", is("Jana"))
                .body("lastName", is("Goedert"));
    }

    @Test
    public void testGetAuthors() {
        // First, add an author
        given()
                .contentType(ContentType.JSON)
                .body("{\"firstName\": \"Test\",\"lastName\":\"Author\"}")
                .when()
                .post("/authors")
                .then()
                .statusCode(201);

        // Then, get all authors
        given()
                .when()
                .get("/authors")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("firstName", hasItem("Test"))
                .body("lastName", hasItem("Author"));
    }

    @Test
    public void testGetAuthor() {
        // First, add an author
        int id = given()
                .contentType(ContentType.JSON)
                .body("{\"firstName\": \"John\",\"lastName\":\"Doe\"}")
                .when()
                .post("/authors")
                .then()
                .statusCode(201)
                .extract().jsonPath().getInt("id");

        // Then, get the author
        given()
                .when()
                .get("/authors/" + id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("firstName", is("John"))
                .body("lastName", is("Doe"));
    }

    @Test
    public void testEditAuthor() {
        // First, add an author
        int id = given()
                .contentType(ContentType.JSON)
                .body("{\"firstName\": \"Jane\",\"lastName\":\"Doe\"}")
                .when()
                .post("/authors")
                .then()
                .statusCode(201)
                .extract().jsonPath().getInt("id");

        // Then, edit the author
        given()
                .contentType(ContentType.JSON)
                .body("{\"firstName\": \"Janet\",\"lastName\": \"Smith\"}")
                .when()
                .put("/authors/" + id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("firstName", is("Janet"))
                .body("lastName", is("Smith"));
    }

    @Test
    public void testDeleteAuthor() {
        // First, add an author
        int id = given()
                .contentType(ContentType.JSON)
                .body("{\"firstName\": \"Delete\",\"lastName\":\"Me\"}")
                .when()
                .post("/authors")
                .then()
                .statusCode(201)
                .extract().jsonPath().getInt("id");

        // Then, delete the author
        given()
                .when()
                .delete("/authors/" + id)
                .then()
                .statusCode(204);

        // Verify the author is deleted
        given()
                .when()
                .get("/authors/" + id)
                .then()
                .statusCode(404);
    }
}
