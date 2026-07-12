package mate.academy.hw.service;

import java.util.List;
import mate.academy.hw.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
