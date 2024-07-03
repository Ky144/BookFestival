package hsos.de.swa.bookFairAdministration.control;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.control.dto.BookFairDTO;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import hsos.de.swa.bookFairAdministration.entity.IBookFairRepository;
import hsos.de.swa.bookFairAdministration.gateway.RegistrationResult;
import hsos.de.swa.bookFairAdministration.gateway.SignOutResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Collection;

@ApplicationScoped
public class BookFairManagement implements IBookFairManagement {
    @Inject
    private IBookFairRepository bookFairRepository;

    @Override
    public Collection<BookFair> getAllBookFairs() {
        return this.bookFairRepository.getAllBookFairs();
    }

    @Override
    public Collection<BookFair> getBookFairByName(String name) {
        return this.bookFairRepository.getBookFairByName(name);
    }

    @Override
    public BookFair getBookFairById(long bookFair_id) {
        return bookFairRepository.getBookFairById(bookFair_id);
    }

    @Override
    @Transactional
    public BookFair addBookFair(BookFairDTO bookFairDTO) {
        return this.bookFairRepository.add(bookFairDTO);
    }

    @Override
    @Transactional
    public BookFair editBookFair(long bookFair_id, BookFairDTO bookFairDTO) {
        return this.bookFairRepository.edit(bookFair_id, bookFairDTO);
    }

    @Override
    @Transactional
    public boolean deleteBookFair(long bookFair_id) {
        return this.bookFairRepository.delete(bookFair_id);
    }

    @Override
    @Transactional
    public RegistrationResult signIn(long bookFair_id, long author_id) {
        return this.bookFairRepository.signIn(bookFair_id, author_id);
    }

    @Override
    @Transactional
    public SignOutResult signOut(long bookFair_id, long author_id) {
        return this.bookFairRepository.signOut(bookFair_id, author_id);
    }

    @Override
    public Collection<BookFair> getBookFairsOfAuthor(long author_id) {
        return this.bookFairRepository.getBookFairsOfAuthor(author_id);
    }

    @Override
    public Collection<Author> getParticipantsByBookFairId(long bookFair_id) {
        return this.bookFairRepository.getParticipantsByBookFairId(bookFair_id);
    }
}
