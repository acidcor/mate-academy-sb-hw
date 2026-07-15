package mate.academy.hw.repository.book;

import java.util.Arrays;
import mate.academy.hw.model.Book;
import mate.academy.hw.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String FIELD = "author";

    @Override
    public String getKey() {
        return FIELD;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder)
                -> root.get(FIELD).in(Arrays.stream(params).toArray());
    }
}
