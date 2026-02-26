package com.example.springbootapplication.controller;

import com.example.springbootapplication.dto.shoppingcart.CreateCartItemRequestDto;
import com.example.springbootapplication.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.springbootapplication.dto.shoppingcart.UpdateCartItemRequestDto;
import com.example.springbootapplication.model.user.User;
import com.example.springbootapplication.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart Management", description = "Endpoints for managing shopping carts")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get user's shopping cart")
    public ShoppingCartResponseDto getCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getCart(user.getId());
    }

    @PostMapping
    @Operation(summary = "Add book to the shopping cart")
    public ShoppingCartResponseDto addBook(
            Authentication authentication,
            @RequestBody @Valid CreateCartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addBookToCart(user.getId(), requestDto);
    }

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update quantity of a book in the shopping cart")
    public void updateQuantity(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        shoppingCartService.updateItemQuantity(cartItemId, requestDto.quantity());
    }

    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a book from the shopping cart")
    public void removeItem(@PathVariable Long cartItemId) {
        shoppingCartService.removeItemFromCart(cartItemId);
    }
}
