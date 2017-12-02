package Dominion.Client.ClientClasses;

import Dominion.ServiceLocator;
import javafx.scene.control.Button;

/**
 * 
 * Prüft Eingaben der client_View_start Klasse
 * @author kab618
 *
 */

public class checkFields {
	private static checkFields instance = null;
	private String btnStr;
	private String tf1;
	private String tf2;
	private String errMsg = "";
	
	private boolean rdyToConnect;
	private boolean userRegistred;
	private boolean userPwOk;
	
	ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
	
	public checkFields(){
	}
	
	checkUserData checkUserData = new checkUserData();
	
	/*protected checkFields(){
		//verhindert Instanzierung
	}	
	
	public static checkFields getInstance(){
		if(instance == null) {
			instance = new checkFields();
		}
		return instance;
		}*/
	


	/**
	 * @author kab: überprüft für Verbinden, ob Eingaben in Textfelder gemacht worden sind.
	 * 				überprüft für Registrieren und Einloggen, ob sich der User Registrieren und Einloggen kann,
	 *              die Prüfung nimmt Klasse checkUserData vor
	 */	
	public void checkfields(String btnStr, String tf1, String tf2){
		this.btnStr = btnStr;
		
		this.tf1 = tf1;
		this.tf2 = tf2;
	
		switch(this.btnStr) {
		
			case "Verbinden": 	
									
				if (this.tf1.equals(""))
					this.errMsg =  "IP-Nr. fehlt";
				if (this.equals("") && this.tf2.equals(""))
					this.errMsg = errMsg +" "+System.getProperty("line.separator");
				if (this.tf2.equals(""))
				this.errMsg = errMsg+"Port-Nr. fehlt";
				
				if (!this.errMsg.equals("")) {
					sl.getLogger().info(errMsg);   
					Client_View_start.lbl_errMsg.setText(errMsg);
				this.errMsg = "";
					break;
				} else { 		
				setRdyToConnect(true);
				}
				break;
			

			case "Registrieren":
				
				if (checkTfUserAndPw(this.tf1, this.tf2)){
					if (checkUserData.checkRegistration(this.btnStr, this.tf1, this.tf2)) {
					setUserRegistred(true);	
					}
				}
				break;


			case "Einloggen":
				
				if (checkTfUserAndPw(this.tf1, this.tf2)) {
				
					if (checkUserData.checkRegistration(this.btnStr, this.tf1, this.tf2)) {
						setUserRegistred(true);
					}

					if(getUserRegistred() && checkUserData.pwCorrect(this.tf1,this.tf2)){
						setUserPwOk(true);	
					}
				}
				this.errMsg = "";
				break;
		}
	}
	
	
	public void setRdyToConnect(boolean bol){
		this.rdyToConnect = bol;
	}

	
	public boolean getRdyToConnect(){
		boolean rdyToConnect = this.rdyToConnect;
		return rdyToConnect;
		}
	
	
	public void setUserRegistred(boolean bol){
		this.userRegistred = bol;
	}
	
	
	public boolean getUserRegistred(){
		boolean userRegistred = this.userRegistred;
				return userRegistred;
	}
	
	
	public void setUserPwOk(boolean bol){
		this.userPwOk = bol;
	}
	
	
	public boolean getUserPwOk(){
		boolean userPwOk = this.userPwOk;
				return userPwOk;
	}
	
	
	/**
	 * @author kab: überprüft ob Textfelder nicht leer sind
	 */
	public boolean checkTfUserAndPw(String tf1, String tf2){
		this.tf1 = tf1;
		this.tf2 = tf2;
		
		if (this.tf1.equals(""))
			this.errMsg =  "User fehlt";
		if (this.tf1.equals("") && this.tf2.equals(""))
			this.errMsg = errMsg+System.getProperty("line.separator");
		if (this.tf2.equals(""))
		this.errMsg = errMsg+"Passwort fehlt";
			sl.setLbl_errMsgView(errMsg);
			
		
		if (!this.errMsg.equals("")) {
			sl.getLogger().info(errMsg);  
			Client_View_start.lbl_errMsg.setText(errMsg);
			this.errMsg = "";
			return false;
		} 	
		return true;	
		
	}


	
	
}
