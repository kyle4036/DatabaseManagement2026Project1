DROP DATABASE IF EXISTS testproject; 
CREATE DATABASE testproject;
USE testproject;

-- Probably changing the naming for final send off, not doing it now cuz all
-- the configs are set to testproject and it'll be a headache
-- DROP DATABASE IF EXISTS travel_reservation;
-- CREATE DATABASE travel_reservation;
-- USE travel_reservation;

CREATE TABLE Airlines (
    lineID CHAR(2) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE Airports (
    portID CHAR(3) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    city VARCHAR(50),
    country VARCHAR(50)
);

CREATE TABLE Aircrafts (
    craftID INT AUTO_INCREMENT PRIMARY KEY,
    lineID CHAR(2) NOT NULL,
    portID CHAR(3) NOT NULL,
    capacity INT NOT NULL,
    model VARCHAR(50),
    FOREIGN KEY (lineID) REFERENCES Airlines(lineID),
    FOREIGN KEY (portID) REFERENCES Airports(portID)
);

CREATE TABLE AirlineOperatesIn (
    lineID CHAR(2) NOT NULL,
    portID CHAR(3) NOT NULL,
    PRIMARY KEY (lineID, portID),
    FOREIGN KEY (lineID) REFERENCES Airlines(lineID),
    FOREIGN KEY (portID) REFERENCES Airports(portID)
);

CREATE TABLE Flights (
    flightNumber VARCHAR(6) NOT NULL,
    lineID CHAR(2) NOT NULL,
    departure_portID CHAR(3) NOT NULL,
    destination_portID CHAR(3) NOT NULL,
    departureTime TIME NOT NULL,
    arrivalTime TIME NOT NULL,
    flightType VARCHAR(15) NOT NULL,
    daysRunning CHAR(7) NOT NULL,
    seatsTaken INT NOT NULL DEFAULT 0,
    craftID INT NOT NULL,
    PRIMARY KEY (flightNumber, lineID),
    FOREIGN KEY (lineID) REFERENCES Airlines(lineID),
    FOREIGN KEY (departure_portID) REFERENCES Airports(portID),
    FOREIGN KEY (destination_portID) REFERENCES Airports(portID),
    FOREIGN KEY (craftID) REFERENCES Aircrafts(craftID)
);

CREATE TABLE Customers (
    customerID INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(30) NOT NULL,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(50),
    phoneNumber VARCHAR(15)
);

CREATE TABLE Employees (
    employeeID INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(30) NOT NULL,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE Tickets (
    ticketNumber INT AUTO_INCREMENT PRIMARY KEY,
    customerID INT NOT NULL,
    purchaseTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    bookingFee DECIMAL(10,2) NOT NULL,
    fareCost DECIMAL(10,2) NOT NULL,
    tripType VARCHAR(15) NOT NULL,
    status VARCHAR(15) NOT NULL DEFAULT 'active',
    FOREIGN KEY (customerID) REFERENCES Customers(customerID)
);

CREATE TABLE FlightTickets (
    ticketNumber INT NOT NULL,
    flightNumber VARCHAR(6) NOT NULL,
    lineID CHAR(2) NOT NULL,
    legOrder INT NOT NULL,
    departureDate DATE NOT NULL,
    seatNumber VARCHAR(5),
    ticketClass VARCHAR(15) NOT NULL,
    mealOrder VARCHAR(50),
    PRIMARY KEY (ticketNumber, legOrder),
    FOREIGN KEY (ticketNumber) REFERENCES Tickets(ticketNumber),
    FOREIGN KEY (flightNumber, lineID) REFERENCES Flights(flightNumber, lineID)
);

CREATE TABLE WaitingList (
    customerID INT NOT NULL,
    flightNumber VARCHAR(6) NOT NULL,
    lineID CHAR(2) NOT NULL,
    requestTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (customerID, flightNumber, lineID),
    FOREIGN KEY (customerID) REFERENCES Customers(customerID),
    FOREIGN KEY (flightNumber, lineID) REFERENCES Flights(flightNumber, lineID)
);

CREATE TABLE QnA (
    questionID INT AUTO_INCREMENT PRIMARY KEY,
    customerID INT NOT NULL,
    employeeID INT,
    question TEXT NOT NULL,
    answer TEXT,
    FOREIGN KEY (customerID) REFERENCES Customers(customerID),
    FOREIGN KEY (employeeID) REFERENCES Employees(employeeID)
);
