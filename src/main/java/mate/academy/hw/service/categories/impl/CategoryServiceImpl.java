package mate.academy.hw.service.categories.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.categories.CategoryRequestDto;
import mate.academy.hw.dto.categories.CategoryResponseDto;
import mate.academy.hw.exceptrion.EntityNotFoundException;
import mate.academy.hw.mapper.CategoryMapper;
import mate.academy.hw.model.Category;
import mate.academy.hw.repository.categories.CategoryRepository;
import mate.academy.hw.service.categories.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public Page<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    @Override
    public CategoryResponseDto save(CategoryRequestDto dto) {
        Category category = categoryRepository.save(categoryMapper.toEntity(dto));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto dto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find a categort by id: " + id)
        );
        categoryMapper.updateFromCategoryRequestDto(dto, category);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find a categort by id: " + id)
        );
        categoryRepository.delete(category);
    }

}
