******************************************** docker-compose **************************************************************

NOTE:
Keep in mind, as the database is not sent by default for the official image, 
it will use the username as the database name!!!

**************************************************************************************************************************

1. docker-compose up
    I put different containers names for postgresql and pgAdmin:
    postgres2
    pgadmin2

    -----------------------
    pgAdmin
    -----------------------
    localhost:5050
    u. admin@vinsguru.com
    p. admin

Connection:
-----------
  Blank side menu - only servers
  Creating connection to postgres:
  see images pgAdmin from 1 to 3.

    General Tab:
        Name: any - I put vinsguru
        Server groups: Servers
    Connection:
        Host Name/Address: postgres2            -- this is container name for postgres (started from docker)
        Port: 5432
        Maintenance database: postgres
        Username: vinsguru
        Password: admin
    Parameters: default
    Save and Connect!
--------------------------------------------------------------------------

    Create 3 tables from db_tables.sql: users, product and purchase_order.

    Go through the db_tables file or db_all.sql
---------------------------------------------------------------------------

Run app:

    POSTMAN
    GET
    http://localhost:8080/po/summary
    [
        {
            "state": "OH",
            "totalSale": 694049.0
        },
        {
            "state": "NY",
            "totalSale": 1366176.0
        },
        {
            "state": "NM",
            "totalSale": 1378298.0
        },
        {
            "state": "NJ",
            "totalSale": 1381017.0
        },
        {
            "state": "NH",
            "totalSale": 692634.0
        },
        {
            "state": "NE",
            "totalSale": 1372656.0
        },
        {
            "state": "ND",
            "totalSale": 1375634.0
        },
        {
            "state": "MS",
            "totalSale": 659766.0
        },
        {
            "state": "MO",
            "totalSale": 1377152.0
        },
        {
            "state": "MN",
            "totalSale": 685853.0
        },
        {
            "state": "MD",
            "totalSale": 695196.0
        },
        {
            "state": "MA",
            "totalSale": 1380704.0
        },
        {
            "state": "AK",
            "totalSale": 689964.0
        },
        {
            "state": "LA",
            "totalSale": 699700.0
        },
        {
            "state": "KS",
            "totalSale": 694275.0
        },
        {
            "state": "IN",
            "totalSale": 2763547.0
        },
        {
            "state": "IL",
            "totalSale": 2034426.0
        },
        {
            "state": "ID",
            "totalSale": 671987.0
        },
        {
            "state": "HI",
            "totalSale": 669743.0
        },
        {
            "state": "GA",
            "totalSale": 696286.0
        },
        {
            "state": "FL",
            "totalSale": 1393653.0
        },
        {
            "state": "CT",
            "totalSale": 1392841.0
        },
        {
            "state": "CO",
            "totalSale": 2057483.0
        },
        {
            "state": "CA",
            "totalSale": 1372076.0
        },
        {
            "state": "AZ",
            "totalSale": 2071693.0
        },
        {
            "state": "OK",
            "totalSale": 680648.0
        },
        {
            "state": "AR",
            "totalSale": 2055568.0
        },
        {
            "state": "OR",
            "totalSale": 2407871.0
        },
        {
            "state": "PA",
            "totalSale": 4194697.0
        },
        {
            "state": "RI",
            "totalSale": 4093101.0
        },
        {
            "state": "SC",
            "totalSale": 696544.0
        },
        {
            "state": "TN",
            "totalSale": 662401.0
        },
        {
            "state": "TX",
            "totalSale": 703372.0
        },
        {
            "state": "UT",
            "totalSale": 3439893.0
        },
        {
            "state": "VA",
            "totalSale": 1375918.0
        },
        {
            "state": "VT",
            "totalSale": 2078467.0
        },
        {
            "state": "WA",
            "totalSale": 2507547.0
        },
        {
            "state": "WI",
            "totalSale": 2714236.0
        },
        {
            "state": "WV",
            "totalSale": 687653.0
        },
        {
            "state": "WY",
            "totalSale": 2067126.0
        }
    ]