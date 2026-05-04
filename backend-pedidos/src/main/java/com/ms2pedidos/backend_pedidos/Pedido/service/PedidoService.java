package com.ms2pedidos.backend_pedidos.Pedido.service;

import com.ms2pedidos.backend_pedidos.client.ProductoClient;
import com.ms2pedidos.backend_pedidos.dto.external.ProductoDTO;
import com.ms2pedidos.backend_pedidos.dto.request.PedidoRequestDTO;
import com.ms2pedidos.backend_pedidos.dto.response.DetalleResponseDTO;
import com.ms2pedidos.backend_pedidos.dto.response.PedidoResponseDTO;
import com.ms2pedidos.backend_pedidos.exception.ResourceNotFoundException;
import com.ms2pedidos.backend_pedidos.DetallePedido.model.DetallePedido;
import com.ms2pedidos.backend_pedidos.Pedido.model.Pedido;
import com.ms2pedidos.backend_pedidos.Pedido.model.Pedido.EstadoPedido;
import com.ms2pedidos.backend_pedidos.Pedido.reposiroty.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final ProductoClient productoClient;

    // Listar todos (admin) con paginación
    public Map<String, Object> listarTodos(int page, int limit) {
        Page<Pedido> pageResult = pedidoRepo.findAll(PageRequest.of(page - 1, limit));
        return Map.of(
            "data",  pageResult.getContent().stream().map(this::toDTO).toList(),
            "total", pageResult.getTotalElements(),
            "page",  page
        );
    }

    // Listar por usuario
    public List<PedidoResponseDTO> listarPorUsuario(String usuarioId) {
        return pedidoRepo.findByUsuarioId(usuarioId)
            .stream().map(this::toDTO).toList();
    }

    // Obtener por ID
    public PedidoResponseDTO obtener(Long id) {
        return toDTO(findOrThrow(id));
    }

    // Crear pedido — valida productos en MS1
    @Transactional
    public PedidoResponseDTO crear(PedidoRequestDTO req) {
        Pedido pedido = Pedido.builder()
            .usuarioId(req.getUsuarioId())
            .estado(EstadoPedido.PENDIENTE)
            .build();

        List<DetallePedido> detalles = req.getItems().stream().map(item -> {
            ProductoDTO producto = productoClient.getProducto(item.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Producto no encontrado en MS1: " + item.getProductoId()
                ));
            return DetallePedido.builder()
                .pedido(pedido)
                .productoId(item.getProductoId())
                .cantidad(item.getCantidad())
                .precioUnitario(producto.getPrecio())
                .build();
        }).toList();

        BigDecimal total = detalles.stream()
            .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setTotal(total);
        pedido.setDetalle(detalles);
        return toDTO(pedidoRepo.save(pedido));
    }

    // Actualizar estado
    @Transactional
    public PedidoResponseDTO actualizarEstado(Long id, EstadoPedido estado) {
        Pedido pedido = findOrThrow(id);
        pedido.setEstado(estado);
        return toDTO(pedidoRepo.save(pedido));
    }

    // Eliminar (solo admin)
    @Transactional
    public void eliminar(Long id) {
        if (!pedidoRepo.existsById(id))
            throw new ResourceNotFoundException("Pedido no encontrado: " + id);
        pedidoRepo.deleteById(id);
    }

    // ── Helpers ──────────────────────────────────────────

    private Pedido findOrThrow(Long id) {
        return pedidoRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
    }

    private PedidoResponseDTO toDTO(Pedido p) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(p.getId());
        dto.setUsuarioId(p.getUsuarioId());
        dto.setTotal(p.getTotal());
        dto.setEstado(p.getEstado());
        dto.setCreadoEn(p.getCreadoEn());

        if (p.getDetalle() != null) {
            dto.setDetalle(p.getDetalle().stream().map(d -> {
                DetalleResponseDTO dd = new DetalleResponseDTO();
                dd.setProductoId(d.getProductoId());
                dd.setCantidad(d.getCantidad());
                dd.setPrecioUnitario(d.getPrecioUnitario());
                dd.setSubtotal(d.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(d.getCantidad())));
                return dd;
            }).toList());
        }
        return dto;
    }
}