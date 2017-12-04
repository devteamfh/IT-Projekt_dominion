package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.net.InetAddress;
import Dominion.Client.abstractClasses.Controller;
import Dominion.appClasses.PlayerWithOS;
import Dominion.appClasses.PlayerWithoutOS;
import Dominion.appClasses.StartInformation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



/**
 * MVC Pattern:
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC), Joel Henz (events), Beda Kaufmann (database, events)
 */
public class Client_Controller_start extends Controller<Client_Model, Client_View_start> {
    
	private Client_View_start view;
	private InetAddress addr;
    private String str_Dot = ".";
    private ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
    private checkUserData checkUserData;
    private checkFields checkFields;
   
    
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
         * edited @author kab: Neue Buttons btn_register und btn_login und diverse Pr�fmechanismen auf tf_ eingebaut (Klassen checkUserFields, checkUserData)
         */    

        checkUserData = new checkUserData();
        checkFields = new checkFields(this.model,this.checkUserData);
        
        	//connect button MouseHandler
		view.btn_connect.addEventHandler(MouseEvent.MOUSE_ENTERED, 
  		    new EventHandler<MouseEvent>() {
  		        @Override public void handle(MouseEvent e) {
  		        	view.btn_connect.getStyleClass().addAll("btn_view_hover");
  		        	view.btn_connect.getStyleClass().remove("btn_view");
  		        }
  		});
      		
