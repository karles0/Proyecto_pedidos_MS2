CREATE TABLE IF NOT EXISTS pedidos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id VARCHAR(255) NOT NULL,
    total NUMERIC(10, 2) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    creado_en TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS detalle_pedidos (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL REFERENCES pedidos(id),
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario NUMERIC(10, 2) NOT NULL
);
