package com.example.springbootapplication.mapper;

import com.example.springbootapplication.config.MapperConfig;
import com.example.springbootapplication.dto.category.CategoryDto;
import com.example.springbootapplication.model.category.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CategoryDto categoryDto);

    void updateCategoryFromDto(CategoryDto categoryDto, @MappingTarget Category category);
}
