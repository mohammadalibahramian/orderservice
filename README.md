# Order Service Application

An application to manage orders

## Requirements

1. Java - 1.8.x

2. Gradle - g.x.x


## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/mohammadalibahramian/sampleRestAPISpringbootAngularJsSwagger.git
```
**2. The application consists of three component**

- Orders Service
    * Provides CRUD operation for orders and send an event to kafka whenever a change has been made on orders
- Apache Kafka
    * Acts as a publisher-subscriber and holding a topic called "order_topic"
- Order Messaging
    * A service that consumes from Kafka "order_topic" and logs the event
    

**3. Run messaging application by using this command**
```bash
cd ordermessaging
docker-compose up -d
```

**4. Build and run the order service by using**


```bash
gradle bootRun
```

The app will start and running at <http://localhost:7575>.

## Explore Rest APIs

The app defines following CRUD APIs.

    GET /orders (get a list of orders)
    
    GET /orders{orderId} (find an order by id)

    POST /orders (create an order)
    
    PUT /orders/{orderId} (update an order)

    DELETE /orders/{orderId}  (delete an order by id)
    
To do CRUD, we need to generate a token from Azure AD by using this command

```shell
curl --location --request POST 'https://login.microsoftonline.com/169e73c7-4d1b-488c-89fd-e3a05c4ceb97/oauth2/v2.0/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'client_id=e1c25825-21cf-47fb-aa36-b4cbc71f4d8a' \
--data-urlencode 'testuser@eriksorderservicedirectory.onmicrosoft.com' \
--data-urlencode 'password=Erik@Order1!' \
--data-urlencode 'scope=api://e1c25825-21cf-47fb-aa36-b4cbc71f4d8a/orders' \
--data-urlencode 'client_secret=s.b5hxPYnW40NNg27~TU96_3~r.Uu46z5l'
```
Add this token as a header as follow

```bash
Header: Authorization
Value: Bearer token 
```


## Rest API Documentation

You can find the APIs documentation for this application on 

<http://localhost:7575/swagger-ui.html>

