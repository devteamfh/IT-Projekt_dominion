package Dominion.Client.ClientClasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import Dominion.ServiceLocator;

public class checkUserData {
	private static checkUserData instance = null;
	
	private File userFile;
	private String tf1;
	private String tf2;
	private String btnStr;
	
	ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
	
	protected checkUserData(){
		//verhindert Instanzierung
	}
	
	public static checkUserData getInstance(){
		if(instance == null) {
			instance = new checkUserData();
		}
		return instance;
		}
	
	
	
	/**
	 * @author kab: überprüft, ob ein File mit Benutzerdaten existiert und erstellt, sofern nütig, eines
	 * 
	 */
	public boolean userFileExists(){
		
		try { 
			this.userFile = new File("user.dat");	
				
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
	 * @author kab: Benutzer wird registriert
	 */
	public boolean enterUserData(String tf1, String tf2){
		this.tf1 = tf1;
		this.tf2 = tf2;
		try {
			FileWriter fw = new FileWriter(this.userFile, true);
					
			fw.write(this.tf1+";"+this.tf2+System.getProperty("line.separator"));
			fw.close();
			sl.getLogger().info("User erfolgreich registriert");
			return true;
			
		} catch (IOException e) {
			sl.getLogger().info("Konnte User nicht registireren");
			return false;
		}
	}
	
	
	/**
	 * @author kab: registirert Benutzer und oder überprüft ob eingeloggt werden kann
	 * 
	 * 
	 */
	public boolean checkRegistration(String btnStr, String tf1, String tf2){
		this.tf1 = tf1;
		this.tf2 = tf2;
		this.btnStr = btnStr;
		
			
			switch (this.btnStr){
			
			case "Registrieren":
									if (userFileExists()) {
										if (userExists(this.tf1)) {
										return true;
										} 
										return enterUserData(this.tf1, this.tf2);
									}
			
			case "Einloggen":
									if(userFileExists()) {
										return userExists(this.tf1);
									}else {
										return false;
									}
			}
			return false;	
	}
	
	
	
	
	/**
	 * @author kab: prüft ob Benutzer existiert
	 */
	public boolean userExists(String tf1){
		this.tf1 = tf1;
		FileReader reader = null;
		String line;
		Integer iLines = 0;

		String[] strArr = null;
		Integer strArr_Capacity = null;
		Integer MAX_NO_OF_ATTRIBUTES_PER_LINE = 10;

		BufferedReader bReader = null;

		iLines = countLines();
		strArr_Capacity = iLines * MAX_NO_OF_ATTRIBUTES_PER_LINE;
		strArr = new String[strArr_Capacity];

		try {
			reader = new FileReader(this.userFile);
			bReader = new BufferedReader(reader);

			while ((line = bReader.readLine()) != null) {
				strArr = line.split(";");

				for (int i = 0; i < strArr.length; i = i + MAX_NO_OF_ATTRIBUTES_PER_LINE) {
					if (this.tf1.equals(strArr[i].toString())) {
						sl.getLogger().info("User existiert bereits");
						bReader.close();
						return true;
					}
				}
			}
			bReader.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sl.getLogger().info("userFile konnte nicht durchsucht werden");
		}
		sl.getLogger().info("User existiert noch nicht");
		return false;
	}

	
			/**
			 * @auathor kab: überprüft ob Username und Passwort zu einander passen
			 * 
			 */
	public boolean pwCorrect(String tf1, String tf2){
		this.tf1 = tf1;
		this.tf2 = tf2;
		FileReader reader = null;
		String line;
        Integer iLines = 0;

        String[] strArr = null;
		Integer strArr_Capacity = null;
		Integer MAX_NO_OF_ATTRIBUTES_PER_LINE = 10;
		
		BufferedReader bReader = null;

		iLines = countLines();
        strArr_Capacity = iLines * MAX_NO_OF_ATTRIBUTES_PER_LINE;
        strArr = new String[strArr_Capacity];

		try {
            reader = new FileReader(this.userFile);
            bReader = new BufferedReader(reader);

			while ((line = bReader.readLine()) != null) {
                strArr = line.split(";");


                for (int i = 0; i < strArr.length; i = i + MAX_NO_OF_ATTRIBUTES_PER_LINE) {
                    if ((this.tf1.equals(strArr[i].toString()) == true) && (this.tf2.equals(strArr[i + 1].toString()) == true)) {
                        bReader.close();
                        sl.getLogger().info("Passwort korrekt. Eintritt gewührt.");
                    	return true;
                    }
                }
            }
			bReader.close();
			
		} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sl.getLogger().info("userFile konnte nicht durchsucht werden");
        }
		sl.getLogger().info("Passwort nicht korrekt. Eintritt verwehrt");
		
		
	return false;
	}


	/**
	 * @author kab: zählt die Anzahl Zeilen in User.dat Datei
	 * 
	 * 
	 */
	
	public int countLines() {
        String line;
        Integer iLines = 0;

        FileReader reader = null;
        BufferedReader bReader = null;

        //initialisiere this.userfile
        if (this.userFile == null) {
        userFileExists();
        }

        try {
            reader = new FileReader(this.userFile);
            bReader = new BufferedReader(reader);

            while ((line = bReader.readLine()) != null) {
                iLines++;
            }
            bReader.close();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sl.getLogger().info("UserFile konnte nicht gefunden werden");
        }

	    return iLines;
    }

	
	

}
