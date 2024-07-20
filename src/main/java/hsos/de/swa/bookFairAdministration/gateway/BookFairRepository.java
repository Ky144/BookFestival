package hsos.de.swa.bookFairAdministration.gateway;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.boundary.dto.BookFairDTO;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import hsos.de.swa.bookFairAdministration.entity.IBookFairRepository;
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
    public Collection<BookFair> getBookFairByLocation(String location) {
        return find("location = ?1", location).list();
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
            int currentMaxParticipants = bookFair.getMaxParticipants();
            int newMaxParticipants = bookFairDTO.getMaxParticipants();
            int currentParticipantsCount = bookFair.getParticipants().size();

            bookFair.setName(bookFairDTO.getName());
            bookFair.setLocation(bookFairDTO.getLocation());
            bookFair.setDate(bookFairDTO.getDate());
            bookFair.setMaxParticipants(newMaxParticipants);
            persist(bookFair);

            // Überprüfen, ob neue maxParticipants kleiner als aktuelle Teilnehmeranzahl sind
            if (newMaxParticipants < currentParticipantsCount) {
                // Liste der überschüssigen Teilnehmer erstellen
                List<Author> participantsToWaitlist = new ArrayList<>(bookFair.getParticipants());
                // Nur die überschüssigen Teilnehmer auf die Warteliste setzen
                for (int i = currentParticipantsCount - 1; i >= newMaxParticipants; i--) {
                    Author author = participantsToWaitlist.get(i);
                    bookFair.removeParticipant(author);
                    waitlistManagement.addToWaitlist(bookFairId, author);
                }
            } else if (newMaxParticipants > currentMaxParticipants) {
                // Autoren von der Warteliste hinzufügen
                List<WaitlistEntry> waitlist = waitlistManagement.getWaitlistByBookFairId(bookFairId);
                while (bookFair.getParticipants().size() < newMaxParticipants && !waitlist.isEmpty()) {
                    WaitlistEntry entry = waitlist.remove(0);
                    bookFair.addParticipant(entry.getAuthor());
                    waitlistManagement.removeFromWaitlist(bookFairId, entry.getAuthor());
                }
            }
        }
        return bookFair;
    }

    @Override
    @Transactional
    public boolean delete(long bookFairId) {
        BookFair bookFair = find("id", bookFairId).firstResult();
        if (bookFair == null) {
            return false;
        }

        // First, remove all waitlist entries for this book fair
        List<WaitlistEntry> waitlistEntries = waitlistManagement.getWaitlistByBookFairId(bookFairId);
        for (WaitlistEntry entry : waitlistEntries) {
            waitlistManagement.removeFromWaitlist(bookFairId, entry.getAuthor());
        }

        // Then, remove all participants
        bookFair.getParticipants().clear();

        // Finally, delete the book fair
        return deleteById(bookFairId);
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
