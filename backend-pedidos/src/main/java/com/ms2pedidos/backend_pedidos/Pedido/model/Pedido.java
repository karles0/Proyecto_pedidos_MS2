package com.ms2pedidos.backend_pedidos.Pedido.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ms2pedidos.backend_pedidos.DetallePedido.model.DetallePedido;

@Entity
@Table(name = "pedidos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DetallePedido> detalle;

    @PrePersist
    public void prePersist() {
        this.creadoEn = LocalDateTime.now();
        if (this.estado == null) this.estado = EstadoPedido.PENDIENTE;
    }

    public enum EstadoPedido { PENDIENTE, ENVIADO, ENTREGADO }
}