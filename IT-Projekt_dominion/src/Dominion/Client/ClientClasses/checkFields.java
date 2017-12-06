package Dominion.Client.ClientClasses;

import Dominion.ServiceLocator;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * Prüft Eingaben der client_View_start Klasse
 * @author kab618
 *
 */

public class checkFields {
	private String btnStr;
	private String tf1;
	private String tf2;
	private String errMsg = "";
	
	private boolean rdyToConnect;
	private boolean userRegistred;
	private boolean userPwOk;
	
	checkUserData checkUserData;
	
	private Client_Model model;
	
	ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
	
	public checkFields(Client_Model m, checkUserData checkUserData){
		this.model = m;
		this.checkUserData = checkUserData;
	}
	
	public checkFields(){

	}


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
					this.errMsg =  "Die IP-Adresse fehlt";
				if (this.tf1.equals("") && this.tf2.equals(""))
					this.errMsg = errMsg.substring(0,14) +" sowie die ";
				if (this.tf2.equals(""))
				this.errMsg = errMsg+"Port-Nr. fehlt";
				
				if (!this.errMsg.equals("")) {
					sl.getLogger().info(this.errMsg);   
					//Client_View_start.lbl_errMsg.setText(errMsg);
					sl.setLbl_popUpMessage(new Label(errMsg.toString()));
					
					Platform.runLater(new Runnable() {

						@Override 
				           public void run() {
							
							Stage popUp = new Stage();	
							popUp.setResizable(false);
							popUp.initModality(Modality.APPLICATION_MODAL);
							Client_View_popUp view = new Client_View_popUp (popUp, model);
							new Client_Controller_popUp(model, view); 
							view.start();
						}	
						
					});
					
					
					
					
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
					
					
					if(!getUserRegistred()){
						
						sl.getLogger().info("User existiert noch nicht");
						
						sl.setLbl_popUpMessage(new Label("User existiert noch nicht"));
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
			this.errMsg =  "Der Spielername fehlt";
		if (this.tf1.equals("") && this.tf2.equals(""))
			this.errMsg = errMsg.substring(0,15) +" sowie ein ";
		if (this.tf2.equals(""))
		this.errMsg = errMsg+"Passwort fehlt";
			//sl.setLbl_errMsgView(errMsg);	
		
		if (!this.errMsg.equals("")) {
			sl.getLogger().info(errMsg);  
			//Client_View_start.lbl_errMsg.setText(errMsg);
			
			sl.setLbl_popUpMessage(new Label(errMsg));
			
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
			
			this.errMsg = "";
			return false;
		} 	
		return true;			
	}
	
	public checkUserData getCheckUserData(){
		return this.checkUserData;
	
	}
	
	
	
}