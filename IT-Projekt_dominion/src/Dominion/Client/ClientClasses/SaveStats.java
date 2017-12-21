package Dominion.Client.ClientClasses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import Dominion.appClasses.StartInformation;

/**
 * @author kab: klasse kann spieler stiatistk aus user.dat file löschen und mit neuen Werten wieder sichern
 */
public class SaveStats {
	ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
	
	private File userFile;
	
	private String 	username;
	private String 	PW;
	private int 	gamesPlayed;
	private int 	gamesWon;
	private int		gamesLost;
	private int 	winLooseRatio;
	private String  att6;
	private String  att7;
	private String  att8;
	private String  att9;
	
	
	public SaveStats() {
	}


	public SaveStats(StartInformation startInformation) {
		this.userFile = new File("user.dat");
		
		this.username	 	 = startInformation.getUsername();
		this.PW		 	 	 = startInformation.getPW();
		this.gamesPlayed 	 = startInformation.getGamesPlayed();
		this.gamesWon	     = startInformation.getGamesWon();
		this.gamesLost		 = startInformation.getGamesLost();
		this.winLooseRatio	 = startInformation.getWinLooseRatio();
		this.att6 			 = startInformation.getAtt6();
		this.att7 			 = startInformation.getAtt7();
		this.att8 			 = startInformation.getAtt8();
		this.att9 			 = startInformation.getAtt9();
		
	}

	
	public void deleteDataRow() throws IOException {
			
		File tempFile = new File("tempUser.dat");

		BufferedReader reader = new BufferedReader(new FileReader(this.userFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

		String currentLine;

		while((currentLine = reader.readLine()) != null) {
		    // trim newline when comparing with lineToRemove
		    String trimmedLine = currentLine.trim();
		    if(trimmedLine.startsWith(this.username)) continue;
		    writer.write(currentLine + System.getProperty("line.separator"));
		}
		writer.close(); 
		reader.close(); 
		this.userFile.delete();
		boolean successful = tempFile.renameTo(this.userFile);
		sl.getLogger().info("neues File wurde erstellt und die Daten des Clients gelöscht: "+successful);
	}
	

	public void saveData() throws IOException {
	
		FileWriter fw = new FileWriter(this.userFile, true);
		fw.write(this.username		+";"+
				 this.PW			+";"+ 		    
				 this.gamesPlayed	+";"+
				 this.gamesWon		+";"+
				 this.gamesLost		+";"+
				 this.winLooseRatio +";"+
				 this.att6			+";"+
				 this.att7			+";"+
				 this.att8			+";"+
				 this.att9              +
			 	 System.getProperty("line.separator"));
		fw.close();	
	}
	
	


}
