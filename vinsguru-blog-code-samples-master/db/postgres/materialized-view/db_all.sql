-- DELETE - from materialzed view, then
DROP SEQUENCE IF EXISTS name [, ...] [ CASCADE | RESTRICT ]

-- FINAL
DROP TABLE IF EXISTS users;
CREATE TABLE users(
   id serial PRIMARY KEY,
   firstname VARCHAR (50),
   lastname VARCHAR (50),
   state VARCHAR(10)
);

-- FINAL
DROP TABLE IF EXISTS product;
CREATE TABLE product(
   id serial PRIMARY KEY,
   description VARCHAR (500),
   price numeric (10,2) NOT NULL
);

DROP TABLE IF EXISTS purchase_order;
CREATE TABLE purchase_order(
    id serial PRIMARY KEY,
    user_id integer references users (id),
    product_id integer references product (id)
);

-- Insert data in tables:
-- users
--INSERT INTO users (firstname, lastname, state) VALUES('James', 'Brown', 'UK');
--INSERT INTO users (firstname, lastname, state) VALUES('Mary', 'Gray', 'US');
--
---- product
--INSERT INTO product (description, price) VALUES('Laptop', 12000);
--INSERT INTO product (description, price) VALUES('Monitor', 8600);
--INSERT INTO product (description, price) VALUES('Keyboard', 2500);

-- purchase_order
--INSERT INTO purchase_order (user_id, product_id) VALUES(1,2);
--INSERT INTO purchase_order (user_id, product_id) VALUES(1,3);
--INSERT INTO purchase_order (user_id, product_id) VALUES(2,1);
--INSERT INTO purchase_order (user_id, product_id) VALUES(2,2);

-- SELECT nextval('context_context_id_seq')
-- 2
-- Order-service exposes an end point which provides the total sale values by users state.
    select
        u.state,
        sum(p.price) as total_sale
    from
        users u,
        product p,
        purchase_order po
    where
        u.id = po.user_id
        and p.id = po.product_id
    group by u.state
    order by u.state;

-- We could create a view to get the results we are interested in as shown here.
    create view purchase_order_summary
    as
    select u.state,
           sum(p.price) as total_sale
    from users u,
         product p,
         purchase_order po
    where u.id = po.user_id
      and p.id = po.product_id
    group by u.state
    order by u.state;

-- after populating data from dataGenerator - users, products, and then, orders
    DROP VIEW purchase_order_summary;

----------------------- FINAL --------------------------------------
    CREATE MATERIALIZED VIEW purchase_order_summary
    AS
    select
        u.state,
        sum(p.price) as total_sale
    from
        users u,
        product p,
        purchase_order po
    where
        u.id = po.user_id
        and p.id = po.product_id
    group by u.state
    order by u.state
    WITH NO DATA;
    CREATE UNIQUE INDEX state_category ON purchase_order_summary (state);

-- to load into the purchase_order_summary - FIRST TIME!
    REFRESH MATERIALIZED VIEW purchase_order_summary;
    REFRESH MATERIALIZED VIEW
-- Query returned successfully in 516 msec.

-- to load into the purchase_order_summary
    REFRESH MATERIALIZED VIEW CONCURRENTLY purchase_order_summary;

-- Lets start with creating a function first to update the materialized view.
    CREATE OR REPLACE FUNCTION refresh_mat_view()
      RETURNS TRIGGER LANGUAGE plpgsql
      AS $$
      BEGIN
      REFRESH MATERIALIZED VIEW CONCURRENTLY purchase_order_summary;
      RETURN NULL;
      END $$;

-- The above function should be called whenever we make entries into the purchase_order table.
-- So I create an after insert trigger.
    CREATE TRIGGER refresh_mat_view_after_po_insert
      AFTER INSERT
      ON purchase_order
      FOR EACH STATEMENT
      EXECUTE PROCEDURE refresh_mat_view();

-- Some testing queries:
--    INSERT INTO purchase_order (user_id, product_id) VALUES(1,1);
--    INSERT INTO purchase_order (user_id, product_id) VALUES(2,3);
--    SELECT * FROM public.purchase_order_summary
--    LIMIT 100

-- Lets create a simple procedure to refresh the view. This procedure would be called periodically via Spring boot.
-- create procedure
    CREATE OR REPLACE PROCEDURE refresh_mat_view()
    LANGUAGE plpgsql
    AS $$
    BEGIN
      REFRESH MATERIALIZED VIEW CONCURRENTLY purchase_order_summary;
    END;
    $$;

-- At last, write the code for refresh in Java class:
--            @Transactional
--            @Scheduled(fixedRate = 5000L)
--            public void refresh(){
--                this.entityManager.createNativeQuery("call refresh_mat_view();").executeUpdate();
--            }