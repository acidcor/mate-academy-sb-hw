package mate.academy.hw.repository.impl;

import mate.academy.hw.dto.BookSearchParametersDto;
import mate.academy.hw.model.Book;
import mate.academy.hw.repository.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    @Override
    public Specification<Book> builder(BookSearchParametersDto bookSearchParametersDto) {
        return null;
    }
}
