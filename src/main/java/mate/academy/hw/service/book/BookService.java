package mate.academy.hw.service.book;

import mate.academy.hw.dto.book.BookCreateRequestDto;
import mate.academy.hw.dto.book.BookResponseDto;
import mate.academy.hw.dto.book.BookSearchParametersDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(BookCreateRequestDto requestDto);

    BookResponseDto findById(Long id);

    Page<BookResponseDto> findAll(Pageable pageable);

    Page<BookResponseDto> search(BookSearchParametersDto searchParametersDto, Pageable pageable);

    void delete(Long id);

    BookResponseDto update(Long id, BookCreateRequestDto requestDto);
}
