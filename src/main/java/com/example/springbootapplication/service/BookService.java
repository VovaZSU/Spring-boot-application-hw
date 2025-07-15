package com.example.springbootapplication.service;

import com.example.springbootapplication.dto.BookDto;
import com.example.springbootapplication.dto.CreateBookRequestDto;
import com.example.springbootapplication.model.Book;
import java.util.List;

public interface BookService {
    List<BookDto> findAll();

    BookDto findBookById(Long id);

    BookDto createBook(CreateBookRequestDto requestDto);

    void deleteBook(Long id);

    BookDto updateBook(Long id, Book updateBook);
}
