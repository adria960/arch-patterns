******************************************** docker-compose **************************************************************
NOTE:
Keep in mind, as the database is not set by default for the official image,
it will use the username as the database name!!!
**************************************************************************************************************************
1. docker-compose up from gitbash (linux env look). From IntelliJ Terminal - error for pgadmin - on some dir
2. create 4 tables and insert data in users and product.
4. Test write=true (app.yml)- create or place order and test command part.
5. Set write=false, and test query part.


NOTE:
The app is for testing purposes:
- 1 dbase - different tables
- application.yml: (there is property in controllers too - need to set
               app:
                write:
                  enabled: true
or false for query.
*********************************************************************

Explicitly configure spring.jpa.open-in-view to disable this warning - Run with update - error when start app without db tables created.

*********************************************************************
PostgreSQL and pgAdmin start from docker-compose!
pgadmin
localhost:5050
master password= masterpassword

admin@vinsguru.com

PurchaseOrder
    private Long id;
    private Long userId;
    private Long productId;
    private Date orderDate

OrderCommandDto
    private int userIndex;
    private int productIndex;

Create users, products and purchase_order tables.
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
--------------------------------------------------

A.
CHANGE write to true in application.yml:
app:
  write:
    enabled: true

1. Test Controller - command:
POST
OrderCommandDto
----------------------
localhost:8080/po/sale

    {
        "userIndex": 0,
        "productIndex": 0
    }

Post more:
0,0
0,1
0,2
1,2
1,1

select * from purchase_order
"id"	"user_id"	"product_id"	"order_date"
------------------------------------------------
4	1	1	"2023-10-24"
5	1	2	"2023-10-24"
6	1	3	"2023-10-24"
7	2	3	"2023-10-24"
8	2	2	"2023-10-24"

2. PUT
http://localhost:8080/po/cancel-order/5
200 OK
Deletes the record no 5 in purchase_order
-----------------------------------------------------

B.
CHANGE write to false in application.yml:
app:
  write:
    enabled: false

1. GET
http://localhost:8080/po/summary
RESPONSE
[
    {
        "state": "UK",
        "totalSale": 15400.4
    },
    {
        "state": "USA",
        "totalSale": 9900.0
    }
]

2. GET
http://localhost:8080/po/summary/UK
RESPONSE
{
    "state": "UK",
    "totalSale": 15400.4
}

3. GET
http://localhost:8080/po/total-sale
RESPONSE
25300.4

---------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------
So, based on a property, we change if the app is going to behave like a read-only node or write-only node.
It gives us the ability to run multiple instances of an app with different modes.
I can have 1 instance of my app which does the writing while I can have multiple instances of my app
just for serving the read requests.
They can be scaled in-out independently. We can place them behind a load balancer / proxy like nginx – so
that READ / WRITE requests could be forwarded to appropriate instances using path based routing or some other mechanism.
-------------------------------------------------------------------------------------
