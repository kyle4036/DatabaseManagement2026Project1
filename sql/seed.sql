-- Airlines
INSERT INTO Airlines (lineID, name) VALUES
  ('AA', 'American Airlines'),
  ('UA', 'United Airlines'),
  ('DL', 'Delta Air Lines'),
  ('B6', 'JetBlue Airways'),
  ('LH', 'Lufthansa'),
  ('BA', 'British Airways');

-- Airports
INSERT INTO Airports (portID, name, city, country) VALUES
  ('EWR', 'Newark Liberty International',   'Newark',         'United States'),
  ('JFK', 'John F. Kennedy International',  'New York',       'United States'),
  ('LAX', 'Los Angeles International',      'Los Angeles',    'United States'),
  ('ORD', 'O''Hare International',          'Chicago',        'United States'),
  ('SFO', 'San Francisco International',    'San Francisco',  'United States'),
  ('ATL', 'Hartsfield-Jackson International','Atlanta',       'United States'),
  ('LHR', 'Heathrow',                       'London',         'United Kingdom'),
  ('CDG', 'Charles de Gaulle',              'Paris',          'France');

-- Aircrafts
INSERT INTO Aircrafts (craftID, lineID, portID, capacity, model) VALUES
  (1, 'AA', 'JFK', 189, 'Boeing 737-800'),
  (2, 'AA', 'LAX', 189, 'Boeing 737-800'),
  (3, 'UA', 'EWR', 312, 'Boeing 777-200'),
  (4, 'UA', 'SFO', 178, 'Boeing 737 MAX 9'),
  (5, 'DL', 'ATL', 240, 'Boeing 767-400'),
  (6, 'B6', 'JFK', 162, 'Airbus A320'),
  (7, 'LH', 'LHR', 364, 'Boeing 747-8'),
  (8, 'BA', 'LHR', 299, 'Boeing 777-300ER');

-- Which airlines operate at which airports
INSERT INTO AirlineOperatesIn (lineID, portID) VALUES
  ('AA','JFK'),('AA','LAX'),('AA','ORD'),('AA','LHR'),
  ('UA','EWR'),('UA','SFO'),('UA','ORD'),('UA','LAX'),
  ('DL','ATL'),('DL','JFK'),('DL','LAX'),
  ('B6','JFK'),('B6','EWR'),('B6','SFO'),
  ('LH','LHR'),('LH','CDG'),('LH','JFK'),
  ('BA','LHR'),('BA','JFK'),('BA','CDG');

-- Flights
-- daysRunning is a 7-char bitmask, Sun-Sat. '1111111' = daily, '0111110' = Mon-Fri.
INSERT INTO Flights
  (flightNumber, lineID, departure_portID, destination_portID,
   departureTime, arrivalTime, flightType, daysRunning, seatsTaken, craftID) VALUES
  ('101','AA','JFK','LAX','08:00:00','11:30:00','domestic',     '0111110',120,1),
  ('202','AA','LAX','JFK','14:00:00','22:15:00','domestic',     '0111110', 95,2),
  ('501','UA','EWR','SFO','07:00:00','10:45:00','domestic',     '1111111',200,3),
  ('502','UA','SFO','EWR','12:00:00','20:30:00','domestic',     '1111111',180,4),
  ('720','DL','ATL','JFK','10:00:00','12:30:00','domestic',     '0111110',150,5),
  ('410','B6','JFK','SFO','11:00:00','14:30:00','domestic',     '0111110',100,6),
  ('100','AA','JFK','LHR','19:00:00','07:00:00','international','1111111',170,1),
  ('50' ,'LH','LHR','JFK','10:00:00','13:00:00','international','0111110',300,7),
  ('51' ,'LH','JFK','LHR','18:30:00','06:30:00','international','0111110',280,7),
  ('75' ,'BA','LHR','JFK','09:00:00','12:00:00','international','1111111',200,8);

-- Customers (passwords are plain text for class project, real apps would hash these)
INSERT INTO Customers (customerID, firstName, lastName, username, password, email, phoneNumber) VALUES
  (1, 'Alice',  'Johnson',  'alicej',   'pass123', 'alice@email.com', '201-555-0101'),
  (2, 'Bob',    'Smith',    'bobsmith', 'pass123', 'bob@email.com',   '212-555-0202'),
  (3, 'Carol',  'Williams', 'carolw',   'pass123', 'carol@email.com', '310-555-0303'),
  (4, 'David',  'Brown',    'davidb',   'pass123', 'david@email.com', '312-555-0404');

-- Employees (one admin, two reps)
INSERT INTO Employees (employeeID, firstName, lastName, username, password, role) VALUES
  (1, 'Sarah', 'Adams', 'admin', 'admin123', 'admin'),
  (2, 'Mike',  'Reyes', 'mike',  'rep123',   'rep'),
  (3, 'Lisa',  'Park',  'lisa',  'rep123',   'rep');

-- Tickets
INSERT INTO Tickets (ticketNumber, customerID, purchaseTime, bookingFee, fareCost, tripType, status) VALUES
  (1, 1, '2026-04-01 10:30:00', 25.00, 350.00, 'one-way',    'active'),
  (2, 2, '2026-04-02 14:00:00', 25.00, 680.00, 'round-trip', 'active'),
  (3, 3, '2026-04-03 09:00:00', 25.00, 220.00, 'one-way',    'active'),
  (4, 4, '2026-04-05 11:00:00', 35.00, 890.00, 'one-way',    'active');

-- Flight legs per ticket
INSERT INTO FlightTickets (ticketNumber, flightNumber, lineID, legOrder, departureDate, seatNumber, ticketClass, mealOrder) VALUES
  (1, '101','AA',1,'2026-05-15','14A','economy', 'vegetarian'),
  (2, '101','AA',1,'2026-05-20','7B', 'business',NULL),
  (2, '202','AA',2,'2026-05-27','7B', 'business','kosher'),
  (3, '410','B6',1,'2026-05-18','22C','economy', NULL),
  (4, '100','AA',1,'2026-06-01','3A', 'first',   'halal');

-- Waiting list — customers waiting on a popular full flight
INSERT INTO WaitingList (customerID, flightNumber, lineID, requestTime) VALUES
  (4, '101','AA','2026-04-10 09:00:00'),
  (3, '101','AA','2026-04-10 14:30:00')