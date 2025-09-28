CREATE DATABASE IF NOT EXISTS tasktrecker_db
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE tasktrecker_db;

CREATE TABLE IF NOT EXISTS tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    dueDate DATE,
    priority INT,
    completed BOOLEAN
);