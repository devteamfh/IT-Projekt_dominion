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
	
	
	
	public boolean userFileExists(){
		
		try { 
			this.userFile = new File("user.dat");	    			
	
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sl.getLogger().info("USer File existiert nicht");
			if (createUserFile()){
			sl.getLogger().info("User File wurde erstellt");
			return true;
			}
			
		}
		
		return false;
	
	}

	
	public boolean createUserFile(){

		try {
			userFile.createNewFile();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sl.getLogger().info("USer File konnte nicht erstellt werden");
			}
		
		return false;
		
	}
	
	public boolean enterUserData(String tf1, String tf2){
		this.tf1 = tf1;
		this.tf2 = tf2;
		try {
			FileWriter fw = new FileWriter(this.userFile, true);
			
			
			
			fw.write(tf1+" "+tf2+"/n");
			fw.close();
		} catch (IOException e) {
			sl.getLogger().info("Konnte Datei nicht erstellen");
		}
		
		
		
		return false;
	
	
	}
	
	
	public boolean doesUserExist(String tf1){
		this.tf1 = tf1;
		FileReader reader = null;
		String line;
		String[] strArr = null;
		
		BufferedReader bReader = new BufferedReader(reader);
		
		try {
			reader = new FileReader(this.userFile);
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
	
	
	
	
	

	public boolean pwCorrect(String tf1, String tf2){
		this.tf1 = tf2;
		this.tf2 = tf2;
		FileReader reader = null;
		String line;
		String[] strArr = null;
		
		BufferedReader bReader = new BufferedReader(reader);
		
		try {
			reader = new FileReader(this.userFile);
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
