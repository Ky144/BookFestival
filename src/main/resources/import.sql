-- Insert initial data into Author
INSERT INTO Author (firstname, lastname) VALUES ('John', 'Doe');
INSERT INTO Author (firstname, lastname) VALUES ('Jane', 'Smith');
INSERT INTO Author (firstname, lastname) VALUES ('Hanna', 'Meier');
INSERT INTO Author (firstname, lastname) VALUES ('Max', 'Mustermann');
INSERT INTO Author (firstname, lastname) VALUES ('Emma', 'Watson');

-- Insert initial data into BookFair
INSERT INTO BookFair (name, location, date, maxParticipants) VALUES ('Frankfurt Book Fair', 'Frankfurt', '2024-10-10', 100);
INSERT INTO BookFair (name, location, date, maxParticipants) VALUES ('Leipzig Book Fair', 'Leipzig', '2024-03-15', 80);
INSERT INTO BookFair (name, location, date, maxParticipants) VALUES ('Bologna Children', 'Bologna', '2024-04-01', 1);
INSERT INTO BookFair (name, location, date, maxParticipants) VALUES ('BolognaBookFair', 'Bologna', '2025-04-01', 1);

-- Insert relationships between Authors and BookFairs

INSERT INTO bookfair_participants (bookfair_id, author_id) VALUES (1, 2);
INSERT INTO bookfair_participants (bookfair_id, author_id) VALUES (2, 2);
INSERT INTO bookfair_participants (bookfair_id, author_id) VALUES (2, 3);
INSERT INTO bookfair_participants (bookfair_id, author_id) VALUES (3, 1);

-- Insert waitlist entries
INSERT INTO Waitlist (bookfair_id, author_id) VALUES (1, 4);
INSERT INTO Waitlist (bookfair_id, author_id) VALUES (1, 5);

INSERT INTO Waitlist (bookfair_id, author_id) VALUES (3, 2);
INSERT INTO Waitlist (bookfair_id, author_id) VALUES (3, 3);
INSERT INTO Waitlist (bookfair_id, author_id) VALUES (4, 4);
