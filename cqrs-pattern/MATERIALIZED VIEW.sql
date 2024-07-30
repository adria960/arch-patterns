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

"state"	"total_sale"
"UK"	15400.40
"USA"	9900.00


-- 1. Create view:
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
order by u.state


-- 2. Create materialized view:
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

-- 3. Refresh results:
-- to load into the purchase_order_summary:
REFRESH MATERIALIZED VIEW purchase_order_summary;
-- REFRESH MATERIALIZED VIEW CONCURRENTLY purchase_order_summary;

-- 4. Create FUNCTION for refresh:
CREATE OR REPLACE FUNCTION refresh_mat_view()
  RETURNS TRIGGER LANGUAGE plpgsql
  AS $$
  BEGIN
  REFRESH MATERIALIZED VIEW CONCURRENTLY purchase_order_summary;
  RETURN NULL;
  END $$;
  
-- -- 4. Create TRIGGER for refresh ON INSERT in purchase_order table:
CREATE TRIGGER refresh_mat_view_after_po_insert
  AFTER INSERT 
  ON purchase_order
  FOR EACH STATEMENT
  EXECUTE PROCEDURE refresh_mat_view();  


