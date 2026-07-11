package mate.academy.hw.repository.impl;

import exceptrion.DataProcessingException;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Queue;

import lombok.RequiredArgsConstructor;
import mate.academy.hw.model.Book;
import mate.academy.hw.repository.BookRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add a new book: " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return book;
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Book ", Book.class).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find any book", e);
        }
    }
}


