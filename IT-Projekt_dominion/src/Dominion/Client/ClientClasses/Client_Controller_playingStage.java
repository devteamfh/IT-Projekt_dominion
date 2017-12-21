package Dominion.Client.ClientClasses;

import java.io.IOException;

import java.util.Collections;


import com.sun.glass.ui.View;

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
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
	private StringBuilder strBuilderTextArea = new StringBuilder();
	private StringBuilder strBuilderLabel = new StringBuilder();
       
    /**
     * @author Joel Henz
     * */
    public Client_Controller_playingStage(Client_Model model, Client_View_playingStage view, GameParty gameParty) {
        super(model, view);
        this.view_playingStage = view;
        sl = ServiceLocatorClient.getServiceLocator(); 
        croupier = Croupier.getCroupier();
        

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

        	//wenn host game schliesst
        	if (sl.getIsHost()){
        		
            	//first we have to get the current GameParty object
        		GameParty party = sl.getCurrentGameParty();
            	
            	//a host is able to end the game until the game isn't full. If a host ends the game, his playing stage will be closed. The same will happen to all other players who have joined the game.
            	//In addition, the game will be removed from the ListView of EVERY client.
            	CancelGame cancel = new CancelGame(party);

            	try {
            		sl.getPlayer_OS().getOut().reset();
            		sl.getPlayer_OS().getOut().writeObject(cancel);
            		sl.getPlayer_OS().getOut().flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        	}
        	
        	
        	//wenn nicht-host game schliesst
        	
        	
        	
        }
    });
    //-----------------------------------------------------------------------------------------------//    
        
        
        
        
        
    /*    sl.getButtonEndGameHost().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//first we have to get the current GameParty object
            	GameParty party = sl.getCurrentGameParty();
            	
            	//a host is able to end the game until the game isn't full. If a host ends the game, his playing stage will be closed. The same will happen to all other players who have joined the game.
            	//In addition, the game will be removed from the ListView of EVERY client.
            	CancelGame cancel = new CancelGame(party);

            	try {
					sl.getPlayer_OS().getOut().writeObject(cancel);
					sl.getPlayer_OS().getOut().flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	
            }
        });*/
        
		
		/// Chatbutton
        view.btn_sendChatMsgPlayingStage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	sendMessageToServer();    	
            }
        });
        
        
        
      //Button enterGame verhalten
    	view.btn_sendChatMsgPlayingStage.addEventHandler(MouseEvent.MOUSE_ENTERED, 
    		    new EventHandler<MouseEvent>() {
    		        @Override public void handle(MouseEvent e) {
    		        	view.btn_sendChatMsgPlayingStage.getStyleClass().addAll("btn_sendChatMsg_hover");
    		        	view.btn_sendChatMsgPlayingStage.getStyleClass().remove("btn_sendChatMsg");
    		        }
    		});
     		
    		view.btn_sendChatMsgPlayingStage.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
    		        @Override public void handle(MouseEvent e) {
    		        	view.btn_sendChatMsgPlayingStage.getStyleClass().remove("btn_sendChatMsg_hover");
    		        	view.btn_sendChatMsgPlayingStage.getStyleClass().addAll("btn_sendChatMsg");
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
       
        
        
        view.btn_userInteraction.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	GameHistory history;
            
            	switch (view.btn_userInteraction.getLbl().getText()) {
            	
            	case "Aktionen beenden":
						            		croupier.setActionMode(false);
						                	croupier.setBuyMode(true);
						                	
						                	croupier.setActions(0);
						                	
						                	if(strBuilderTextArea != null){
						                		strBuilderTextArea.delete(0, strBuilderTextArea.length());
						                	}
						                	
						                	if(strBuilderLabel != null){
						                		strBuilderLabel.delete(0, strBuilderLabel.length());
						                	}
						                	
						                	//sl.getButtonEndActions().setDisable(true);
						                	//sl.getButtonEndBuys().setDisable(false);
						                	
						                	strBuilderTextArea.append(model.getPlayer().getUsername()+" beendet Aktionsphase\n");
						                	strBuilderLabel.append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
						                	
						                	//we don't send a card here, so set it null
						                	history = new GameHistory(strBuilderTextArea.toString(), strBuilderLabel.toString(), sl.getCurrentGameParty(),model.getPlayer(),null,null, GameHistory.HistoryType.EndAction);
						
						                	try {
						                		//maybe reset needed because the GameParty object could have been changed (f.e. one player has left the game -> -1 player)
						                		//sl.getPlayer_OS().getOut().reset();
						                		sl.getPlayer_OS().getOut().writeObject(history);
						                		sl.getPlayer_OS().getOut().flush();
						            		} catch (IOException e) {
						            			// TODO Auto-generated catch block
						            			e.printStackTrace();
						            		}
						                	break;
					                	
					                	
            	case "Kaufen beenden": 
						            		if(strBuilderTextArea != null){
						                		strBuilderTextArea.delete(0, strBuilderTextArea.length());
						                	}
						                	
						                	croupier.setBuyMode(false);
						                	croupier.setBuys(0);
						                	//set also buy power = 0 in case the player uses treasure cards but doesn't buy anything
						                	croupier.setBuyPower(0);
						                	
						                	//sl.getButtonEndBuys().setDisable(true);
						
						            		strBuilderTextArea.append(model.getPlayer().getUsername()+" beendet Kaufphase\n\n");
						            		strBuilderLabel.append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
						                	
						            		
						            		//we will create the Label on playing stage later....because we first have to determine the next player in the sequence on server-side
						            		history = new GameHistory(strBuilderTextArea.toString(), strBuilderLabel.toString() ,sl.getCurrentGameParty(),model.getPlayer(),null,null, GameHistory.HistoryType.EndBuy);
						
						            		
						            		//Restliche Karten in h�nden werden auf ablagestapel gelegt
						            		//croupier.muckHoleCards();
						            		
						            		//es werden 5 neue Karten gezogen
						            		//croupier.drawHoleCards();
						            		
						            		croupier.removeHoleCards();
						            		
						            		
						                	try {
						                		//maybe reset needed because the GameParty object could have been changed (f.e. one player has left the game -> -1 player)
						                		//sl.getPlayer_OS().getOut().reset();
						                		sl.getPlayer_OS().getOut().writeObject(history);
						                		sl.getPlayer_OS().getOut().flush();
						            		} catch (IOException e) {
						            			// TODO Auto-generated catch block
						            			e.printStackTrace();
						            		}
						                	
						                	break;
						                	
            	case "Wegwerfen beenden":
            		
            		if(sl.getStrBuilderTextArea() != null){
                		sl.getStrBuilderTextArea().delete(0, sl.getStrBuilderTextArea().length());
                	}
            		
            		if(croupier.isTrashModeChapel()){
            			sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" beendet Wegwerfen\n");
            			sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
            			croupier.setTrashModeChapel(false);
						croupier.setTrashCounterModeChapel(0);
						if(croupier.getActions()==0){
							croupier.setBuyMode(true);
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" beendet Aktionsphase\n");
						}else{
							croupier.setActionMode(true);
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" hat noch weitere Aktionen\n");
						}
						
						history = new GameHistory(sl.getStrBuilderTextArea().toString(),null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.Trash);
						
						try {
							//sl.getPlayer_OS().getOut().reset();
							sl.getPlayer_OS().getOut().writeObject(history);
							sl.getPlayer_OS().getOut().flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
            		}
            		
            		break;
            		
            	case "Tauschen beenden":
            		
            		if(sl.getStrBuilderTextArea() != null){
                		sl.getStrBuilderTextArea().delete(0, sl.getStrBuilderTextArea().length());
                	}
            		
            		croupier.setDiscardMode(false);
					sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" beendet das Ablegen und darf "+croupier.getDiscrardCounter()+" Karten nachziehen\n");
					sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
					croupier.getNewHoleCards(croupier.getDiscrardCounter());
					croupier.setDiscardedCounter(0);
					
					if(croupier.getActions()==0){
						croupier.setBuyMode(true);
						sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" beendet Aktionsphase\n");
					}else{
						croupier.setActionMode(true);
						sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" hat noch weitere Aktionen\n");
					}
					
					history = new GameHistory(sl.getStrBuilderTextArea().toString(),null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.Discard);
					
					try {
						//sl.getPlayer_OS().getOut().reset();
						sl.getPlayer_OS().getOut().writeObject(history);
						sl.getPlayer_OS().getOut().flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            		break;
            		
            	}
            	
            	
            	
            }
            
        });
     
    
        
        
       
        
       /** sl.getButtonLeaveGamePlayer().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	if(strBuilder != null){
            		strBuilder.delete(0, strBuilder.length());
            	}
            	
            	sl.getButtonLeaveGamePlayer().setDisable(true);
            	strBuilder.append(sl.getPlayer_noOS().getUsername()+" verlässt das Spiel\n\n");
            	//here we set current_player of the playing stage as null so we don't overwrite the current_player saved on server-side (instance variable in class ClientHandler)
            	GameHistory history;
            	if(sl.getButtonEndActions().isDisabled() && sl.getButtonEndBuys().isDisabled()){
            		//here we set current player as null
            		history = new GameHistory (strBuilder.toString(),sl.getCurrentGameParty(),null,GameHistory.HistoryType.LeaveGame);
            		history.setID();
                	history.setLeavingPlayer(sl.getPlayer_noOS());
            	}else{
            		//the leaving player is also the current player
            		history = new GameHistory (strBuilder.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),GameHistory.HistoryType.LeaveGame);
            		//history.setID();
                	history.setLeavingPlayer(sl.getPlayer_noOS());
            	}
           	
            	try {
            		sl.getPlayer_OS().getOut().writeObject(history);
            		sl.getPlayer_OS().getOut().flush();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            	
            }
        }); */ 
        
        
        
        
    }

    protected void sendMessageToServer() {
		String name = model.getName();
		String msg = view.tf_messagePlayingStage.getText();
		ChatMessagePlayingStage cmsg = new ChatMessagePlayingStage(name, msg,sl.getCurrentGameParty());
		//cmsg.setID();
		
		try {
			sl.getPlayer_OS().getOut().writeObject(cmsg);
			sl.getPlayer_OS().getOut().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		view.tf_messagePlayingStage.clear();
		view.tf_messagePlayingStage.requestFocus();	
	}
    
    

	
}

