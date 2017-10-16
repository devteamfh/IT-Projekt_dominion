package Dominion.Client.ClientClasses;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



public class test {
	private static String workingDirectory = System.getProperty("user.dir");
        public static void main(String[] args) {
        	
        	System.out.println(dbClass.getInstance().userFileExists());
            System.out.println(workingDirectory);
            dbClass.getInstance().pwCorrect("abc", "123");
        	
        }
    }

