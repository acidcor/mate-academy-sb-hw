package mate.academy.hw.controller;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import mate.academy.hw.dto.categories.CategoryRequestDto;
import mate.academy.hw.dto.categories.CategoryResponseDto;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoriesControllerTest {
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
            "classpath:database/books/add-3-categories.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-all-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    @DisplayName("Find all categories - valid request, returns list")
    void findAllCategories_ValidRequest_Success() throws Exception {
        // Given: In DB saved 3 books

        // When
        MvcResult result = mockMvc.perform(
                get("/categories")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        // Then
        JsonNode json = objectMapper.readTree(
                result.getResponse().getContentAsByteArray()
        );

        CategoryResponseDto[] actual = objectMapper.treeToValue(
                json.get("content"),
                CategoryResponseDto[].class);

        assertEquals(3, actual.length);
    }

    @Sql(scripts = {
            "classpath:database/books/add-3-categories.sql",
            "classpath:database/books/add-book.sql",
            "classpath:database/books/add-category-to-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    @DisplayName("Find all books by category - valid id, returns list")
    void findAllBooksByCategory_ValidId_Success() throws Exception {
        // Given
        Long categoryId = 1L;

        // When
        MvcResult result = mockMvc.perform(
                get("/categories/"+ categoryId + "/books")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        // Then
        JsonNode json = objectMapper.readTree(
                result.getResponse().getContentAsByteArray()
        );

        JsonNode content = json.get("content");
        assertEquals(1, content.size());
    }

    @Sql(scripts = {
            "classpath:database/books/add-3-categories.sql",
            "classpath:database/books/add-book.sql",
            "classpath:database/books/add-category-to-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    @DisplayName("Find all books by category - invalid id, returns empty list")
    void findAllBooksByCategory_InvalidId_Success() throws Exception {
        // Given
        Long categoryId = 4L;

        // When
        MvcResult result = mockMvc.perform(
                get("/categories/"+ categoryId + "/books")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        // Then
        JsonNode json = objectMapper.readTree(
                result.getResponse().getContentAsByteArray()
        );

        JsonNode content = json.get("content");
        assertEquals(0, content.size());
    }

    @Sql(scripts = {
            "classpath:database/books/delete-all-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Create category - valid request, returns response DTO")
    void createCategory_ValidRequest_Success() throws Exception {
        // Given
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("New Category");
        requestDto.setDescription("New Desc");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(
                post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        // Then
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryResponseDto.class);

        assertEquals("New Category", actual.name());
    }

    @Sql(scripts = {
            "classpath:database/books/add-3-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-all-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Update category - valid request, returns response DTO")
    void updateCategory_ValidRequest_Success() throws Exception {
        // Given
        Long categoryId = 1L;
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Updated Category");
        requestDto.setDescription("Updated Desc");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(
                put("/categories/" + categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        // Then
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryResponseDto.class);

        assertEquals("Updated Category", actual.name());
    }

    @Sql(scripts = {
            "classpath:database/books/add-3-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/delete-all-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Delete category - valid id, returns status 200")
    void deleteCategory_ValidId_Success() throws Exception {
        // Given
        Long categoryId = 1L;

        // When
        mockMvc.perform(
                delete("/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}