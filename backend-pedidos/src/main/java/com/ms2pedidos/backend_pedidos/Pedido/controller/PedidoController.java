package com.ms2pedidos.backend_pedidos.Pedido.controller;

import com.ms2pedidos.backend_pedidos.dto.request.PedidoRequestDTO;
import com.ms2pedidos.backend_pedidos.dto.response.PedidoResponseDTO;
import com.ms2pedidos.backend_pedidos.Pedido.model.Pedido.EstadoPedido;
import com.ms2pedidos.backend_pedidos.Pedido.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Gestión de pedidos")
@SecurityRequirement(name = "bearerAuth")
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    @Operation(summary = "Listar todos los pedidos (paginado)")
    public ResponseEntity<Map<String, Object>> listarTodos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(pedidoService.listarTodos(page, limit));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar pedidos de un usuario")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorUsuario(
            @PathVariable String usuarioId) {
        return ResponseEntity.ok(pedidoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/mis-pedidos")
    @Operation(summary = "Listar mis propios pedidos (usa el token)")
    public ResponseEntity<List<PedidoResponseDTO>> misPedidos(Authentication auth) {
        return ResponseEntity.ok(pedidoService.listarPorUsuario(auth.getName()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID")
    public ResponseEntity<PedidoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtener(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo pedido (valida productos en MS1)")
    public ResponseEntity<PedidoResponseDTO> crear(@Valid @RequestBody PedidoRequestDTO req) {
        return ResponseEntity.status(201).body(pedidoService.crear(req));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del pedido")
    public ResponseEntity<PedidoResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        EstadoPedido estado = EstadoPedido.valueOf(body.get("estado").toUpperCase());
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pedido")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}