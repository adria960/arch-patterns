

Start kafka, zookeeper and all services:

1. docker-compose up -- kafka, zookeeper
2. order-service
3. order-orchestration
4. inventory-service
5. payment-service


TEST ORDERS endpoints:

1. POST
localhost:8080/order/create
BODY
{
    "userId": 1,
    "productId": 3
}
RESPONSE
{
    "id": "808e830e-9706-40ce-a4ac-6edbe81c5e65",
    "userId": 1,
    "productId": 3,
    "price": 300.0,
    "status": "ORDER_CREATED"
}


2. POST 5 times - to spend user's money and come to 100 balance (product price is 300)

check orders with:
GET localhost:8080/order/all
RESPONSE
[
    {
        "userId": 1,
        "productId": 3,
        "orderId": "808e830e-9706-40ce-a4ac-6edbe81c5e65",
        "amount": 300.0,
        "status": "ORDER_COMPLETED"
    },
    {
        "userId": 1,
        "productId": 3,
        "orderId": "cdbe1893-20a0-478c-a80e-3b17e723def4",
        "amount": 300.0,
        "status": "ORDER_COMPLETED"
    },
    {
        "userId": 1,
        "productId": 3,
        "orderId": "84213418-b0df-492e-ab1e-e6137849fadc",
        "amount": 300.0,
        "status": "ORDER_COMPLETED"
    },
    {
        "userId": 1,
        "productId": 3,
        "orderId": "c3b4f620-7dc6-4b38-a0d5-d46b7b2ecc5e",
        "amount": 300.0,
        "status": "ORDER_CANCELLED"
    },
    {
        "userId": 1,
        "productId": 3,
        "orderId": "57955d23-f0c5-4f20-8f43-83ba85de1023",
        "amount": 300.0,
        "status": "ORDER_CANCELLED"
    },
    {
        "userId": 1,
        "productId": 3,
        "orderId": "d760d828-b04a-4a1a-82e8-7216d3176f99",
        "amount": 300.0,
        "status": "ORDER_CANCELLED"
    },
    {
        "userId": 1,
        "productId": 3,
        "orderId": "0201283f-4539-48cd-a358-3ec3a105d708",
        "amount": 300.0,
        "status": "ORDER_CANCELLED"
    }
]
2 orders are canceled - not enough balance



3.  Buy product that cost 100 - order completed!

POST    localhost:8080/order/create
   BODY
{
   "userId": 1,
   "productId": 1
}
RESPONSE
{
    "id": "7f01842e-c138-4d0a-8ad8-56f615d8f687",
    "userId": 1,
    "productId": 1,
    "price": 100.0,
    "status": "ORDER_CREATED"
}


4. check orders with:
   GET localhost:8080/order/all
   RESPONSE
   [
       {
           "userId": 1,
           "productId": 3,
           "orderId": "808e830e-9706-40ce-a4ac-6edbe81c5e65",
           "amount": 300.0,
           "status": "ORDER_COMPLETED"
       },
       {
           "userId": 1,
           "productId": 3,
           "orderId": "cdbe1893-20a0-478c-a80e-3b17e723def4",
           "amount": 300.0,
           "status": "ORDER_COMPLETED"
       },
       {
           "userId": 1,
           "productId": 3,
           "orderId": "84213418-b0df-492e-ab1e-e6137849fadc",
           "amount": 300.0,
           "status": "ORDER_COMPLETED"
       },
       {
           "userId": 1,
           "productId": 3,
           "orderId": "c3b4f620-7dc6-4b38-a0d5-d46b7b2ecc5e",
           "amount": 300.0,
           "status": "ORDER_CANCELLED"
       },
       {
           "userId": 1,
           "productId": 3,
           "orderId": "57955d23-f0c5-4f20-8f43-83ba85de1023",
           "amount": 300.0,
           "status": "ORDER_CANCELLED"
       },
       {
           "userId": 1,
           "productId": 3,
           "orderId": "d760d828-b04a-4a1a-82e8-7216d3176f99",
           "amount": 300.0,
           "status": "ORDER_CANCELLED"
       },
       {
           "userId": 1,
           "productId": 3,
           "orderId": "0201283f-4539-48cd-a358-3ec3a105d708",
           "amount": 300.0,
           "status": "ORDER_CANCELLED"
       },
       {
           "userId": 1,
           "productId": 1,
           "orderId": "7f01842e-c138-4d0a-8ad8-56f615d8f687",
           "amount": 100.0,
           "status": "ORDER_COMPLETED"
       }
   ]