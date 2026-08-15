package mate.academy.hw.repository.user;

import mate.academy.hw.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book>,
        PagingAndSortingRepository<Book, Long> {
}
