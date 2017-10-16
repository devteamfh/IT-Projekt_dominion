package Dominion.Server.ServerClasses;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 *
public class Database {
     *
     * @author sqlitetutorial.net
     */
    public class Database {


        private String workingDirecotry = System.getProperty("user.dir");
        private String url =  "jdbc:sqlite:"+workingDirecotry+"/" + "test.db";
        /**
         * Connect to a sample database
         *
         * @param fileName the database file name
         */


        public  void createNewDatabase(String fileName) {


            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    DatabaseMetaData meta = conn.getMetaData();
                    System.out.println("The driver name is " + meta.getDriverName());
                    System.out.println("A new database has been created.");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


        public  void createTables() {

            String sql_createTables = "CREATE TABLE IF NOT EXISTS Users (\n"
                    + "	id integer PRIMARY KEY AUTOINCREMENT NOT NULL,\n"
                    + "	name text NOT NULL\n"
                    + ");\n"
                    + "CREATE TABLE IF NOT EXISTS Statistics (\n"
                    + "	id integer,\n"
                    + "	gamesStarted text NOT NULL\n"
                    + "	gamesWon text NOT NULL\n"
                    + "	gamesLost text NOT NULL\n"
                    + "	FOREIGN KEY (id) REFERENCES Users(id)"
                    + ");";

                sql_execute(sql_createTables);
        }


        public void sql_execute(String sqlStatement){

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {

                stmt.execute(sqlStatement);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }


        public void addPlayer(String user, String pw) {

        }


        public void addStatistics(String s) {

        }



        /**
         * @param args the command line arguments
         */
        public static void main(String[] args) {

            Database db = new Database();
            db.createNewDatabase("test.db");
            db.createTables();


        }
    }








