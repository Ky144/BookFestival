package hsos.de.swa.authorAdministration.control;

import hsos.de.swa.authorAdministration.boundary.dto.AuthorDTO;
import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.entity.BookFair;

import java.util.Collection;

public interface IAuthorManagement {

    public Collection<Author> getAll();
    public Author get(long id);
    public Author add(AuthorDTO authorDTO);
    public Author edit(long id,AuthorDTO authorDTO);
    public boolean delete(long id);

    public Collection<BookFair> getBookFairsOfAuthor(long author_id);

}
