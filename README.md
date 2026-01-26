## Application Flow in Detail

1. **Order Creation (REST API)**  
   A client creates an order using a REST endpoint (`/api/orders`).  
   The request contains customer details, product, and amount.

2. **File-Based Processing**  
   After validation, the order is written as a JSON file to the `input/orders` directory.

3. **Apache Camel Route**  
   Apache Camel continuously monitors the input directory.  
   When a new order file is detected, Camel reads the file and performs validation.

4. **Message Publishing to ActiveMQ**  
   Once validated, Camel publishes the order message to the  
   `ORDER.CREATED.QUEUE` in Apache ActiveMQ.

5. **Message Consumption**  
   A consumer listens to the ActiveMQ queue and receives the order message.

6. **Order Processing**  
   The order is processed successfully and relevant details are logged in the
   Spring Boot application console.

---

### End-to-End Flow
