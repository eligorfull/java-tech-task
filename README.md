# Lunch Microservice

The service provides an endpoint that will determine, from a set of recipes, what I can have for lunch at a given date, based on my fridge ingredient's expiry date, so that I can quickly decide what I’ll be having to eat, and the ingredients required to prepare the meal.

## Prerequisites

* [Java 11 Runtime](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Docker](https://docs.docker.com/get-docker/) & [Docker-Compose](https://docs.docker.com/compose/install/)

*Note: Docker is used for the local MySQL database instance, feel free to use your own instance or any other SQL database and insert data from lunch-data.sql script* 


### Run

1. Start database:

    ```
    docker-compose up -d
    ```
   
2. Add test data from  `sql/lunch-data.sql` to the database. Here's a helper script if you prefer:


    ```
    CONTAINER_ID=$(docker inspect --format="{{.Id}}" lunch-db)
    ```
    
    ```
    docker cp sql/lunch-data.sql $CONTAINER_ID:/lunch-data.sql
    ```
    
    ```
    docker exec $CONTAINER_ID /bin/sh -c 'mysql -u root -prezdytechtask lunch </lunch-data.sql'
    ```
    
3. Run Springboot LunchApplication

4. The lunch endpoint has been update it to use GET instead of POST e.g. http://localhost:8080/lunch?date=2020-06-06

This will return all the valid recipes that do not have expired ingredients with the addition of
displaying the bestBefore date ingredients at the bottom  

5. A new endpoint has been added to get a recipe by title e.g. localhots:8080/recipe?title=Salad

6. A new API endpoint was included to exclude recipes that include ingredients that I want to avoid e.g. http://localhost:8080/exclude?date=2020-06-06&exclude=Ham
The exclude parameter is a comma separated string that can include one or many ingredients to be excluded

7. Tests
Test cases were included to validate the functionality at each layer of the application including
repository
service
controller
