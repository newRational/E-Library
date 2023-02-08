package ru.perevoshchikov.springcourse.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be in range between 2 and 100 characters")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Author name cannot be empty")
    @Size(min = 2, max = 100, message = "Author name must be in range between 2 and 100 characters")
    @Column(name = "author")
    private String author;

    @Column(name = "year")
    @Min(-10000)
    @Max(2022)
    private int year;

    @Column(name = "date_of_issuing")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Transient
    private boolean overdue;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    public Book() {
    }

    public Book(String title, String author, int year, Person owner) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String authorFullName) {
        this.author = authorFullName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int yearOfProduction) {
        this.year = yearOfProduction;
    }

    public Optional<Date> getDate() {
        return Optional.ofNullable(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person person) {
        this.owner = person;
    }

    public boolean isUsed() {
        return owner != null;
    }

    @Override
    public String toString() {
        return getId() + " " + getTitle();
    }
}
