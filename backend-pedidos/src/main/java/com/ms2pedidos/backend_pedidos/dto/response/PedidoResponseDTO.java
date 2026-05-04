package com.ms2pedidos.backend_pedidos.dto.response;

import com.ms2pedidos.backend_pedidos.Pedido.model.Pedido.EstadoPedido;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponseDTO {
    private Long id;
    private String usuarioId;
    private BigDecimal total;
    private EstadoPedido estado;
    private LocalDateTime creadoEn;
    private List<DetalleResponseDTO> detalle;
}