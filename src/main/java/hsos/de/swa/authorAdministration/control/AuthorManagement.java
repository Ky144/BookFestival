package hsos.de.swa.authorAdministration.control;

import hsos.de.swa.authorAdministration.boundary.dto.AuthorDTO;
import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.authorAdministration.entity.IAuthorRepository;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Collection;


@ApplicationScoped
public class AuthorManagement implements IAuthorManagement {
    @Inject
    IAuthorRepository authorRepository;
    /**
     * Holt alle Autoren.
     * @return eine Collection aller Autoren.
     */
    @Override
    public Collection<Author> getAll() {
        return authorRepository.getAll();
    }

    /**
     * Holt einen spezifischen Autor anhand seiner ID.
     * @param id die ID des Autors.
     * @return der gefundene Autor oder null, falls nicht gefunden.
     */
    @Override
    public Author get(long id) {
        return authorRepository.get(id);
    }

    /**
     * Fügt einen neuen Autor hinzu.
     * @param authorDTO die Daten des neuen Autors.
     * @return der hinzugefügte Autor.
     */
    @Override
    @Transactional
    public Author add(AuthorDTO authorDTO) {
        return authorRepository.add(authorDTO);
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
        return authorRepository.edit(id, authorDTO);
    }

    /**
     * Löscht einen Autor anhand seiner ID.
     * @param id die ID des zu löschenden Autors.
     * @return true, wenn der Autor erfolgreich gelöscht wurde, false sonst.
     */
    @Override
    @Transactional
    public boolean delete(long id) {
        return authorRepository.delete(id);
    }

    /**
     * Holt alle Buchmessen, an denen ein spezifischer Autor teilnimmt.
     * @param author_id die ID des Autors.
     * @return eine Collection von Buchmessen.
     */
    @Override
    public Collection<BookFair> getBookFairsOfAuthor(long author_id) {
        return this.authorRepository.getBookFairsOfAuthor(author_id);
    }
}
