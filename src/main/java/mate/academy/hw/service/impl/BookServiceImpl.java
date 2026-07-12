package mate.academy.hw.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.hw.model.Book;
import mate.academy.hw.repository.BookRepository;
import mate.academy.hw.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository repository;

    @Override
    public Book save(Book book) {
        return repository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }
}
