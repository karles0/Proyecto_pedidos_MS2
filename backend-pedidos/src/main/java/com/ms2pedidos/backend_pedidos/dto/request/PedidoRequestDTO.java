package com.ms2pedidos.backend_pedidos.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class PedidoRequestDTO {
    @NotEmpty(message = "usuarioId es obligatorio")
    private String usuarioId;

    @NotEmpty(message = "Debe incluir al menos un item")
    @Valid
    private List<ItemDTO> items;

    @Data
    public static class ItemDTO {

        @NotNull(message = "productoId es obligatorio")
        private Long productoId;

        @NotNull(message = "cantidad es obligatoria")
        private Integer cantidad;
    }
}