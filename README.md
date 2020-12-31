# Toll Parking API
This project is a Toll Parking REST API  server sample. The project provides REST API endpoints that could be used by the client ( an example of a client is a toll parking appliance).

## Installation and running the project
### On Windows
The application requires:
- Java 15
- Apache Maven 3.6.3
- Git (and Git Bush)

Be sure thate they are installed and that their environment variable are set.
Steps:
- Clone or download this project from GitHub OR you can run *Git Bush* shell and target the folder where you want to install the project and run the following command (from *Git Bush*)
```
git clone https://github.com/adigirolamo/parkingtoll.git
```
- Go into the project folder. To do it, if you are still in the folder where you have run *git clone* you could run the following command:
```
cd ./parkingtoll/
```
- Run the following command:
```
mvn -N io.takari:maven:wrapper
```
- Now we can use maven wrapper, therefore run:
```
./mvnw clean package spring-boot:repackage
```
- When the build is completed you can run the project with the following command:
```
java -jar ./target/parkingtoll-1.0.0.jar
```
In case of error it is possible to analyze the logs in *./logs* . The application uses the port 8080, therefore you could check that 8080 is not already in use.

## Useful links
### REST API endpoints documentation
- http://localhost:8080/swagger-ui.html

REST API endpoints have been documented using swagger. It's possible to access the interactive documentation via the above link, **while the project is running**.

### Database console
- http://localhost:8080/h2-console
	- Driver Class: org.h2.Driver
	- JDBC URL: jdbc:h2:mem:mydb
	- username: sa
	- password: sa

It has been used H2 has Database. **While the project is running** it is possible to access the DB console via the above link and credentials.

## Overview
The project is a Toll Parking REST API server sample. 
The application manages more than one toll parking.
Each toll parking contains multiple parking slots of different types:
- the standard parking slots for gasoline-powered cars
- parking slots with 20kw power supply for electric cars
- parking slots with 50kw power supply for electric cars

**Note**: 20kw electric cars cannot use 50kw power supplies and vice-versa

The application provides four API endpoints that could be used by clients (ex. a toll parking appliance) to:
#### Get a free parking slot
 Get a free parking slot for the incoming vehicle and assign it to the vehicle. The slot is assigned by vehicle *engines type*. If there isn't any free slot (of the right type) left, the vehicle will be refused (the api returns an error,  [check swagger doc](http://localhost:8080/swagger-ui.html)). Ex. car A of type gasoline will request a parking slot, if the toll parking has not FREE parking slot for gasoline engine, the BE will respond with an error (404).

The appliance (client) must send:
- the toll parking that it is requesting the parking slot for
- the vechile engine type
- the vehicle plate

#### Get vehicle bill
The BE will calculate the amount that the vehicle has to pay and it will return it. **The bill calculation is based on the time that the vehicle has used the slot, the pricing policy that the Toll parking has set and the toll parking pricing rate**. Two strategies are supported: 
	- Price per minutes * time spent
	- Fixed amount plus price per minutes * time spent 
#### Update vehicle bill status to PAID
The appliance notificates the BE that the bill has been paid.
#### Update the parking slot to free
The appliance notificates the BE that has to update the parking slot to free , the slot was before used by the leaving vehicle (raise the bar!)

### Parking slot state concept
An important design concept is that the events described above have been managed introducing a state strategy. A parking slot has 4 different state:
- free
- reserved
- paying
- paid

