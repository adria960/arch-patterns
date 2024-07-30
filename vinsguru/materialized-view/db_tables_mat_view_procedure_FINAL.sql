-- SQL PART - FINAL - USE update in app properties - but start docker - postgres pgadmin,
-- create 3 tables and 1 mat view:
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

--    DROP VIEW purchase_order_summary;

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

-- This procedure is called from scheduled task in MaterializedViewRefresher class
CREATE OR REPLACE PROCEDURE refresh_mat_view()
LANGUAGE plpgsql
AS $$
BEGIN
  REFRESH MATERIALIZED VIEW CONCURRENTLY purchase_order_summary;
END;
$$;