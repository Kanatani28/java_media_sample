DROP TABLE IF EXISTS article_images;

CREATE TABLE article_images(
    id INT PRIMARY KEY AUTO_INCREMENT, 
    article_id INT NOT NULL,
    image_id INT NOT NULL, 
    created_at DATETIME NOT NULL DEFAULT NOW(), 
    updated_at DATETIME, 
    deleted_at DATETIME 
);