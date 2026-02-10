package com.example.springbootapplication.mapper;

import com.example.springbootapplication.config.MapperConfig;
import com.example.springbootapplication.dto.book.BookDto;
import com.example.springbootapplication.dto.book.CreateBookRequestDto;
import com.example.springbootapplication.dto.category.BookDtoWithoutCategoryIds;
import com.example.springbootapplication.model.Book;
import com.example.springbootapplication.model.Category;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateBookFromDto(CreateBookRequestDto dto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book != null && book.getCategories() != null) {
            bookDto.setCategoryIds(book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toList()));
        }
    }
}
