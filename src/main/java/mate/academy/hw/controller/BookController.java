package mate.academy.hw.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.book.BookCreateRequestDto;
import mate.academy.hw.dto.book.BookResponseDto;
import mate.academy.hw.dto.book.BookSearchParametersDto;
import mate.academy.hw.service.book.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
@Tag(
        name = "Books",
        description = "Provide CRUD operations related to books"
)
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Add a new book")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public BookResponseDto createBook(@RequestBody @Valid BookCreateRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @Operation(summary = "Return all books")
    @GetMapping
    public Page<BookResponseDto> getAll(
            @SortDefault(value = "title", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Return book by ID")
    @GetMapping("{id}")
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @Operation(summary = "Return all books with specs")
    @GetMapping("/search")
    public Page<BookResponseDto> search(BookSearchParametersDto bookSearchParametersDto,
                                        @SortDefault.SortDefaults({
                                            @SortDefault(value = "title",
                                                direction = Sort.Direction.ASC),
                                            @SortDefault(value = "price",
                                                direction = Sort.Direction.DESC)
                                        })
                                Pageable pageable) {
        return bookService.search(bookSearchParametersDto, pageable);
    }

    @Operation(summary = "Update a single book")
    @PutMapping("{id}")
    public BookResponseDto updateBook(
            @PathVariable Long id,
            @RequestBody @Valid BookCreateRequestDto requestDto
    ) {
        return bookService.update(id, requestDto);
    }

    @Operation(summary = "Delete a single book")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
