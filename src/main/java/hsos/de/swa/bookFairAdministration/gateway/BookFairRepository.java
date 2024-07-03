package hsos.de.swa.bookFairAdministration.gateway;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.control.dto.BookFairDTO;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import hsos.de.swa.bookFairAdministration.entity.IBookFairRepository;
import hsos.de.swa.bookFairAdministration.gateway.RegistrationResult;
import hsos.de.swa.bookFairAdministration.gateway.SignOutResult;
import hsos.de.swa.waitlistAdministration.control.WaitlistManagement;
import hsos.de.swa.waitlistAdministration.entity.WaitlistEntry;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ApplicationScoped
public class BookFairRepository implements IBookFairRepository, PanacheRepository<BookFair> {
    @Inject
    private WaitlistManagement waitlistManagement;

    @Inject
    private EntityManager em;

    @Override
    public Collection<BookFair> getAllBookFairs() {
        return listAll();
    }

    @Override
    public BookFair getBookFairById(long bookFairId) {
        return find("id", bookFairId).firstResult();
    }

    @Override
    public Collection<BookFair> getBookFairByName(String name) {
        return find("name = ?1", name).list();
    }

    @Override
    @Transactional
    public BookFair add(BookFairDTO bookFairDTO) {
        BookFair bookFair = new BookFair();
        bookFair.setName(bookFairDTO.getName());
        bookFair.setLocation(bookFairDTO.getLocation());
        bookFair.setDate(bookFairDTO.getDate());
        bookFair.setMaxParticipants(bookFairDTO.getMaxParticipants());
        persist(bookFair);
        return bookFair;
    }

    @Override
    @Transactional
    public BookFair edit(long bookFairId, BookFairDTO bookFairDTO) {
        BookFair bookFair = getBookFairById(bookFairId);
        if (bookFair != null) {
            bookFair.setName(bookFairDTO.getName());
            bookFair.setLocation(bookFairDTO.getLocation());
            bookFair.setDate(bookFairDTO.getDate());
            bookFair.setMaxParticipants(bookFairDTO.getMaxParticipants());
            persist(bookFair);
        }
        return bookFair;
    }

    @Override
    @Transactional
    public boolean delete(long bookFairId) {
        return deleteById(bookFairId);
    }

    @Override
    public Collection<BookFair> getBookFairsOfAuthor(long authorId) {
        Author author = this.em.find(Author.class, authorId);
        if (author == null) {
            return List.of();
        }
        Collection<BookFair> allBookFairs = listAll();
        Collection<BookFair> bookFairsOfAuthor = new ArrayList<>();
        for (BookFair bookFair : allBookFairs) {
            if (bookFair.isParticipantSignedIn(author)) {
                bookFairsOfAuthor.add(bookFair);
            }
        }
        return bookFairsOfAuthor;
    }

    @Override
    @Transactional
    public RegistrationResult signIn(long bookFairId, long authorId) {
        BookFair bookFair = this.em.find(BookFair.class, bookFairId);
        Author author = this.em.find(Author.class, authorId);

        if (bookFair == null || author == null) {
            return RegistrationResult.FAILED;
        }

        if (bookFair.isParticipantSignedIn(author)) {
            return RegistrationResult.ALREADY_SIGNEDIN;
        }

        if (bookFair.getSignendInAuthorsCount() < bookFair.getMaxParticipants()) {
            bookFair.addParticipant(author);
            return RegistrationResult.REGISTERED;
        }

        waitlistManagement.addToWaitlist(bookFairId, author);
        return RegistrationResult.WAITLISTED;
    }

    @Override
    @Transactional
    public SignOutResult signOut(long bookFairId, long authorId) {
        BookFair bookFair = this.em.find(BookFair.class, bookFairId);
        Author author = this.em.find(Author.class, authorId);

        if (bookFair == null || author == null) {
            return SignOutResult.FAILED;
        }

        if (!bookFair.isParticipantSignedIn(author)) {
            return SignOutResult.NOT_SIGNED_IN;
        }

        bookFair.removeParticipant(author);

        WaitlistEntry nextInLine = waitlistManagement.getNextInLine(bookFairId);
        if (nextInLine != null) {
            bookFair.addParticipant(nextInLine.getAuthor());
            waitlistManagement.removeFromWaitlist(bookFairId, nextInLine.getAuthor());
        }
        return SignOutResult.SIGNED_OUT;
    }

    @Override
    public Collection<Author> getParticipantsByBookFairId(long bookFairId) {
        BookFair bookFair = this.em.find(BookFair.class, bookFairId);
        if (bookFair != null) {
            return bookFair.getParticipants();
        }
        return List.of();
    }
}
