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

	1)	SEARCH PEOPLE
			QUERY
				SELECT name FROM usr WHERE lower(name) LIKE lower('%TOY%');
			DISPLAY
				???????????????????????????????????????????????????????????

	2)	VIEW FRIENDS, THEN GO TO FRIENDS PROFILE
			QUERY - VIEW FRIENDS
				SELECT userid AS FRIEND(S) FROM connection WHERE connectionid = login and status = 'Accept'
				UNION SELECT connectionid AS FRIEND(S) FROM connection WHERE userid = login and status = 'Accept';i
			DISPLAY
				???????????????????????????????????????????????????????????

	3)	VIEW MESSAGES
			QUERY - VIEW USERS WITH COMMUNICATIONS
				SELECT senderid AS user FROM message WHERE receiverid = login
				UNION SELECT receiverid AS user FROM message where senderid = login;
			QUERY - VIEW COMMUNICATION BETWEEN USER AND SPECIFIC CONTACT
				SELECT senderid, contents, sendtime FROM messages WHERE (senderid = login and receiverid = contact) or (senderid = contact and receiverid = login);
			DISPLAY
				???????????????????????????????????????????????????????????

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
      boolean outputHeader = true;
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
	boolean outputHeader = true;
	System.out.println("-----Educational Experience-----");
    while (rs.next()){
		for (int i=1; i<=numCol; ++i){
			String total = rs.getString(i);

			total = total.replaceAll("\\s+", " ");
			String[] tokens = total.split("[,]+");
			System.out.print("\033[1;34m");
            System.out.println("Institution: \t" + tokens[1].replaceAll("\"",""));
			System.out.println("Major: \t\t" + tokens[2].replaceAll("\"",""));
			System.out.println("Degree: \t" + tokens[3].replaceAll("\"",""));
			System.out.println("Start Date: \t" + tokens[4].replaceAll("\"",""));
			System.out.println("End Date: \t" + tokens[5].replaceAll("[\")]+",""));
            System.out.print("\033[0m");
			System.out.println();
			System.out.println();
			++rowCount;
		}
		System.out.println();
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
	boolean outputHeader = true;
	System.out.println("-----Work Experience-----");
    while (rs.next()){
		for (int i=1; i<=numCol; ++i){
			String total = rs.getString(i);

			total = total.replaceAll("\\s+", " ");
			String[] tokens = total.split("[,]+");
			System.out.print("\033[1;35m");
            System.out.println("Company: \t" + tokens[1].replaceAll("\"",""));
			System.out.println("Role: \t\t" + tokens[2].replaceAll("\"",""));
			System.out.println("Location: \t" + tokens[3].replaceAll("\"",""));
			System.out.println("Start Date: \t" + tokens[4].replaceAll("\"",""));
			System.out.println("End Date: \t" + tokens[5].replaceAll("[\")]+",""));
            System.out.print("\033[0m");
			System.out.println();
			System.out.println();
			++rowCount;
		}
		System.out.println();
	}//end while
	stmt.close ();

	return rowCount;


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
    *
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
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
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
                System.out.println("1. Go to Friend List");
                System.out.println("2. Update Profile");
                System.out.println("3. Write a new message");
                System.out.println("4. Send Friend Request");
                System.out.println("5. View Others Friends List");
                //System.out.println("5. Clear Screen");
                System.out.println(".........................");
                System.out.println("9. Log out");
                switch (readChoice()){
                  case 1: FriendList(esql,authorisedUser); break;
                  //case 2: UpdateProfile(esql); break;
                  //case 3: NewMessage(esql); break;
                  //case 4: SendRequest(esql); break;
                  case 5: OtherFriendList(esql); break;
			      case 6: otherProfile(esql); break;
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

	public static void FriendList(ProfNetwork esql, String user){
		try{
			System.out.println("\u001b[2J");
			System.out.flush();
			System.out.println("   Friend(s)  ");
			System.out.println("--------------");
			String query = String.format("SELECT getfriends('%s') as FRIENDLIST;",user);
			int friends = esql.executeQueryAndPrintResult(query);
		}catch(Exception e){
        	 System.err.println (e.getMessage ());
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

		String query = String.format("SELECT getWork('%s');",user);
		int friends = esql.executeAndPrintWork(query);

        query = String.format("SELECT getEdu('%s');",user);
        int educate = esql.executeAndPrintEduc(query);

		//contact
//		String Namequery = String.format("SELECT name FROM usr WHERE userid = '%s'", user);
//		String Emailquery = String.format("SELECT email FROM usr WHERE userid = '%s'", user);
//		String Idquery = String.format("SELECT userid FROM usr WHERE userid = '%s'", user);
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

    public static void DisplayResults(ProfNetwork esql, String educationProfile){

        //for(int i = 1; i <= educationProfile.size(); i++){
            System.out.println(educationProfile);
        //}
    }

/*	public static void Profile(ProfNetwork esql, String user){
		try{
			//contact
			String Namequery = String.format("SELECT name FROM usr WHERE userid = '%s'", user);
			String Emailquery = String.format("SELECT email FROM usr WHERE userid = '%s'", user);
			String Idquery = String.format("SELECT userid FROM usr WHERE userid = '%s'", user);
            //Educational Details

            //try counting number of edu details, number of work expr and use
            //this for the size of our new lists

            String num_edu_details = String.format("SELECT count(distinct userid) FROM educational_details");
            //System.out.println(num_edu_details);

            String user_edu_details = String.format("SELECT count(institutionName) FROM educational_details WHERE userid = '%s'", user);

            List<List<String>> educationProfile = new ArrayList<List<String>>();

            int edu_number = Integer.parseInt(user_edu_details);

            for (int i = 1; i <= edu_number; i++){

                String user_edu = String.format("SELECT institutionname from educational_details WHERE userid = '%s'", user);
                //educationProfile.add(user_edu);

            }
            //placer("Searhing educational details);



            while(educationProfile.size() <= 0){
                //look at the query and result

                String query = String.format("SELECT * FROM USR WHERE userid = '%s'", user);
                educationProfile = esql.executeQueryAndReturnResult(query);
                if(educationProfile.size() <=0){
                    System.out.println("\tNo Results.");
                }

            }
            System.out.println(educationProfile);
            //DisplayResults(esql, educationProfile);
            //ChooseOption(esql, educationProfile, educationProfile.size(), currentUser);

            //Work Experience

		}catch(Exception e){
			System.err.println (e.getMessage());
		}

	}*/

// Rest of the functions definition go in here

}//end ProfNetwork
