DROP DATABASE IF EXISTS media_sample;
CREATE DATABASE media_sample;
USE media_sample;

DROP USER IF EXISTS 'media-user'@'localhost';
CREATE USER IF NOT EXISTS 'media-user'@'localhost' IDENTIFIED BY 'p@ssw0rD';
ALTER USER 'media-user'@'localhost'  IDENTIFIED WITH MYSQL_NATIVE_PASSWORD BY 'p@ssw0rD';
GRANT SELECT, INSERT, UPDATE, DELETE ON media_sample.* TO 'media-user'@'localhost';
