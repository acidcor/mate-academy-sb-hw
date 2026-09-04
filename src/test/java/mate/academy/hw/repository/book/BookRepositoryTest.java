package mate.academy.hw.repository.book;

import mate.academy.hw.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by category")
    @Sql(scripts = {
            "classpath:database/books/add-book.sql",
            "classpath:database/books/add-category.sql",
            "classpath:database/books/add-category-to-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllBooksByCategory_ValidId_Success() {
        // Given
        Long bookId = 1L;
        Long categoryId = 1L;

        // When
        Pageable pageable = Pageable.ofSize(1);
        List<Book> actual = bookRepository
                .findAllBooksByCategory(pageable,
                        categoryId
                ).stream().toList();

        // Then
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(bookId, actual.get(0).getId());
    }
}