package hsos.de.swa;

import com.google.inject.Inject;
import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.waitlistAdministration.control.WaitlistManagement;
import hsos.de.swa.waitlistAdministration.entity.WaitlistEntry;
import hsos.de.swa.waitlistAdministration.gateway.WaitlistRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.wildfly.common.Assert.assertNotNull;
import static org.wildfly.common.Assert.assertTrue;

@QuarkusTest
public class WaitlistTest {
    @Inject
    WaitlistManagement waitlistManagement;

    @Inject
    WaitlistRepository waitlistRepository;

    @BeforeEach
    @Transactional
    public void setup() {
        waitlistRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testAddToWaitlist() {
        Author author = new Author();
        author.setId(1L);
        long bookFairId = 1L;

        WaitlistEntry entry = waitlistManagement.addToWaitlist(bookFairId, author);
        assertNotNull(entry);
        assertEquals(author.getId(), entry.getAuthor().getId());
    }

    @Test
    @Transactional
    public void testRemoveFromWaitlist() {
        Author author = new Author();
        author.setId(1L);
        long bookFairId = 1L;

        waitlistManagement.addToWaitlist(bookFairId, author);
        boolean removed = waitlistManagement.removeFromWaitlist(bookFairId, author);
        assertTrue(removed);
    }
}
