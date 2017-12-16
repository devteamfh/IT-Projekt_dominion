package Dominion.Client.ClientClasses;

import Dominion.Client.abstractClasses.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC), Joel Henz (events), kab
 */
public class Client_Controller_popUp extends Controller<Client_Model, Client_View_popUp> {
      ServiceLocatorClient sl;
    /**
     * @author Joel Henz, kab
     * */
      
      
    public Client_Controller_popUp(Client_Model model, Client_View_popUp view) {
        super(model, view);
        sl = ServiceLocatorClient.getServiceLocator();

        
        
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
            	view.stop();
            	
            }
        });
        
        //Button btn_ok verhalten (create game)
        
    	view.btn_ok.addEventHandler(MouseEvent.MOUSE_ENTERED, 
    		    new EventHandler<MouseEvent>() {
    		        @Override public void handle(MouseEvent e) {
    		        	view.btn_ok.getStyleClass().addAll("btn_sendChatMsg_hover");
    		        	view.btn_ok.getStyleClass().remove("btn_sendChatMsg");
    		        }
    		});
     		
    		view.btn_ok.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
    		        @Override public void handle(MouseEvent e) {
    		        	view.btn_ok.getStyleClass().remove("btn_sendChatMsg_hover");
    		        	view.btn_ok.getStyleClass().addAll("btn_sendChatMsg");
    		        }
    		});
        
    		
    		view.btn_ok.setOnAction(new EventHandler<ActionEvent>() {
			            @Override
				            public void handle(ActionEvent event) {
				
				                    	view.stop();
				                    	 
	                    	
				            }
                
                });
        
        
  
    }
	
}