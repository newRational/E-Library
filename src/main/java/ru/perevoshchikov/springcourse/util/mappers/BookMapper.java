package ru.perevoshchikov.springcourse.util.mappers;

import org.springframework.stereotype.Component;
import ru.perevoshchikov.springcourse.models.Book;

@Component
public class BookMapper implements Mapper<Book, Book> {

    @Override
    public void map(Book dest, Book source) {
        dest.setId(source.getId());
        dest.setAuthor(source.getAuthor());
        dest.setTitle(source.getTitle());
        dest.setYear(source.getYear());
    }
}
