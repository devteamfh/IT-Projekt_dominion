package Dominion.Server.ServerClasses;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import Dominion.ServiceLocator;
import Dominion.Server.abstractClasses.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;


/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC), Joel Henz (resources/events)
 */
public class Server_Controller extends Controller<Server_Model, Server_View> {
	private ServiceLocator sl;
	private Server_View view;
	private InetAddress addr;

    public Server_Controller(Server_Model model, Server_View view) {
        super(model, view);
        this.view = view;
        sl = ServiceLocator.getServiceLocator();

        // register ourselves to handle window-closing event
        view.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
            	
            	Platform.exit();       
            }
        });
        
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

				if(model.runServer(addr, portNr)){
                	model.listenToClient();
                }
			
            }

        });
        
    
    }

  
    
}
