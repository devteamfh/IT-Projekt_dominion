package Dominion.Client.ClientClasses;

import java.io.IOException;

import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.Client.abstractClasses.Controller;
import Dominion.appClasses.CancelGame;
import Dominion.appClasses.ChatMessagePlayingStage;
import Dominion.appClasses.GameHistory;
import Dominion.appClasses.GameParty;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC), Joel Henz (events)
 */
public class Client_Controller_playingStage extends Controller<Client_Model, Client_View_playingStage> {
	private ServiceLocatorClient sl;
	private Client_View_playingStage view_playingStage;
       
    /**
     * @author Joel Henz
     * */
    public Client_Controller_playingStage(Client_Model model, Client_View_playingStage view, GameParty gameParty) {
        super(model, view);
        this.view_playingStage = view;
        sl = ServiceLocatorClient.getServiceLocator(); 
        
        sl.getButtonEndGameHost().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//first we have to get the current GameParty object
            	GameParty party = sl.getCurrentGameParty();
            	
            	//a host is able to end the game until the game isn't full. If a host ends the game, his playing stage will be closed. The same will happen to all other players who have joined the game.
            	//In addition, the game will be removed from the ListView of EVERY client.
            	CancelGame cancel = new CancelGame(party);
            	try {
					model.out.writeObject(cancel);
					model.out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	
            }
        });
        
        view.btn_sendChatMsgPlayingStage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	sendMessageToServer();    	
            }
        });
        
        view.action2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            	Croupier.getCroupier().setBuyMode(true);
            	Croupier.getCroupier().setBuyPowerCopper(10);
            }
        });
        
        
        sl.getButtonEndActions().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(sl.getPlayer_noOS().getNumberOfActions()==0){
            		sl.getButtonEndActions().setDisable(true);
                	sl.getButtonEndBuy().setDisable(false);
            	}
            	sl.getPlayer_noOS().decreaseNumberOfActions();
            	//sl.getNumberOfActionsProperty().set(sl.getPlayer_noOS().getNumberOfActions());
            	String text = sl.getPlayer_noOS().getUsername()+" macht eine Aktion\n";
            	GameHistory history = new GameHistory(text,sl.getCurrentGameParty(),sl.getPlayer_noOS(), GameHistory.HistoryType.EndAction);
            	
            	try {
        			model.out.writeObject(history);
        			model.out.flush();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            }
        });
        
        sl.getButtonEndBuy().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(sl.getPlayer_noOS().getNumberOfBuys()==0){
            		sl.getButtonEndBuy().setDisable(true);
            	}
            	sl.getButtonEndBuy().setDisable(true);
            	String text = sl.getPlayer_noOS().getUsername()+" macht einen Kauf\n";
            	GameHistory history = new GameHistory(text,sl.getCurrentGameParty(),sl.getPlayer_noOS(), GameHistory.HistoryType.EndBuy);
            	            	
            	try {
        			model.out.writeObject(history);
        			model.out.flush();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            }
        });  
        
    }
    
    
    
    
    
    
    
    protected void sendMessageToServer() {
		String name = model.getName();
		String msg = view.tf_messagePlayingStage.getText();
		ChatMessagePlayingStage cmsg = new ChatMessagePlayingStage(name, msg,sl.getCurrentGameParty());
		
		try {
			model.out.writeObject(cmsg);
			model.out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		view.tf_messagePlayingStage.clear();
		view.tf_messagePlayingStage.requestFocus();	
	}

	
}