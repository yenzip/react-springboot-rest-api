create TABLE products (
    product_id BINARY(16) PRIMARY KEY,
    product_name VARCHAR(20) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price BIGINT NOT NULL,
    description VARCHAR(500) DEFAULT NULL,
    image_url VARCHAR(500) DEFAULT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) DEFAULT NULL
);

create TABLE members (
    member_id BINARY(16) PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    address VARCHAR(200) NOT NULL,
    postcode VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) DEFAULT NULL
);

create TABLE orders (
    order_id     binary(16) PRIMARY KEY,
    member_id    binary(16) NOT NULL,  -- 회원과 주문의 관계를 나타내는 외래 키
    order_status VARCHAR(50)  NOT NULL,
	address VARCHAR(200) NOT NULL,
    postcode VARCHAR(20) NOT NULL,
    created_at   datetime(6)  NOT NULL,
    updated_at   datetime(6)  DEFAULT NULL,
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

create TABLE order_items (
    seq        bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_id   binary(16)  NOT NULL,
    product_id binary(16)  NOT NULL,
    category   VARCHAR(50) NOT NULL,
    price      bigint      NOT NULL,
    quantity   int         NOT NULL,
    INDEX (order_id),
    CONSTRAINT fk_order_items_to_order FOREIGN KEY (order_id) REFERENCES orders (order_id) ON delete CASCADE,
    CONSTRAINT fk_order_items_to_product FOREIGN KEY (product_id) REFERENCES products (product_id)
);