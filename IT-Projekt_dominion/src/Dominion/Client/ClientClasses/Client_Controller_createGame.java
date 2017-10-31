package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import Dominion.ServiceLocator;
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
 * @author Brad Richards (MVC), Joel Henz (events)
 */
public class Client_Controller_createGame extends Controller<Client_Model, Client_View_createGame> {
    ServiceLocatorClient sl;
    Client_View_createGame view2;
       
    /**
     * @author Joel Henz
     * */
    public Client_Controller_createGame(Client_Model model, Client_View_createGame view) {
        super(model, view);
        this.view2 = view;
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
        
        view.endOfGame2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	view.sl.getTextFieldForRounds().setDisable(false);
            	sl.setSelectedMode("rounds");
            }
        });
        
        view.endOfGame1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	view.sl.getTextFieldForRounds().clear();
            	view.sl.getTextFieldForRounds().setDisable(true);
            	sl.setSelectedMode("province");
            }
        });
        
        view.twoPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	sl.setNumberOfPlayer(2);
            }
        });
        
        view.threePlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	sl.setNumberOfPlayer(3);
            }
        });
        
        view.fourPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	sl.setNumberOfPlayer(4);
            }
        });
        
        view.cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	view2.stop();
            }
        });
        
        view.finish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	String selectedMode = sl.getSelectedMode();
            	int numberOfPlayers = sl.getNumberOfPlayers();
            	String creator = model.playerName;
            	
            	GameParty newParty = new GameParty(selectedMode,creator,numberOfPlayers);
            	newParty.setID();
            	
            	GameObject obj = new GameObject (GameObject.ObjectType.GameParty);
            	obj.setID();
            	
            	obj=newParty;
            	try {
					model.out.writeObject(obj);
					model.out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	view2.stop();
            			
            			
            }
        });
        
    }

	
}
