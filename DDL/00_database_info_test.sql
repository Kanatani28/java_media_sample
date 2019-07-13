-- For TEST

DROP DATABASE IF EXISTS media_sample_test;
CREATE DATABASE media_sample_test;
USE media_sample_test;

DROP USER IF EXISTS 'media-user-test'@'localhost';
CREATE USER IF NOT EXISTS 'media-user-test'@'localhost' IDENTIFIED BY 'p@ssw0rD';
ALTER USER 'media-user-test'@'localhost'  IDENTIFIED WITH MYSQL_NATIVE_PASSWORD BY 'p@ssw0rD';
GRANT CREATE, DROP, SELECT, INSERT, UPDATE, DELETE ON media_sample_test.* TO 'media-user-test'@'localhost';