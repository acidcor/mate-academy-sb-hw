package mate.academy.hw.service;

import java.util.List;
import mate.academy.hw.dto.BookDto;
import mate.academy.hw.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> findAll();
}
