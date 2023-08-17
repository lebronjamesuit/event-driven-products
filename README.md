# 1. Microservices Architecture Overview in the Project


<img width="702" alt="image" src="https://github.com/lebronjamesuit/event-driven-products/assets/11584601/eb82c1e3-6a7a-4bbd-a257-723dd245c0a9">
<img width="700" alt="Screenshot 2023-08-17 at 14 26 37" src="https://github.com/lebronjamesuit/event-driven-products/assets/11584601/dd82963c-11ba-44dd-9f28-d9fa3a06ed12">

---------------------------------------------------------------------

# 2. SAGA Design Pattern Overview in the Project

- Flowchart of successful order

<img width="704" alt="image" src="https://github.com/lebronjamesuit/event-driven-products/assets/11584601/0cd1895c-486d-4aca-bb24-4cff3588bcbd">

<img width="706" alt="image" src="https://github.com/lebronjamesuit/event-driven-products/assets/11584601/fe2275d5-05b3-4a66-90d6-0671b7e2251e">

--------------------------

-  Flowchart of rollback after failed at payment microservice flowchart

<img width="700" alt="image" src="https://github.com/lebronjamesuit/event-driven-products/assets/11584601/f583a41d-ba69-48a3-8fcb-c1ffdda63438">
<img width="695" alt="image" src="https://github.com/lebronjamesuit/event-driven-products/assets/11584601/ee20d52f-7fd8-45f6-8d52-5a8dff605f7c">

-------------------------------------------------------------------------------------

# 3. Detail of implementation Command Query Responsibility Segregation (CQRS)  &  Event Sourcing

3.1 Command Query Responsibility Segregation shows how microservices communicate with each other.

  <img width="701" alt="image" src="https://github.com/lebronjamesuit/event-driven-products/assets/11584601/e33b7fc0-596a-4fce-85e1-3aabd03c22f1">




3.2 Event Sourcing

  Part 1: Update to RDB

<img width="669" alt="image" src="https://github.com/lebronjamesuit/event-driven-products/assets/11584601/955c1e64-30dd-4011-b47e-5a0ef91d5b2f">

  
  Part 2: Publish Event

<img width="674" alt="image" src="https://github.com/lebronjamesuit/event-driven-products/assets/11584601/ceee311e-c7a1-47e4-a511-33d606071b46">

  Completed a lifecyle of event driven. 

  
 Aggregate product becomes first class citizen, every event ( create or update) will be tracked.
<img width="689" alt="image" src="https://github.com/lebronjamesuit/event-driven-products/assets/11584601/d1786c6f-7b74-4e09-ba61-6bd03a0d5f39">
<img width="686" alt="image" src="https://github.com/lebronjamesuit/event-driven-products/assets/11584601/7e6aa37b-64e9-47c1-a339-dbbb20f2ebe5">
<img width="690" alt="image" src="https://github.com/lebronjamesuit/event-driven-products/assets/11584601/8e693be9-ed96-4e85-b658-a78c3f8ae7aa">

-------------------------------------------------------------------------------------

# 4. General transactions in Microservice

<img width="700" alt="image" src="https://github.com/lebronjamesuit/event-driven-products/assets/11584601/aeca4736-3a81-41ca-8b20-7b1d0ed991d9">


-------------------------------------------------------------------------------------

# 5. Coding convention  

- Controller:
  
   a) Domain-CommandController (POST, PUT, DELETE)
  
       e.g OrderCommandController, ProductCommandController
  
   b) Domain-QueryController (GET)
  
       e.g ProductQueryController
       

- Command: Action-Domain-Command
  
      e.g CreateOrderCommand, CreateProductCommand, UpdateProductCommand, ReserveProductCommand, DeleteProductCommand

- Model: Domain-Model
  
      e.g  OrderRestModel, ProductRestModel

- Aggregate: Domain-Aggregate
  
      e.g  ProductAggregate, OrderAggregate, PaymentAggregate, UserAggregate
  
- Event: Domain-Action-Event
  
      e.g OrderApproveEvent, OrderCreateEvent, OrderRejectEvent

- Event handler: Domain-Events-Hander
  
      e.g OrderEventsHandler, PaymentEventHandler, ProductEventHandler

- Query : Action-Domain-Query
  
      e.g FindOrderQuery, FindProductQuery, FindUserQuery

- Query Handler: Domain-QueryHandler
  
      e.g OrderQueryHandler, ProductQueryHandler

-------------------------------------------------------------------------------------
# 6. Technologies
- Java 17+
- Axon Framework Spring boot 
- Spring Cloud 
- Lombok
- Postgresql, H2-DB 
- Axon Server 

-------------------------------------------------------------------------------------
# 7. Additional features in the project
 Product service by Snapshot

- Snapshoot threhold = 4
<img width="1272" alt="Screenshot 2023-08-17 at 12 02 46" src="https://github.com/lebronjamesuit/event-driven-products/assets/11584601/71fc4f7e-9d24-497b-9e00-f44cb261727e">
