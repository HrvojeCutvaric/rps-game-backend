BEGIN;

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS games (
    id SERIAL PRIMARY KEY,
    state TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ONSTRAINT games_state_check
    CHECK (state IN ('WAITING_FOR_PLAYERS', 'IN_PROGRESS', 'FINISHED'))
);

CREATE TABLE IF NOT EXISTS game_players (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    game_id INT REFERENCES games(id),
    score INT DEFAULT 0,
    has_created_game BOOLEAN DEFAULT FALSE,
    UNIQUE (user_id, game_id)
);

CREATE TABLE IF NOT EXISTS rounds (
    id SERIAL PRIMARY KEY,
    game_id INT NOT NULL REFERENCES games(id),
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS moves (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    round_id INT REFERENCES rounds(id),
    choice TEXT NOT NULL,
    UNIQUE (user_id, round_id),
    CONSTRAINT moves_choice_check
    CHECK (choice IN ('ROCK', 'PAPER', 'SCISSORS'))
);

COMMIT;
