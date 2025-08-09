package com.example.springbootapplication.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Book title is required")
    @Size(max = 255, message = "Book title must not exceed 255 characters")
    private String title;

    @Size(max = 255, message = "Author name must not exceed 255 characters")
    private String author;

    @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$",
            message = "Invalid ISBN format")
    private String isbn;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Invalid price format")
    private BigDecimal price;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Size(max = 512, message = "Cover image URL must not exceed 512 characters")
    private String coverImage;
}
