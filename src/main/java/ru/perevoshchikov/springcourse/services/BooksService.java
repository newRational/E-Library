package ru.perevoshchikov.springcourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.perevoshchikov.springcourse.models.Book;
import ru.perevoshchikov.springcourse.models.Person;
import ru.perevoshchikov.springcourse.repositories.BooksRepository;
import ru.perevoshchikov.springcourse.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository,
                        PeopleRepository peopleRepository)
    {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll(Optional<Integer> page, Optional<Integer> booksPerPage, boolean sortByYear) {
        Sort sort = chooseSort(sortByYear);
        if (page.isPresent())
            return booksRepository.findAll(PageRequest.of(page.get(), booksPerPage.get(), sort)).getContent();
        else
            return booksRepository.findAll(sort);
    }

    public void setOverdue(Book book) {
        if (book.getDate().isEmpty())
            book.setOverdue(false);
        long passed = new Date().getTime() - book.getDate().get().getTime();
        book.setOverdue(msToDays(passed) > 10);
    }

    int msToDays(long ms) {
        return (int) (ms / 1000 / 60 / 60 / 24);
    }

    Sort chooseSort(boolean sortByYear) {
        return sortByYear ? Sort.by("year") : Sort.unsorted();
    }

    public List<Book> findByTitleStartingWith(Optional<String> startingWith) {
        if (startingWith.isPresent())
            return booksRepository.findByTitleStartingWith(startingWith.get());
        else
            return Collections.emptyList();
    }

    public List<Book> findByTitle(String title) {
        return booksRepository.findByTitle(title);
    }

    public Book findById(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = booksRepository.getReferenceById(id);

        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());

        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void release(int id) {
        Book book = booksRepository.getReferenceById(id);
        release(book);
    }

    @Transactional
    public void release(Book book) {
        book.setOwner(null);
        book.setDate(null);
        book.setOverdue(false);
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        Book book = booksRepository.getReferenceById(id);
        Person newBookOwner = peopleRepository.getReferenceById(selectedPerson.getId());
        book.setOwner(newBookOwner);
        book.setDate(new Date());
        newBookOwner.getBooks().add(book);
    }
}
