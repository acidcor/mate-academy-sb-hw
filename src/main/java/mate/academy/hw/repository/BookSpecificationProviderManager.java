package mate.academy.hw.repository;

import java.util.List;
import mate.academy.hw.exceptrion.SpecificationProviderNotFoundException;
import mate.academy.hw.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    @Autowired
    private List<SpecificationProvider<Book>> bookSpecificationProvider;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProvider.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(
                        () -> new SpecificationProviderNotFoundException("Can't find current "
                                + "specification provider for key: " + key));
    }
}
