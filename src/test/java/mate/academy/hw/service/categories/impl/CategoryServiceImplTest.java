package mate.academy.hw.service.categories.impl;

import mate.academy.hw.dto.categories.CategoryResponseDto;
import mate.academy.hw.dto.categories.CategoryRequestDto;
import mate.academy.hw.exceptrion.EntityNotFoundException;
import mate.academy.hw.mapper.CategoryMapper;
import mate.academy.hw.model.Category;
import mate.academy.hw.repository.categories.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Find all categories - valid pageable, returns page")
    void findAll_ValidPageable_Success() {
        // Given
        Pageable pageable = Pageable.ofSize(1);
        Category category = new Category();
        CategoryResponseDto response = new CategoryResponseDto(1L, "", "");

        when(categoryRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(category)));
        when(categoryMapper.toDto(category)).thenReturn(response);
        
        // When 
        categoryService.findAll(pageable);
        
        // Then
        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Save category - valid request, returns response DTO")
    void save_ValidRequest_Success() {
        // Given
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("name");
        requestDto.setDescription("desc");
        Category category = new Category();
        CategoryResponseDto responseDto = new CategoryResponseDto(1L, "name", "desc");

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        // When
        CategoryResponseDto actual = categoryService.save(requestDto);

        // Then
        assertEquals(responseDto, actual);
        verify(categoryMapper).toEntity(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Update category - valid id, returns updated category")
    void update_ValidId_Success() {
        // Given
        Long id = 1L;
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("newName");
        requestDto.setDescription("newDesc");
        Category category = new Category();
        CategoryResponseDto responseDto = new CategoryResponseDto(id, "newName", "newDesc");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        // When
        CategoryResponseDto actual = categoryService.update(id, requestDto);

        // Then
        assertEquals(responseDto, actual);
        verify(categoryRepository).findById(id);
        verify(categoryMapper).updateFromCategoryRequestDto(requestDto, category);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Update category - invalid id, throws exception")
    void update_InvalidId_ThrowsException() {
        // Given
        Long id = 1L;
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("name");
        requestDto.setDescription("desc");
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class, () -> categoryService.update(id, requestDto));
    }

    @Test
    @DisplayName("Delete category - valid id, deletes category")
    void delete_ValidId_Success() {
        // Given
        Long id = 1L;
        Category category = new Category();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        // When
        categoryService.delete(id);

        // Then
        verify(categoryRepository).findById(id);
        verify(categoryRepository).delete(category);
    }

    @Test
    @DisplayName("Delete category - invalid id, throws exception")
    void delete_InvalidId_ThrowsException() {
        // Given
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> categoryService.delete(id));
    }
}