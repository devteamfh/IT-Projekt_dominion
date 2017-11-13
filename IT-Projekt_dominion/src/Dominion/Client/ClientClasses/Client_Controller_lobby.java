package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import Dominion.Client.abstractClasses.Controller;
import Dominion.appClasses.ChatMessageLobby;
import Dominion.appClasses.GameObject;
import Dominion.appClasses.GameParty;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC), Joel Henz (Events)
 */
public class Client_Controller_lobby extends Controller<Client_Model, Client_View_lobby> {
    ServiceLocatorClient sl;
    Client_View_lobby view;
    Client_View_createGame view2;
       
    public Client_Controller_lobby(Client_Model model, Client_View_lobby view) {
        super(model, view);
        this.view = view;
        sl = ServiceLocatorClient.getServiceLocator();
               
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
					model.client.close();				
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
        view.btn_send.setOnAction(new EventHandler<ActionEvent>() {
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
        
        /**
         * @author Joel Henz
         *event for ListView mouse clicks
         * */
       sl.getListView().setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if(!sl.getListView().getSelectionModel().isEmpty()){
					view.btn_enterGame.setDisable(false);
					
					GameParty newGAME = sl.getListView().getSelectionModel().getSelectedItem();
					System.out.println(newGAME);
					
	                //ObservableList<GameParty> text = view.sl.getListView().getSelectionModel().getSelectedItems();
	            
	               /** Iterator<GameParty> iter = text.iterator();
	                
	                while (iter.hasNext()){
	                	GameParty newGame = iter.next();
	                	System.out.println(newGame.toString());
	                }*/
				}
				
			}
		});
       
       // Button-Close Style "Hover" und Action "press"
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
           
           	//if(model.connected){
           	//disconnect from server	
           	//}
           	
           view.stop();
           	 	
           }
           });
   	
       
       
       
       /**view.enterGame.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
           	GameParty joinGame = sl.getListView().getSelectionModel().getSelectedItem();
           	createNewGame.initModality(Modality.APPLICATION_MODAL);
               
               view2 = new Client_View_createGame(createNewGame, model);
               new Client_Controller_createGame(model, view2);

               view2.start();
           }
       });*/
        
        
        /**
         * @author Joel Henz
         *opens a new stage for creating the configs of a new game
         * */
        view.btn_newGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	Stage createNewGame = new Stage();
            	createNewGame.initModality(Modality.APPLICATION_MODAL);
                
                view2 = new Client_View_createGame(createNewGame, model);
                new Client_Controller_createGame(model, view2);

                view2.start();
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
		ChatMessageLobby cmsg = new ChatMessageLobby(name, msg);
		cmsg.setID();
		
		GameObject obj = new GameObject (GameObject.ObjectType.ChatMessageLobby);
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



