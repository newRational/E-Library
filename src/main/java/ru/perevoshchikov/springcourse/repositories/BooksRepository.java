package ru.perevoshchikov.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.perevoshchikov.springcourse.models.Book;
import ru.perevoshchikov.springcourse.models.Person;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    List<Book> findByTitle(String title);
    List<Book> findByTitleStartingWith(String startingWith);
    List<Book> findByOwner(Person owner);
}
