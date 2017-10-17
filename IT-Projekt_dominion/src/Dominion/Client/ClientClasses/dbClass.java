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
	
	
	
	//pr�ft ob user File da
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
	
	
	
	public boolean checkRegistration(String tf1){
		this.tf1 = tf1;
		if (userFileExists()) {
			if (userExists(this.tf1)) {
				return true;
			} else {
				enterUserData(this.tf1, this.tf2);
			}
		}
	return false;
	}
	
	
	
	
	/**
	 * @author kab: prüft ob User existiert
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
	return false;
	}



	public int countLines() {

        String line;
        Integer iLines = 0;

        FileReader reader = null;
        BufferedReader bReader = null;

        //initialisiere this.userfile
        userFileExists();

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
