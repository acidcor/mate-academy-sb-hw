package mate.academy.hw;

import java.math.BigDecimal;
import mate.academy.hw.model.Book;
import mate.academy.hw.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HwApplication {
    private final BookService bookService;

    @Autowired
    public HwApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(HwApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setAuthor("Leeroy Jenkins");
            book.setTitle("Leeroy Jenkins HD (High Quality)");
            book.setIsbn("https://www.youtube.com/watch?v=hooKVstzbz0");
            book.setPrice(BigDecimal.valueOf(12));

            bookService.save(book);
            System.out.println(bookService.findAll());
        };
    }
}
