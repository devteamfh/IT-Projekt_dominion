package Dominion.Client.ClientClasses;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.sun.media.jfxmedia.logging.Logger;

import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * MVC Pattern:
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class Client_Controller_start extends Controller<Client_Model, Client_View_start> {
    
    Client_View_start view;
    InetAddress addr;

    
    public Client_Controller_start(Client_Model model, Client_View_start view) {
        super(model, view);
        this.view = view;

        // register ourselves to handle window-closing event
        view.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
            }
        });
             
        /**
         * @author Joel Henz:
         * connecting a client to the server by getting the IP address and the port number from the TextFields and then creating the playing Stage
         * edited kab: 10.10.17: Neue Buttons btn_register und btn_login und diverse Prüfmechanismen auf tf_ eingebaut
         */    
      
        view.btn_connect.setOnAction(new EventHandler<ActionEvent>() { 
            @Override
            public void handle(ActionEvent event) {
            	     
            /*	//Prüfe ob Felder nicht leer sind
            	String logInfo = "";
            	int fehler = 0;
            	
            	if (view.tf_ip.getText().compareTo("") == 0) {
            		logInfo = "Server IP nicht eingegeben"; 
            		fehler = 1;
            	}
            	
            	if (view.tf_port.getText().compareTo("") == 0) {
            		logInfo = logInfo +"\n"+ "Port Nr. nicht eingegeben";
            		fehler = 1;
            	}
 
            	if (view.tf_userName.getText().compareTo("") == 0) {
            		logInfo = logInfo +"\n"+ "User Name nicht eingegeben";
            		fehler = 1;
            	}
            	
            	if(view.tf_password.getText().compareTo("") == 0) {
            		logInfo = logInfo +"\n"+ "Passwort nicht eingegeben";   		
            		fehler = 1;
            	}
            	
            	//Gebe Fehlermeldungen aus
            	if (fehler == 1){
            	  model.sl.getLogger().info(logInfo);   
            	  
            	       	
            	} else {  */
            	
            	checkFields.getInstance().checkfields(view.btn_connect.getText(), view.tf_ip.getText(),view.tf_port.getText());
            	
            	if (checkFields.getInstance().getRdyToConnect())
            	
            	   	try { 
                		
                		addr = InetAddress.getByName(view.tf_ip.getText());
                		int portNr = Integer.parseInt(view.tf_port.getText());
                    	String name = view.tf_userName.getText();
                    	model.setName(name);
        				model.connectToServer(addr,portNr);	
        				
        				if (model.connected){
        		        	
        		        	view.btn_connect.getStyleClass().add("connected");
        		        	view.btn_connect.setText("Verbunden");
        		        }				
    			
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    					model.sl.getLogger().info("Verbindung mit Server nicht erfolgreich");
    				}
     		        
            }
        });   
        
        
		        view.btn_register.setOnAction(new EventHandler<ActionEvent>() { 
		            @Override
		            public void handle(ActionEvent event) {
		            	
		            	if (!model.connected) {
		            	model.sl.getLogger().info("Sie sind mit keinem Server verbunden");
		            	return;
		            	}
		            	
		            	
		            
		            	/* prüfen ob name und pw vorhanden in feld, info etwas fehlt
		            	 * ->return
		            	 * prüfen ob file vorhanden und name existiert 
		            	 * -> user schon registirert, return
		            	 * -> user jetzt registriert, return
		            	 * 
		            	 *
		            	 */
		            	
		            }
		            });
		        
		                
		        
		        
		        view.btn_login.setOnAction(new EventHandler<ActionEvent>() { 
		            @Override
		            public void handle(ActionEvent event) {
		            	
		            	if (!model.connected) {
		            	model.sl.getLogger().info("Sie sind mit keinem Server verbunden");
		            	return;
		            	}
		            	
		            	/*prüfen ob user und pw eingetragen
		            	 * prüfen ob file vorhanden und name existiert  -> sie sind noch nicht registriret, return
		            	 *                                              -> sie sind registriet, pw inkorrekt, return
		            	 *  prüfen ob benutzer bereits auf server vorhanden -> benutzer ist bereits angemeldet, return
		            	 */                                                
		            	
		            	
		            	if (model.connected){
				    	Stage playingStage = new Stage();				
				        Client_View view2 = new Client_View(playingStage, model);
				        new Client_Controller(model, view2); 
				        view2.start();
				        view.stop();
		            	}  else {
		            		
		            		model.sl.getLogger().info("Sie sind mit keinem Server verbunden");
		            	}
		        
		        
			
		            }
		            });

		        
		        
		        
		        
		        
        
    }
    
}
