package com.example.springbootapplication.service.order;

import com.example.springbootapplication.dto.order.OrderItemResponseDto;
import com.example.springbootapplication.dto.order.OrderRequestDto;
import com.example.springbootapplication.dto.order.OrderResponseDto;
import com.example.springbootapplication.dto.order.UpdateOrderStatusRequestDto;
import com.example.springbootapplication.exception.EntityNotFoundException;
import com.example.springbootapplication.mapper.OrderMapper;
import com.example.springbootapplication.model.order.Order;
import com.example.springbootapplication.model.order.OrderItem;
import com.example.springbootapplication.model.order.Status;
import com.example.springbootapplication.model.shoppingcart.ShoppingCart;
import com.example.springbootapplication.model.user.User;
import com.example.springbootapplication.repository.order.OrderItemRepository;
import com.example.springbootapplication.repository.order.OrderRepository;
import com.example.springbootapplication.repository.shoppingcart.ShoppingCartRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ShoppingCartRepository cartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public OrderResponseDto placeOrder(User user, OrderRequestDto requestDto) {
        ShoppingCart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find shopping cart for user "
                        + user.getId()));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.shippingAddress());

        Set<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem item = new OrderItem();
                    item.setBook(cartItem.getBook());
                    item.setQuantity(cartItem.getQuantity());
                    item.setPrice(cartItem.getBook().getPrice());
                    item.setOrder(order);
                    return item;
                })
                .collect(Collectors.toSet());

        order.setOrderItems(orderItems);
        order.setTotal(orderItems.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Order savedOrder = orderRepository.save(order);
        cartRepository.delete(cart);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderResponseDto> findAll(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto updateStatus(Long id, UpdateOrderStatusRequestDto statusDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(statusDto.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemResponseDto> findOrderItems(Long orderId, Pageable pageable) {
        return orderItemRepository.findAllByOrderId(orderId, pageable).stream()
                .map(orderMapper::toItemDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto findOrderItem(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order item " + itemId + " for order " + orderId));
        return orderMapper.toItemDto(orderItem);
    }
}
