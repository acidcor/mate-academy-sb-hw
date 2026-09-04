package mate.academy.hw.service.book.impl;

import mate.academy.hw.dto.book.BookRequestDto;
import mate.academy.hw.dto.book.BookResponseDto;
import mate.academy.hw.dto.book.BookResponseDtoWithoutCategoryIds;
import mate.academy.hw.dto.book.BookSearchParametersDto;
import mate.academy.hw.exceptrion.EntityNotFoundException;
import mate.academy.hw.mapper.BookMapper;
import mate.academy.hw.model.Book;
import mate.academy.hw.model.Category;
import mate.academy.hw.repository.book.BookRepository;
import mate.academy.hw.repository.categories.CategoryRepository;
import mate.academy.hw.repository.specifiacation.book.BookSpecificationBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save book - valid request, returns response DTO")
    void save_ValidRequest_Success() {
        // Given
        BookRequestDto bookRequest = new BookRequestDto();
        bookRequest.setIsbn("123123123");
        Category expectedCategory = new Category();
        expectedCategory.setId(1L);
        expectedCategory.setName("TestName");
        Set<Category> categories = Set.of(expectedCategory);
        bookRequest.setCategories(categories);
        bookRequest.setAuthor("Test");
        bookRequest.setTitle("Test");
        bookRequest.setPrice(BigDecimal.valueOf(11.99));
        Book expected = new Book();
        expected.setCategories(categories);
        expected.setAuthor("Test");
        expected.setTitle("Test");
        expected.setPrice(BigDecimal.valueOf(11.99));
        BookResponseDto responseDto = new BookResponseDto();
        responseDto.setCategories(categories);
        responseDto.setAuthor("Test");
        responseDto.setTitle("Test");
        responseDto.setPrice(BigDecimal.valueOf(11.99));

        Mockito.when(bookMapper.toEntity(any(BookRequestDto.class))).thenReturn(expected);
        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(expected);
        Mockito.when(categoryRepository.findAllById(Set.of(1L))).thenReturn(categories.stream().toList());
        Mockito.when(bookMapper.toDto(any(Book.class))).thenReturn(responseDto);

        // When
        BookResponseDto actual = bookService.save(bookRequest);

        // Then
        assertTrue(EqualsBuilder.reflectionEquals(responseDto, actual, "id"));

    }

    @Test
    @DisplayName("Find book by id - invalid id, throws exception")
    void findById_InvalidId_ThrowsException() {
        // Given
        Long unexistedId = anyLong();

        // When
        Mockito.when(bookRepository.findById(unexistedId)).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class, () -> bookService.findById(unexistedId));
    }

    @Test
    @DisplayName("Update book - invalid id, throws exception")
    void update_InvalidId_ThrowsException() {
        // given
        Long wrongId = 9L;
        BookRequestDto requestDto = new BookRequestDto();

        // When
        when(bookRepository.findById(wrongId)).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class, () -> bookService.update(wrongId, requestDto));

    }

    @Test
    @DisplayName("Find all books - valid pageable, returns page")
    void findAll_ValidPageable_Success() {
        // Given
        Book book = new Book();
        Page<Book> books = new PageImpl<>(List.of(book));
        Pageable pageable = Pageable.ofSize(1);

        when(bookRepository.findAll(pageable)).thenReturn(books);
        when(bookMapper.toDto(book)).thenReturn(new BookResponseDto());

        // When
        bookService.findAll(pageable);

        // Then
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
    }



    @Test
    @DisplayName("Update book - valid id, returns updated book")
    void update_ValidId_Success() {
        // Given
        Long correctId = 1L;

        BookRequestDto request = new BookRequestDto();
        Book book = new Book();
        BookResponseDto responseDto = Mockito.spy();

        when(bookRepository.save(book)).thenReturn(book);
        when(bookRepository.findById(correctId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(responseDto);

        // When
        BookResponseDto actual = bookService.update(correctId, request);

        // Then
        assertNotNull(actual);
        verify(bookRepository).findById(correctId);
        verify(bookMapper).updateFromCreateBookRequestDto(request, book);
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("Find all books by category - valid id, returns page")
    void findAllBooksByCategory_ValidId_Success() {
        // Given
        Long categoryId = 1L;
        Pageable pageable = Pageable.ofSize(1);
        Book book = new Book();
        Page<Book> books = new PageImpl<>(List.of(book));

        when(bookRepository.findAllBooksByCategory(pageable, categoryId)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategory(book)).thenReturn(new BookResponseDtoWithoutCategoryIds());

        // When
        Page<BookResponseDtoWithoutCategoryIds> actual = bookService.findAllBooksByCategory(categoryId, pageable);

        // Then
        assertNotNull(actual);
        verify(bookRepository).findAllBooksByCategory(pageable, categoryId);
        verify(bookMapper).toDtoWithoutCategory(book);
    }

    @Test
    @DisplayName("Search books - valid parameters, returns page")
    void search_ValidParams_Success() {
        // Given
        Pageable pageable = Pageable.ofSize(1);
        String[] array = {};
        BookSearchParametersDto dto = new BookSearchParametersDto(array, array, array);
        Specification<Book> spec = Mockito.mock(Specification.class);
        BookResponseDto bookResponseDto = new BookResponseDto();
        Book book = new Book();
        List<Book> listBook = List.of(book);

        // When
        when(bookSpecificationBuilder.build(dto)).thenReturn(spec);
        when(bookRepository.findAll(spec,pageable)).thenReturn(new PageImpl<Book>(listBook));
        when(bookMapper.toDto(book)).thenReturn(bookResponseDto);
        Page<BookResponseDto> actual = bookService.search(dto, pageable);

        // Then
        assertNotNull(actual);
        verify(bookSpecificationBuilder).build(dto);
        verify(bookRepository).findAll(spec, pageable);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Delete book - valid id, deletes book")
    void delete_ValidId_Success() {
        // Given
        Long id = 1L;

        // When
        bookService.delete(id);

        // Then
        verify(bookRepository).deleteById(id);
    }
}