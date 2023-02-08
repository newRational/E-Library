package ru.perevoshchikov.springcourse.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.perevoshchikov.springcourse.models.Book;
import ru.perevoshchikov.springcourse.services.BooksService;

@Component
public class BookValidator implements Validator {
    private final BooksService booksService;

    @Autowired
    public BookValidator(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public boolean supports(Class<?> entireClass) {
        return Book.class.equals(entireClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;

        if (!booksService.findByTitle(book.getTitle()).isEmpty())
            errors.rejectValue("name", "", "Book name must be unique");
    }
}
