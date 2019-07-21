DROP TABLE IF EXISTS images;

CREATE TABLE images(
    id INT PRIMARY KEY AUTO_INCREMENT,
    image_token VARCHAR(50) UNIQUE NOT NULL, 
    file_name VARCHAR(255) NOT NULL, 
    data MEDIUMBLOB NOT NULL, 
    created_at DATETIME NOT NULL DEFAULT NOW(), 
    updated_at DATETIME, 
    deleted_at DATETIME 
);