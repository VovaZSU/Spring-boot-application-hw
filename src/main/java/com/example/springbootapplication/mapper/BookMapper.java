package com.example.springbootapplication.mapper;

import com.example.springbootapplication.config.MapperConfig;
import com.example.springbootapplication.dto.BookDto;
import com.example.springbootapplication.dto.CreateBookRequestDto;
import com.example.springbootapplication.model.Book;
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
}
