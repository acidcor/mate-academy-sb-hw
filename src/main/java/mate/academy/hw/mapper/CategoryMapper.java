package mate.academy.hw.mapper;

import mate.academy.hw.config.MapperConfig;
import mate.academy.hw.dto.categories.CategoryRequestDto;
import mate.academy.hw.dto.categories.CategoryResponseDto;
import mate.academy.hw.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryResponseDto dto);

    Category toEntity(CategoryRequestDto dto);

    void updateFromCategoryRequestDto(CategoryRequestDto requestDto,
                                        @MappingTarget Category category);
}
