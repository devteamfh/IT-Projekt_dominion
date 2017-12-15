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
import javafx.scene.input.MouseEvent;

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
        	view.stop();
        	
        }
    });
    //-----------------------------------------------------------------------------------------------//    
        
        
        
        
        
        sl.getButtonEndGameHost().setOnAction(new EventHandler<ActionEvent>() {
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
            	croupier.setActionMode(false);
            	croupier.setBuyMode(true);
            	croupier.setBuys(3);
            
            	System.out.println(croupier.getHoleCards().size());

            }
            
          
           
            
            
        });
        view.provisorischCard2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	croupier.setBuyMode(false);
            	croupier.setActionMode(true);
            	croupier.setActions(3);
            	
            	
            }
        });
       
        view.provisorischCard3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	croupier.setBuyPower();
            	croupier.setBuys(5);
            	croupier.setActions(5);
            	croupier.muckHoleCards();
            	croupier.drawHoleCards();
            	

            }
        });
     

        
        
        /**sl.getButtonEndActions().setOnAction(new EventHandler<ActionEvent>() {
        	
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
            		model.out.reset();//reset necessary for correct number of actions
        			model.out.writeObject(history); 
        			model.out.flush();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            }
        });*/
        
        /**sl.getButtonPlayBuy().setOnAction(new EventHandler<ActionEvent>() {
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
            		//model.out.reset();
        			model.out.writeObject(history);
        			model.out.flush();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            }
        });*/
        
        sl.getButtonEndActions().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	croupier.setActionMode(false);
            	croupier.setBuyMode(true);
            	
            	croupier.setActions(0);
            	
            	if(strBuilderTextArea != null){
            		strBuilderTextArea.delete(0, strBuilderTextArea.length());
            	}
            	
            	if(strBuilderLabel != null){
            		strBuilderLabel.delete(0, strBuilderLabel.length());
            	}
            	
            	sl.getButtonEndActions().setDisable(true);
            	sl.getButtonEndBuys().setDisable(false);
            	
            	strBuilderTextArea.append(sl.getPlayer_noOS().getUsername()+" beendet Aktionsphase\n");
            	strBuilderLabel.append("an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
            	
            	//we don't send a card here, so set it null
            	GameHistory history = new GameHistory(strBuilderTextArea.toString(), strBuilderLabel.toString(), sl.getCurrentGameParty(),sl.getPlayer_noOS(),null, GameHistory.HistoryType.EndAction);

            	try {
            		//maybe reset needed because the GameParty object could have been changed (f.e. one player has left the game -> -1 player)
            		//sl.getPlayer_OS().getOut().reset();
            		sl.getPlayer_OS().getOut().writeObject(history);
            		sl.getPlayer_OS().getOut().flush();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            }
        });
        
        sl.getButtonEndBuys().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	if(strBuilderTextArea != null){
            		strBuilderTextArea.delete(0, strBuilderTextArea.length());
            	}
            	
            	croupier.setBuyMode(false);
            	croupier.setBuys(0);
            	//set also buy power = 0 in case the player uses treasure cards but doesn't buy anything
            	croupier.setBuyPower(0);
            	
            	sl.getButtonEndBuys().setDisable(true);

        		strBuilderTextArea.append(sl.getPlayer_noOS().getUsername()+" beendet Kaufphase\n\n");
        		
        		//we will create the Label on playing stage later....because we first have to determine the next player in the sequence on server-side
        		GameHistory history = new GameHistory(strBuilderTextArea.toString(), null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),null, GameHistory.HistoryType.EndBuy);

            	try {
            		//maybe reset needed because the GameParty object could have been changed (f.e. one player has left the game -> -1 player)
            		//sl.getPlayer_OS().getOut().reset();
            		sl.getPlayer_OS().getOut().writeObject(history);
            		sl.getPlayer_OS().getOut().flush();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
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