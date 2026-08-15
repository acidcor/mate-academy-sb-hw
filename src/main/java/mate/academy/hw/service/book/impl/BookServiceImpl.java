package mate.academy.hw.service.book.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.book.BookCreateRequestDto;
import mate.academy.hw.dto.book.BookResponseDto;
import mate.academy.hw.dto.book.BookSearchParametersDto;
import mate.academy.hw.exceptrion.EntityNotFoundException;
import mate.academy.hw.mapper.BookMapper;
import mate.academy.hw.model.Book;
import mate.academy.hw.repository.book.BookRepository;
import mate.academy.hw.repository.specifiacation.book.BookSpecificationBuilder;
import mate.academy.hw.service.book.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder specificationBuilder;

    @Override
    public BookResponseDto save(BookCreateRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(repository.save(book));
    }

    @Override
    public BookResponseDto findById(Long id) {
        return bookMapper.toDto(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find a book by id: " + id)));
    }

    @Override
    public Page<BookResponseDto> findAll(Pageable pageable) {
        List<BookResponseDto> list = repository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
        return new PageImpl<>(list);
    }

    @Override
    public Page<BookResponseDto> search(
            BookSearchParametersDto searchParametersDto,
            Pageable pageable
    ) {
        Specification<Book> specification = specificationBuilder.build(searchParametersDto);
        List<BookResponseDto> list = repository.findAll(specification, pageable).stream()
                .map(bookMapper::toDto)
                .toList();
        return new PageImpl<>(list);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public BookResponseDto update(Long id, BookCreateRequestDto requestDto) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find a book by id: " + id));
        bookMapper.updateFromCreateBookRequestDto(requestDto, book);
        return bookMapper.toDto(repository.save(book));
    }
}
