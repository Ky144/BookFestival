package hsos.de.swa.waitlistAdministration.gateway;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import hsos.de.swa.waitlistAdministration.control.WaitlistManagement;
import hsos.de.swa.waitlistAdministration.entity.IWaitlistEntryRepository;
import hsos.de.swa.waitlistAdministration.entity.WaitlistEntry;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class WaitlistRepository implements IWaitlistEntryRepository,PanacheRepository<WaitlistEntry> {
    @Inject
    private EntityManager em;
    @Inject
    private WaitlistManagement WaitlistManagement;

    @Override
    public List<WaitlistEntry> findByBookFairId(long bookFairId) {
        return find("bookFair.id", bookFairId).list();
    }

    @Override
    public WaitlistEntry findByBookFairAndAuthor(long bookFairId, long authorId) {
        return find("bookFair.id = ?1 and author.id = ?2", bookFairId, authorId).firstResult();
    }

    @Override
    @Transactional
    public SignOutWaitlistResult signOutWaitlist(long bookFairId, long authorId) {
        WaitlistEntry entry = findByBookFairAndAuthor(bookFairId, authorId);
        if (entry != null) {
            delete(entry);
            return SignOutWaitlistResult.SIGN_OUT;
        }
        return SignOutWaitlistResult.NOT_SIGNED_IN;

    }
    @Override
    @Transactional
    public WaitlistEntry addToWaitlist(long bookFairId, Author author) {
        WaitlistEntry entry = new WaitlistEntry(author, em.find(BookFair.class, bookFairId));
        persist(entry);
        return entry;
    }

    @Override
    @Transactional
    public boolean removeFromWaitlist(long bookFairId, Author author) {
        return signOutWaitlist(bookFairId, author.getId()) == SignOutWaitlistResult.SIGN_OUT;
    }
}
