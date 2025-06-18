package com.example.springbootapplication.repository;

import com.example.springbootapplication.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {

    List<Book> findAll();

    Optional<Book> findBookById(Long id);

    Book createBook(Book book);

}
