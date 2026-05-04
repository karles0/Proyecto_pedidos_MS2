package com.ms2pedidos.backend_pedidos.exception;

public class StockInsufficientException extends RuntimeException {
    public StockInsufficientException(String message) {
        super(message);
    }
}