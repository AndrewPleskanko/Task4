INSERT INTO app_user (username, password, email, role_id, phone, age)
VALUES ('john', '$2a$10$5vvbROzmmXGkfPVaZTyNjODxOEBkobazyMcGWaoSKugSaMLSER0Pq', 'john@example.com',
        (SELECT id FROM role WHERE name = 'ROLE_USER'), '1234567890', 30);

INSERT INTO app_user (username, password, email, role_id, phone, age)
VALUES ('jane', '$2a$10$5vvbROzmmXGkfPVaZTyNjODxOEBkobazyMcGWaoSKugSaMLSER0Pq', 'jane@example.com',
        (SELECT id FROM role WHERE name = 'ROLE_USER'), '0987654321', 25);

INSERT INTO app_user (username, password, email, role_id, phone, age)
VALUES ('admin', '$2a$10$H7Tw60M/fe.vwwBgxCTvYuDrHGOhJ6s.yxArIrjsgfQOwL6lR2RdO', 'admin@example.com',
        (SELECT id FROM role WHERE name = 'ROLE_ADMIN'), '1122334455', 35);

INSERT INTO app_user (username, password, email, role_id, phone, age)
VALUES ('moder', '$2a$10$TWJR3OY0oCMUqxRbnJVw1eREnjYUIyI24IbFgox.Fpf6PD0ZJa2Au', 'moder@example.com',
        (SELECT id FROM role WHERE name = 'ROLE_MODERATOR'), '5566778899', 40);