package com.ms2pedidos.backend_pedidos.client;

import com.ms2pedidos.backend_pedidos.dto.external.ProductoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductoClient {

    private final RestTemplate restTemplate;

    @Value("${ms1.url}")
    private String ms1Url;

    public Optional<ProductoDTO> getProducto(Long id) {
        try {
            ProductoDTO producto = restTemplate.getForObject(
                ms1Url + "/productos/" + id, ProductoDTO.class
            );
            return Optional.ofNullable(producto);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Producto {} no encontrado en MS1", id);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error al consultar MS1 para producto {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al comunicarse con el servicio de productos");
        }
    }
}