CREATE TABLE sgroup(
g_number BIGINT PRIMARY KEY,
g_faculty  VARCHAR(50)
);
CREATE TABLE student(
s_id BIGINT PRIMARY KEY,
s_name varchar(20),
s_surname varchar(20),
s_middlename varchar(20),
s_birthdate DATE,
s_group BIGINT
);
ALTER TABLE student ADD FOREIGN KEY (s_group) REFERENCES sgroup(g_number) ON DELETE RESTRICT;