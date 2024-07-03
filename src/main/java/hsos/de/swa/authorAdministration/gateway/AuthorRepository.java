package hsos.de.swa.authorAdministration.gateway;

import hsos.de.swa.authorAdministration.control.dto.AuthorDTO;
import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.authorAdministration.entity.IAuthorRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Collection;
@ApplicationScoped
public class AuthorRepository implements IAuthorRepository, PanacheRepository<Author> {
    @Override
    public Collection<Author> getAll() {
        return listAll();
    }

    @Override
    public Author get(long id) {
        return findById(id);
    }

    @Override
    @Transactional
    public Author add(AuthorDTO authorDTO) {
        Author author = new Author(authorDTO.firstName, authorDTO.lastName);
        persist(author);
        return author;
    }

    @Override
    @Transactional
    public Author edit(long id, AuthorDTO authorDTO) {
        Author author = findById(id);
        author.setFirstname(authorDTO.firstName);
        author.setLastname(authorDTO.lastName);
        return author;
    }

    @Override
    @Transactional
    public boolean delete(long id) {
        return deleteById(id);
    }
}
