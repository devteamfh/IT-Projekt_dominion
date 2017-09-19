package Dominion.Client.ClientClasses;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
    ServiceLocator serviceLocator;
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
         */    
        view.connect.setOnAction(new EventHandler<ActionEvent>() { 
            @Override
            public void handle(ActionEvent event) {
            	try {
					addr = InetAddress.getByName(view.ip.getText());
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	int portNr = Integer.parseInt(view.port.getText());
            	String name = view.insertName.getText();
            	model.setName(name);
				model.connectToServer(addr,portNr);				
				Stage playingStage = new Stage();				
		        Client_View view2 = new Client_View(playingStage, model);
		        new Client_Controller(model, view2);
		        
		        if (model.connected){
		        	view2.start();
			        view.stop();
		        }				
            }
        });        
    }
    
}
