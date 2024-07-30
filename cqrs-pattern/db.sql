CREATE TABLE users(
   id serial PRIMARY KEY,
   firstname VARCHAR (50),
   lastname VARCHAR (50),
   state VARCHAR(10)
);

CREATE TABLE product(
   id serial PRIMARY KEY,
   description VARCHAR (500),
   price numeric (10,2) NOT NULL
);

CREATE TABLE purchase_order(
    id serial PRIMARY KEY,
    user_id integer references users (id),
    product_id integer references product (id),
    order_date date not null default current_timestamp
);

-- DO NOT CREATE THIS TABLE. --> materialized view
--CREATE TABLE purchase_order_summary (
--    state VARCHAR (50) PRIMARY KEY,
--    totalSale numeric (10,2)
--);

Insert data manually into user and product tables.

-- Insert data in tables:
-- users table
INSERT INTO users (firstname, lastname, state) VALUES('James', 'Brown', 'UK');
INSERT INTO users (firstname, lastname, state) VALUES('Mary', 'Gray', 'US');
--
-- product table
INSERT INTO product (description, price) VALUES('Laptop', 12000);
INSERT INTO product (description, price) VALUES('Monitor', 8600);
INSERT INTO product (description, price) VALUES('Keyboard', 2500);

-- NOTE: Do not need to install anything in purchase_order table manually
-- The table is updated with POST createOrder method:
-- purchase_order
--INSERT INTO purchase_order (user_id, product_id) VALUES(1,2);
--INSERT INTO purchase_order (user_id, product_id) VALUES(1,3);
--INSERT INTO purchase_order (user_id, product_id) VALUES(2,1);
--INSERT INTO purchase_order (user_id, product_id) VALUES(2,2);