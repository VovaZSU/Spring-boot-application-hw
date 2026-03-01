package com.example.springbootapplication.service.order;

import com.example.springbootapplication.dto.order.OrderItemResponseDto;
import com.example.springbootapplication.dto.order.OrderRequestDto;
import com.example.springbootapplication.dto.order.OrderResponseDto;
import com.example.springbootapplication.dto.order.UpdateOrderStatusRequestDto;
import com.example.springbootapplication.model.user.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDto placeOrder(User user, OrderRequestDto requestDto);

    List<OrderResponseDto> findAll(Long userId, Pageable pageable);

    OrderResponseDto updateStatus(Long id, UpdateOrderStatusRequestDto statusDto);

    List<OrderItemResponseDto> findOrderItems(Long userId, Long orderId, Pageable pageable);

    OrderItemResponseDto findOrderItem(Long orderId, Long itemId);
}
