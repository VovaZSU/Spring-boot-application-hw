package com.example.Spring_boot_application.repository;

import com.example.Spring_boot_application.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List findAll();
}
