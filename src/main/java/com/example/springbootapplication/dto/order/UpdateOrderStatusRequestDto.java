package com.example.springbootapplication.dto.order;

import com.example.springbootapplication.model.order.Status;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDto(
        @NotNull Status status
) {
}
