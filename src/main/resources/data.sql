MERGE INTO friendship_status (friendship_status_id, friendship_status_name) VALUES (1, 'true');
MERGE INTO friendship_status (friendship_status_id, friendship_status_name) VALUES (2, 'false');

MERGE INTO genre (genre_id, genre_name) VALUES (1, 'comedy');
MERGE INTO genre (genre_id, genre_name) VALUES (2, 'drama');
MERGE INTO genre (genre_id, genre_name) VALUES (3, 'thriller');
MERGE INTO genre (genre_id, genre_name) VALUES (4, 'documentary');
MERGE INTO genre (genre_id, genre_name) VALUES (5, 'action');
MERGE INTO genre (genre_id, genre_name) VALUES (6, 'cartoon');

MERGE INTO adult_rate (adult_rate_id, adult_rate_name) VALUES (1, 'G');
MERGE INTO adult_rate (adult_rate_id, adult_rate_name) VALUES (2, 'PG');
MERGE INTO adult_rate (adult_rate_id, adult_rate_name) VALUES (3, 'PG-13');
MERGE INTO adult_rate (adult_rate_id, adult_rate_name) VALUES (4, 'R');
MERGE INTO adult_rate (adult_rate_id, adult_rate_name) VALUES (5, 'NC-17');