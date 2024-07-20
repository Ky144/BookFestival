package hsos.de.swa.waitlistAdministration.control;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import hsos.de.swa.waitlistAdministration.entity.WaitlistEntry;
import hsos.de.swa.waitlistAdministration.gateway.SignOutWaitlistResult;
import hsos.de.swa.waitlistAdministration.gateway.WaitlistRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.delete;

@ApplicationScoped
public class WaitlistManagement implements IWaitlistManagement {

    @Inject
    WaitlistRepository waitlistRepository;

    @Override
    public List<WaitlistEntry> getWaitlistByBookFairId(long bookFairId) {
        return waitlistRepository.findByBookFairId(bookFairId);
    }


    @Transactional
    @Override
    public WaitlistEntry addToWaitlist(long bookFairId, Author author) {
        WaitlistEntry entry = new WaitlistEntry(author, new BookFair());
        entry.getBookFair().setId(bookFairId);
        waitlistRepository.persist(entry);
        return entry;
    }

    @Transactional
    @Override
    public boolean removeFromWaitlist(long bookFairId, Author author) {
        return waitlistRepository.signOutWaitlist(bookFairId, author.getId()) == SignOutWaitlistResult.SIGN_OUT;
    }

    @Transactional
    @Override
    public boolean removeFromWaitlistByAuthorId(long bookFairId, long authorId) {
        return waitlistRepository.signOutWaitlist(bookFairId, authorId) == SignOutWaitlistResult.SIGN_OUT;
    }
    public WaitlistEntry getNextInLine(long bookFairId) {
        List<WaitlistEntry> waitlist = waitlistRepository.findByBookFairId(bookFairId);
        if (waitlist == null || waitlist.isEmpty()) {
            return null;
        }
        return waitlist.get(0);
    }

    @Transactional
    @Override
    public void removeAllEntriesForAuthor(long authorId) {
        waitlistRepository.removeAllEntriesForAuthor(authorId);
    }

}
