package mate.academy.hw.repository.book;

import java.util.Arrays;
import mate.academy.hw.model.Book;
import mate.academy.hw.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String FIELD_TITLE = "title";

    @Override
    public String getKey() {
        return FIELD_TITLE;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder)
                -> root.get(FIELD_TITLE).in(Arrays.stream(params).toArray());
    }
}
