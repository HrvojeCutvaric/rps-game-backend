BEGIN;

CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password TEXT               NOT NULL
);

CREATE TABLE games
(
    id             SERIAL PRIMARY KEY,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    first_user_id  INT NOT NULL REFERENCES users (id),
    second_user_id INT REFERENCES users (id)
);

CREATE TABLE rounds
(
    id               SERIAL PRIMARY KEY,
    game_id          INT NOT NULL REFERENCES games (id),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    first_user_move  VARCHAR(50),
    second_user_move VARCHAR(50),
    CONSTRAINT rounds_first_user_move_check
        CHECK (first_user_move IN ('ROCK', 'PAPER', 'SCISSORS') OR first_user_move IS NULL),
    CONSTRAINT rounds_second_user_move_check
        CHECK (second_user_move IN ('ROCK', 'PAPER', 'SCISSORS') OR second_user_move IS NULL)
);

COMMIT;