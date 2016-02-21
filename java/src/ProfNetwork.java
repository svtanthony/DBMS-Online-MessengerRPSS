/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */
//**********************************************************************************************************************************
/*   SELIK :ALL OTHER FUNCTIONALITY HAS BEEN IMPLEMENTED AS SQL FUNCTIONS WITH ALL THE CHEKING DONE THERE TOO
			FOR OUR CURRENT TO DO LIST, WE NEED TO IMPLEMENT THE FOLLOWING FUNCTIONALITY (part of queries provided)

	1)	USE SQL HELPER FUNCTIONS TO COMPLETE # 2
			searchUser('%user%')
			newconnection(login,victim)
			sendMessage(login, receiver, message)
			acceptConnection(login, sender)
			rejectConnection(login, sender)

	2)	PLAN AND IMPLEMENT
			a)	DELETE ACCOUNT - similar to login = sql does all the work
			b)	MANAGE MESSAGES
				switch with options to view summary, read, send, delete, quit
				QUERY - VIEW COMMUNICATION BETWEEN USER AND SPECIFIC CONTACT
				SELECT senderid, contents, sendtime FROM messages WHERE (senderid = login and receiverid = contact) or (senderid = contact and receiverid = login);
			c)	MANAGE CONNECTIONS
				switch with options to view pending, change existing, make new, accept, decline, quit

	3) Make Pretty

*/
//**********************************************************************************************************************************
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class ProfNetwork {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));
   /**
    * Creates a new instance of ProfNetwork
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public ProfNetwork (String dbname, String dbport, String user, String passwd) throws SQLException {
      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         //String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         String url = "jdbc:postgresql://jpc.mine.nu:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");
         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end ProfNetwork

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = false;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

public int executeAndPrintEduc(String query) throws SQLException {
    // Print for Work Experience
	Statement stmt = this._connection.createStatement ();

	// issues the query instruction
	ResultSet rs = stmt.executeQuery (query);

	/*
	** obtains the metadata object for the returned result set.  The metadata
	** contains row and column info.
	*/
	ResultSetMetaData rsmd = rs.getMetaData ();
	int numCol = rsmd.getColumnCount ();
	int rowCount = 0;

	// iterates through the result set and output them to standard out.
	System.out.println("-----Educational Experience-----\n");
    while (rs.next()){
		for (int i=1; i<=numCol; ++i){
			String total = rs.getString(i);
			total = total.replaceAll("\\s+", " ");
			String[] tokens = total.split("[,]+");
            System.out.println("\033[1;34mInstitution:\033[0m \t" + tokens[1].replaceAll("\"",""));
			System.out.println("\033[1;34mMajor:\033[0m \t\t" + tokens[2].replaceAll("\"",""));
			System.out.println("\033[1;34mDegree:\033[0m \t" + tokens[3].replaceAll("\"",""));
			System.out.println("\033[1;34mStart Date:\033[0m \t" + tokens[4].replaceAll("\"",""));
			System.out.println("\033[1;34mEnd Date:\033[0m \t" + tokens[5].replaceAll("[\")]+",""));
			System.out.println("\n");
			++rowCount;
		}
    }//end while
    stmt.close ();
	return rowCount;
}

public int executeAndPrintWork(String query) throws SQLException {
	// Print for Work Experience
	Statement stmt = this._connection.createStatement ();

	// issues the query instruction
	ResultSet rs = stmt.executeQuery (query);

	/*
	** obtains the metadata object for the returned result set.  The metadata
	** contains row and column info.
	*/
	ResultSetMetaData rsmd = rs.getMetaData ();
	int numCol = rsmd.getColumnCount ();
	int rowCount = 0;

	// iterates through the result set and output them to standard out.
	System.out.println("-----Work Experience-----\n");
    while (rs.next()){
		for (int i=1; i<=numCol; ++i){
			String total = rs.getString(i);

			total = total.replaceAll("\\s+", " ");
			String[] tokens = total.split("[,]+");
            System.out.println("\033[1;35mCompany:\033[0m \t" + tokens[1].replaceAll("\"",""));
			System.out.println("\033[1;35mRole: \033[0m\t\t" + tokens[2].replaceAll("\"",""));
			System.out.println("\033[1;35mLocation:\033[0m \t" + tokens[3].replaceAll("\"",""));
			System.out.println("\033[1;35mStart Date:\033[0m \t" + tokens[4].replaceAll("\"",""));
			System.out.println("\033[1;35mEnd Date:\033[0m \t" + tokens[5].replaceAll("[\")]+",""));
			System.out.println();
			++rowCount;
		}
	}//end while
	stmt.close ();
	return rowCount;
}

