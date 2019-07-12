
DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, description, datetime, calories) VALUES
    (100000, 'завтрак1', '2015-05-30 10:00:00', 500),
    (100000, 'обед1', '2015-05-30 13:00:00', 1000),
    (100000, 'ужин1', '2015-05-30 17:00:00', 800),
    (100000, 'завтрак1', '2015-05-31 11:00:00', 400),
    (100000, 'обед1', '2015-05-31 14:00:00', 1500),
    (100000, 'ужин1', '2015-05-30 18:00:00', 900),
    (100000, 'полдник1', '2015-05-30 16:00:00', 500),
    (100000, 'обед1', '2015-05-29 10:00:00', 500),
    (100001, 'завтрак', '2015-05-30 10:00:00', 500),
    (100001, 'обед', '2015-05-30 13:00:00', 1000);