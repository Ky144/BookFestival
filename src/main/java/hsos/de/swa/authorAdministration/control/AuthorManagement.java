package hsos.de.swa.authorAdministration.control;

import hsos.de.swa.authorAdministration.control.dto.AuthorDTO;
import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.authorAdministration.entity.IAuthorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Collection;


@ApplicationScoped
public class AuthorManagement implements IAuthorManagement {
    @Inject
    IAuthorRepository authorRepository;
    @Override
    public Collection<Author> getAll() {
        return authorRepository.getAll();
    }

    @Override
    public Author get(long id) {
        return authorRepository.get(id);
    }

    @Override
    @Transactional
    public Author add(AuthorDTO authorDTO) {
        return authorRepository.add(authorDTO);
    }

    @Override
    @Transactional
    public Author edit(long id, AuthorDTO authorDTO) {
        return authorRepository.edit(id, authorDTO);
    }

    @Override
    @Transactional
    public boolean delete(long id) {
        return authorRepository.delete(id);
    }
}