public int executeAndPrintHeader(String query) throws SQLException{
	Statement stmt = this._connection.createStatement();
	ResultSet rs = stmt.executeQuery(query);
	ResultSetMetaData rsmd = rs.getMetaData();
	int numCol = rsmd.getColumnCount();
	int numRow = 0;
	if(rs.next())
	{
		String total = rs.getString(1);
		total = total.replaceAll("\\s+", " ");
		String[] tokens = total.split("[,]+");
		System.out.println("\033[1;32mName:\033[0m \t" + tokens[3].replaceAll("[\")]+",""));
		System.out.println("\033[1;32mEmail: \033[4;34m\t" + tokens[2].replaceAll("[\")]+","") + "\033[0m");
		numRow++;
	}
	System.out.println();
	return numRow;
}
	public String executeQueryStr (String query) throws SQLException {
		// creates a statement object
		Statement stmt = this._connection.createStatement();

		// issues the query instruction
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		String retVal = rs.getString("retVal");
		stmt.close();
		return retVal;
	}

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
          List<String> record = new ArrayList<String>();
         for (int i=1; i<=numCol; ++i)
            record.add(rs.getString (i));
         result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       if(rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup
   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 4) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            ProfNetwork.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      ProfNetwork esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the ProfNetwork object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
	String passwd = args[3];
         esql = new ProfNetwork (dbname, dbport, user, passwd);

         boolean keepon = true;
         while(keepon) {
			System.out.println("\u001b[2J");
			System.out.println(
			"\n\n*******************************************************\n" +
			"   	  	  Welcome - Log In/Create Account		  \n" +
			"*******************************************************\n");
            System.out.println("\033[1;32mMAIN MENU\033[0m");
            System.out.println("---------");
            System.out.println("1. Create user \t\t 3. Change Password");
            System.out.println("2. Log in \t\t 4. Delete Account");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
				case 1: CreateUser(esql); break;
				case 2: authorisedUser = LogIn(esql); break;
				case 3: ChangePassword(esql); break;
				case 4: deleteAccount(esql);break;
				case 9: keepon = false; break;
				default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
				System.out.println(
				"\n\n*******************************************************\n" +
				"              Welcome - User Menu                    \n" +
				"*******************************************************\n");
                //cout <<"\033[1;32m" << signature <<"\033[3;31m" <<  workingDir << " $ " << "\033[0m";
		        System.out.println("\033[1;32mMAIN MENU\033[0m");
                System.out.println("---------");
                System.out.println("1. Go to Friend List \t\t 6. View Profile");
                System.out.println("2. View other Profile");
                System.out.println("3. Go to messages menu");
                System.out.println("4. Send Friend Request");
                System.out.println("5. View Others Friends List");
                System.out.println(".........................");
                System.out.println("9. Log out");
                switch (readChoice()){
                  case 1: FriendList(esql,authorisedUser); break;
                  case 2: otherProfile(esql); break;
                  case 3: msgMenu(esql, authorisedUser); break;
                  case 4: SendRequest(esql, authorisedUser); break;
                  case 5: OtherFriendList(esql); break;
			      case 6: Profile(esql,authorisedUser); break;
				  case 7: otherProfile(esql); break;
                  case 9: usermenu = false; break;
                  default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              Online Messenger      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user with privided login, passowrd and phoneNum
    * An empty block and contact list would be generated and associated with a user
    **/
public static void CreateUser(ProfNetwork esql){
      try{
		System.out.print("\t\033[1;32mEnter user login: ");
		String login = in.readLine();
		System.out.print("\tEnter user password: ");
		String password = in.readLine();
		System.out.print("\tEnter user email: \033[0m");
		String email = in.readLine();

		//Creating empty contact\block lists for a user
		String query = String.format("SELECT newAccount('%s','%s','%s') as retVal",login,password,email);

		String rVal = esql.executeQueryStr(query);

		if(rVal.isEmpty())
			System.out.println ("User successfully created!");
		else
			System.out.println(rVal);

	}catch(Exception e){
		System.err.println (e.getMessage ());
	}
}//end

   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
public static String LogIn(ProfNetwork esql){
	try{
		System.out.print("\tEnter user login: ");
		String login = in.readLine();
		System.out.print("\tEnter user password: ");
		String password = in.readLine();

		String query =  String.format("SELECT login('%s','%s') as retVal",login,password);

		String rVal = esql.executeQueryStr(query);

		if(rVal.isEmpty()){
			System.out.println("Log In Successful!");
			return login;
		}

	System.out.println(rVal);
	return null;

	}catch(Exception e){
		System.err.println (e.getMessage ());
		return null;
	}
}//end
public static void ChangePassword(ProfNetwork esql){
	try{
		System.out.println("\n\tEnter user Login");
		String login = in.readLine();
		System.out.println("\tEnter user old Password");
		String oldP = in.readLine();
		System.out.println("\tEnter user new Password");
		String newP = in.readLine();

		String query = String.format("SELECT newpassword('%s','%s','%s') as retVal",login,oldP,newP);

		String rVal = esql.executeQueryStr(query);

		if(rVal.isEmpty()){
            System.out.print("\033[1;32m");
			System.out.println("Password Successfully Changed!");
		    System.out.print("\033[0m");
        }
        else
			System.out.println(rVal);

	}catch(Exception e){
		System.out.println(e.getMessage());
        System.out.print("");
	}
}
public static void deleteAccount(ProfNetwork esql){

    try{

		System.out.println("THIS IS WHERE WE DELETE ACCOUNT");
        System.out.print("\tEnter user login: ");
		String login = in.readLine();
		System.out.print("\tEnter user password: ");
		String password = in.readLine();
		String query =  String.format("SELECT login('%s','%s') as retVal",login,password);
		String rVal = esql.executeQueryStr(query);
		if(rVal.isEmpty()){
			System.out.println("Log In Successful!");
		    System.out.println("ARE YOU SURE YOU WANT TO DELETE YOUR ACCOUNT (yes or no)?");
            String toDelete = in.readLine();
            String yesDelete = "yes";
            if(toDelete == yesDelete){
                System.out.println("Now deleteing the account");
                String deleteQuery = String.format("DELETE FROM usr WHERE userid = '%s'", login);
            }
            else {
                System.out.println("Did not successfully delete and returning to menu.");
            }
            //return login;
		}
		System.out.println(rVal);
	}catch(Exception e){
		System.err.println (e.getMessage ());
	}
        //******************************************************************************************************************************************************
}
/*
public static void sendMsg(ProfNetwork esql) throws IOException {
    try{

        //need to break out of loop here
        System.out.println("Message: ");
        String line = "";
        String paragraph = "";
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(isr);
            do{
                line = bufferedReader.readLine();
                paragraph = paragraph + line + " ";
            }while (!line.equals("exit"));
        isr.close();
        bufferedReader.close();
        System.out.print("\tEnter user login: ");
	    String login = in.readLine();
	    System.out.print("\tEnter user password: ");
	    String password = in.readLine();
	    String query =  String.format("SELECT sendMessage('%s','%s', '%s') as retVal",login,password, paragraph);
    }catch (Exception e){
        System.err.println(e.getMessage ());
    }
}
*/
public static void viewMsg(ProfNetwork esql, String user){
    //this should return the last five messages
    try{
        String thisUsr = user;

        String query = String.format("SELECT contents FROM message WHERE receiverid = '%s' OR senderid = '%s' ORDER BY sendtime desc limit 5;",thisUsr, thisUsr);
        int fewMsg = esql.executeQueryAndPrintResult(query);

        if(fewMsg == 0){
            System.out.println("-----NONE----\n");
            System.out.println("\n");
        }

    }catch (Exception e){
        System.err.println(e.getMessage());
    }
}

public static void sentMsg(ProfNetwork esql, String user){
    //view sent messages
    try{
        String sender = user;
        //System.out.println("View messages sent to: ");
        //String receiver = in.readLine();
        //sendMessage(login, receiver, message)

        String squery = String.format("SELECT contents FROM message WHERE senderid = '%s' ORDER BY sendtime DESC;", sender);

        int numMsg = esql.executeQueryAndPrintResult(squery);
        if(numMsg == 0){
            System.out.println("-----NONE----\n");
            System.out.println("\n");
        }
    }catch (Exception e){
        System.err.println(e.getMessage());
    }
}

public static void recdMsg(ProfNetwork esql, String user){
    try{
        String receiver = user;
        String rQuery = String.format("SELECT contents FROM message WHERE receiverid = '%s' ORDER BY sendtime DESC;", receiver);

        int recdquery = esql.executeQueryAndPrintResult(rQuery);

    } catch (Exception e){
        System.err.println(e.getMessage());
    }
}

public static void SendRequest(ProfNetwork esql, String User){
    try{
        //newconnection(login,victim)
        //acceptConnection(login, sender)
		//rejectConnection(login, sender)
        String author = User;
        System.out.println("Request friendship with(userid) : ");
        String whom = in.readLine();

        //System.out.println("Sending request...");

        String connectQuery = String.format("SELECT newconnection('%s','%s') as retVal;", author, whom);
        String rVal = esql.executeQueryStr(connectQuery);

        if(rVal.isEmpty()){
            System.out.println("Sent request");
        }

        else
            System.out.println(rVal);
    }catch (Exception e){
        System.err.println(e.getMessage());
    }

}

public static void rejectRequest(ProfNetwork esql, String User){
    try{

        String me = User;
        System.out.print("Reject request from:  ");

        String from = in.readLine();

        //String sender = String.format("SELECT contents FROM message WHERE receiverid = '%s' AND senderid = '%s'", User);

        System.out.println("Rejecting Request... ");

        //String whom = in.readLine();

        //String makeQuery = String.format("SELECT");

        String rejectQuery =  String.format("SELECT rejectconnection('%s','%s') as retVal", User,from);

        //String rVal = esql.executeQueryStr(query);

        String Query = esql.executeQueryStr(rejectQuery);
        System.out.println("Rejected request");

    }catch (Exception e){
        System.err.println(e.getMessage());
    }

}

public static void acceptRequest(ProfNetwork esql, String User){
    try{

        String me = User;
        System.out.print("accept request from:  ");

        String from = in.readLine();

        //String sender = String.format("SELECT contents FROM message WHERE receiverid = '%s' AND senderid = '%s'", User);

        System.out.println("Accepting Request... ");

        //String whom = in.readLine();

        //String makeQuery = String.format("SELECT");

        String rejectQuery =  String.format("SELECT acceptconnection('%s','%s') as retVal", User,from);

        //String rVal = esql.executeQueryStr(query);

        String Query = esql.executeQueryStr(rejectQuery);
        System.out.println("accepted request");

    }catch (Exception e){
        System.err.println(e.getMessage());
    }

}

public static void makeMsg(ProfNetwork esql, String user){
    try{
        String author = user;
        System.out.print("To whom:  ");
        //enter the userid
        String whom = in.readLine();
        System.out.println("Message: ");
        String line = "";
        String paragraph = "";
        //InputStreamReader isr = new InputStreamReader(System.in);
        //BufferedReader bufferedReader = new BufferedReader(isr);

        boolean keepRead = true;
            do{
                line = in.readLine();
                paragraph = paragraph + line + "\n ";
                //line = bufferedReader.readLine();
                //paragraph = paragraph + line + " ";
                if(line.equals("EXIT")){
                    keepRead = false;
                }
            //}while (!line.equals("exit"));
            }while(keepRead);
        //bufferedReader.close();
        //isr.close();
	    //bufferedReader.close();
        //String makeQuery = String.format("INSERT into ");

        //String squery = String.format("SELECT sendMessage('%s', '%s', '%s'", user, whom, paragraph);
        String makeQuery =  String.format("SELECT sendMessage('%s','%s', '%s') as retVal", user, whom, paragraph);

        //String rVal = esql.executeQueryStr(query);

        String insertQuery = esql.executeQueryStr(makeQuery);
        System.out.println("Inserted message, returning");
    } catch (Exception e){
        System.err.println(e.getMessage());
    }
}

public static void deleteMsg(ProfNetwork esql, String viewer){
    try{

        String viewId = viewer;

        System.out.println("Delete last message from: ");
        String from = in.readLine();

        System.out.println("Now deleteing the message");
        String deleteQuery = String.format("DELETE FROM message WHERE receiverid = '%s' AND senderid = '%s' OR receiverid = '%s' AND senderid = '%s';",viewer,from,from,viewer );

        String removeQuery = esql.executeQueryStr(deleteQuery);



    }catch (Exception e){
        System.err.println(e.getMessage());
    }

}

public static void msgMenu(ProfNetwork esql, String user){
    try{
        boolean notComplete = true;
        String currUser = user;
        while(notComplete){
            System.out.println("\033[1;32mMESSAGES MENU\033[0m");
            System.out.println("--------------");
            System.out.println("1. View last few Messages");
            System.out.println("2. Read sent Messages");
            System.out.println("3. Read Revd Messages");
            System.out.println("4. Send a new message");
            System.out.println("5. Delete a message");
            System.out.println("6. Reject a request");
            System.out.println("7. Accept a request");
            System.out.println(".........................");
            System.out.println("9. Go back to Main Menu");

            switch (readChoice()){
    		    case 1: viewMsg(esql, currUser); break;
			    case 2: sentMsg(esql, currUser); break;
			    case 3: recdMsg(esql, currUser); break;
                case 4: makeMsg(esql, currUser); break;
                case 5: deleteMsg(esql, currUser); break;
                case 6: rejectRequest(esql, currUser); break;
                case 7: acceptRequest(esql, currUser); break;
			    case 9: notComplete = false; break;
                default : System.out.println("Unrecognized choice!"); break;
            }//end switch
        }
    }catch (Exception e){
        System.err.println (e.getMessage ());
    }
}
public static void FriendList(ProfNetwork esql, String user){
	try{
		System.out.println("\u001b[2J");
		System.out.flush();
		System.out.println("   Friend(s)  ");
		System.out.println("--------------");
		String query = String.format("SELECT getfriends('%s') as FRIENDLIST;",user);
		int friends = esql.executeQueryAndPrintResult(query);
		if(friends == 0)
			System.out.println("None Yet!\n");
	}catch(Exception e){
       	 System.err.println (e.getMessage ());
	}
}
public static void UpdateProfile(ProfNetwork esql, String user){
    try{
        boolean notComplete = true;
        String currUser = user;
        while(notComplete){
            System.out.println("\033[1;32mUPDATE MENU\033[0m");
            System.out.println("--------------");
            System.out.println("1. Delete Messages");
            System.out.println("2. Add Education");
            System.out.println("3. Add Work Experience");
            System.out.println("4. Send a new message");
            System.out.println("5. Delete a message");
            System.out.println(".........................");
            System.out.println("9. Go back to Main Menu");

            switch (readChoice()){
    		    case 1: viewMsg(esql, currUser); break;
			    case 2: sentMsg(esql, currUser); break;
			    case 3: recdMsg(esql, currUser); break;
                case 4: makeMsg(esql, currUser); break;
                //case 5: deleteMsg(esql, currUser); break;
			    case 9: notComplete = false; break;
                default : System.out.println("Unrecognized choice!"); break;
            }
        }
    }catch (Exception e){
        System.err.println(e.getMessage());
    }

}
public static void OtherFriendList(ProfNetwork esql){
	try{
		System.out.println("Enter the username of the person you want to view friends for");
		String otherUser = in.readLine();
		System.out.println("\n");
		FriendList(esql,otherUser);
	}catch(Exception e){
		System.err.println (e.getMessage());
	}
}
public static void Profile(ProfNetwork esql, String user){
	try{
		//Clear Screen
		System.out.println("\u001b[2J");
		//Headder
		String title = String.format("------'%s''s Profile -------\n",user);
		System.out.println(title);
		String query = String.format("SELECT userData('%s');",user);
		int found = esql.executeAndPrintHeader(query);
		if(found == 0)
			System.out.println("User Does Not Exist!");
		else{
			//	Work Experience
			query = String.format("SELECT getWork('%s');",user);
			int work = esql.executeAndPrintWork(query);
			if(work == 0)
				System.out.println("--NONE--\n");
			//	Educational Details
    	    query = String.format("SELECT getEdu('%s');",user);
    	    int educate = esql.executeAndPrintEduc(query);
			if(educate == 0)
				System.out.println("--NONE--\n");
		}
	}catch(Exception e){
		System.err.println (e.getMessage());
	}
}

public static void otherProfile(ProfNetwork esql){
	try{
            System.out.println("Enter the username of the name of the person whose profile you wanto to view.");
            String otherUser = in.readLine();
            System.out.println("\n");
            Profile(esql,otherUser);
	}catch(Exception e){
		System.out.println(e.getMessage());
	}
}

}//end ProfNetwork
