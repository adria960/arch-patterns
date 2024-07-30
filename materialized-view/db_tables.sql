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
    product_id integer references product (id)
);

-- Insert data in tables:
INSERT INTO users (firstname, lastname, state) VALUES('James', 'Brown', 'UK');
INSERT INTO users (firstname, lastname, state) VALUES('Mary', 'Gray', 'US');

INSERT INTO product (description, price) VALUES('Laptop', 12000);
INSERT INTO product (description, price) VALUES('Monitor', 8600);
INSERT INTO product (description, price) VALUES('Keyboard', 2500);

INSERT INTO purchase_order (user_id, product_id) VALUES(1,2);
INSERT INTO purchase_order (user_id, product_id) VALUES(1,3);
INSERT INTO purchase_order (user_id, product_id) VALUES(2,1);
INSERT INTO purchase_order (user_id, product_id) VALUES(2,2);


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

Result:
"state"	"total_sale"
"UK"	11100.00
"US"	20600.00

We could create a view to get the results we are interested in as shown here.
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

    select * from purchase_order_summary;
Result:
"state"	"total_sale"
"UK"	11100.00
"US"	20600.00

Performance Test – DB View:
---------------------------
1st
Author comments:
I inserted 10000 users in the users table
I inserted 1000 products into the product table
--
        this.createUsers(faker);
        this.createProducts(faker);
-- comment
        //TODO update range
      //  IntStream.range(7, 17)
      //          .forEach(i -> this.createOrder(faker));
      //  this.createOrder(faker);
2nd
-- comment createUsers and createProducts methods
    I inserted 5 Million user orders for random user + product combination into the purchase_order table
    I run a performance test using JMeter with 11 concurrent users
    10 users for sending the requests for READ
    1 user for creating purchase order continuously

RESULTS - PENDING...
As we can see, sale-summary average response time is 7.2 second.
It is trying to aggregate the information by state from the purchase_order table for every GET request.

Problem With Views:
Views are virtual tables in a DB
Even though DB Views are great in hiding some sensitive information and provide data in a simpler table like structure,
the underlying query is executed every time. It could be required in some cases where the data changes very frequently.
However in most of the cases it could affect the performance of the application very badly!

Materialized View PostgreSQL:
Materialized Views are most likely views in a DB. But they are not virtual tables.
Instead the data is actually calculated / retrieved using the query and the result is stored in the hard disk as a separate table.
So when we execute below query, the underlying query is not executed every time.
Instead the data is fetched directly from the table.
This is something like using the cached data.
So it improves the performance.

--
DROP VIEW purchase_order_summary;

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
Query returned successfully in 516 msec.

-- to load into the purchase_order_summary
REFRESH MATERIALIZED VIEW CONCURRENTLY purchase_order_summary;

The obvious question would be what if the source data is updated.
That is, if we make new entry into the purchase_order table, how the purchase_order_summary table will be updated!?
It will not automatically update.
We need to make some actions to do that.

Materialized View PostgreSQL – Auto Update With Triggers:
We need to update purchase_order_summary only when we make entries into the purchase_order.
(I ignore delete/update operations as of now).
So lets create a trigger to update the materialized views whenever we make entries into purchase_order table.
So lets start with creating a function first to update the materialized view.

CREATE OR REPLACE FUNCTION refresh_mat_view()
  RETURNS TRIGGER LANGUAGE plpgsql
  AS $$
  BEGIN
  REFRESH MATERIALIZED VIEW CONCURRENTLY purchase_order_summary;
  RETURN NULL;
  END $$;

The above function should be called whenever we make entries into the purchase_order table.
So I create an after insert trigger.

CREATE TRIGGER refresh_mat_view_after_po_insert
  AFTER INSERT
  ON purchase_order
  FOR EACH STATEMENT
  EXECUTE PROCEDURE refresh_mat_view();

NOTE:   CANNOT SEE the trigger - created but cannot find it.

INSERT INTO purchase_order (user_id, product_id) VALUES(1,1);
SELECT * FROM public.purchase_order_summary
LIMIT 100
"state"	"total_sale"
"US"	20600.00
"UK"	23100.00
-- it added for user 1 UK laptop

    INSERT INTO purchase_order (user_id, product_id) VALUES(2,3);

  Materialized View – Performance Test:
  ---------------------------------------
  I re-run the same performance test.
  This time I get exceptionally great result for my sale-summary. As the underlying query is not executed for every GET request, the performance is great! The throughput goes above 3000 requests / second.
  However the performance of the new purchase_order request is affected as it is responsible for updating the materialized view.
  In some cases it could be OK if we are doing the new order placement asynchronously.

...
But do we really need to update summary for every order.
Instead, we could update the materialized view certain interval like 5 seconds.
The data might not be very accurate for few seconds. It will eventually be refreshed in 5 seconds.
This could be a nice solution to avoid the new order performance issue which we saw above.

Lets drop the trigger and the function we had created.
    drop trigger refresh_mat_view_after_po_insert ON purchase_order;
    drop function refresh_mat_view();

Lets create a simple procedure to refresh the view. This procedure would be called periodically via Spring boot.
---------------------------------------------------------------------------------------
-- create procedure
    CREATE OR REPLACE PROCEDURE refresh_mat_view()
    LANGUAGE plpgsql
    AS $$
    BEGIN
      REFRESH MATERIALIZED VIEW CONCURRENTLY purchase_order_summary;
    END;
    $$;

Materialized View With Spring Boot:
-----------------------------------------
I add the new component which will be responsible for calling the procedure periodically.
        @Component
        public class MaterializedViewRefresher {

            @Autowired
            private EntityManager entityManager;

            @Transactional
            @Scheduled(fixedRate = 5000L)
            public void refresh(){
                this.entityManager.createNativeQuery("call refresh_mat_view();").executeUpdate();
            }

        }

I re-run the same performance test to get the below results.
I get extremely high throughput for my both read and write operations.
The average response time is 6 milliseconds in both cases

Summary:
We were able to demonstrate the usage of Materialized View PostgreSQL with Spring Boot  to improve the performance of the
read heavy operations for the Microservices architecture.
Implementing this pattern will also enable us implementing CQRS pattern to improve the performance further of our microservices.
