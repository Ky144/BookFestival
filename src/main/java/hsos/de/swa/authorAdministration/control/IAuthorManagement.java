package hsos.de.swa.authorAdministration.control;

import hsos.de.swa.authorAdministration.control.dto.AuthorDTO;
import hsos.de.swa.authorAdministration.entity.Author;

import java.util.Collection;

public interface IAuthorManagement {

    public Collection<Author> getAll();
    public Author get(long id);
    public Author add(AuthorDTO authorDTO);
    public Author edit(long id,AuthorDTO authorDTO);
    public boolean delete(long id);
}
