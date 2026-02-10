package com.example.springbootapplication.service.shoppingcart;

import com.example.springbootapplication.dto.shoppingcart.CreateCartItemRequestDto;
import com.example.springbootapplication.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.springbootapplication.model.shoppingcart.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCart(Long userId);

    ShoppingCartResponseDto addBookToCart(Long userId, CreateCartItemRequestDto requestDto);

    void updateItemQuantity(Long cartItemId, int quantity);

    void removeItemFromCart(Long cartItemId);

    ShoppingCart getByUserId(Long userId);
}
