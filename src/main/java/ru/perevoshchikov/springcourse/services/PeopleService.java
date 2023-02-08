package ru.perevoshchikov.springcourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.perevoshchikov.springcourse.models.Book;
import ru.perevoshchikov.springcourse.models.Person;
import ru.perevoshchikov.springcourse.repositories.BooksRepository;
import ru.perevoshchikov.springcourse.repositories.PeopleRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;
    private final BooksService booksService;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository,
                         BooksRepository booksRepository,
                         BooksService booksService)
    {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
        this.booksService = booksService;
    }

    public List<Person> findAll(boolean sortByName) {
        Sort sort = chooseSort(sortByName);
        return peopleRepository.findAll(sort);
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    Sort chooseSort(boolean sortByName) {
        return sortByName ? Sort.by("fullName") : Sort.unsorted();
    }

    public List<Person> findByTitleStartingWith(Optional<String> startingWith) {
        if (startingWith.isPresent())
            return peopleRepository.findByFullNameStartingWith(startingWith.get());
        else
            return Collections.emptyList();
    }

    public Person findById(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public List<Book> getBooks(int ownerId) {
        List<Book> books = booksRepository.findByOwner(findById(ownerId));
        books.forEach(booksService::setOverdue);
        return books;
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
        getBooks(id).forEach(booksService::release);
    }
}
