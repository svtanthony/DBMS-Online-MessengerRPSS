-----------------------------------------------
--	Declare language
DROP LANGUAGE plpgsql CASCADE;
CREATE language plpgsql;
-- ********************************************

-----------------------------------------------
--
--	Proc/Function to add a new user
--
CREATE OR REPLACE function newAccount(login char(25), passwd char(15), newEmail text) returns text as $$
declare
	retVal text := '';
	num_rows integer := 0;
begin

	-- save number of records that match user's login into a temp variable
	select into num_rows count(*) from usr where lower(userid) = lower(login) or lower(email) = lower(newEmail);

	-- some conditionals to check validity of credentials
	if num_rows = 0 then
        -- Accout and Email have not been used previously.
		insert into usr (userid,password,email) values (login,passwd,newEmail);
	else
		-- Login or email already exists.
		retVal := 'Error: Login/Email is already in use.';
end if;

return retVal;
end;
$$ language plpgsql volatile;

--********************************************

----------------------------------------------
--
--	Proc/Function to Login
--
CREATE OR REPLACE function login(acct char(25), passwd char(15)) returns text as $$
declare
	retVal text := '';
	num_rows integer := 0;
	num_login integer := 0;
begin
	-- save # if records that match login
	select into num_rows count(*) from usr where userid = acct;
	
	-- check return
	if num_rows = 0 then
		retVal := 'Error: Invalid Username.';
	elsif num_rows > 1 then
		retVal := 'Error: Username entered is not unique';
	else
		SELECT into num_login count(*) FROM usr WHERE userid = acct and password = passwd;
		if num_login = 1 then
			retVal :='';
		else
			retVal := 'Error: Incorrect Password';
		end if;
	end if;
return retVal;
end;
$$ language plpgsql volatile;
--********************************************

----------------------------------------------
--
--	Proc/Function to add to send a connection request.
--
CREATE OR REPLACE FUNCTION newConnection(login char(25), victim char(25)) returns text as $$
declare
	retVal text := '';
	num_rows integer := 0;
	num_connect integer := 0;
	separation integer :=0;
begin
	
	-- See if already an attempted connection
	select into num_rows count(*) from connection where (lower(userid) = lower(login) and lower(connectionid) = lower(victim)) or (lower(userid) = lower(victim) and lower(connectionid) = lower(login));

	-- Connection hasn't been attempted previously
	if num_rows = 0 then
		-- Check if connections is less than 10
		select into num_connect count(*) from connection where (lower(userid) = lower(login) or lower(connectionid) = lower(login)) and status = 'Accept';
		if num_connect < 10 then
			insert into connection(userid,connectionid,status) values (login,victim,'Request');
		else
			-- check if within 3 degrees of separation
			CREATE TEMP TABLE SEP0 AS select connectionid as users from connection where lower(userid) = lower(login) and status = 'Accept' 
			UNION select userid as users from connection where lower(connectionid) = lower(login) and status = 'Accept';
			
			CREATE TEMP TABLE SEP1 AS select connectionid as users from connection where userid IN (SELECT * from SEP0) and status = 'Accept' 
			UNION select userid as users from connection where connectionid IN (select * from SEP0) and status = 'Accept';

			CREATE TEMP TABLE SEP2 AS select connectionid as users from connection where userid IN (SELECT * from SEP1) and status = 'Accept' 
			UNION select userid as users from connection where connectionid IN (select * from SEP1) and status = 'Accept';

			CREATE TEMP TABLE SEP3 AS select connectionid as users from connection where userid IN (SELECT * from SEP2) and status = 'Accept' 
			UNION select userid as users from connection where connectionid IN (select * from SEP2) and status = 'Accept';

			SELECT into separation count(*) FROM (select * from sep1 union select * from sep2 union select * from sep3) as temp1 where lower(users) = lower(victim);			
			if separation = 0 then
				retVal := 'Error: The connection recipient is outside your connection network.';
			else
				-- 
				insert into connection(userid,connectionid,status) values (login,victim,'Request');
			end if;

		end if;
	else
		-- Connection exists or has already been attempted
		retVal := 'Error: Connection or connection attempt already exists.';
end if;

return retVal;
end;
$$ language plpgsql volatile;
--*********************************************

-----------------------------------------------
--
--	Proc/Function to update password
--
CREATE OR REPLACE function newPassword(login char(25), oldpasswd char(15), newpasswd char(15)) returns text as $$
declare
	retVal text := '';
	num_rows integer := 0;
begin

	-- save number of records that match user's login into a temp variable
	select into num_rows count(*) from usr where userid = login and password = oldpasswd;

	-- some conditionals to check validity of credentials
	if num_rows = 0 then
        -- INCORRECT PASSWORD.
		retVal := 'Password is incorrect';
	elsif num_rows > 1 then
		-- Ambigious result 
		retVal := 'Error: Ambigious result, please try again.';
	else
		-- UPDATE.
		UPDATE usr SET password = newpasswd WHERE userid = login AND password = oldpasswd;	
