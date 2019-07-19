INSERT INTO users(
    name,
    email,
    password
)VALUES(
    'MaroKanatani',
    'maro_kanatani@example.com',
    '$2a$10$jNq1qGjHOk4dkcv3oKkXpu36rtIpL717iF6kdyMNoARb3c6WiVSpS'
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