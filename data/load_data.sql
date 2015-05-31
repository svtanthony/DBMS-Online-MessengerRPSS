COPY USR(
	userId,
	password,
	email,
	name,
	dateOfBirth)
FROM '/home/csmajs/rpasi001/cs166/project/data/USR.csv'
WITH DELIMITER ';';

COPY WORK_EXPR(
	userId,
	company,
	role,	
	location,
	startDate,
	endDate)
FROM '/home/csmajs/rpasi001/cs166/project/data/WORK_EX.csv'
WITH DELIMITER ';';

COPY EDUCATIONAL_DETAILS(
	userId,
	institutionName,
	major,
	degree,
	startDate,
	endDate)
FROM '/home/csmajs/rpasi001/cs166/project/data/EDU_DET.csv'
WITH DELIMITER ';';

COPY MESSAGE(
        msgId,
        senderId,
        receiverId,
        contents, 
        sendTime,
        deleteStatus,
        status)
FROM '/home/csmajs/rpasi001/cs166/project/data/MESSAGE.csv'
WITH DELIMITER ';'
CSV QUOTE '"';

select setval('message_msgid_seq',max(msgid)) from message;

COPY CONNECTION(
        userId,
        connectionId,
        status)
FROM '/home/csmajs/rpasi001/cs166/project/data/CONNECTION.csv'
WITH DELIMITER ';';
