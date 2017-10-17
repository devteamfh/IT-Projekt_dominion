package Dominion.Client.ClientClasses;

import Dominion.ServiceLocator;

/**
 * 
 * Pr�ft Eingaben der client_View_start Klasse
 * @author kab618
 *
 */

public class checkFields {
	private static checkFields instance = null;
	private String btn;
	private String tf1;
	private String tf2;
	private String errMsg = "";

	private boolean rdyToConnect;
	private boolean userRegistred;
	private boolean userPwOk;

	
	ServiceLocator sl = ServiceLocator.getServiceLocator();

	
	protected checkFields(){
		//verhindert Instanzierung
	}	
	
	public static checkFields getInstance(){
		if(instance == null) {
			instance = new checkFields();
		}
		return instance;
		}
	
	
	
	public void checkfields(String btn, String tf1, String tf2){
		this.btn = btn;
		this.tf1 = tf1;
		this.tf2 = tf2;
	
		switch(this.btn) {
		
			case "Verbinden": 	
									
				if (this.tf1.equals(""))
					this.errMsg =  "IP-Nr. fehlt";
				if (this.equals("") && this.tf2.equals(""))
					this.errMsg = errMsg +" "+System.lineSeparator();
				if (this.tf2.equals(""))
				this.errMsg = errMsg+"Port-Nr. fehlt";
				
				if (!this.errMsg.equals("")) {
					sl.getLogger().info(errMsg);     
				this.errMsg = "";
					break;
				} else { 		
				setRdyToConnect(true);
				}
				break;
			

			case "Registrieren":
				
				if (checkTfUserAndPw(this.tf1, this.tf2)){
					if (dbClass.getInstance().checkRegistration(this.tf1)) {
					setUserRegistred(true);	
					}
				}
				break;


			case "Einloggen":
				
				if (checkTfUserAndPw(this.tf1, this.tf2)) {
				
					if (dbClass.getInstance().checkRegistration(this.tf1)) {
							setUserRegistred(true);
					}

					if(getUserRegistred() && dbClass.getInstance().pwCorrect(this.tf1,this.tf2)){
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
	
	
	
	public boolean checkTfUserAndPw(String tf1, String tf2){
		this.tf1 = tf1;
		this.tf2 = tf2;
		
		if (this.tf1.equals(""))
			this.errMsg =  "User fehlt";
		if (this.tf1.equals("") && this.tf2.equals(""))
			this.errMsg = errMsg+"\n";
		if (this.tf2.equals(""))
		this.errMsg = errMsg+"Passwort fehlt";
		
		if (!this.errMsg.equals("")) {
			sl.getLogger().info(errMsg);  
			this.errMsg = "";
			return false;
		} 	
		return true;	
		
	}
	
	
	

	
	
}
