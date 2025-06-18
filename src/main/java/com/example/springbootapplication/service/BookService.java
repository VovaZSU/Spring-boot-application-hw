package com.example.springbootapplication.service;

import com.example.springbootapplication.dto.BookDto;
import com.example.springbootapplication.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    List<BookDto> findAll();

    BookDto findBookById(Long id);

    BookDto createBook(CreateBookRequestDto requestDto);

}
