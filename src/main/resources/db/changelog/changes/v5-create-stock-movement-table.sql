CREATE TABLE IF NOT EXISTS stock_movement (
    id BIGSERIAL PRIMARY KEY,
    movement_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantity INTEGER NOT NULL,
    movement_type VARCHAR(20) NOT NULL,
    unit_price DECIMAL(10, 2),
    product_id BIGINT NOT NULL,
    order_id BIGINT,
    CONSTRAINT fk_movement_product FOREIGN KEY (product_id) REFERENCES product(id),
    CONSTRAINT fk_movement_order FOREIGN KEY (order_id) REFERENCES supplier_order(id)
);
