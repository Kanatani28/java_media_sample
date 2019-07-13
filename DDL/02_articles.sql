DROP TABLE IF EXISTS articles;

CREATE TABLE articles(
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL, 
    body TEXT NOT NULL,
    author_id INT NOT NULL, 
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME,
    deleted_at DATETIME
);