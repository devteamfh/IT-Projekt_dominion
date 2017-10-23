package Dominion.Client.ClientClasses;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.sun.glass.ui.View;
import com.sun.media.jfxmedia.logging.Logger;

import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.Controller;
import Dominion.Client.abstractClasses.Model;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
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
         * edited @author kab: Neue Buttons btn_register und btn_login und diverse Prï¿½fmechanismen auf tf_ eingebaut
         */    
        
        	//connect button MouseHandler
     		view.btn_connect.addEventHandler(MouseEvent.MOUSE_ENTERED, 
      		    new EventHandler<MouseEvent>() {
      		        @Override public void handle(MouseEvent e) {
      		        	view.btn_connect.getStyleClass().addAll("btn_view_hover");
      		        	view.btn_connect.getStyleClass().remove("btn_view");
      		        }
      		});
      		
      		//
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
            					view.btn_connect.getStyleClass().addAll("btn_view_verbunden");
            		        }				
        			
        				} catch (Exception e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        					model.sl.getLogger().info("Verbindung mit Server nicht erfolgreich");
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
          		
          		//
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
		            	}         	else {

			            	checkFields.getInstance().checkfields(view.btn_register.toString(), view.tf_userName.getText(),view.tf_password.getText());

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
              		
              		//
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
    		            	return;
    		            	}
    		            	
    		            	checkFields.getInstance().checkfields(view.btn_login.toString(), view.tf_userName.getText(),view.tf_password.getText());

    		            	
    		            	if (model.connected && checkFields.getInstance().getUserPwOk()){
    		            		String name = view.tf_userName.getText();
    	                    	model.setName(name);
    					    	Stage playingStage = new Stage();				
    					        Client_View view2 = new Client_View(playingStage, model);
    					        new Client_Controller(model, view2); 
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
		      		
		      		//
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
