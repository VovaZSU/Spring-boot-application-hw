package com.example.Spring_boot_application.service;

import com.example.Spring_boot_application.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll() ;
}
