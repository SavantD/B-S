Step 1. Design the application

 

 - The rules mentioned in the Assignment were taken in to careful consideration
 - Additional features were added considering the fact that, this REST API endpoints would provide more flexibility, and would help a feature rich UI (Client-App) to take maximum advantage of the well-structured endpoints.
 - Features such as product categorization, couponing for discounts and order placement has been added
-	Have maintained a separate table store the orders for a Customer(apart from the ShoppingCart), and would be creating a Discount Coupon for the Customer relevant to his Order. The Discount coupons would be issued for orders above a certain price threshold which could be configured. The order price would be calculated for the total items which were purchased by the customer. 
-	The customer could claim the coupon to get his discount at the next purchase (Order).
-	Furthermore each product is mapped to a specific product category. Reason behind this approach was since its much more easier for the client to query the product results. 
-	Main reason behind adding these additional features was that it would improve the overall User Experience (UX) factor.

Step 2. Implementation
 - The application was written in line to the REST-full principles and the microservice design structure. 
 - SpringBoot was used to develop the microservices pertaining to the resources we are modifying within our app.
 - The Java SpringBoot application consists of 5 RestControllers which handles the HTTP Requests and Responses ideally for each resource. 
 - Also there is a centralized exception handler written as per the best practices in microservice design. 
 - Unit tests have been written for couple of Services using Mockito to mock the Repositories. 
 - Repetitive logic has been moved to a Utility class which handles calculation logic. This is shared amongst couple of other services. Also unit tested.
 - The Database schema/tables are created upon application bootstrap. So running DDL statements manually is unnecessary. 
 -  org.slf4j.Logger has been used for logging purposes. (At Controller & Service levels.)
 - JPQL has been utilized in some cases where querying with join columns were required.



Testing & Debugging
-	Postman was used to test the API endpoints. 
-	The Collection of these requests (along with metadata for params & requestBody) have been attached to the GitHub itself. Please import this postman collection to test: 
https://github.com/SavantD/B-S/blob/master/Postman%20Collection/B%26S.postman_collection.json

 
Once you’ve imported the above json file, you’ll be seeing the API collection to test the API’s, as shown above.

You may clone this project and run/debug it with intellij.
