package hsos.de.swa;

import hsos.de.swa.bookFairAdministration.boundary.dto.BookFairDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
/*
Testet alle Methoden der „BookFairResource“
 */
@QuarkusTest
public class BookFairResourceTest {


    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testGetAllBookFairs() {
        given()

                .when().get("/api/v1/bookFair")
                .then()
                .statusCode(200)
                .body("size()", is(3))
                .body("name", hasItems("Frankfurt Book Fair", "Leipzig Book Fair", "Bologna Children"));
    }

    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testGetBookFairById() {
        given()
                .when().get("/api/v1/bookFair/1")
                .then()
                .statusCode(200)
                .body("name", is("Frankfurt Book Fair"))
                .body("location", is("Frankfurt"))
                .body("date", is("2024-10-10"))
                .body("maxParticipants", is(100));
    }

    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testAddBookFair() {
        LocalDate date = LocalDate.parse("2024-03-15");
        BookFairDTO newBookFair = new BookFairDTO("New Book Fair", "Berlin", 150, date);

        given()
                .contentType(ContentType.JSON)
                //https://javaee.github.io/jsonb-spec/getting-started.html
                .body(JsonbBuilder.create().toJson(newBookFair))  // Explicite Konvertierung zu JSON
                .when().post("/api/v1/bookFair")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("name", is("New Book Fair"))
                .body("location", is("Berlin"))
                .body("maxParticipants", is(150))
                .body("date", is("2024-03-15"));
    }

    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testEditBookFair() {
        // Create a BookFairDTO object with updated information
        BookFairDTO updatedBookFair = new BookFairDTO(
                "Updated Leipzig Fair",
                "Berlin",
                50,
                LocalDate.parse("2024-10-10")
        );

        given()
                .contentType(ContentType.JSON)
                //Quelle: https://javaee.github.io/jsonb-spec/getting-started.html
                .body(JsonbBuilder.create().toJson(updatedBookFair))
                .when().put("/api/v1/bookFair/2")
                .then()
                .log().ifValidationFails()  // This will log the response if the test fails
                .statusCode(200)
                .body("name", is("Updated Leipzig Fair"))
                .body("location", is("Berlin"))
                .body("maxParticipants", is(50))
                .body("date", is("2024-10-10"));
    }



    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testDeleteBookFair() {
        given()

                .when().delete("/api/v1/bookFair/4")
                .then()
                .statusCode(200);

        // Überprüfe, dass die Buchmesse gelöscht wurde
        given()
                .when().get("/api/v1/bookFair/4")
                .then()
                .statusCode(404);
    }
}