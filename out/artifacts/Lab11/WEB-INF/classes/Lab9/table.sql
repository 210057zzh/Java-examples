DROP DATABASE IF EXISTS Lab9;

CREATE DATABASE Lab9;

/* */
USE Lab9;

CREATE TABLE Grades
(
    ID        INT PRIMARY KEY AUTO_INCREMENT,
    SID       INT         NOT NULL,
    ClassName VARCHAR(50) NOT NULL,
    Grade     VARCHAR(2)  NOT NULL
);

/* */
USE Lab9;

INSERT INTO Grades (SID, ClassName, Grade)
VALUES (111, 'ART123', 'F'),
       (111, 'BUS456', 'A-'),
       (113, 'REL100', 'D-'),
       (113, 'ECO966', 'A-'),
       (113, 'BUS456', 'B+'),
       (112, 'BUS456', 'A'),
       (112, 'ECO966', 'B+');

/* */
USE Lab9;

CREATE TABLE StudentInfo
(
    SID  INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(50) NOT NULL
);

/* */
INSERT INTO lab9.studentinfo (SID, Name)
VALUES (111, 'Jack Xu'),
       (112, 'Daniel Mizrahi'),
       (113, 'Emily Jin');

/* */
ALTER TABLE lab9.grades
    ADD FOREIGN KEY (SID)
        REFERENCES lab9.studentinfo (SID);

select *
from Lab9.grades;

select *
from Lab9.studentinfo

select Lab9.grades.ClassName,
       count(Lab9.grades.ClassName)
           as 'Number of Students'
from lab9.grades
GROUP BY ClassName
order by `Number of Students`

