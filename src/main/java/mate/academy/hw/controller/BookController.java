package mate.academy.hw.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.BookDto;
import mate.academy.hw.dto.BookSearchParametersDto;
import mate.academy.hw.dto.CreateBookRequestDto;
import mate.academy.hw.service.BookService;
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
public class BookController {
    private final BookService bookService;

    @Tag(name = "Create")
    @Operation(summary = "Add a new book")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @Tag(name = "find")
    @Operation(summary = "Return all books")
    @GetMapping
    public Page<BookDto> getAll(
            @SortDefault(value = "title", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return bookService.findAll(pageable);
    }

    @Tag(name = "find")
    @Operation(summary = "Return book by ID")
    @GetMapping("{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @Tag(name = "find")
    @Operation(summary = "Return all books with specs")
    @GetMapping("/search")
    public Page<BookDto> search(BookSearchParametersDto bookSearchParametersDto,
                                @SortDefault.SortDefaults({
                                        @SortDefault(value = "title",
                                                direction = Sort.Direction.ASC
                                        ),
                                        @SortDefault(value = "price",
                                                direction = Sort.Direction.DESC
                                        )
                                })
                                Pageable pageable) {
        return bookService.search(bookSearchParametersDto, pageable);
    }

    @Tag(name = "Update")
    @Operation(summary = "Update a single book")
    @PutMapping("{id}")
    public BookDto updateBook(
            @PathVariable Long id,
            @RequestBody @Valid CreateBookRequestDto requestDto
    ) {
        return bookService.update(id, requestDto);
    }

    @Tag(name = "Delete")
    @Operation(summary = "Delete a single book")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
