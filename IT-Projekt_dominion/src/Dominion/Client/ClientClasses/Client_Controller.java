package Dominion.Client.ClientClasses;

import java.io.IOException;
import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.Controller;
import Dominion.appClasses.ChatMessage;
import Dominion.appClasses.GameObject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;

/**
 * MVC Pattern:
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class Client_Controller extends Controller<Client_Model, Client_View> {
    ServiceLocator serviceLocator;
    Client_View view;
       
    public Client_Controller(Client_Model model, Client_View view) {
        super(model, view);
        this.view = view;
               
        /**
         * @author Joel Henz
         * register ourselves to handle window-closing event
         * interrupts all client threads and closes all client sockets
         * */
        view.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
            	model.t1.interrupt();
               
                try {
					model.client_chat.close();				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              
                Platform.exit();
            }
        });
        
        /**
         * @author Joel Henz:
         * action event for the chat program (sending the message by clicking on "Senden" button)
         * */
        view.send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sendMessageToServer();
            }
        });
        
        /**
         * @author Joel Henz
         * key event for the chat program (sending the chat message by pressing enter)
         * */
        view.scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	if (event.getCode().equals(KeyCode.ENTER)){
            		sendMessageToServer();	
            	}
            }
        });  
    }
       
    /**
     * @author Joel Henz: 
     * method for sending the chat messages
     * */
	protected void sendMessageToServer() {
		String name = model.getName();
		String msg = view.tf_message.getText();
		ChatMessage cmsg = new ChatMessage(name, msg);
		cmsg.setID();
		
		GameObject obj = new GameObject (GameObject.ObjectType.ChatMessage);
		obj.setID();
		obj = cmsg;
		
		try {
			model.out.writeObject(obj);
			model.out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		view.tf_message.clear();
		view.tf_message.requestFocus();	
	}
	
}
