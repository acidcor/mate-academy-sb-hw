package mate.academy.hw.mapper;

import mate.academy.hw.config.MapperConfig;
import mate.academy.hw.dto.book.BookCreateRequestDto;
import mate.academy.hw.dto.book.BookResponseDto;
import mate.academy.hw.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookResponseDto toDto(Book book);

    Book toModel(BookCreateRequestDto requestDto);

    void updateFromCreateBookRequestDto(BookCreateRequestDto requestDto,
                                        @MappingTarget Book book);
}
