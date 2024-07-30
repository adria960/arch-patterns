
1. Start zookeper and kafka from my PC.

2. Start docker compose - changes:
    mongo-express commented.
    Start MongoDB Compass

3. Start pgAdmin in browser localhost:5050
    Create new Connection (read postgres dbase settings in other project notes).
    Create user and product tables.

4. Changed method for KafkaTemplate in UserServiceImpl in raiseEvent:
           // this.kafkaTemplate.sendDefault(dto.getId(), value);
           this.kafkaTemplate.send("user-service-event", dto.getId(), value);

5. Start both services

6. Fist, populate user (localhost:8080/user-service/create) and product (manually) tables.

7. Now, create purchase-order record in Mongo DB - order-service.purchaseOrder collection.
8. Update User from user-service and the purchase order gets the changes - check it!

order-service       MONGO
------------------------------------
Sample data for Mongo db service-order
    private String id;
    private User user;
    private Product product;
    private double price;

user:  - postgresql:
    "id"	"firstname"	"lastname"	"email"
    ----------------------------------------
    3	"Slim"	"Shady"	"shady@vinsguru.com"
    2	"Michael"	"Jackson"	"mj@vinsguru.com"
    1	"Vins"	"Guru"	"admin@vinsguru.com"

    product  - postgresql:
    "id"	"description"	"price"	"qty_available"
    -----------------------------------------------
    3	"tv"	320.00	560
    2	"iphone"	650.00	98
    1	"ipad"	300.00	10

-------------------------------------------------------------

----------------------
MONGO data for testing
----------------------
POST
    localhost:8081/order-service/create
BODY
  {
        "user": {
            "id": 1,
            "firstname": "vins",
            "lastname": "guru",
            "email": "admin@vinsguru.com"
        },
        "product": {
            "id": 1,
            "description": "ipad"
        },
        "price": 300
    }

2nd record:
    {
        "user": {
            "id": 2,
            "firstname": "Michael",
            "lastname": "Jackson",
            "email": "mj@vinsguru.com"
        },
        "product": {
            "id": 1,
            "description": "ipad"
        },
        "price": 300
    }
3rd:
... todo
    {
        "user": {
            "id": 2,
            "firstname": "Michael",
            "lastname": "Jackson",
            "email": "mj@vinsguru.com"
        },
        "product": {
            "id": 2,
            "description": "iphone"
        },
        "price": 650
    }

 4th
     {
         "user": {
             "id": 3,
             "firstname": "Slim",
             "lastname": "Shady",
             "email": "shady@vinsguru.com"
         },
         "product": {
             "id": 3,
             "description": "tv"
         },
         "price": 320
     }

-----------------------------------------------------------------

   GET
   http://localhost:8081/order-service/all
   RESPONSE
[
    {
        "id": "653be2eaf9c2543a2d16dc18",
        "user": {
            "id": 1,
            "firstname": "vins",
            "lastname": "guru",
            "email": "admin@vinsguru.com"
        },
        "product": {
            "id": 1,
            "description": "ipad"
        },
        "price": 300.0
    },
    {
        "id": "653be517f9c2543a2d16dc19",
        "user": {
            "id": 2,
            "firstname": "Michael",
            "lastname": "Jackson",
            "email": "mj@vinsguru.com"
        },
        "product": {
            "id": 1,
            "description": "ipad"
        },
        "price": 300.0
    }
]
-------------- USER-SERVICE 8080 ------------------------
user-service
PUT
   http://localhost:8080/user-service/update
{
    "id": 1,
    "firstname":"vins",
    "lastname": "gur",
    "email": "admin-updated@vinsguru.com"
}
ok

NOW, CALL AGAIN:
http://localhost:8081/order-service/all
RESPONSE
[
    {
        "id": "653be2eaf9c2543a2d16dc18",
        "user": {
            "id": 1,
            "firstname": "vins",
            "lastname": "gur",
            "email": "admin-updated@vinsguru.com"
        },
        "product": {
            "id": 1,
            "description": "ipad"
        },
        "price": 300.0
    },
    {
        "id": "653be517f9c2543a2d16dc19",
        "user": {
            "id": 2,
            "firstname": "Michael",
            "lastname": "Jackson",
            "email": "mj@vinsguru.com"
        },
        "product": {
            "id": 1,
            "description": "ipad"
        },
        "price": 300.0
    }
]