end if;

return retVal;
end;
$$ language plpgsql volatile;
--*********************************************

-----------------------------------------------
--
--	Proc/Function to Send Message
--	
--	STATUS = {Sent,Failed to Deliver,Read,Delivered,Draft}
CREATE OR REPLACE function sendMessage(login char(25), receiver char(25), msg char(500)) returns text as $$
declare
	retVal text := '';
	num_usr integer := 0;
	num_rec integer := 0;
begin
	-- save number of records that match user's login into a temp variable
	select into num_usr count(*) from usr where userid = login;
	select into num_rec count(*) from usr where userid = receiver;
	--c some conditionals to verify to and from blocks
	if num_usr = 0 or num_rec = 0 then
		retVal := 'Error: Message sender/receiver error';
	elsif num_usr > 1 or num_rec > 1 then
		retVal := 'Error: Ambigous sender/receiver';
	else
		INSERT INTO message (senderid,receiverid,contents,sendtime,deletestatus,status) VALUES (login,receiver,msg,now(),3,'Sent');
	end if;

return retVal;
end;
$$ language plpgsql volatile;
--*********************************************

-----------------------------------------------
--
--	Proc/Function to Accept connection
--	
--	
CREATE OR REPLACE function acceptConnection(login char(25), sender char(25)) returns text as $$
declare
	retVal text := '';
	num_usr integer := 0;
	num_rec integer := 0;
begin
	-- save number of records that match user's login into a temp variable
	select into num_usr count(*) from usr where userid = login;
	select into num_rec count(*) from usr where userid = receiver;
	--c some conditionals to verify to and from blocks
	if num_usr = 0 or num_rec = 0 then
		retVal := 'Error: Verify usernames.';
	elsif num_usr > 1 or num_rec > 1 then
		retVal := 'Errro: Ambigous userids, verify usernames.';
	else
		UPDATE connection SET status = 'Accept' WHERE connectionid = login AND userid = sender AND status = 'Request';	
	end if;

return retVal;
end;
$$ language plpgsql volatile;
--*********************************************

-----------------------------------------------
--
--	Proc/Function to Accept connection
--	
--	
CREATE OR REPLACE function rejectConnection(login char(25), sender char(25)) returns text as $$
declare
	retVal text := '';
	num_usr integer := 0;
	num_rec integer := 0;
begin
	-- save number of records that match user's login into a temp variable
	select into num_usr count(*) from usr where userid = login;
	select into num_rec count(*) from usr where userid = receiver;
	--c some conditionals to verify to and from blocks
	if num_usr = 0 or num_rec = 0 then
		retVal := 'Error: Verify usernames.';
	elsif num_usr > 1 or num_rec > 1 then
		retVal := 'Errro: Ambigous userids, verify usernames.';
	else
		UPDATE connection SET status = 'Reject' WHERE connectionid = login AND userid = sender AND status = 'Request';	
	end if;

return retVal;
end;
$$ language plpgsql volatile;
--*********************************************

----------------------------------------------
--
--	Function to retrieve friends list
--
CREATE or REPLACE FUNCTION getFriends(login char(25))
RETURNS setof char AS $$
	SELECT userid as friends FROM connection WHERE connectionid = $1 AND status = 'Accept'
        UNION SELECT connectionid as friends FROM connection WHERE userid = $1 AND status = 'Accept';
$$ language sql ;
--*********************************************

----------------------------------------------
--
--	Function to retrieve work experience
--
CREATE or REPLACE FUNCTION getWork(login char(25))
RETURNS setof work_expr as $$
	SELECT * FROM work_expr WHERE userid = $1 ORDER  BY startdate DESC;
$$ language sql;
--********************************************

----------------------------------------------
--
--	Function to retrieve educational experience
--
CREATE or REPLACE FUNCTION getEdu(login char(25))
RETURNS setof educational_details as $$
	SELECT * FROM educational_details WHERE userid = $1 ORDER  BY startdate DESC;
$$ language sql;
--********************************************

----------------------------------------------
--
--	Function to search for a user
--
CREATE or REPLACE FUNCTION searchUser(login char(25))
RETURNS SETOF CHAR AS $$
	SELECT userid FROM usr WHERE lower(userid) LIKE lower($1);
$$ language sql;
--********************************************

----------------------------------------------
--
--	Function to get userData
--
Create or REPLACE FUNCTION userData(login char(25))
RETURNS SETOF usr AS $$
	SELECT * FROM usr WHERE userid = $1;
$$ language sql;
--********************************************


