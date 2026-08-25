package mate.academy.hw.service.book.impl;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.book.BookRequestDto;
import mate.academy.hw.dto.book.BookResponseDto;
import mate.academy.hw.dto.book.BookResponseDtoWithoutCategoryIds;
import mate.academy.hw.dto.book.BookSearchParametersDto;
import mate.academy.hw.exceptrion.EntityNotFoundException;
import mate.academy.hw.mapper.BookMapper;
import mate.academy.hw.model.Book;
import mate.academy.hw.model.Category;
import mate.academy.hw.repository.book.BookRepository;
import mate.academy.hw.repository.categories.CategoryRepository;
import mate.academy.hw.repository.specifiacation.book.BookSpecificationBuilder;
import mate.academy.hw.service.book.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder specificationBuilder;

    @Override
    public BookResponseDto save(BookRequestDto requestDto) {
        Book book = bookMapper.toEntity(requestDto);
        Set<Category> categories = findAllCategories(requestDto);
        book.setCategories(categories);
        return bookMapper.toDto(repository.save(book));
    }

    @Override
    public BookResponseDto findById(Long id) {
        return bookMapper.toDto(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find a book by id: " + id)));
    }

    @Override
    public Page<BookResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public Page<BookResponseDto> search(
            BookSearchParametersDto searchParametersDto,
            Pageable pageable
    ) {
        Specification<Book> specification = specificationBuilder.build(searchParametersDto);

        return repository.findAll(specification, pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public BookResponseDto update(Long id, BookRequestDto requestDto) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find a book by id: " + id));
        bookMapper.updateFromCreateBookRequestDto(requestDto, book);
        return bookMapper.toDto(repository.save(book));
    }

    @Override
    public Page<BookResponseDtoWithoutCategoryIds> findAllBooksByCategory(
            Long id,
            Pageable pageable
    ) {
        return repository.findAllBooksByCategory(pageable, id)
                .map(bookMapper::toDtoWithoutCategory);
    }

    private HashSet<Category> findAllCategories(BookRequestDto dto) {
        Set<Long> categories = new java.util.HashSet<>(Set.of());

        for (Category category : dto.getCategories()) {
            categories.add(category.getId());
        }
        return new HashSet<>(categoryRepository.findAllById(categories));
    }
}
