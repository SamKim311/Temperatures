CREATE DATABASE IF NOT EXISTS temperatures; -- Just relying on default character set and collation
USE temperatures;

CREATE USER 'temperature-test'@'%' IDENTIFIED BY 'test123';
GRANT ALL PRIVILEGES ON temperatures.* TO 'temperature-test'@'%';

CREATE TABLE temperature (
    id INT PRIMARY KEY AUTO_INCREMENT,
    temperature DECIMAL(5,1) NOT NULL,

    create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);