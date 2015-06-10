DROP INDEX usr_userid; 
DROP INDEX con_user; 
DROP INDEX con_con;
DROP INDEX msg_sndr;
DROP INDEX msg_rcvr; 
DROP INDEX wrk_usr;
DROP INDEX edu_usr; 

CREATE INDEX usr_userid ON usr (userid);
CREATE INDEX con_user ON connection (userid);
CREATE INDEX con_con ON connection (connectionid);
CREATE INDEX msg_sndr ON message (senderid);
CREATE INDEX msg_rcvr ON message (receiverid);
CREATE INDEX wrk_usr ON work_expr (userid);
CREATE INDEX edu_usr ON educational_details (userid);
