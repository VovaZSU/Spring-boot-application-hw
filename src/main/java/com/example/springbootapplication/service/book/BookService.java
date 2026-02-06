package com.example.springbootapplication.service.book;

import com.example.springbootapplication.dto.book.BookDto;
import com.example.springbootapplication.dto.book.CreateBookRequestDto;
import com.example.springbootapplication.dto.category.BookDtoWithoutCategoryIds;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<BookDto> findAll(Pageable pageable);

    BookDto findBookById(Long id);

    BookDto createBook(CreateBookRequestDto requestDto);

    void deleteBook(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto updateBook);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId);
}