  		view.btn_connect.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
  		        @Override public void handle(MouseEvent e) {
  		        	view.btn_connect.getStyleClass().remove("btn_view_hover");
  		        	view.btn_connect.getStyleClass().addAll("btn_view");
  		        }
  		});
      		    		
        view.btn_connect.setOnAction(new EventHandler<ActionEvent>() { 
            @Override
            public void handle(ActionEvent event) {  
          
            	//pr�ft ob Felder IP und Port ausgef�llt
            	//pr�ft ob Felder IP und Port ausgef�llt
            	checkFields.checkfields(view.btn_connect.toString(), view.tf_ip.getText(),view.tf_port.getText());
            	
            	//wenn IP und Port ausgef�llt, versuche mit Server zu verbinden
            	if (checkFields.getRdyToConnect())
            	
            	   	try { 
                		addr = InetAddress.getByName(view.tf_ip.getText());
                		int portNr = Integer.parseInt(view.tf_port.getText());
                    	//String name = view.tf_userName.getText();
                    	//model.setName(name);
        				model.connectToServer(addr,portNr);	
        				
        				if (model.connected){
        					
        		        	view.btn_connect.getStyleClass().removeAll("btn_view","btn_view_hover");            		        				
        					view.btn_connect.getStyleClass().addAll("btn_view_green");
							view.btn_connect.setBtnText("Verbunden!");

							view.tf_ip.setDisable(true);
							view.tf_port.setDisable(true);
							view.tf_ip.setStyle("-fx-opacity: 0.65;");
							view.tf_port.setStyle("-fx-opacity: 0.65;");
        		        }				
    			
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    					model.sl.getLogger().info("Verbindung mit Server nicht erfolgreich");
    					Client_View_start.lbl_errMsg.setText("Verbindung mit Server nicht erfolgreich");
    				}     		        
            }
        });  
      		     	
        
    	//register button MouseHandler     		
 		view.btn_register.addEventHandler(MouseEvent.MOUSE_ENTERED, 
      		    new EventHandler<MouseEvent>() {
      		        @Override public void handle(MouseEvent e) {
      		        	view.btn_register.getStyleClass().addAll("btn_view_hover");
      		        	view.btn_register.getStyleClass().remove("btn_view");
      		        }
      	});
          		
          		
  		view.btn_register.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
  		        @Override public void handle(MouseEvent e) {
  		        	view.btn_register.getStyleClass().remove("btn_view_hover");
  		        	view.btn_register.getStyleClass().addAll("btn_view");
  		        }
  		});
        
        view.btn_register.setOnAction(new EventHandler<ActionEvent>() { 
            @Override
            public void handle(ActionEvent event) {
            	
            	if (!model.connected) {
            	model.sl.getLogger().info("Sie sind mit keinem Server verbunden");
            	//Client_View_start.lbl_errMsg.setText("Sie sind mit keinem Server verbunden");
            	sl.setLbl_popUpMessage(new Label("Sie sind mit keinem Server verbunden"));
            	
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
            	
            	}         	else {
            			
	            	checkFields.checkfields(view.btn_register.toString(), view.tf_userName.getText(),view.tf_password.getText());
            		
            		
            		
	            	//wenn registiert, f�rbe button gr�n und deaktiviere inputfelder
	            	if (checkFields.getUserRegistred()) {
						view.btn_register.getStyleClass().removeAll("btn_view","btn_view_hover");
						view.btn_register.getStyleClass().addAll("btn_view_green");

						view.btn_register.setBtnText("Registriert!");
						view.tf_userName.setDisable(true);
						view.tf_password.setDisable(true);
						view.tf_userName.setStyle("-fx-opacity: 0.65;");
						view.tf_password.setStyle("-fx-opacity: 0.65;");
	            	}
	            	
            	}
            	
            }
         });


            	//login button MouseHandler

		view.btn_login.addEventHandler(MouseEvent.MOUSE_ENTERED,
      		    new EventHandler<MouseEvent>() {
      		        @Override public void handle(MouseEvent e) {
      		        	view.btn_login.getStyleClass().addAll("btn_view_hover");
      		        	view.btn_login.getStyleClass().remove("btn_view");
      		        }
      		});
              		           		
  		view.btn_login.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
  		        @Override public void handle(MouseEvent e) {
  		        	view.btn_login.getStyleClass().remove("btn_view_hover");
  		        	view.btn_login.getStyleClass().addAll("btn_view");
  		        }
  		});

  

        view.btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            	if (!model.connected) {
            	model.sl.getLogger().info("Sie sind mit keinem Server verbunden");
            	//Client_View_start.lbl_errMsg.setText("Sie sind mit keinem Server verbunden");
            	sl.setLbl_popUpMessage(new Label("Sie sind mit keinem Server verbunden"));
            	
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
            	
            	return;
            	}
            	            	
            	checkFields.checkfields(view.btn_login.toString(), view.tf_userName.getText(),view.tf_password.getText());
            	
            	            	
            	//Wenn PW falsch Felder zur�cksetzen
            	if(model.connected && !checkFields.getUserPwOk()){
            		view.tf_password.clear();
            		//view.tf_userName.clear();
            		view.tf_password.setDisable(false);
            		view.tf_userName.setDisable(false);
            		checkFields.setUserRegistred(false);
            		view.tf_userName.setStyle("-fx-opacity: 1;");
            		view.tf_password.setStyle("-fx-opacity: 1;");
            		view.btn_register.setBtnText("Registrieren");
            		view.btn_register.getStyleClass().remove("btn_view_green");
            		
            	}
            	
            	//weiter zur lobby, sofern connected, user registiert und pw ok
            	if (model.connected && checkFields.getUserPwOk()) {

	            	view.btn_login.getStyleClass().removeAll("btn_view", "btn_view_hover");
					view.btn_login.getStyleClass().addAll("btn_view_green");	
					view.btn_login.setBtnText("entering...");					
					
				
					view.tf_userName.setDisable(false);
            		String name = view.tf_userName.getText();
                	model.setName(name);
                	PlayerWithOS player_OS = new PlayerWithOS (name,model.getOutput());
                	sl.setPlayer_OS(player_OS);
                	
                	PlayerWithoutOS player_noOS = new PlayerWithoutOS (name);
                	sl.setPlayer_noOS(player_noOS);
                	
                	//the StartInformation object is needed on server-side so the server can store the username and statistics of all connected players
                	StartInformation current = new StartInformation(model.getName());
        			current.setPW			 (checkUserData.getAl_currentUserandStats().get(1));
        			current.setGamesPlayed	 (Integer.parseInt(checkUserData.getAl_currentUserandStats().get(2)));
        			current.setGamesWon		 (Integer.parseInt(checkUserData.getAl_currentUserandStats().get(3)));
        			current.setGamesLost     (Integer.parseInt(checkUserData.getAl_currentUserandStats().get(4)));
        			current.setWinLooseRatio (Integer.parseInt(checkUserData.getAl_currentUserandStats().get(5)));
        			current.setAtt6          (checkUserData.getAl_currentUserandStats().get(6));
        			current.setAtt7          (checkUserData.getAl_currentUserandStats().get(7));
        			current.setAtt8          (checkUserData.getAl_currentUserandStats().get(8));
        			current.setAtt9          (checkUserData.getAl_currentUserandStats().get(9));
                	
        			try {
						model.getOutput().writeObject(current);
						model.getOutput().flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	
			    	Stage lobby_stage = new Stage();				
			        Client_View_lobby view2 = new Client_View_lobby(lobby_stage, model); 
			        sl.setView_lobby(view2);
			        new Client_Controller_lobby(model, view2); 
			        view2.start();  
			        view.stop();
	            	}                                             	
    			
            }
        });

        // Button Close Style Hover und Action press
    	view.btn_close.addEventHandler(MouseEvent.MOUSE_ENTERED, 
      		    new EventHandler<MouseEvent>() {
      		        @Override public void handle(MouseEvent e) {
      		        	view.btn_close.getStyleClass().addAll("btn_close_hover");
      		        	view.btn_close.getStyleClass().remove("btn_close_normal");
      		        }
  		});
		      		
		      		
  		view.btn_close.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
  		        @Override public void handle(MouseEvent e) {
  		        	view.btn_close.getStyleClass().remove("btn_close_hover");
  		        	view.btn_close.getStyleClass().addAll("btn_close_normal");
  		        }
  		});
		        
        view.btn_close.setOnAction(new EventHandler<ActionEvent>() { 
            @Override
            public void handle(ActionEvent event) {
            
            	if(model.connected){
            	//disconnect from server	
            	}
            	
            view.stop();
         
            }
        });	                
    }
    
}
