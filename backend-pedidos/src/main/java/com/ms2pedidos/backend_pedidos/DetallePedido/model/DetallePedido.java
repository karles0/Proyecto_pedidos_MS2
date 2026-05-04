package com.ms2pedidos.backend_pedidos.DetallePedido.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

import com.ms2pedidos.backend_pedidos.Pedido.model.Pedido;

@Entity
@Table(name = "detalle_pedidos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    @ToString.Exclude
    private Pedido pedido;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
}