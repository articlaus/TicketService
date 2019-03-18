**********************************
*  TicketService Homework
*
*  Ganbat Bayar 03/18/2019
**********************************

A simple ticket booking service, which can reserve the given number of seats on the Stage/Venue for 
a limited amount of time before releasing it back to be ordered again. Held seats can be reserved 
before the expiration. Reserved tickets cannot be cancelled nor expired tickets can be reserved.

************************************
This homework was done in High volume and concurrency in mind, hence using parallel streams etc which
is why performance may suffer on small sized Stage/Venue.

**********************************

Class purpose and certain descriptions are written inside the class via comments.
Refer to it for more details
**********************************
Seating algorithm tries to first fulfill request by seating everyone in a single row. If it couldn't find
a row which fulfills the condition, it assigns seats from the remaining free seats from top to bottom. 

**********************************
Project written using base java with only imports being

- JUnit 

For testing purpose

- Lombok

For ease of code readability.

*************
While in the project folder

Test the code using

- mvn test

To start the package

- mvn package exec:java
