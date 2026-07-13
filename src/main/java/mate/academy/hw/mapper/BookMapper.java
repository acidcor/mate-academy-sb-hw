package mate.academy.hw.mapper;

import mate.academy.hw.config.MapperConfig;
import mate.academy.hw.dto.BookDto;
import mate.academy.hw.dto.CreateBookRequestDto;
import mate.academy.hw.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateFromCreateBookRequestDto(CreateBookRequestDto requestDto,
                                           @MappingTarget Book book);
}
