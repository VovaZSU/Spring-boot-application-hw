package com.example.springbootapplication.mapper;

import com.example.springbootapplication.config.MapperConfig;
import com.example.springbootapplication.dto.shoppingcart.CreateCartItemRequestDto;
import com.example.springbootapplication.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.springbootapplication.model.book.Book;
import com.example.springbootapplication.model.shoppingcart.CartItem;
import com.example.springbootapplication.model.shoppingcart.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", source = "cart")
    @Mapping(target = "book", source = "book")
    @Mapping(target = "quantity", source = "requestDto.quantity")
    CartItem toEntity(CreateCartItemRequestDto requestDto, Book book, ShoppingCart cart);
}
