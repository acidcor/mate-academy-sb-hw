package mate.academy.hw.service.categories;

import mate.academy.hw.dto.categories.CategoryRequestDto;
import mate.academy.hw.dto.categories.CategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto save(CategoryRequestDto dto);

    CategoryResponseDto update(Long id, CategoryRequestDto dto);

    void delete(Long id);
}
