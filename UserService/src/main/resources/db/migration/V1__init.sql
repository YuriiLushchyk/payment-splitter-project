CREATE TABLE user(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  email VARCHAR(50) NOT NULL UNIQUE,
  date_of_birth DATE NOT NULL,
  receive_notifications BOOLEAN NOT NULL
);

INSERT INTO user(id, username, first_name, last_name, email, date_of_birth, receive_notifications)
VALUES (1, 'testUser', 'Test', 'User', 'test.user@eleks.com', '2008-7-04', TRUE);