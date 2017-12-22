package Dominion.Client.ClientClasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import Dominion.ServiceLocator;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CheckUserData {

	private Client_Model model;
	private File userFile;
	private String tf1;
	private String tf2;
	private String btnStr;
	private boolean userExists;
	private ArrayList<String> al_currentUserandStats = new ArrayList<String>(10);
	
	ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
	
	
	public CheckUserData() {
	}
	
	public boolean userFileExists(){
		
		try { 
			this.userFile = new File("user.dat");	
				
			if (userFile.createNewFile()){
				sl.getLogger().info("User File wurde erstellt");
				return true;
			} else {
				sl.getLogger().info("User File existiert schon");
				return true;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
				return false;
				}
	}

	
	
	
	
	/**
	 * @author kab: Benutzer wird Registriert (Daten des Benutzers werden in ein File geschrieben. 
	 * 				Initialer Wert f�r Attribute 3-10 = 0
	 */
	public boolean enterUserData(String tf1, String tf2){
		this.tf1 = tf1;
		this.tf2 = tf2;
		String str_initialStats = ";0;0;0;0;Jungspieler;0;0;0";   //initiale Statsitik Werte	
		try {
			FileWriter fw = new FileWriter(this.userFile, true);
			fw.write(this.tf1+";"+this.tf2+str_initialStats+System.getProperty("line.separator"));
			fw.close();
			
			getAl_currentUserandStats().add(tf1);getAl_currentUserandStats().add(tf2);
			
			//�brige attribute m�ssen noch ausgelesen werden und in die array list gelegt werden
			getAl_currentUserandStats().add("0");getAl_currentUserandStats().add("0");
			getAl_currentUserandStats().add("0");getAl_currentUserandStats().add("0");
			getAl_currentUserandStats().add("Jungspieler");getAl_currentUserandStats().add("0");
			getAl_currentUserandStats().add("0");getAl_currentUserandStats().add("0");

			sl.getLogger().info("Spieler erfolgreich registriert");
			//Client_View_start.lbl_errMsg.setText("Spieler erfolgreich registriert");
			return true;
			
		} catch (IOException e) {
			sl.getLogger().info("Spieler konnte nicht registriert werden");
			Client_View_start.lbl_errMsg.setText("Spieler konnte nicht registriert werden");
			return false;
		}
	}
	
	
	/**
	 * @author kab: registirert Benutzer und oder �berpr�ft ob eingeloggt werden kann
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
	 * @author kab: pr�ft ob Benutzer existiert
	 * 				
	 */
	public boolean userExists(String tf1){
		this.tf1 = tf1;
		FileReader reader = null;
		String line;
		Integer iLines = 0;
		
		String[] userAndStats = null;
		Integer strArr_Capacity = null;
		Integer MAX_NO_OF_ATTRIBUTES_PER_LINE = 10;

		BufferedReader bReader = null;

		iLines = countLines();
		strArr_Capacity = iLines * MAX_NO_OF_ATTRIBUTES_PER_LINE;
		userAndStats = new String[strArr_Capacity];

		try {
			reader = new FileReader(this.userFile);
			bReader = new BufferedReader(reader);

			while ((line = bReader.readLine()) != null) {
				userAndStats = line.split(";");

				for (int i = 0; i < userAndStats.length; i = i + MAX_NO_OF_ATTRIBUTES_PER_LINE) {
					if (this.tf1.equals(userAndStats[i].toString())) {
						sl.getLogger().info("Spieler existiert bereits");
						
							for(int j = 0; j < MAX_NO_OF_ATTRIBUTES_PER_LINE ; j++ ) {
								this.al_currentUserandStats.add(userAndStats[j].toString());
							}

						
						bReader.close();
						this.userExists = true;
						return true;
					}
				}
			}
			bReader.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sl.getLogger().info("userFile konnte nicht durchsucht werden");
			Client_View_start.lbl_errMsg.setText("Fehler beim Zugriff auf lokale Dateien");
		}
		
		return false;
		
	}


			/**
			 * @auathor kab: �berpr�ft ob Username und Passwort zu einander passen
			 * 
			 */
	public boolean pwCorrect(String tf1, String tf2){
		this.tf1 = tf1;
		this.tf2 = tf2;
		FileReader reader = null;
		String line;
        Integer iLines = 0;

        String[] strArr_userStats = null;
		Integer strArr_Capacity = null;
		Integer MAX_NO_OF_ATTRIBUTES_PER_LINE = 10;
		
		BufferedReader bReader = null;

		iLines = countLines();
        strArr_Capacity = iLines * MAX_NO_OF_ATTRIBUTES_PER_LINE;
        strArr_userStats = new String[strArr_Capacity];

		try {
            reader = new FileReader(this.userFile);
            bReader = new BufferedReader(reader);

			while ((line = bReader.readLine()) != null) {
                strArr_userStats = line.split(";");


                for (int i = 0; i < strArr_userStats.length; i = i + MAX_NO_OF_ATTRIBUTES_PER_LINE) {
                    if ((this.tf1.equals(strArr_userStats[i].toString()) == true) && (this.tf2.equals(strArr_userStats[i + 1].toString()) == true)) {
                        bReader.close();
                        sl.getLogger().info("Passwort korrekt. Eintritt gew�hrt.");
                    	return true;
                    }
                }
            }
			bReader.close();
			
		} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sl.getLogger().info("userFile konnte nicht durchsucht werden");
    		Client_View_start.lbl_errMsg.setText("Probleme beim Zugriff auf lokale Dateien");
        }
		sl.getLogger().info("Passwort nicht korrekt. Eintritt verwehrt.");
		//Client_View_start.lbl_errMsg.setText("Passwort nicht korrekt. Der Eintritt wird verwehrt.");
		
		sl.setLbl_popUpMessage(new Label("Passwort nicht korrekt. Der Eintritt wird verwehrt."));
		
		Platform.runLater(new Runnable() {

			@Override 
	           public void run() {
				
				Stage popUp = new Stage();	
				popUp.setResizable(false);
				//popUp.initModality(Modality.APPLICATION_MODAL);
				Client_View_popUp view = new Client_View_popUp (popUp, model);
				new Client_Controller_popUp(model, view); 
				view.start();
			}	
			
		});
		
		
	return false;
	}


	/**
	 * @author kab: z�hlt die Anzahl Zeilen in User.dat Datei
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
    		Client_View_start.lbl_errMsg.setText("Fehler beim Zugriff auf lokale Dateien");
        }

	    return iLines;
    }

	
	
	public ArrayList<String> getAl_currentUserandStats() {
		return al_currentUserandStats;
	}

	public void setAl_currentUserandStats(ArrayList<String> al_currentUserandStats) {
		this.al_currentUserandStats = al_currentUserandStats;
	}



	
	

	

}
