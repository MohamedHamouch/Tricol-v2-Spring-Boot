CREATE TABLE IF NOT EXISTS supplier (
    id BIGSERIAL PRIMARY KEY,
    company VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    contact VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    city VARCHAR(100),
    ice VARCHAR(50)
);
