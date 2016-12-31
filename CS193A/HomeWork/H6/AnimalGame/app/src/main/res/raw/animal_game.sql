CREATE TABLE IF NOT EXISTS nodes(nodeID INTEGER PRIMARY KEY, type TEXT, text TEXT, userID INTEGER, time_stamp TIMESTAMP, view_count INTEGER);
INSERT INTO nodes VALUES(1, "question", "Does it have 4 legs?", 0, '2016-12-24 18:00:00', 0);
INSERT INTO nodes VALUES(2, "question", "Is it hairy?", 0, '2016-12-24 18:00:00', 0);
INSERT INTO nodes VALUES(3, "question", "Does it bark?", 0, '2016-12-24 18:00:00', 0);
INSERT INTO nodes VALUES(4, "answer", "dog", 0, '2016-12-24 18:00:00', 0);
INSERT INTO nodes VALUES(5, "answer", "cat", 0, '2016-12-24 18:00:00', 0);
INSERT INTO nodes VALUES(6, "answer", "elephant", 0, '2016-12-24 18:00:00', 0);
INSERT INTO nodes VALUES(7, "question", "Does it swim?", 0, '2016-12-24 18:00:00', 0);
INSERT INTO nodes VALUES(8, "answer", "teacher", 0, '2016-12-24 18:00:00', 0);
INSERT INTO nodes VALUES(9, "answer", "dragon", 0, '2016-12-24 18:00:00', 0);
INSERT INTO nodes VALUES(10, "question", "Does it have fins?", 0, '2016-12-24 18:00:00', 0);
INSERT INTO nodes VALUES(11, "answer", "fish", 0, '2016-12-24" 18:00:00', 0);


CREATE TABLE IF NOT EXISTS edges(graphID INTEGER PRIMARY KEY, parentID INTEGER, childID INTEGER, type TEXT);
INSERT INTO edges VALUES(101, 1, 2, "yes");
INSERT INTO edges VALUES(102, 1, 7, "no");
INSERT INTO edges VALUES(103, 2, 3, "yes");
INSERT INTO edges VALUES(104, 2, 6, "no");
INSERT INTO edges VALUES(105, 3, 4, "yes");
INSERT INTO edges VALUES(106, 3, 5, "no");
INSERT INTO edges VALUES(107, 7, 10, "yes");
INSERT INTO edges VALUES(108, 7, 9, "no");
INSERT INTO edges VALUES(109, 10, 11, "yes");
INSERT INTO edges VALUES(110, 10, 8, "no");
