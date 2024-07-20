package hsos.de.swa.bookFairAdministration.entity;

import hsos.de.swa.bookFairAdministration.boundary.dto.BookFairDTO;
import jakarta.ws.rs.FormParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//Ansatz basiert auf: https://www.morling.dev/blog/quarkus-qute-test-ride/
public class BookFairForm {
    @FormParam("name")
    public String name;
    //Quarkus kann nicht automatisch einen String in ein LocalDate-Objekt konvertieren
    @FormParam("date")
    public String dateString;

    @FormParam("location")
    public String location;

    @FormParam("maxParticipants")
    public int maxParticipants;


        //dto.date = LocalDate.parse(this.dateString, DateTimeFormatter.ISO_DATE); // Konvertieren
        public BookFairDTO toDTO() {
            BookFairDTO dto = new BookFairDTO();
            dto.setName(this.name);
            dto.setDate(LocalDate.parse(this.dateString, DateTimeFormatter.ISO_DATE)); // Konvertieren);
            dto.setLocation(this.location);
            dto.setMaxParticipants(this.maxParticipants);
            return dto;
        }


}
