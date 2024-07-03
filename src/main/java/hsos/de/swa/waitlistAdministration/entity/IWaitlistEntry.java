package hsos.de.swa.waitlistAdministration.entity;

import hsos.de.swa.authorAdministration.control.dto.AuthorDTO;
import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.control.dto.BookFairDTO;
import hsos.de.swa.bookFairAdministration.entity.BookFair;

import java.util.Collection;
import java.util.List;

public interface IWaitlistEntry {
    /*public Collection<WaitlistEntry> findByBookFair(BookFair bookFair);
    public void addAuthorToWaitlist(Author author, BookFair bookFair);
    public void removeAuthorFromWaitlist(WaitlistEntry waitlistEntry);
    public WaitlistEntry getFirstInWaitlist(BookFair bookFair);

     */
    public List<WaitlistEntry> findByBookFairId(long bookFairId);
    public WaitlistEntry findByBookFairAndAuthor(long bookFairId, long authorId);

}
