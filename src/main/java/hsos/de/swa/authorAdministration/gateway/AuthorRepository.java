package hsos.de.swa.authorAdministration.gateway;

import hsos.de.swa.authorAdministration.boundary.dto.AuthorDTO;
import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.authorAdministration.entity.IAuthorRepository;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import hsos.de.swa.waitlistAdministration.control.WaitlistManagement;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.Collection;
import java.util.List;

@ApplicationScoped
public class AuthorRepository implements IAuthorRepository, PanacheRepository<Author> {

    /**
     * Holt alle Autoren aus der Datenbank.
     * @return eine Collection aller Autoren.
     */
    @Override
    public Collection<Author> getAll() {
        return listAll();
    }
    /**
     * Holt einen spezifischen Autor anhand seiner ID.
     * @param id die ID des Autors.
     * @return der gefundene Autor oder null, falls nicht gefunden.
     */
    @Inject
    private WaitlistManagement waitlistManagement;
    @Override
    public Author get(long id) {
        return findById(id);
    }

    /**
     * Fügt einen neuen Autor zur Datenbank hinzu.
     * @param authorDTO die Daten des neuen Autors.
     * @return der hinzugefügte Autor.
     */
    @Override
    @Transactional
    public Author add(AuthorDTO authorDTO) {
        Author author = new Author(authorDTO.firstName, authorDTO.lastName);
        persist(author);
        return author;
    }

    /**
     * Bearbeitet einen bestehenden Autor.
     * @param id die ID des zu bearbeitenden Autors.
     * @param authorDTO die neuen Daten des Autors.
     * @return der bearbeitete Autor.
     */
    @Override
    @Transactional
    public Author edit(long id, AuthorDTO authorDTO) {
        Author author = findById(id);
        author.setFirstname(authorDTO.firstName);
        author.setLastname(authorDTO.lastName);
        return author;
    }

    /**
     * Löscht einen Autor anhand seiner ID.
     * @param id die ID des zu löschenden Autors.
     * @return true, wenn der Autor erfolgreich gelöscht wurde, false sonst.
     */
    @Override
    @Transactional
    public boolean delete(long id) {
        Author author = findById(id);
        if (author == null) {
            return false;
        }
        waitlistManagement.removeAllEntriesForAuthor(id);
        for (BookFair bookFair : author.getBookFairs()) {
            bookFair.removeParticipant(author);
        }
        author.getBookFairs().clear();
        return deleteById(id);
    }

    /**
     * Holt alle Buchmessen, an denen ein spezifischer Autor teilnimmt.
     * @param authorId die ID des Autors.
     * @return eine Collection von Buchmessen.
     */
    @Override
    public Collection<BookFair> getBookFairsOfAuthor(long authorId) {
        Author author = findById(authorId);
        if (author == null) {
            return List.of();
        }
        return author.getBookFairs();
    }
}
