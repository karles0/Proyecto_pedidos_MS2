package com.ms2pedidos.backend_pedidos.client;

import com.ms2pedidos.backend_pedidos.dto.external.ProductoDTO;
import com.ms2pedidos.backend_pedidos.dto.external.ProductoUpdateDTO;
import com.ms2pedidos.backend_pedidos.exception.StockInsufficientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public Optional<ProductoDTO> actualizarStock(Long id, Integer stock) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ProductoUpdateDTO> request = new HttpEntity<>(
                new ProductoUpdateDTO(stock), headers
            );

            ResponseEntity<ProductoDTO> response = restTemplate.exchange(
                ms1Url + "/productos/" + id,
                HttpMethod.PATCH,
                request,
                ProductoDTO.class
            );

            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Producto {} no encontrado al actualizar stock en MS1", id);
            return Optional.empty();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                throw new StockInsufficientException(
                    "No fue posible actualizar el stock del producto " + id
                );
            }
            log.error("Error HTTP al actualizar stock del producto {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al actualizar stock del producto " + id);
        } catch (Exception e) {
            log.error("Error al actualizar stock del producto {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al actualizar stock del producto " + id);
        }
    }
}