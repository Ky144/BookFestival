package hsos.de.swa.bookFairAdministration.entity;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.control.dto.BookFairDTO;
import hsos.de.swa.bookFairAdministration.gateway.RegistrationResult;
import hsos.de.swa.bookFairAdministration.gateway.SignOutResult;

import java.util.Collection;

public interface IBookFairRepository {
    Collection<BookFair> getAllBookFairs();
    BookFair getBookFairById(long bookFair_id);
    Collection<BookFair> getBookFairByName(String name);
    BookFair add(BookFairDTO bookFairDTO);
    BookFair edit(long bookFair_id, BookFairDTO bookFairDTO);
    boolean delete(long bookFair_id);
    Collection<BookFair> getBookFairsOfAuthor(long author_id);
    RegistrationResult signIn(long bookFair_id, long author_id);
    SignOutResult signOut(long bookFair_id, long author_id);
    Collection<Author> getParticipantsByBookFairId(long bookFair_id);
}
