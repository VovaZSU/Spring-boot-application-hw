package com.example.springbootapplication.service.shoppingcart;

import com.example.springbootapplication.dto.shoppingcart.CreateCartItemRequestDto;
import com.example.springbootapplication.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.springbootapplication.exception.EntityNotFoundException;
import com.example.springbootapplication.mapper.ShoppingCartMapper;
import com.example.springbootapplication.model.book.Book;
import com.example.springbootapplication.model.shoppingcart.CartItem;
import com.example.springbootapplication.model.shoppingcart.ShoppingCart;
import com.example.springbootapplication.model.user.User;
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
    @Transactional
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getCart(Long userId) {
        return shoppingCartMapper.toDto(getByUserId(userId));
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto addBookToCart(Long userId, CreateCartItemRequestDto requestDto) {
        ShoppingCart cart = getByUserId(userId);
        updateOrAddCartItem(cart, requestDto);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(cart));
    }

    @Override
    @Transactional
    public void updateItemQuantity(Long cartItemId, int quantity) {
        CartItem cartItem = findCartItemById(cartItemId);
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    @Override
    public void removeItemFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    private void updateOrAddCartItem(ShoppingCart cart, CreateCartItemRequestDto requestDto) {
        findCartItemInCart(cart, requestDto.bookId())
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + requestDto.quantity()),
                        () -> addNewCartItem(cart, requestDto)
                );
    }

    private Optional<CartItem> findCartItemInCart(ShoppingCart cart, Long bookId) {
        return cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst();
    }

    private void addNewCartItem(ShoppingCart cart, CreateCartItemRequestDto requestDto) {
        Book book = findBookById(requestDto.bookId());
        CartItem cartItem = shoppingCartMapper.toEntity(requestDto, book, cart);
        cart.getCartItems().add(cartItem);
    }

    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: " + bookId));
    }

    private CartItem findCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item by id: "
                        + cartItemId));
    }

    public ShoppingCart getByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart for user id: " + userId));
    }
}
