package mate.academy.hw.service.book;

import mate.academy.hw.dto.book.BookRequestDto;
import mate.academy.hw.dto.book.BookResponseDto;
import mate.academy.hw.dto.book.BookResponseDtoWithoutCategoryIds;
import mate.academy.hw.dto.book.BookSearchParametersDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(BookRequestDto requestDto);

    BookResponseDto findById(Long id);

    Page<BookResponseDto> findAll(Pageable pageable);

    Page<BookResponseDto> search(BookSearchParametersDto searchParametersDto, Pageable pageable);

    void delete(Long id);

    BookResponseDto update(Long id, BookRequestDto requestDto);

    Page<BookResponseDtoWithoutCategoryIds> findAllBooksByCategory(
            Long categoryId,
            Pageable pageable
    );
}
