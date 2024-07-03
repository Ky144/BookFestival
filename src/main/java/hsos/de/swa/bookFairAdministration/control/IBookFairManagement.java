package hsos.de.swa.bookFairAdministration.control;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.control.dto.BookFairDTO;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import hsos.de.swa.bookFairAdministration.gateway.RegistrationResult;
import hsos.de.swa.bookFairAdministration.gateway.SignOutResult;

import java.util.Collection;

public interface IBookFairManagement {
    public Collection<BookFair> getAllBookFairs();
    public Collection<BookFair> getBookFairByName(String name);

    public BookFair getBookFairById(long bookFair_id);
    public BookFair addBookFair(BookFairDTO bookFairDTO);
    public BookFair editBookFair(long bookFair_id, BookFairDTO bookFairDTO);
    public boolean deleteBookFair(long bookFair_id);
    public RegistrationResult signIn(long bookFair_id, long author_id);
    public SignOutResult signOut(long bookFair_id, long author_id);
    public Collection<BookFair> getBookFairsOfAuthor(long author_id);
    public Collection<Author> getParticipantsByBookFairId(long bookFair_id);

}
