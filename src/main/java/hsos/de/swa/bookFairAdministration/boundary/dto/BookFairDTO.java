package hsos.de.swa.bookFairAdministration.boundary.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

public class BookFairDTO {
    public String name;
    public int maxParticipants;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public LocalDate date;
    public String location;
    public BookFairDTO() {}
    public BookFairDTO(String name, String location, int maxParticipants, LocalDate date) {
        this.name = name;
        this.location = location;
        this.maxParticipants = maxParticipants;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
