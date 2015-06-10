COPY USR(
	userId,
	password,
	email,
	name,
	dateOfBirth)
FROM '/home/postgres/project/USR.csv'
WITH DELIMITER ';';

COPY WORK_EXPR(
	userId,
	company,
	role,	
	location,
	startDate,
	endDate)
FROM '/home/postgres/project/WORK_EX.csv'
WITH DELIMITER ';';

COPY EDUCATIONAL_DETAILS(
	userId,
	institutionName,
	major,
	degree,
	startDate,
	endDate)
FROM '/home/postgres/project/EDU_DET.csv'
WITH DELIMITER ';';

COPY MESSAGE(
        msgId,
        senderId,
        receiverId,
        contents, 
        sendTime,
        deleteStatus,
        status)
FROM '/home/postgres/project/MESSAGE.csv'
WITH DELIMITER ';'
CSV QUOTE '"';

select setval('message_msgid_seq',max(msgid)) from message;

COPY CONNECTION(
        userId,
        connectionId,
        status)
FROM '/home/postgres/project/CONNECTION.csv'
WITH DELIMITER ';';
