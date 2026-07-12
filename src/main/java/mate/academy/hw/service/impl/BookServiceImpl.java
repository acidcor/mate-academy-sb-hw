package mate.academy.hw.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.BookDto;
import mate.academy.hw.dto.CreateBookRequestDto;
import mate.academy.hw.exceptrion.EntityNotFoundException;
import mate.academy.hw.mapper.BookMapper;
import mate.academy.hw.model.Book;
import mate.academy.hw.repository.BookRepository;
import mate.academy.hw.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(repository.save(book));
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(repository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find a book by id: " + id)));
    }

    @Override
    public List<BookDto> findAll() {
        return repository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
