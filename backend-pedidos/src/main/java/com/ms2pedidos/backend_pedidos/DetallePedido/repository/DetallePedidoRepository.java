package com.ms2pedidos.backend_pedidos.DetallePedido.repository;

import com.ms2pedidos.backend_pedidos.DetallePedido.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByPedido_Id(Long pedidoId);
}