package hsos.de.swa.waitlistAdministration.control;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.waitlistAdministration.entity.WaitlistEntry;

import java.util.List;


public interface IWaitlistManagement {
    public List<WaitlistEntry> getWaitlistByBookFairId(long bookFairId);
    public boolean removeFromWaitlist(long bookFairId, Author author);
    public boolean removeFromWaitlistByAuthorId(long bookFairId, long authorId);
    public WaitlistEntry getNextInLine(long bookFairId);
    public WaitlistEntry addToWaitlist(long bookFairId, Author author);
    public void removeAllEntriesForAuthor(long authorId);
}
