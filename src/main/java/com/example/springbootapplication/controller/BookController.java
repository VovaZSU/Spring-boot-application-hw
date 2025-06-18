package com.example.springbootapplication.controller;

import com.example.springbootapplication.dto.BookDto;
import com.example.springbootapplication.dto.CreateBookRequestDto;
import com.example.springbootapplication.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import org.springframework.http.HttpStatus;
=======
>>>>>>> origin/DtoAdditionBranch
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.ResponseStatus;
=======
import org.springframework.web.bind.annotation.RequestParam;
>>>>>>> origin/DtoAdditionBranch
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
<<<<<<< HEAD
@RequestMapping("/books")
=======
@RequestMapping(value = "/books")
>>>>>>> origin/DtoAdditionBranch
public class BookController {

    private final BookService bookService;

    @GetMapping
    List<BookDto> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    BookDto findBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    @PostMapping
<<<<<<< HEAD
    @ResponseStatus(HttpStatus.CREATED)
    BookDto createBook(@RequestBody CreateBookRequestDto requestDto) {
        return bookService.createBook(requestDto);
    }
=======
    BookDto createBook(@RequestBody CreateBookRequestDto requestDto) {
        return bookService.createBook(requestDto);
    }

    @GetMapping("/by-author")
    List<BookDto> findAllByAuthor(@RequestParam String author) {
        return bookService.findAllByAuthor(author);
    }

>>>>>>> origin/DtoAdditionBranch
}
