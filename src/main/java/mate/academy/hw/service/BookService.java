package mate.academy.hw.service;

import java.util.List;
import mate.academy.hw.dto.BookDto;
import mate.academy.hw.dto.BookSearchParametersDto;
import mate.academy.hw.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> findAll(Pageable pageable);

    List<BookDto> search(BookSearchParametersDto searchParametersDto);

    void delete(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);
}
