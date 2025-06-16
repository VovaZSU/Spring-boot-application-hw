package com.example.springbootapplication.repository;

import com.example.springbootapplication.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookDaoImpl implements BookRepository {

    private final EntityManagerFactory entityManagerFactory;

    @Override
    public List<Book> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT e FROM Book e", Book.class).getResultList();
        }
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Book book = entityManager.find(Book.class, id);
            // return employee != null ? Optional.of(employee) : Optional.empty();
            return Optional.ofNullable(book);
        }
    }

    @Override
    public Book createBook(Book book) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(book);
            transaction.commit();
            return book;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Book> findAllByAuthor(String author) {
        String lowerCaseName = author.toLowerCase();
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager
                    .createQuery("SELECT e FROM Book e WHERE lower(e.author) LIKE :author",
                        Book.class)
                    .setParameter("author", "%" + lowerCaseName + "%")
                    .getResultList();
        }
    }
}
