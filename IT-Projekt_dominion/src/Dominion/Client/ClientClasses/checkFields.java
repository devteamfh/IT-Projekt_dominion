package Dominion.Client.ClientClasses;

import Dominion.ServiceLocator;

/**
 * 
 * Prüft Eingaben der client_View_start Klasse
 * @author kab618
 *
 */

public class checkFields {
	private static checkFields instance = null;
	private String btn;
	private String tf1;
	private String tf2;
	
	private boolean rdyToConnect;
	String errMsg = "";
	//private String errFlags;
	
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
	
		switch(btn) {
		
			case "Verbinden": 	
				//checkTfEmptyness(tf1,tf2);
					
				if (tf1 == "")
					this.errMsg =  "IP-Nr. fehlt";
				if (tf1 == "" && tf2 =="")
					this.errMsg = errMsg+"/n";
				if (tf2 == "")
				this.errMsg = errMsg+"Port-Nr. fehlt";
				
				if (this.errMsg != "") {
					sl.getLogger().info(errMsg);     
				this.errMsg = "";
					break;
				} 		
				
				setRdyToConnect(true);
				
				break;
				
			case "Registrieren":
				checkTfUserAndPw(this.tf1, this.tf2);
				
				if (this.errMsg != "") {
					//methode registirerung
					this.errMsg = "";
					break;
				}
				
				break;
				
			case "Einloggen":
				
				checkTfUserAndPw(this.tf1, this.tf2);
				
				if (this.errMsg != "") {
					//methode checkregistr
					this.errMsg = "";
					break;
				}
				
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
	
	
	
	public void checkTfUserAndPw(String tf1, String tf2){
		this.tf1 = tf1;
		this.tf2 = tf2;
		
		if (tf1 == "")
			this.errMsg =  "User fehlt";
		if (tf1 == "" && tf2 =="")
			this.errMsg = errMsg+"/n";
		if (tf2 == "")
		this.errMsg = errMsg+"Passwort fehlt";
		
		if (this.errMsg != "") {
			sl.getLogger().info(errMsg);     
		this.errMsg = "";
		} 		
	}
	
	
	public void checkRegistration(){
	
		if (dbClass.getInstance().userFileExists()){
			
		}
		
		
		/*
		 * 
		 * 
		 * file vorhanden 
		 * 		ja
		 * wenn file vorhanden, name vorhanden 
		 *		ja  registration succesfull
		 *		nein   call methode registrierung
		 * 				registierung succesfull
		 * filevorhanden
		 * 		nein  mache file, registirere, registierung succesvul
		 * 
		 * 
		 * 
		 * 
		 * 
		 * */
		
	}
	
	
	public void checkDBUserAndPw(){
	}
	
	/*public String checkTfEmptyness(String tf1, String tf2){
		this.tf1 = tf1;
		this.tf2 = tf2;
		
		int paramOne =     0;
		int paramTwo =     0;
		String errFlag = "00";	
		
		if (tf1 == "")
			paramOne = 1;
		
		if (tf2 == "")
		   	paramTwo = 1;
		
		errFlag = Integer.toString(paramOne).concat(Integer.toString(paramTwo));
		
		return this.errFlags = errFlag;		
	}
	*/
	
	
}
