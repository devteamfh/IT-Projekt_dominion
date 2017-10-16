package Dominion.Client.ClientClasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;

import Dominion.ServiceLocator;

public class dbClass {
	private static dbClass instance = null;
	
	private File userFile;
	private String tf1;
	private String tf2;
	private String workingDirectory = System.getProperty("user.dir");
	
	ServiceLocator sl = ServiceLocator.getServiceLocator();
	
	protected dbClass(){
		//verhindert Instanzierung
	}
	
	public static dbClass getInstance(){
		if(instance == null) {
			instance = new dbClass();
		}
		return instance;
		}
	
	
	
	//prüft ob user File da
	public boolean userFileExists(){
		
		try { 
			this.userFile = new File(workingDirectory+"/user.txt");	
				
			if (userFile.createNewFile()){
				sl.getLogger().info("User File wurde erstellt");
				return true;
			} else {
				sl.getLogger().info("USer File existiert schon");
				return true;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
				return false;
				}	


	}

	
	
	
	
	/**
	 * @author kab: registirert user
	 */
	public boolean enterUserData(String tf1, String tf2){
		this.tf1 = tf1;
		this.tf2 = tf2;
		try {
			FileWriter fw = new FileWriter(this.userFile, true);
					
			fw.write(tf1+" "+tf2+"/n");
			fw.close();
			sl.getLogger().info("User erfolgreich registriert");
			return true;
			
		} catch (IOException e) {
			sl.getLogger().info("Konnte User nicht registireren");
			return false;
		}
	}
	
	
	
	public boolean checkRegistration(){
		
		if (userFileExists()){
			if (userExists(this.tf1)){
				sl.getLogger().info("User ist bereits angelegt");
				return true;
			} else {
			enterUserData(this.tf1,this.tf2);	
			}
			
		}
		
	return false;
	
		
				
	}
	
	
	
	
	/**
	 * @author kab: prüft ob User existiert
	 */
	public boolean userExists(String tf1){

		this.tf1 = tf1;
		String line;
		String[] strArr = null;
		FileReader reader = null;
		BufferedReader bReader = null;
		
		
		try {
			reader = new FileReader(this.userFile);
			bReader = new BufferedReader(reader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sl.getLogger().info("UserFile konnte nicht gefunden werden");
		
		}
		
	
		try {
			line = bReader.readLine();
			
			while (line !=null) {
				strArr = line.split(";");
			
			}
			
			for (int i = 0; i < strArr.length; i++ ) {
				if (tf1.compareTo(strArr[i]) == 0 && (i&1) == 0) {
					sl.getLogger().info("User ist  angelegt.");
					return true;
				
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sl.getLogger().info("userFile konnte nicht durchsucht werden");
			return false;
		}
		
		return false;
	
	}
	
	
	
	
	

	public boolean pwCorrect(String tf1, String tf2){
		this.tf1 = tf2;
		this.tf2 = tf2;
		FileReader reader = null;
		String line;
		String[] strArr = new String[9];
		
		BufferedReader bReader = null;
		
		try {
			reader = new FileReader(this.userFile);
			bReader = new BufferedReader(reader);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sl.getLogger().info("UserFile konnte nicht gefunden werden");
		}
		
		
		
		try {
			line = bReader.readLine();
			
			while (line !=null) {
				strArr = line.split(";");
			
			}
			
			for (int i = 0; i < strArr.length; i++ ) {
				if (tf1.compareTo(strArr[i]) == 0 && (i&1) == 1 && tf2.compareTo(strArr[i-1]) == 0) {
				return true;
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sl.getLogger().info("userFile konnte nicht durchsucht werden");
		}
		
		
	return false;	
	}
	

	
	
	
	

}
