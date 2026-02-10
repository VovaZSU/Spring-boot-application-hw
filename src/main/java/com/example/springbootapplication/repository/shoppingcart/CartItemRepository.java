package com.example.springbootapplication.repository.shoppingcart;

import com.example.springbootapplication.model.shoppingcart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
