package mate.academy.hw.repository;

import mate.academy.hw.dto.BookSearchParametersDto;
import mate.academy.hw.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_ISBN = "isbn";
    private static final String FIELD_AUTHOR = "author";
    @Autowired
    private BookSpecificationProviderManager bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> specification = Specification.unrestricted();
        if (bookSearchParametersDto.title() != null && bookSearchParametersDto.title().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(FIELD_TITLE)
                    .getSpecification(bookSearchParametersDto.title()));
        }
        if (bookSearchParametersDto.isbn() != null && bookSearchParametersDto.isbn().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(FIELD_ISBN)
                    .getSpecification(bookSearchParametersDto.isbn()));
        }
        if (bookSearchParametersDto.author() != null
                && bookSearchParametersDto.author().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(FIELD_AUTHOR)
                    .getSpecification(bookSearchParametersDto.author()));
        }
        return specification;
    }
}

