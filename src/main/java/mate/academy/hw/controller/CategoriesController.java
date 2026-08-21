package mate.academy.hw.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.book.BookResponseDtoWithoutCategoryIds;
import mate.academy.hw.dto.categories.CategoryRequestDto;
import mate.academy.hw.dto.categories.CategoryResponseDto;
import mate.academy.hw.service.book.BookService;
import mate.academy.hw.service.categories.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
@Tag(
        name = "Categories",
        description = "Provide CRUD operations related to categories"
)
public class CategoriesController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @Operation(summary = "Find all categories")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public Page<CategoryResponseDto> findAllCategories(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(summary = "Find all books by category ID")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}/books")
    public Page<BookResponseDtoWithoutCategoryIds> findAllBooksByCategory(
            Pageable pageable, @PathVariable Long id
    ) {
        return bookService.findAllBooksByCategory(id, pageable);
    }

    @Operation(summary = "Create a new category")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public CategoryResponseDto createCategory(
            @RequestBody @Valid CategoryRequestDto dto
    ) {
        return categoryService.save(dto);
    }

    @Operation(summary = "Update a category by ID")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public CategoryResponseDto updateCategory(
            @PathVariable Long id,@RequestBody @Valid CategoryRequestDto dto
    ) {
        return categoryService.update(id, dto);
    }

    @Operation(summary = "Delete a category by ID")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
