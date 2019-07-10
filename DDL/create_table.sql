DROP DATABASE IF EXISTS media_sample;
CREATE DATABASE media_sample;
USE media_sample;

DROP USER IF EXISTS 'media-user'@'localhost';
CREATE USER IF NOT EXISTS 'media-user'@'localhost' IDENTIFIED BY 'p@ssw0rD';
ALTER USER 'media-user'@'localhost'  IDENTIFIED WITH MYSQL_NATIVE_PASSWORD BY 'p@ssw0rD';
GRANT SELECT, INSERT, UPDATE, DELETE ON media_sample.* TO 'media-user'@'localhost';

CREATE TABLE articles(
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL, 
    body TEXT NOT NULL,
    author_id INT NOT NULL, 
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME,
    deleted_at DATETIME
);

CREATE TABLE users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, 
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME,
    deleted_at DATETIME
);

INSERT INTO users(
    name,
    email,
    password
)VALUES(
    'MaroKanatani',
    'maro_kanatani@example.com',
    'sample'
);

INSERT INTO articles(
    title, 
    body,
    author_id
)VALUES(
    'Sample Title',
    'Sample Body',
    1
);



-- For Test

DROP DATABASE IF EXISTS media_sample_test;
CREATE DATABASE media_sample_test;
USE media_sample_test;

DROP USER IF EXISTS 'media-user-test'@'localhost';
CREATE USER IF NOT EXISTS 'media-user-test'@'localhost' IDENTIFIED BY 'p@ssw0rD';
ALTER USER 'media-user-test'@'localhost'  IDENTIFIED WITH MYSQL_NATIVE_PASSWORD BY 'p@ssw0rD';
GRANT SELECT, INSERT, UPDATE, DELETE ON media_sample_test.* TO 'media-user-test'@'localhost';

CREATE TABLE articles(
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL, 
    body TEXT NOT NULL,
    author_id INT NOT NULL, 
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME,
    deleted_at DATETIME
);

CREATE TABLE users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL, 
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME,
    deleted_at DATETIME
);