Each event above changes the parking slot from its actual state to the one related to the event:
1. [get free parking slot](#get-a-free-parking-slot) changes parking slot state from *free* to *reserved*.
2. [get vehicle bill](#get-vehicle-bill) changes parking slot state from *reserved* to *paying*
3. [update vehicle bill status to paid](#update-vehicle-bill-status-to-paid) changes parking slot state from *paying* to *paid*
4. [update the parking slot to free](#update-the-parking-slot-to-free) changes the parking slot state from *paid* to *free*

A parking slot can changes from state A to state B only if state B is an allowed next state for state A, otherwise an exception will be raised. 
Based on the new state, there is a concrete implementation, for each entity, that changes the entity values to be consistents with the new stage.
[State Service](https://github.com/adigirolamo/parkingtoll/blob/main/src/main/java/com/adigi/parkingtoll/service/state/ParkingSlotStateService.java)

### What has been used
The project uses:
- Java 15
- Apache Maven 3.6.3
- Spring Boot 2.4.1
	- Spring Boot data jdbc
	- Spring Boot web
	- Spring Boot validation
	- Spring Boot data jpa
	- Spring Boot test
	- Spring restdocs mockmvc
	- Springdoc openapi
- h2
- lombok

### Functional description example
##### Incoming vehicle
[Get free parking slot](#get-a-free-parking-slot).

An incoming vehicle arrives to toll parking *Parking1*. *Parking1*'s appliance recognizes the vehicle's plate, it requests the vehicle engine type to and external Statal Vechiles API and it requests a free parking slot for the vehicle to the BE. The slot is requested targeting an engine type.
If there is an available slot the BE returns the available slot to the appliance and the appliance displays it to the vechile. The vehicle enters the toll parking.

<details>
  <summary>Click to display sequence diagram</summary>

![image](https://user-images.githubusercontent.com/12895385/103404638-a2775e00-4b54-11eb-892b-fad959cd90f7.png)
</details>
	
##### Vehicle asks the bill
[Get vehicle bill](#get-vehicle-bill)
<details>
  <summary>Click to display sequence diagram</summary>

![image](https://user-images.githubusercontent.com/12895385/103404666-bfac2c80-4b54-11eb-8ca9-bad5210c44c9.png)
</details>

##### Vehicle pays the bill AND appliance raises the bar
[Update-vehicle-bill-status-to-paid](#update-vehicle-bill-status-to-paid)
[Update the parking slot to free](#update-the-parking-slot-to-free)
<details>
  <summary>Click to display sequence diagram</summary>

![image](https://user-images.githubusercontent.com/12895385/103404679-d2266600-4b54-11eb-8861-e6d4d8efcc95.png)
</details>

### Database Entities
<details>
  <summary>Click to display sequence diagram</summary>
	
![image](https://user-images.githubusercontent.com/12895385/103404893-89bb7800-4b55-11eb-8cb3-58019eda4e24.png)
</details>

List of entities:
##### Parking
[Parking](https://github.com/adigirolamo/parkingtoll/blob/main/src/main/java/com/adigi/parkingtoll/model/persistence/entity/Parking.java) Represents a toll parking.
Relation:
- one to many with parking slot

Important columns:
- NAME_UID: Parking unique name
- FIXED_AMOUNT: Fixed pricing rate that could be used by the pricing strategy
- MINUTE_PRICE: price per minute cost
- PRICINGPOLICY: the bill amount will be calculated based on the chosen type of pricing policy.
**Note**: two parkings are already configured when the application starts:
- PARKING1: its pricing strategy is price per minutes
- PARKING2: its pricing strategy is price per minutes plus fixed amount.

##### ParkingSlot: 
[ParkingSlot](https://github.com/adigirolamo/parkingtoll/blob/main/src/main/java/com/adigi/parkingtoll/model/persistence/entity/ParkingSlot.java) Represents a parking slot of a toll parking. 
Relations:
	- many to one with Parking
	- one to one with Reservation
Important columns:
- VEHICLETYPE: Vehicle type that the parking slot is designed for
- ENGINETYPE: Engine type that the parking slot is designed for.

##### Reservation
[Reservation](https://github.com/adigirolamo/parkingtoll/blob/main/src/main/java/com/adigi/parkingtoll/model/persistence/entity/Reservation.java)
it is the reservation related to a parking slot.
Relations:
- it has one to one relation to a parking slot
- it has one to one relation to a bill
Important columns:
- localArriveDateTime: vehicle arrival time
- localDepartureDateTime: vehicle departure time
- localPaymentDateTime: vehicle payment time 
#### Bill
[Bill](https://github.com/adigirolamo/parkingtoll/blob/main/src/main/java/com/adigi/parkingtoll/model/persistence/entity/Bill.java)
Relations:
- it has one to one relation to a reservation
Important columns:
- AMOUNT: indicates how mutch must be paid
- Currency

## Code

### Overview
The project has been designed and organized in:
- presentation layer: in this package there are controllers, DTO and response error handling.
- model: in this package there are the entities and the repositories. Repositories use Spring Data JPA
- service: in this package there are the services. They that take care of the business logic.
It is not a classic MVC (no view!)

### Sequence diagrams
#### [Sequence diagram of Get a free parking slot](#get-a-free-parking-slot)
<details>
  <summary>Click to display sequence diagram</summary>

![image](https://user-images.githubusercontent.com/12895385/103404373-8cb56900-4b53-11eb-92b4-32dae7782fc6.png)
</details>

A client requests *get free parking slot*.
The request arrives to ParkingSlotControl that delegates it to ParkingSlotService.
ParkingSlotService (PSService) has the BL related to parking slot entity.
PSService (call 1.1.1) asks ParkingSlotRepository to get a free parking slot. *Note* that the isolation level of this transaction is set to avoid phantom reads.
When the object Parking slot is returned , PSService calls ExceptionService that checks if the object is ok or if it has to throw an exception (ex. NOT FOUND).
PSService asks ParkingSlotStateService (PSStateService) to handle the Parking slot state change.
PSStateService has a master role in *state changes BL*. It organizes the state change process and it delegates the change execution to its worker:
- PSStateService checks if the change is allowed 
- if the change is allowed PSStateService asks the specific *Entity State Change* to do the change : 
	- ParkingSlotChangeState
	- BillChangeState
	- ReservationChangeState.
These 3 classes applies a different strategy implementation based on the requested state. They extends AbstractStateChange.
When the change process is completed the entity is returned to PSControl that maps the entity to DTO and returns it.

**Note:** the following sequence diagrams are the sequence diagrams of the 3 other API request, they have the same structure has this diagram.

#### [Sequence diagram of Get vehicle bill](#get-vehicle-bill)
<details>
  <summary>Click to display sequence diagram</summary>

![image](https://user-images.githubusercontent.com/12895385/103404722-f2562500-4b54-11eb-98b3-36ff3c7a1f43.png)
</details>

#### [Update vehicle bill status to paid](#update-vehicle-bill-status-to-paid)
<details>
  <summary>Click to display sequence diagram</summary>
	
![image](https://user-images.githubusercontent.com/12895385/103404739-013cd780-4b55-11eb-926c-01c78fc8b1d0.png)
</details>

#### [Sequence diagram of Update the parking slot to free](#update-the-parking-slot-to-free)
<details>
  <summary>Click to display sequence diagram</summary>

![image](https://user-images.githubusercontent.com/12895385/103404766-174a9800-4b55-11eb-99bc-1ace61465345.png)
</details>

### Code Point Of Interest

#### Pricing Policies
Each toll parking can have its own pricing and its own pricing policy.
They are defined in the [parking entity](#Parking) columns:
- FIXED_AMOUNT: Fixed amount pricing
- MINUTE_PRICE: price per minute pricing
- PRICINGPOLICY (PP): 
	- Price per minute
	- Fixed amount plus price per minute
The pricing bill, for a leaving vehicle, will be calculated based on the PP set for the toll parking. 
To implement the concrete pricing strategy, for each PP, it has been used a strategy pattern:
[link to the code](https://github.com/adigirolamo/parkingtoll/tree/main/src/main/java/com/adigi/parkingtoll/service/pricing/strategy)
At the moment there are only two PP, but they can grown, therefore it has been used a *Factory Design Pattern* specific for *Spring*:
[link to PricingStrategyFactory class](https://github.com/adigirolamo/parkingtoll/blob/main/src/main/java/com/adigi/parkingtoll/service/pricing/PricingStrategyFactory.java)
*PricingStrategyFactory* has an autowired constructor method that has as arguments a list of *PricingStrategy*. Each class that implements *PricingStrategy*,
has to be marked as Spring Service (@Service), doing so Spring will take care of passing the *PricingStrategy* classes to *PricingStrategyFactory* constructor,
therefore no change will be needed for *PricingStrategyFactory* when a new PricingStrategy will be added. 
<details>
  <summary>Click to display sequence diagram of how the price is calculated</summary>
	
![image](https://user-images.githubusercontent.com/12895385/103407338-a14b2e80-4b5e-11eb-86c4-109838260155.png)
</details>


#### Parking slot state
[Parking slot state change concept](#parking-slot-state-concept)
It has been used a strategy pattern to handle the different implementation of each state change.
[link to the code](https://github.com/adigirolamo/parkingtoll/tree/main/src/main/java/com/adigi/parkingtoll/service/state/strategy)
Concrete implementations:
- BillChangeState
- ParkingSlotChangeState
- ReservationChangeState
Each concrete impl extends AbstractChangeState<T> and implements the strategy to be used for each state change.

##### Devired query and JPQL examples
[BillRepository](https://github.com/adigirolamo/parkingtoll/blob/main/src/main/java/com/adigi/parkingtoll/model/persistence/repository/BillRepository.java)
In BillRepository there is an example of the same query implemented via JPQL and Derived query.

### Response Error handling
It has been implemented a global error hadnling managment, using *@ControllerAdvice* annotation: 
[CustomRestExceptionHandler](https://github.com/adigirolamo/parkingtoll/blob/main/src/main/java/com/adigi/parkingtoll/presentation/errorhandling/CustomRestExceptionHandler.java)

### Test
There are 78 tests implemented. 
The tests go from unit test, to persistance integration test to controller integration test (end to end).
Important to note:
- the persistance integration test classes extend a common base classes *BaseEntityTest* that is annotated with *@DataJpaTestJunit*
- there are two main groups of end to end test:
	1. [Controller integration test](https://github.com/adigirolamo/parkingtoll/tree/main/src/test/java/com/adigi/parkingtoll/presentation/controller) 
	2. [Response Error Handling tests](https://github.com/adigirolamo/parkingtoll/tree/main/src/test/java/com/adigi/parkingtoll/presentation/errorhandling)

The classes related *Controller integration test* have been implemented using *MockMvc*.
The class related to *Response Error Handling tests* uses *TestRestTemplate*.
It has been chosen two different implementations to show different ways to realize integration test. 
PS: if you can, use *TestRestTemplate*, it avoids lots additional code.

## How to try it
The project could be demoed :
1. Using Swagger UI
2. Using Postman (ex. Postman chrome extention)

It is important to keep in mind that the order of requests matter, otherwise the BE will not repond with *success* but with an error. This is done because it is not possible to pay a bill if the bill price has not been requested before, or it is not possible to ask to set the parking slot to free (and raise the parking bar) if the parking slot's bill has not been paid.

#### Request order:
1. Parking slot API GET (Get FREE parking slot) : /parkings/{parkingNameUid}/parkingslots?plate={plate}&engineType={engineType}
2. Bill API GET (Get vehicle's bill) : /parkings/{parkingNameUid}/parkingslots/{plate}/reservation/bill
3. Bill API PUT (Update bill to paid) : /parkings/{parkingNameUid}/parkingslots/{plate}/reservation/bill
4. Parking slot API PUT(Update parking slot to FREE) : /parkings/{parkingNameUid}/parkingslots/{parkingSlotId}

#### DB pre loaded data
It has been used [data.sql](https://github.com/adigirolamo/parkingtoll/blob/091e7d38e5a5e36d6146e71dcc297721c6e888d1/src/main/resources/data.sql) to configure two different Toll Parking with their own slot configured.
The toll parking name UID are:
- PARKING1
- PARKING2
Parking 1 has configured the pricing policy *price per minutes only*.
Parking 2 has configured the pricing policy *fixed amount plus price per minutes only*.
The 2 toll parkings have different amount for *price per minute* pricing.

#### List of Postman useful requests
| Toll Parking | Req TYPE      | Request |
| -------------| ------------- | ---------------------------------------------------- |
|  |  | Test PARKING1   |
| PARKING1 | GET | http://localhost:8080/parkings/PARKING1/parkingslots?plate=Dasd&engineType=GASOLINE  |
| PARKING1 | GET | http://localhost:8080/parkings/PARKING1/parkingslots/Dasd/reservation/bill  |
| PARKING1 | PUT | http://localhost:8080/parkings/PARKING1/parkingslots/Dasd/reservation/bill  |
| PARKING1 | PUT | http://localhost:8080/parkings/PARKING1/parkingslots/1  |
|  |  | Test PARKING2   |
| PARKING2 | GET | http://localhost:8080/parkings/PARKING2/parkingslots?plate=zzAsz&engineType=GASOLINE  |
| PARKING2 | GET | http://localhost:8080/parkings/PARKING2/parkingslots/zzAsz/reservation/bill  |
| PARKING2 | PUT | http://localhost:8080/parkings/PARKING2/parkingslots/zzAsz/reservation/bill  |
| PARKING2 | PUT | http://localhost:8080/parkings/PARKING2/parkingslots/6  |
|  |  | Test get free parking slot for different engine type   |
| PARKING1 | GET | http://localhost:8080/parkings/PARKING1/parkingslots?plate=Dasd&engineType=GASOLINE  |
| PARKING1 | GET | http://localhost:8080/parkings/PARKING1/parkingslots?plate=Dasd2&engineType=GASOLINE  |
| PARKING1 | GET | http://localhost:8080/parkings/PARKING1/parkingslots?plate=Dasd1&engineType=GASOLINE  |
| PARKING1 | GET | http://localhost:8080/parkings/PARKING1/parkingslots?plate=Dasdzzl&engineType=ELECTRIC_20KW  |



## Documentation that has been helpful to realize the project
- https://www.baeldung.com/
- https://springdoc.org/
- https://swagger.io/docs/specification/adding-examples/
