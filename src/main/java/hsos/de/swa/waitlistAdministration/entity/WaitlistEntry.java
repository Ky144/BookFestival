package hsos.de.swa.waitlistAdministration.entity;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.bookFairAdministration.entity.BookFair;
import jakarta.persistence.*;

@Entity
@Table(name="waitlist")
public class WaitlistEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "bookfair_id", nullable = false)
    private BookFair bookFair;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    public WaitlistEntry() {}

    public WaitlistEntry(Author author, BookFair bookFair) {
        this.author = author;
        this.bookFair = bookFair;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public BookFair getBookFair() {
        return bookFair;
    }

    public void setBookFair(BookFair bookFair) {
        this.bookFair = bookFair;
    }
}
