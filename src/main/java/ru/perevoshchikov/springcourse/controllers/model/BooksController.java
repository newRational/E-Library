package ru.perevoshchikov.springcourse.controllers.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.perevoshchikov.springcourse.models.Book;
import ru.perevoshchikov.springcourse.models.Person;
import ru.perevoshchikov.springcourse.services.BooksService;
import ru.perevoshchikov.springcourse.services.PeopleService;
import ru.perevoshchikov.springcourse.util.validators.BookValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookValidator bookValidator;
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BookValidator bookValidator,
                           BooksService booksService,
                           PeopleService peopleService)
    {
        this.bookValidator = bookValidator;
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(@RequestParam(value = "page", required = false) Optional<Integer> page,
                        @RequestParam(value = "books_per_page", required = false) Optional<Integer> bpp,
                        @RequestParam(value = "sort_by_year", required = false) boolean sort,
                        Model model)
    {
        model.addAttribute("books", booksService.findAll(page, bpp, sort));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model)
    {
        Book book = booksService.findById(id);

        if (book.isUsed())
            model.addAttribute("owner", book.getOwner());
        else {
            model.addAttribute("person", new Person());
            model.addAttribute("people", peopleService.findAll());
        }

        model.addAttribute("book", book);

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book)
    {
        return "books/new";
    }


    @GetMapping("/search")
    public String search(@RequestParam(required = false, name="sw") Optional<String> startingWith,
                         Model model)
    {
        List<Book> books = booksService.findByTitleStartingWith(startingWith);
        model.addAttribute("startingWith", startingWith.orElse(null));
        model.addAttribute("books", books);

        return "books/search";
    }

    @PostMapping()
    public String save(@ModelAttribute("book") @Valid Book book,
                       BindingResult bindingResult)
    {
        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors())
            return "books/new";

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("book", booksService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult)
    {
        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);

        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id)
    {
        booksService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id,
                         @ModelAttribute("person") Person selectedPerson)
    {
        booksService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }
}