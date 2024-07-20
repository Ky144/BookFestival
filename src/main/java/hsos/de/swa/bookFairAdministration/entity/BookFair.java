package hsos.de.swa.bookFairAdministration.entity;

import hsos.de.swa.authorAdministration.entity.Author;
import hsos.de.swa.waitlistAdministration.entity.WaitlistEntry;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class BookFair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String location;
    private LocalDate date;
    private int maxParticipants;
//Quelle: https://stackoverflow.com/questions/2990799/difference-between-fetchtype-lazy-and-eager-in-java-persistence-api
@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
@JoinTable(
        name="bookfair_participants",
        joinColumns = {@JoinColumn(name="bookfair_id")},
        inverseJoinColumns = {@JoinColumn(name="author_id")}
)
private Set<Author> participants = new HashSet<>();


    public void loadParticipants() {
        Hibernate.initialize(participants);
    }


    public BookFair() {}
    public BookFair(String title, String location, LocalDate date, int maxParticipants) {
        this.name = title;
        this.location = location;
        this.date = date;
        this.maxParticipants = maxParticipants;
    }
    public void addParticipant(Author author) {
        participants.add(author);
    }
    public void removeParticipant(Author author) {
        participants.remove(author);
    }
    public boolean isParticipantSignedIn(Author author) {
        return this.participants.contains(author);
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Set<Author> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Author> participants) {
        this.participants = participants;
    }


    public long getId() {
        return id;
    }
    @JsonbTransient
    public int getSignendInAuthorsCount(){
        return this.participants.size();
    }
}
