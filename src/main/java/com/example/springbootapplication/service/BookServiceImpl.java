package com.example.springbootapplication.service;

import com.example.springbootapplication.dto.BookDto;
import com.example.springbootapplication.dto.CreateBookRequestDto;
import com.example.springbootapplication.exception.EntityNotFoundException;
import com.example.springbootapplication.mapper.BookMapper;
import com.example.springbootapplication.model.Book;
import com.example.springbootapplication.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
            .stream()
            .map(bookMapper::toDto)
            .toList();
    }

    @Override
    public BookDto findBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                    "Cant find book by id" + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto createBook(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        bookRepository.delete(book);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto updateBook) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        bookMapper.updateBookFromDto(updateBook, existingBook);
        bookRepository.save(existingBook);
        return bookMapper.toDto(existingBook);
    }
}
