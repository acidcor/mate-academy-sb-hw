package mate.academy.hw.repository.book;

import java.util.Arrays;
import mate.academy.hw.model.Book;
import mate.academy.hw.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String FIELD_ISBN = "isbn";

    @Override
    public String getKey() {
        return FIELD_ISBN;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder)
                -> root.get(FIELD_ISBN).in(Arrays.stream(params).toArray());
    }
}
