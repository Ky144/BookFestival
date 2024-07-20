package hsos.de.swa.waitlistAdministration.gateway;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import hsos.de.swa.waitlistAdministration.entity.WaitlistEntry;
import hsos.de.swa.waitlistAdministration.entity.IWaitlistRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class WaitlistRepository implements IWaitlistRepository, PanacheRepository<WaitlistEntry> {

    @Inject
    private EntityManager em;

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
    public WaitlistEntry addToWaitlist(long bookFairId, Author author) {
        try {
            BookFair bookFair = em.find(BookFair.class, bookFairId);
            if (bookFair == null) {
                return null;
            }
            WaitlistEntry entry = new WaitlistEntry(author, bookFair);
            persist(entry);
            return entry;
        } catch (Exception e) {
            return null;
        }
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

    @Transactional
    @Override
    public void removeAllEntriesForAuthor(long authorId) {
        em.createQuery("DELETE FROM WaitlistEntry w WHERE w.author.id = :authorId")
                .setParameter("authorId", authorId)
                .executeUpdate();
    }
}
