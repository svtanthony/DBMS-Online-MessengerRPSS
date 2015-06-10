DROP TABLE WORK_EXPR;
DROP TABLE EDUCATIONAL_DETAILS;
DROP TABLE MESSAGE;
DROP TABLE CONNECTION;
DROP TABLE USR;


CREATE TABLE USR(
	userId varchar(25) UNIQUE NOT NULL, 
	password varchar(15) NOT NULL,
	email text NOT NULL,
	name char(50),
	dateOfBirth date,
	Primary Key(userId)
);

CREATE TABLE WORK_EXPR(
	userId char(25) NOT NULL, 
	company char(50) NOT NULL, 
	role char(50) NOT NULL,
	location char(50),
	startDate date,
	endDate date,
	PRIMARY KEY(userId,company,role,startDate),
	FOREIGN KEY (userId) REFERENCES USR(userId)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE EDUCATIONAL_DETAILS(
	userId char(25) NOT NULL, 
	institutionName char(50) NOT NULL, 
	major char(50) NOT NULL,
	degree char(50) NOT NULL,
	startdate date,
	enddate date,
	PRIMARY KEY(userId,major,degree),
	FOREIGN KEY (userId) REFERENCES USR(userId)
		ON DELETE CASCADE
                ON UPDATE CASCADE
);

CREATE TABLE MESSAGE(
	msgId serial UNIQUE NOT NULL, 
	senderId char(25) NOT NULL,
	receiverId char(25) NOT NULL,
	contents char(500) NOT NULL,
	sendTime timestamp,
	deleteStatus integer,
	status char(30) NOT NULL,
	PRIMARY KEY(msgId),
	FOREIGN KEY (senderId) REFERENCES USR(userId)
		ON DELETE NO ACTION
		ON UPDATE CASCADE,
	FOREIGN KEY (receiverId) REFERENCES USR(userId)
                ON DELETE NO ACTION
                ON UPDATE CASCADE
);

CREATE TABLE CONNECTION(
	userId char(25) NOT NULL, 
	connectionId char(25) NOT NULL, 
	status char(30) NOT NULL,
	PRIMARY KEY(userId,connectionId),
	FOREIGN KEY (userId) REFERENCES USR(userId)
                ON DELETE CASCADE
                ON UPDATE CASCADE
);
