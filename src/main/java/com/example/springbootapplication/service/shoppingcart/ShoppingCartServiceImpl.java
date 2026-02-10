package com.example.springbootapplication.service.shoppingcart;

import com.example.springbootapplication.dto.shoppingcart.CreateCartItemRequestDto;
import com.example.springbootapplication.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.springbootapplication.exception.EntityNotFoundException;
import com.example.springbootapplication.mapper.ShoppingCartMapper;
import com.example.springbootapplication.model.book.Book;
import com.example.springbootapplication.model.shoppingcart.CartItem;
import com.example.springbootapplication.model.shoppingcart.ShoppingCart;
import com.example.springbootapplication.repository.BookRepository;
import com.example.springbootapplication.repository.shoppingcart.CartItemRepository;
import com.example.springbootapplication.repository.shoppingcart.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartResponseDto getCart(Long userId) {
        return shoppingCartMapper.toDto(getByUserId(userId));
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto addBookToCart(Long userId, CreateCartItemRequestDto requestDto) {
        ShoppingCart cart = getByUserId(userId);
        Book book = bookRepository.findById(requestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: "
                        + requestDto.bookId()));
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(requestDto.bookId()))
                .findFirst();
        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity()
                    + requestDto.quantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setShoppingCart(cart);
            cartItem.setBook(book);
            cartItem.setQuantity(requestDto.quantity());
            cart.getCartItems().add(cartItem);
        }
        return shoppingCartMapper.toDto(shoppingCartRepository.save(cart));
    }

    @Override
    @Transactional
    public void updateItemQuantity(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item by id: "
                        + cartItemId));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    @Override
    public void removeItemFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public ShoppingCart getByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart for user id: " + userId));
    }
}
