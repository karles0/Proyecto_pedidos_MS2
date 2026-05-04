package com.ms2pedidos.backend_pedidos.Pedido.reposiroty;

import com.ms2pedidos.backend_pedidos.Pedido.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioId(String usuarioId);
    Page<Pedido> findAll(Pageable pageable);
}