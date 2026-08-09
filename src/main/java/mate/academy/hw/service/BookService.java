package mate.academy.hw.service;

import mate.academy.hw.dto.BookDto;
import mate.academy.hw.dto.BookSearchParametersDto;
import mate.academy.hw.dto.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    Page<BookDto> findAll(Pageable pageable);

    Page<BookDto> search(BookSearchParametersDto searchParametersDto, Pageable pageable);

    void delete(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);
}
