INSERT INTO pedidos (usuario_id, total, estado, creado_en)
SELECT 
    gen_random_uuid()::text, 
    (random() * 990 + 10)::numeric(10,2), 
    (ARRAY['PENDIENTE','ENVIADO','ENTREGADO'])[floor(random()*3+1)], 
    NOW() - (random() * INTERVAL '365 days') 
FROM generate_series(1, 20000)
WHERE NOT EXISTS (SELECT 1 FROM pedidos);