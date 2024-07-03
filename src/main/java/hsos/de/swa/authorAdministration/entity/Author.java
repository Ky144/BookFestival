package hsos.de.swa.authorAdministration.entity;

import hsos.de.swa.bookFairAdministration.entity.BookFair;
import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Author{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstname;
    private String lastname;

    @ManyToMany(mappedBy = "participants")
    private Set<BookFair> bookFairs = new HashSet<>();

    // Konstruktoren
    public Author() {
    }

    public Author(String firstname, String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
    }

    // Getter und Setter
    public Long getId(){
        return id;
    }

    public String getFirstname(){
        return firstname;
    }

    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    public String getLastname(){
        return lastname;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    public Set<BookFair> getBookFairs() {
        return bookFairs;
    }

    public void addBookFair(BookFair bookFair) {
        this.bookFairs.add(bookFair);
        bookFair.getParticipants().add(this);
    }

    public void removeBookFair(BookFair bookFair) {
        this.bookFairs.remove(bookFair);
        bookFair.getParticipants().remove(this);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBookFairs(Set<BookFair> bookFairs) {
        this.bookFairs = bookFairs;
    }
}
