CREATE TABLE IF NOT EXISTS supplier_order (
    id BIGSERIAL PRIMARY KEY,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    total_amount DECIMAL(10, 2),
    supplier_id BIGINT NOT NULL,
    CONSTRAINT fk_order_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);
