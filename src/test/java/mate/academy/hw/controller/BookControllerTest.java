package mate.academy.hw.controller;

import mate.academy.hw.dto.book.BookRequestDto;
import mate.academy.hw.dto.book.BookResponseDto;
import mate.academy.hw.model.Category;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
            ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Sql(scripts = {
            "classpath:database/books/add-category.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Create book - valid request, returns response DTO")
    void createBook_ValidRequest_Success() throws Exception {
        // Given
        Category category = new Category();
        category.setId(1L);
        category.setName("test");
        category.setDescription("test");

        BookRequestDto requestDto = new BookRequestDto()
                .setAuthor("Author")
                .setTitle("Test")
                .setIsbn("9789382036470")
                .setPrice(BigDecimal.TEN)
                .setDescription("desc")
                .setCategories(Set.of(category));

        BookResponseDto expect = new BookResponseDto()
                .setAuthor("Author")
                .setTitle("Test")
                .setIsbn("9789382036470")
                .setPrice(BigDecimal.TEN)
                .setDescription("desc")
                .setCategories(Set.of(category));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        // Then

        BookResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto.class);

        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(actual, expect, "id", "categories"));
        assertTrue(EqualsBuilder.reflectionEquals(
                actual.getCategories().toArray()[0],
                expect.getCategories().toArray()[0])
        );
    }

    @Sql(scripts = {
            "classpath:database/books/add-3-book.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-all-books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    @DisplayName("Get all books - valid pageable, returns list")
    void getAll_ValidPageable_Success() throws Exception {
        // Given: In DB saved 3 books

        // When
        MvcResult result = mockMvc.perform(
                get("/books")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        // Then
        JsonNode json = objectMapper.readTree(
                result.getResponse().getContentAsByteArray()
        );

        BookResponseDto[] actual = objectMapper.treeToValue(
                json.get("content"),
                BookResponseDto[].class);

        assertEquals(3, actual.length);
    }

    @Sql(scripts = {
            "classpath:database/books/add-book.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-all-books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    @DisplayName("Get book by id - valid id, returns book")
    void getBookById_ValidId_Success() throws Exception {
        // Given
        Long expectId = 1L;

        // When
        MvcResult result = mockMvc.perform(
                        get("/books/" + expectId)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
        .andReturn();
        BookResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto.class);

        assertNotNull(actual);
        assertEquals(expectId, actual.getId());
    }

    @Sql(scripts = {
            "classpath:database/books/add-book.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-all-books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    @DisplayName("Search books - valid parameters, returns list")
    void search_ValidParameters_Success() throws Exception {
        // When
        MvcResult result = mockMvc.perform(
                        get("/books/search?title=Test")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        // Then
        JsonNode json = objectMapper.readTree(
                result.getResponse().getContentAsByteArray()
        );
        BookResponseDto[] actual = objectMapper.treeToValue(
                json.get("content"),
                BookResponseDto[].class);
        assertEquals(1, actual.length);
    }

    @Sql(scripts = {
            "classpath:database/books/add-category.sql",
            "classpath:database/books/add-book.sql",
            "classpath:database/books/add-category-to-book.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Update book - valid request, returns response DTO")
    void updateBook_ValidRequest_Success() throws Exception {
        // Given
        Long bookId = 1L;
        Category category = new Category();
        category.setId(1L);
        category.setName("test");
        category.setDescription("test");

        BookRequestDto requestDto = new BookRequestDto()
                .setAuthor("New Author")
                .setTitle("New Title")
                .setIsbn("9789382036470")
                .setPrice(BigDecimal.TEN)
                .setDescription("desc")
                .setCategories(Set.of(category));

        BookResponseDto expect = new BookResponseDto()
                .setAuthor("New Author")
                .setTitle("New Title")
                .setIsbn("9789382036470")
                .setPrice(BigDecimal.TEN)
                .setDescription("desc")
                .setCategories(Set.of(category));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(
                        put("/books/" + bookId)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        // Then

        BookResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto.class);

        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(actual, expect, "id", "categories"));
        assertTrue(EqualsBuilder.reflectionEquals(
                actual.getCategories().toArray()[0],
                expect.getCategories().toArray()[0])
        );
    }

    @Sql(scripts = {
            "classpath:database/books/add-book.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-all-books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Delete book - valid id, returns status 204")
    void deleteBook_ValidId_Success() throws Exception {
        // Given 1 book with ID 1 from sql
        // When
        mockMvc.perform(
                delete("/books/1")
        ).andExpect(status().isNoContent());
    }
}