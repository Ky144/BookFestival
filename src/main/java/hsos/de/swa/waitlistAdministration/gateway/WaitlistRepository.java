package hsos.de.swa.waitlistAdministration.gateway;

import hsos.de.swa.waitlistAdministration.entity.WaitlistEntry;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class WaitlistRepository implements PanacheRepository<WaitlistEntry> {

    public List<WaitlistEntry> findByBookFairId(long bookFairId) {
        return find("bookFair.id", bookFairId).list();
    }

    public WaitlistEntry findByBookFairAndAuthor(long bookFairId, long authorId) {
        return find("bookFair.id = ?1 and author.id = ?2", bookFairId, authorId).firstResult();
    }
}
