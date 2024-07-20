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
--INSERT INTO bookfair_participants (bookfair_id, author_id) VALUES (1, 1);  -- John Doe attends Frankfurt Book Fair
INSERT INTO bookfair_participants (bookfair_id, author_id) VALUES (1, 2);  -- Jane Smith attends Frankfurt Book Fair
INSERT INTO bookfair_participants (bookfair_id, author_id) VALUES (2, 2);  -- Jane Smith attends Leipzig Book Fair
INSERT INTO bookfair_participants (bookfair_id, author_id) VALUES (2, 3);  -- Hanna Meier attends Leipzig Book Fair
INSERT INTO bookfair_participants (bookfair_id, author_id) VALUES (3, 1);  -- John Doe attends Bologna Children

-- Insert waitlist entries
INSERT INTO Waitlist (bookfair_id, author_id) VALUES (1, 4);  -- Max Mustermann is on waitlist for Frankfurt Book Fair
INSERT INTO Waitlist (bookfair_id, author_id) VALUES (1, 5);  -- Emma Watson is on waitlist for Frankfurt Book Fair

INSERT INTO Waitlist (bookfair_id, author_id) VALUES (3, 2);  -- Jane Smith is on waitlist for Bologna Children
INSERT INTO Waitlist (bookfair_id, author_id) VALUES (3, 3);  -- Hanna Meier is on waitlist for Bologna Children
INSERT INTO Waitlist (bookfair_id, author_id) VALUES (4, 4);  -- Max Mustermann is on waitlist for BolognaBookFair
