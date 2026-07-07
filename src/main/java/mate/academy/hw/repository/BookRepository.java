package mate.academy.hw.repository;

import java.util.List;
import mate.academy.hw.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
