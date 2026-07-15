package mate.academy.hw.repository;

import mate.academy.hw.dto.BookSearchParametersDto;
import mate.academy.hw.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    @Autowired
    private BookSpecificationProviderManager bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParametersDto) {
        /*
         * - alternative to current impl, have no clue what's a diff.
         *         Specification<Book> specification = (root, query, builder) -> null
         * - also Specification.where(null) is deprecated
         */
        Specification<Book> specification = Specification.unrestricted();
        if (bookSearchParametersDto.title() != null && bookSearchParametersDto.title().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(bookSearchParametersDto.title()));
        }
        if (bookSearchParametersDto.isbn() != null && bookSearchParametersDto.isbn().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("isbn")
                    .getSpecification(bookSearchParametersDto.isbn()));
        }
        if (bookSearchParametersDto.author() != null
                && bookSearchParametersDto.author().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(bookSearchParametersDto.author()));
        }
        return specification;
    }
}

