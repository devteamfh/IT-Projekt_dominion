package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.sun.glass.ui.View;
import com.sun.media.jfxmedia.logging.Logger;

import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.Controller;
import Dominion.Client.abstractClasses.Model;
import Dominion.appClasses.GameObject;
import Dominion.appClasses.Player;
import Dominion.appClasses.StartInformation;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;



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
         * edited @author kab: Neue Buttons btn_register und btn_login und diverse Prüfmechanismen auf tf_ eingebaut
         */    
        
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
          
            	//prüft ob Felder IP und Port ausgefüllt
            	checkFields.getInstance().checkfields(view.btn_connect.toString(), view.tf_ip.getText(),view.tf_port.getText());
            	
            	//wenn IP und Port ausgefüllt, versuche mit Server zu verbinden
            	if (checkFields.getInstance().getRdyToConnect())
            	
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
            	Client_View_start.lbl_errMsg.setText("Sie sind mit keinem Server verbunden");
            	}         	else {

	            	checkFields.getInstance().checkfields(view.btn_register.toString(), view.tf_userName.getText(),view.tf_password.getText());

	            	//wenn registiert, färbe button grün und deaktiviere inputfelder
	            	if (checkFields.getInstance().getUserRegistred()) {
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
            	Client_View_start.lbl_errMsg.setText("Sie sind mit keinem Server verbunden");
            	return;
            	}

            	checkFields.getInstance().checkfields(view.btn_login.toString(), view.tf_userName.getText(),view.tf_password.getText());
            	
            	//Wenn PW falsch Felder zurücksetzen
            	if(model.connected && !checkFields.getInstance().getUserPwOk()){
            		view.tf_password.clear();
            		view.tf_userName.clear();
            		view.tf_password.setDisable(false);
            		view.tf_userName.setDisable(false);
            		checkFields.getInstance().setUserRegistred(false);
            		view.tf_userName.setStyle("-fx-opacity: 1;");
            		view.tf_password.setStyle("-fx-opacity: 1;");
            		view.btn_register.setBtnText("Registrieren");
            		view.btn_register.getStyleClass().remove("btn_view_green");
            		
            	}
            	
            	//weiter zur lobby, sofern connected, user registiert und pw ok
            	if (model.connected && checkFields.getInstance().getUserPwOk()) {

	            	view.btn_login.getStyleClass().removeAll("btn_view", "btn_view_hover");
					view.btn_login.getStyleClass().addAll("btn_view_green");	
					view.btn_login.setBtnText("entering...");					
					
				
					view.tf_userName.setDisable(false);
            		String name = view.tf_userName.getText();
                	model.setName(name);
                	Player player = new Player (name,model.getOutput());
                	model.setPlayer(player);
                	
                	//the StartInformation object is needed on server-side so the server can store the username of all connected players
                	StartInformation current = new StartInformation(model.getName());
        			
        			try {
						model.getOutput().writeObject(current);
						model.getOutput().flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	
			    	Stage lobby_stage = new Stage();				
			        Client_View_lobby view2 = new Client_View_lobby(lobby_stage, model);
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
