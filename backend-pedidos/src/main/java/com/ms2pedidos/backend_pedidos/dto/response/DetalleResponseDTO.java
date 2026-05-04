package com.ms2pedidos.backend_pedidos.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleResponseDTO {
    private Long productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
