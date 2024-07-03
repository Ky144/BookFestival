package hsos.de.swa.waitlistAdministration.control;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import hsos.de.swa.waitlistAdministration.entity.WaitlistEntry;
import hsos.de.swa.waitlistAdministration.gateway.WaitlistRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class WaitlistManagement {

    @Inject
    WaitlistRepository waitlistRepository;

    public List<WaitlistEntry> getWaitlistByBookFairId(long bookFairId) {
        return waitlistRepository.findByBookFairId(bookFairId);
    }

    @Transactional
    public WaitlistEntry addToWaitlist(long bookFairId, Author author) {
        if (waitlistRepository.findByBookFairAndAuthor(bookFairId, author.getId()) != null) {
            throw new IllegalStateException("Author ist bereits auf der Warteliste für diese Buchmesse");
        }
        WaitlistEntry entry = new WaitlistEntry(author, new BookFair());
        entry.getBookFair().setId(bookFairId);
        waitlistRepository.persist(entry);
        return entry;
    }

    @Transactional
    public boolean removeFromWaitlist(long bookFairId, Author author) {
        WaitlistEntry entry = waitlistRepository.findByBookFairAndAuthor(bookFairId, author.getId());
        if (entry != null) {
            waitlistRepository.delete(entry);
            return true;
        }
        return false;
    }

    public WaitlistEntry getNextInLine(long bookFairId) {
        List<WaitlistEntry> waitlist = waitlistRepository.findByBookFairId(bookFairId);
        if (waitlist == null || waitlist.isEmpty()) {
            return null;
        }
        return waitlist.get(0);
    }
}
