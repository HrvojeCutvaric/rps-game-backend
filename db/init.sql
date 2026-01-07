BEGIN;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'game_state') THEN
CREATE TYPE game_state AS ENUM (
            'WAITING_FOR_PLAYERS',
            'IN_PROGRESS',
            'FINISHED'
        );
END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'move_type') THEN
CREATE TYPE move_type AS ENUM (
    'ROCK',
    'PAPER',
    'SCISSORS'
);
END IF;
END$$;

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS games (
    id SERIAL PRIMARY KEY,
    state game_state NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS game_players (
    user_id INT REFERENCES users(id),
    game_id INT REFERENCES games(id),
    score INT DEFAULT 0,
    PRIMARY KEY (user_id, game_id)
);

CREATE TABLE IF NOT EXISTS rounds (
    id SERIAL PRIMARY KEY,
    game_id INT NOT NULL REFERENCES games(id),
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS moves (
    user_id INT REFERENCES users(id),
    round_id INT REFERENCES rounds(id),
    choice move_type NOT NULL,
    PRIMARY KEY (user_id, round_id)
);

COMMIT;
