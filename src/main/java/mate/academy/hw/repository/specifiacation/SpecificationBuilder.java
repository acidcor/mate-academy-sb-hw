package mate.academy.hw.repository.specifiacation;

import mate.academy.hw.dto.book.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParametersDto bookSearchParametersDto);
}
