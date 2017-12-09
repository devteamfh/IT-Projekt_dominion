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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
	private Croupier croupier;
	private Client_View_playingStage view_playingStage;
	private StringBuilder strBuilder = new StringBuilder();
       
    /**
     * @author Joel Henz
     * */
    public Client_Controller_playingStage(Client_Model model, Client_View_playingStage view, GameParty gameParty) {
        super(model, view);
        this.view_playingStage = view;
        sl = ServiceLocatorClient.getServiceLocator(); 
        croupier = Croupier.getCroupier();
        
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

        view.provisorischCard1.setOnAction(new EventHandler<ActionEvent>() {
        	
        	
        	
        	
        	
        	
            @Override
            public void handle(ActionEvent event) {

            	croupier.setBuyMode(true);
            	croupier.setBuyPower();
            	System.out.println(croupier.getBuyPower());
            	

            }
            
          
            
            
            
            
            
        });
        view.provisorischCard2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            	croupier.setBuyMode(true);
            	croupier.setBuyPower(0);
            	System.out.println(croupier.getBuyPower());
            }
        });
       
        view.provisorischCard3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            
            }
        });
     

        
        
        sl.getButtonEndActions().setOnAction(new EventHandler<ActionEvent>() {
        	
            @Override
            public void handle(ActionEvent event) {
            	
            	croupier.setActionMode(false);
            	croupier.setBuyMode(true);
            	
            	if(strBuilder != null){
            		strBuilder.delete(0, strBuilder.length());
            	}

            	
            	
            	GameHistory history;
            	sl.getPlayer_noOS().decreaseNumberOfActions();
            	
            	
            	
            	if(sl.getPlayer_noOS().getNumberOfActions()==0){
            		sl.getButtonPlayActions().setDisable(true);
            		sl.getButtonEndActions().setDisable(true);
                	sl.getButtonPlayBuy().setDisable(false);
                	sl.getButtonEndBuys().setDisable(false);
                	strBuilder.append(sl.getPlayer_noOS().getUsername()+" macht eine Aktion\n");
                	strBuilder.append(sl.getPlayer_noOS().getUsername()+" beendet Aktionsphase\n");
                	history = new GameHistory(strBuilder.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(), GameHistory.HistoryType.EndAction);
            	}else{
            		strBuilder.append(sl.getPlayer_noOS().getUsername()+" macht eine Aktion\n");
            		history = new GameHistory(strBuilder.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(), GameHistory.HistoryType.PlayAction);
            	}
            
                     	
            	try {
            		model.out.reset();
        			model.out.writeObject(history); //reset necessary for correct number of actions
        			model.out.flush();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            }
        });
        
        sl.getButtonPlayBuy().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	

            	
            	if(strBuilder != null){
            		strBuilder.delete(0, strBuilder.length());
            	}
           
            	GameHistory history;
            	sl.getPlayer_noOS().decreaseNumberOfBuys();
            	
            	if(sl.getPlayer_noOS().getNumberOfBuys()==0){
            		sl.getButtonPlayBuy().setDisable(true);
            		sl.getButtonEndBuys().setDisable(true);
            		strBuilder.append(sl.getPlayer_noOS().getUsername()+" macht einen Kauf\n");
            		strBuilder.append(sl.getPlayer_noOS().getUsername()+" beendet Kaufphase\n");
            		history = new GameHistory(strBuilder.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(), GameHistory.HistoryType.EndBuy);
            		
            		//setting the initial numbers of actions and buys so the player will start with 1 action and 1 buy when his turn will start again in the next round
            		sl.getPlayer_noOS().setInitialActionsAndBuys();
            	}else{
            		strBuilder.append(sl.getPlayer_noOS().getUsername()+" macht einen Kauf\n");
            		history = new GameHistory(strBuilder.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(), GameHistory.HistoryType.PlayBuy);
            	}

            	try {
            		model.out.reset();
        			model.out.writeObject(history);
        			model.out.flush();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            }
        });
        
        sl.getButtonEndActions().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	croupier.setActionMode(false);
            	croupier.setBuyMode(true);
            	
            	if(strBuilder != null){
            		strBuilder.delete(0, strBuilder.length());
            	}
            	
            	GameHistory history;
            	sl.getPlayer_noOS().actionEnded();
            	sl.getButtonPlayActions().setDisable(true);
            	sl.getButtonEndActions().setDisable(true);
            	sl.getButtonPlayBuy().setDisable(false);
            	sl.getButtonEndBuys().setDisable(false);
            	strBuilder.append(sl.getPlayer_noOS().getUsername()+" beendet Aktionsphase\n");
            	history = new GameHistory(strBuilder.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(), GameHistory.HistoryType.EndAction);

            	try {
            		model.out.reset();
        			model.out.writeObject(history);
        			model.out.flush();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            }
        });
        
        sl.getButtonEndBuys().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	if(strBuilder != null){
            		strBuilder.delete(0, strBuilder.length());
            	}
            	
            	GameHistory history;
            	sl.getPlayer_noOS().buyEnded();
            	sl.getButtonPlayBuy().setDisable(true);
        		sl.getButtonEndBuys().setDisable(true);
        		strBuilder.append(sl.getPlayer_noOS().getUsername()+" beendet Kaufphase\n");
        		history = new GameHistory(strBuilder.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(), GameHistory.HistoryType.EndBuy);

            	try {
            		model.out.reset();
        			model.out.writeObject(history);
        			model.out.flush();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            	//setting the initial numbers of actions and buys so the player will start with 1 action and 1 buy when his turn will start again in the next round
            	sl.getPlayer_noOS().setInitialActionsAndBuys();
            }
        });
        
        sl.getButtonEndGamePlayer().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	if(strBuilder != null){
            		strBuilder.delete(0, strBuilder.length());
            	}
            	
            	sl.getButtonEndGamePlayer().setDisable(true);
            	strBuilder.append(sl.getPlayer_noOS().getUsername()+" verlässt das Spiel\n");
            	GameHistory history = new GameHistory (strBuilder.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),GameHistory.HistoryType.LeaveGame);
            	history.setLeavingPlayer(sl.getPlayer_noOS());
            	
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