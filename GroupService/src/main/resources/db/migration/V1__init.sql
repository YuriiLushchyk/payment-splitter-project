CREATE TABLE user_group(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  group_name VARCHAR(50) NOT NULL,
  currency VARCHAR(50) NOT NULL,
  members VARCHAR(255)
);

CREATE TABLE payment(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  payment_description VARCHAR(200) NOT NULL,
  price DOUBLE NOT NULL,
  co_payers VARCHAR(255),
  creator_id BIGINT NOT NULL,
  group_id BIGINT NOT NULL,
  timestamp DATE NOT NULL
);