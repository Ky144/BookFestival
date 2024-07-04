package hsos.de.swa.waitlistAdministration.entity;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.waitlistAdministration.gateway.SignOutWaitlistResult;

import java.util.List;

public interface IWaitlistEntryRepository {

    public List<WaitlistEntry> findByBookFairId(long bookFairId);
    public WaitlistEntry findByBookFairAndAuthor(long bookFairId, long authorId);
    public SignOutWaitlistResult signOutWaitlist(long bookFairId, long authorId);
    public WaitlistEntry addToWaitlist(long bookFairId, Author author);
    public boolean removeFromWaitlist(long bookFairId, Author author);
}
