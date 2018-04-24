DROP DATABASE IF EXISTS EtaCalculatorTest;
CREATE DATABASE EtaCalculatorTest;
USE EtaCalculatorTest;

CREATE TABLE user_info(
username VARCHAR(50) PRIMARY KEY,
login BOOLEAN DEFAULT 1,
notification BOOLEAN DEFAULT 1);

CREATE TABLE current_project(
customer VARCHAR(15) NOT NULL DEFAULT '' PRIMARY KEY,
jira VARCHAR(15) DEFAULT '',
dc VARCHAR(15) DEFAULT '',
data_size int DEFAULT 0,
import_engr VARCHAR(15) DEFAULT '',
tem VARCHAR(15) DEFAULT '',
notes VARCHAR(256) DEFAULT '',
appliance_count int DEFAULT 0,
created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
is_completed BOOLEAN DEFAULT 0);

CREATE TABLE upcoming_sow(
customer_name VARCHAR(15) NOT NULL DEFAULT '' PRIMARY KEY,
sow_created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
estimated_size int DEFAULT 0,
jira VARCHAR(15) DEFAULT '',
dc VARCHAR(15) DEFAULT '',
tem VARCHAR(15) DEFAULT '',
notes VARCHAR(256) DEFAULT '',
expected_start_month DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
expected_end_month DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
apps_needed int DEFAULT 0);

CREATE TABLE appliance_assignment(
appliance VARCHAR(50) NOT NULL PRIMARY KEY,
current VARCHAR(15) DEFAULT '',
previous VARCHAR(15) DEFAULT '');

CREATE TABLE upcoming_project
(
customer VARCHAR(256) NOT NULL PRIMARY KEY,
size VARCHAR(50) DEFAULT NULL,
expected VARCHAR(50) DEFAULT NULL,
reliability VARCHAR(50) DEFAULT NULL,
jira VARCHAR(50) DEFAULT NULL,
dc VARCHAR(50) DEFAULT NULL,
tem VARCHAR(50) DEFAULT NULL,
notes VARCHAR(256) DEFAULT NULL
);


INSERT INTO user_info values(
'mihuang', true, false);

INSERT INTO upcoming_sow values(
'Tesla',
NOW(),
2048,
'sample jira',
'sample dc',
'Aimee',
'Database init dummy data',
'2018-04-30',
'2018-05-30',
NOW(),
0);
