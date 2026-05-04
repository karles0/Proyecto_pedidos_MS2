package com.ms2pedidos.backend_pedidos.dto.external;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoUpdateDTO {

    @NotNull
    @PositiveOrZero
    private Integer stock;
}