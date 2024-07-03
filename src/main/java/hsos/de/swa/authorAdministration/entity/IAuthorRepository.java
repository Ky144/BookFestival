package hsos.de.swa.authorAdministration.entity;

import hsos.de.swa.authorAdministration.control.dto.AuthorDTO;

import java.util.Collection;


public interface IAuthorRepository
{
    public Collection<Author> getAll();
    public Author get(long id);
    public Author add(AuthorDTO authorDTO);
    public  Author edit (long id, AuthorDTO authorDTO);
    public boolean delete (long id);
}
