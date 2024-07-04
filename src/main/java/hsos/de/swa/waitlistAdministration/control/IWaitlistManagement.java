package hsos.de.swa.waitlistAdministration.control;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.waitlistAdministration.entity.WaitlistEntry;

import java.util.List;


public interface IWaitlistManagement {
    /*public Collection<WaitlistEntry>getWaitlistEntries(long bookFair_id);
    public void addAuthorToWaitlist(BookFair bookFair, Author author);
    public void removeAuthorFromWaitlist(BookFair bookFair, Author author);
    public void processWaitlistEntries(BookFair bookFair);

     */
    public List<WaitlistEntry> getWaitlistByBookFairId(long bookFairId);
    public WaitlistEntry addToWaitlist(long bookFairId, Author author);
    public boolean removeFromWaitlist(long bookFairId, Author author);
    public boolean removeFromWaitlistByAuthorId(long bookFairId, long authorId);

}
