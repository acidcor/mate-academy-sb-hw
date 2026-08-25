package mate.academy.hw.mapper;

import mate.academy.hw.config.MapperConfig;
import mate.academy.hw.dto.book.BookRequestDto;
import mate.academy.hw.dto.book.BookResponseDto;
import mate.academy.hw.dto.book.BookResponseDtoWithoutCategoryIds;
import mate.academy.hw.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookResponseDto toDto(Book book);

    BookResponseDtoWithoutCategoryIds toDtoWithoutCategory(Book book);

    Book toEntity(BookResponseDtoWithoutCategoryIds requestDto);

    Book toEntity(BookRequestDto requestDto);

    void updateFromCreateBookRequestDto(BookRequestDto requestDto,
                                        @MappingTarget Book book);
}
