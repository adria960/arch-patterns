
spring.jpa.hibernate.ddl-auto=update
and
spring.jpa.hibernate.ddl-auto=create


1. Independent - no Sync:
Command - PORT  9191
--------------------
        POST
        localhost:9191/products
        BODY
            {
                "name": "Mobile",
                "description": "Samsung",
                "price": 650.50
            }
        RESPONSE
        BODY
            {
                "id": 1,
                "name": "Mobile",
                "description": "Samsung",
                "price": 650.50
            }

Query  PORT 9292
----------------
        GET
        localhost:9191/products
        RESPONSE
        []


2. Add Kafka stuff:
    producer, consumer to Command, Query - respectively
        POST
        localhost:9191/products                     -- PAY ATTENTION FOR JSON ProductEvent!!!
        BODY - ProductEvent
        {
            "eventType": "CreateProduct",
            "product": {
                "name": "Bag",
                "description": "Beneton",
                "price": 1200.50
            }
        }
        RESPONSE - Product
                {
                    "id": 1,
                    "name": "Shoes",
                    "description": "Aldo",
                    "price": 870.5
                }


        ..added one more product
        {
            "id": 2,
            "name": "Bag",
            "description": "Beneton",
            "price": 560.5
        }



2. GET Query
        localhost:9292/products
        RESPONSE
        [
            {
                "id": 1,
                "name": "Shoes",
                "description": "Aldo",
                "price": 870.5
            },
            {
                "id": 2,
                "name": "Bag",
                "description": "Beneton",
                "price": 560.5
            }
        ]
Dbase tables are in sync!

3. Command -
 PUT
     localhost:9191/products/1
    {
        "eventType": "UpdateProduct",
        "product": {
            "name": "Shoes",
            "description": "Aldo",
            "price": 2800.50
        }
    }

    RESPONSE
    {
        "id": 1,
        "name": "Shoes",
        "description": "Aldo",
        "price": 2800.5
    }

Dbase tables are in sync!!!

Get QUERY
localhost:9292/products/
RESPONSE
[
    {
        "id": 1,
        "name": "Shoes",
        "description": "Aldo",
        "price": 2800.5
    },
    {
        "id": 2,
        "name": "Bag",
        "description": "Beneton",
        "price": 560.5
    }
]

OK!!!

curl:

curl --location 'http://localhost:9191/products' \
--header 'Content-Type: application/json' \
--data '{
    "type": "CreateProduct",
    "product": {
        "name": "Books",
        "description": "KK publication",
        "price": 999
    }
}'

curl --location --request PUT 'http://localhost:9191/products/1' \
--header 'Content-Type: application/json' \
--data '{
    "type": "UpdateProduct",
    "product": {
        "id": 1,
        "name": "Watch",
        "description": "Samsung latest model",
        "price": 58000.0
    }
}'