package Dominion.Server.ServerClasses;


import com.sun.xml.internal.fastinfoset.util.StringArray;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.sqlite.core.DB;



public class test {
	private static String workingDirectory = System.getProperty("user.dir");
        public static void main(String[] args) {
        	
        	//System.out.println(dbClass.getInstance().userFileExists());
            //System.out.println(workingDirectory);
            //System.out.println(dbClass.getInstance().pwCorrect("jada","2222"));
            //System.out.println("jada"+System.getProperty("line.separator")+"jada");
            //System.out.println();
            String dbUsers = new String("");
            
           /* try {
				Class.forName("org.sqlite.JDBC");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
            
            Database db = new Database();
            
            db.createNewDatabase();
            db.createTables();
            
          //  db.addPlayer("abc", "123", "att2", "att3", "att4", "att5", "att6", "att7", "att8", "att9");
            
            
        }
    }

