package com.example.springbootapplication.service.order;

import com.example.springbootapplication.dto.order.OrderItemResponseDto;
import com.example.springbootapplication.dto.order.OrderRequestDto;
import com.example.springbootapplication.dto.order.OrderResponseDto;
import com.example.springbootapplication.dto.order.UpdateOrderStatusRequestDto;
import com.example.springbootapplication.exception.EntityNotFoundException;
import com.example.springbootapplication.mapper.OrderItemMapper;
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
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public OrderResponseDto placeOrder(User user, OrderRequestDto requestDto) {
        ShoppingCart cart = getShoppingCartWithItems(user.getId());
        Order order = initOrder(user, requestDto);
        Set<OrderItem> orderItems = convertCartToOrderItems(cart, order);
        order.setOrderItems(orderItems);
        order.setTotal(calculateTotal(orderItems));
        orderRepository.save(order);
        cartRepository.delete(cart);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> findAll(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponseDto updateStatus(Long id, UpdateOrderStatusRequestDto statusDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        order.setStatus(statusDto.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemResponseDto> findOrderItems(Long userId, Long orderId, Pageable pageable) {
        orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found for user"));

        return orderItemRepository.findAllByOrderId(orderId, pageable).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto findOrderItem(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order item " + itemId + " for order " + orderId));
        return orderItemMapper.toDto(orderItem);
    }

    private ShoppingCart getShoppingCartWithItems(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart not found for user: " + userId));
    }

    private Order initOrder(User user, OrderRequestDto requestDto) {
        Order order = orderMapper.toModel(requestDto);
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    private Set<OrderItem> convertCartToOrderItems(ShoppingCart cart, Order order) {
        return cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = orderItemMapper.toModel(cartItem);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
