package Dominion.Server.ServerClasses;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import Dominion.ServiceLocator;

/**
 *
 *
public class Database {
     *
     * 
     */
	
   /**
    * @author Beda Kaufmann
    */ 
    public class Database {

        private String workingDirecotry = System.getProperty("user.dir");
        private String url =  "JDBC:sqlite:"+workingDirecotry+"/" + "user.db";

        
        ServiceLocator sl = ServiceLocator.getServiceLocator();
        
        
        /**
         * @author kab: kreiere und connecte zu Datenbank
         * @param fileName name der Datenbank
         */
             

        public  void createNewDatabase() {


            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    DatabaseMetaData meta = conn.getMetaData();
                    sl.getLogger().info("The driver name is " + meta.getDriverName());
                    sl.getLogger().info("A new Database has been created");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


        /**
         * @author kab: kreiere Tabelle Users
         * 
         */      
        public  void createTables() {

            String sql_createTables = "CREATE TABLE IF NOT EXISTS Users (\n"
                    + "	Name text NOT NULL,\n"
                    + "	PassWrd text NOT NULL,\n"
                    + "	gamesStarted INT NULL,\n"
                    + "	gamesWon INT NULL,\n"
                    + "	gamesLost INT NULL,\n"
                    + "	Column05 INT NULL,\n"
                    + "	Column06 INT NULL,\n"
                    + "	Column07 INT NULL,\n"
                    + "	Column08 INT NULL,\n"
                    + "	Column09 INT NULL,\n"
                    + "	PRIMARY KEY (Name, PassWrd)"
                    + ");";

                if(sql_execute(sql_createTables)) {
                    sl.getLogger().info("User Table has been created");
                }
        }


        /**
         * @author sqlitetutorial.net
         *  f�hrt SQL STatements aus
         */
        public boolean sql_execute(String sqlStatement){

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {
                stmt.execute(sqlStatement);
                return true;
            } catch (SQLException e) {
                sl.getLogger().info(e.getMessage());
                return false;
            }

        }


        /**
         * @author kab:
         */
        public void addPlayer(String user, String pw, String att2, String att3, String att4, String att5, String att6, String att7, String att8, String att9){
        	String sql_userData = 
        			  " INSERT INTO Users (\n"
                    + "	Name,        \n"
                    + "	PassWrd,     \n"
                    + "	gamesStarted,\n"
                    + "	gamesWon,    \n"
                    + "	gamesLost,   \n"
                    + "	Column05,    \n"
                    + "	Column06,    \n"
                    + "	Column07,    \n"
                    + "	Column08,    \n"
                    + "	Column09)    \n"
                    + " VALUES       \n"
                    + " ('"+user+"', \n"
                    + "  '"+pw+  "', \n"
                    + "  '"+att2+"', \n"
                    + "  '"+att3+"', \n"
                    + "  '"+att4+"', \n"
                    + "  '"+att5+"', \n"
                    + "  '"+att6+"', \n"
                    + "  '"+att7+"', \n"
                    + "  '"+att8+"', \n"
                    + "  '"+att9+ "' \n"    
                    + ");";

                if(sql_execute(sql_userData)) {
                    sl.getLogger().info("User Data has been entered into Table Users");
                }
        }

       //methode update statistik
        
        
        /**
         * @author kab:
         */
        public void addStatistics(String s) {

        }



    /*    /**
         * @author sqlitetutorial.net
         * @param args the command line arguments
         /
        public static void main(String[] args) {

            Database db = new Database();
            db.createNewDatabase();
            db.createTables();


        }*/
    }








