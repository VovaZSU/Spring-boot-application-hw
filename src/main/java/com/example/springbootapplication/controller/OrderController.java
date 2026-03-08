package com.example.springbootapplication.controller;

import com.example.springbootapplication.dto.order.OrderItemResponseDto;
import com.example.springbootapplication.dto.order.OrderRequestDto;
import com.example.springbootapplication.dto.order.OrderResponseDto;
import com.example.springbootapplication.dto.order.UpdateOrderStatusRequestDto;
import com.example.springbootapplication.model.user.User;
import com.example.springbootapplication.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Endpoints for managing orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Place an order", description =
            "Create a new order from shopping cart items")
    public OrderResponseDto placeOrder(Authentication authentication, @RequestBody @Valid
            OrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.placeOrder(user, requestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order history", description = "Retrieve list of previous orders")
    public List<OrderResponseDto> getHistory(Authentication authentication, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAll(user.getId(), pageable);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update status", description = "Admin can change the status of any order")
    public OrderResponseDto updateStatus(@PathVariable Long id, @RequestBody @Valid
            UpdateOrderStatusRequestDto statusDto) {
        return orderService.updateStatus(id, statusDto);
    }

    @GetMapping("/{orderId}/items")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get items by order id")
    public List<OrderItemResponseDto> getOrderItems(@PathVariable Long userId,
                                                    Long orderId, Pageable pageable) {
        return orderService.findOrderItems(userId, orderId, pageable);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get a specific item from an order")
    public OrderItemResponseDto getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.findOrderItem(orderId, itemId);
    }
}
