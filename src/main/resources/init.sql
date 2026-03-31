drop database if exists restaurant_db;
CREATE DATABASE IF NOT EXISTS restaurant_db;
USE restaurant_db;

-- create users table
CREATE TABLE IF NOT EXISTS users (
     user_id    INT          PRIMARY KEY AUTO_INCREMENT,
     username   VARCHAR(50)  NOT NULL UNIQUE,
     password   VARCHAR(255) NOT NULL,
     role       VARCHAR(20)  NOT NULL,               -- 'CUSTOMER' | 'CHEF' | 'MANAGER'
     is_active  BOOLEAN      NOT NULL DEFAULT TRUE,  -- FALSE = banned
     created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- create tables table
CREATE TABLE IF NOT EXISTS tables (
    table_id   INT         PRIMARY KEY AUTO_INCREMENT,
    capacity   INT         NOT NULL,
    status     VARCHAR(20) NOT NULL DEFAULT 'EMPTY' -- 'EMPTY' | 'OCCUPIED'
);

-- create menu items table
CREATE TABLE IF NOT EXISTS menu_items (
    item_id      INT            PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100)   NOT NULL,
    item_type    VARCHAR(10)    NOT NULL,            -- 'FOOD' | 'DRINK'
    price        DECIMAL(10, 2) NOT NULL,
    stock        INT            NOT NULL DEFAULT -1, -- -1 = unlimited (food), >= 0 for drinks
    is_available BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- create orders table
CREATE TABLE IF NOT EXISTS orders (
    order_id    INT            PRIMARY KEY AUTO_INCREMENT,
    table_id    INT            NOT NULL,
    customer_id INT            NOT NULL,
    status      VARCHAR(20)    NOT NULL DEFAULT 'OPEN', -- 'OPEN' | 'PAID'
    total_price DECIMAL(10, 2)          DEFAULT 0.00,
    created_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    checkout_at DATETIME                DEFAULT NULL,
    FOREIGN KEY (table_id)    REFERENCES tables(table_id),
    FOREIGN KEY (customer_id) REFERENCES users(user_id)
);

-- create order items table
CREATE TABLE IF NOT EXISTS order_items (
    order_item_id INT            PRIMARY KEY AUTO_INCREMENT,
    order_id      INT            NOT NULL,
    item_id       INT            NOT NULL,
    quantity      INT            NOT NULL DEFAULT 1,
    unit_price    DECIMAL(10, 2) NOT NULL,
    status        VARCHAR(20)    NOT NULL DEFAULT 'PENDING', -- 'PENDING'|'COOKING'|'READY'|'SERVED'
    created_at    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (item_id)  REFERENCES menu_items(item_id)
);

-- create reviews table
CREATE TABLE IF NOT EXISTS reviews (
    review_id   INT      PRIMARY KEY AUTO_INCREMENT,
    customer_id INT      NOT NULL,
    item_id     INT               DEFAULT NULL, -- NULL = review for restaurant overall
    star_rating INT      NOT NULL,              -- 1 to 5
    comment     TEXT              DEFAULT NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(user_id),
    FOREIGN KEY (item_id)     REFERENCES menu_items(item_id)
);

-- insert users
INSERT IGNORE INTO users (username, password, role) VALUES
('chef_rudy',     '$2a$10$zuXrBU1xRS9ez4TD.wsNION7F288OAB/aZ6a8Uk7BdtUwmGhGR3KS', 'CHEF'),
('chef_john', '$2a$10$zuXrBU1xRS9ez4TD.wsNION7F288OAB/aZ6a8Uk7BdtUwmGhGR3KS', 'CHEF'),
('chef_anna', '$2a$10$zuXrBU1xRS9ez4TD.wsNION7F288OAB/aZ6a8Uk7BdtUwmGhGR3KS', 'CHEF'),
('alice',     '$2a$10$zuXrBU1xRS9ez4TD.wsNION7F288OAB/aZ6a8Uk7BdtUwmGhGR3KS', 'CUSTOMER'),
('bob',       '$2a$10$zuXrBU1xRS9ez4TD.wsNION7F288OAB/aZ6a8Uk7BdtUwmGhGR3KS', 'CUSTOMER'),
('charlie',   '$2a$10$zuXrBU1xRS9ez4TD.wsNION7F288OAB/aZ6a8Uk7BdtUwmGhGR3KS', 'CUSTOMER'),
('admin',   '$2a$10$zuXrBU1xRS9ez4TD.wsNION7F288OAB/aZ6a8Uk7BdtUwmGhGR3KS', 'MANAGER');
-- insert tables
INSERT IGNORE INTO tables (capacity, status) VALUES
(2, 'EMPTY'),
(4, 'EMPTY'),
(4, 'EMPTY'),
(6, 'EMPTY'),
(6, 'EMPTY'),
(8, 'EMPTY');

-- insert menu items
INSERT IGNORE INTO menu_items (product_name, item_type, price, stock) VALUES
('Fried Rice',         'FOOD',  45000.00, -1),
('Grilled Chicken',    'FOOD',  85000.00, -1),
('Spring Rolls',       'FOOD',  35000.00, -1),
('Beef Pho',           'FOOD',  65000.00, -1),
('Banh Mi',            'FOOD',  30000.00, -1),
('Seafood Hotpot',     'FOOD', 250000.00, -1),
('Steamed Fish',       'FOOD', 120000.00, -1),
('Vegetable Stir Fry', 'FOOD',  50000.00, -1),
('Coca Cola',          'DRINK', 20000.00, 50),
('Orange Juice',       'DRINK', 30000.00, 30),
('Mineral Water',      'DRINK', 10000.00, 100),
('Iced Tea',           'DRINK', 15000.00, 60),
('Beer (333)',         'DRINK', 25000.00, 80),
('Lemon Soda',         'DRINK', 20000.00, 45);

-- insert sample orders
INSERT IGNORE INTO orders (order_id, table_id, customer_id, status, total_price, created_at, checkout_at) VALUES
(1, 1, 4, 'PAID', 175000.00, NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 6 DAY + INTERVAL 1 HOUR),
(2, 2, 5, 'PAID', 310000.00, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY + INTERVAL 2 HOUR),
(3, 3, 6, 'PAID',  95000.00, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY + INTERVAL 1 HOUR),
(4, 1, 4, 'PAID', 420000.00, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY + INTERVAL 3 HOUR),
(5, 4, 5, 'PAID', 215000.00, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY + INTERVAL 2 HOUR),
(6, 2, 6, 'OPEN',   0.00,    NOW() - INTERVAL 1 DAY, NULL);

-- insert sample order items
INSERT IGNORE INTO order_items (order_id, item_id, quantity, unit_price, status) VALUES
(1, 1,  2, 45000.00,  'SERVED'),
(1, 9,  2, 20000.00,  'SERVED'),
(2, 2,  2, 85000.00,  'SERVED'),
(2, 6,  1, 250000.00, 'SERVED'),
(3, 4,  1, 65000.00,  'SERVED'),
(3, 11, 2, 10000.00,  'SERVED'),
(4, 6,  1, 250000.00, 'SERVED'),
(4, 13, 4, 25000.00,  'SERVED'),
(5, 3,  3, 35000.00,  'SERVED'),
(5, 2,  1, 85000.00,  'SERVED'),
(5, 10, 2, 30000.00,  'SERVED'),
(6, 1,  1, 45000.00,  'SERVED'),
(6, 3,  2, 35000.00,  'READY'),
(6, 9,  2, 20000.00,  'COOKING'),
(6, 5,  1, 30000.00,  'PENDING');

-- insert sample reviews
INSERT IGNORE INTO reviews (customer_id, item_id, star_rating, comment) VALUES
(4, 1,    5, 'Amazing fried rice, will order again!'),
(4, NULL, 4, 'Great service and atmosphere.'),
(5, 2,    5, 'Best grilled chicken in town.'),
(6, 4,    3, 'Pho was okay, could be hotter.');
