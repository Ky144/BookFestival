package hsos.de.swa.authorAdministration.boundary.dto;

import hsos.de.swa.authorAdministration.entity.Author;

import java.util.stream.Collectors;

/**
 * Data Transfer Object (DTO) für einen Autor.
 */
public class AuthorDTO {
    public String firstName; // Vorname des Autors
    public String lastName;  // Nachname des Autors

    /**
     * Konstruktor für die Initialisierung eines AuthorDTO-Objekts mit Vorname und Nachname.
     * @param firstName Der Vorname des Autors
     * @param lastName Der Nachname des Autors
     */
    public AuthorDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }



    /**
     * Standard-Konstruktor für AuthorDTO.
     */
    public AuthorDTO() {}

    /**
     * Getter für den Nachnamen des Autors.
     * @return Der Nachname des Autors
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter für den Nachnamen des Autors.
     * @param lastName Der Nachname des Autors
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter für den Vornamen des Autors.
     * @return Der Vorname des Autors
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter für den Vornamen des Autors.
     * @param firstName Der Vorname des Autors
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


   /* public AuthorDTO toDTO() {
        AuthorDTO dto = new AuthorDTO();
        dto.setFirstName(this.firstName);
        dto.setLastName(this.lastName);
        return dto;
    }*/
   public static AuthorDTO fromEntity(Author author) {
       AuthorDTO dto = new AuthorDTO();
       dto.setFirstName(author.getFirstname());
       dto.setLastName(author.getLastname());
       // Fügen Sie hier weitere Felder hinzu, aber vermeiden Sie zirkuläre Referenzen
       // Zum Beispiel, anstatt die vollständigen BookFair-Objekte hinzuzufügen, fügen Sie nur deren IDs hinzu
       // dto.setBookFairIds(author.getBookFairs().stream().map(BookFair::getId).collect(Collectors.toList()));
       return dto;
   }
}
