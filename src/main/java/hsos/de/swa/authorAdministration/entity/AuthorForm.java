package hsos.de.swa.authorAdministration.entity;

import hsos.de.swa.authorAdministration.boundary.dto.AuthorDTO;
import jakarta.ws.rs.FormParam;

public class AuthorForm {
    @FormParam("firstname")
    public String firstname;
    @FormParam("lastname")
    public String lastname;

    public AuthorDTO toDTO() {
        AuthorDTO dto = new AuthorDTO();
        dto.setFirstName(this.firstname);
        dto.setLastName(this.lastname);
        return dto;
    }
}
