package com.example.springbootapplication.mapper;

import com.example.springbootapplication.config.MapperConfig;
import com.example.springbootapplication.dto.order.OrderItemResponseDto;
import com.example.springbootapplication.dto.order.OrderResponseDto;
import com.example.springbootapplication.model.order.Order;
import com.example.springbootapplication.model.order.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toDto(Order order);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toItemDto(OrderItem orderItem);
}
