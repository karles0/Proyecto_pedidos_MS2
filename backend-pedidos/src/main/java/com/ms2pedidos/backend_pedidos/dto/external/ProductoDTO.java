package com.ms2pedidos.backend_pedidos.dto.external;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
}