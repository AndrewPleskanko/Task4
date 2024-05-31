CREATE TABLE app_user
(
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(50) NOT NULL,
    role_id INT NOT NULL,
    phone VARCHAR(15),
    age INT,
    FOREIGN KEY (role_id) REFERENCES role (id)
);