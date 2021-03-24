DROP DATABASE IF EXISTS Lab8;

CREATE DATABASE Lab8;

/* */
USE Lab8;

CREATE TABLE Grades
(
    ID        INT PRIMARY KEY AUTO_INCREMENT,
    SID       INT         NOT NULL,
    ClassName VARCHAR(50) NOT NULL,
    Grade     VARCHAR(2)  NOT NULL
);

/* */
USE Lab8;

INSERT INTO Grades (SID, ClassName, Grade)
VALUES (111, 'ART123', 'B+'),
       (113, 'REL100', 'B+'),
       (113, 'ECO966', 'A-'),
       (103, 'CSCI101', 'A'),
       (111, 'BUS456', 'A'),
       (111, 'BUS456', 'B');

/* */
USE Lab8;

CREATE TABLE StudentInfo
(
    SID  INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(50) NOT NULL
);

/* */
INSERT INTO lab8.studentinfo (SID, Name)
VALUES (111, 'Swapnil Patel'),
       (113, 'Andrew Garcia'),
       (103, 'Carol Liang');

/* */
ALTER TABLE lab8.grades
    ADD FOREIGN KEY (SID)
        REFERENCES studentinfo (SID);

/* */
SELECT *
FROM lab8.grades;

/* */
SELECT ClassName
FROM lab8.grades;

/* */
SELECT Name
FROM lab8.StudentInfo
ORDER BY lab8.StudentInfo.Name;

/* */
SELECT s.Name,
       g.ClassName,
       g.grade
FROM lab8.StudentInfo s,
     lab8.grades g
WHERE s.SID = g.SID
ORDER BY s.Name,
         g.ClassName;

/* */
SELECT g.ClassName,
       g.grade
FROM lab8.StudentInfo s,
     lab8.grades g
WHERE s.SID = g.SID
  AND s.Name = 'Andrew Garcia'
ORDER BY g.ClassName;