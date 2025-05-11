CREATE DATABASE test_java;
CREATE SCHEMA IF NOT EXISTS test;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA test;

SET search_path TO test;

CREATE TABLE files (
    id UUID NOT NULL DEFAULT test.uuid_generate_v4() PRIMARY KEY,
    path TEXT NOT NULL,
    filename TEXT UNIQUE NOT NULL
);

CREATE TABLE word_frequency (
    id UUID NOT NULL DEFAULT test.uuid_generate_v4() PRIMARY KEY,
    file_id UUID NOT NULL,
    word VARCHAR(255) NOT NULL,
    count INT NOT NULL,
    percentage FLOAT NOT NULL,
    FOREIGN KEY (file_id) REFERENCES files(id)
);

CREATE INDEX idx_word ON word_frequency(word);