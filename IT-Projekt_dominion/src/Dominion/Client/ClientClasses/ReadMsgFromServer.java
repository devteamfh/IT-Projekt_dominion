package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JOptionPane;

import Dominion.Client.Client;
import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.Server.ServerClasses.GamePartyOnServer;
import Dominion.appClasses.CancelGame;
import Dominion.appClasses.ChatMessageLobby;
import Dominion.appClasses.ChatMessagePlayingStage;
import Dominion.appClasses.GameHistory;
import Dominion.appClasses.GameObject;
import Dominion.appClasses.GameParty;
import Dominion.appClasses.JoinGameParty;
import Dominion.appClasses.PlayerWithoutOS;
import Dominion.appClasses.StartInformation;
import Dominion.appClasses.UpdateLobby;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Joel Henz: 
 * this is the class for receiving chat messages from the server. If a client sends a chat message to server, the server will send that message to all clients
 * */
public class ReadMsgFromServer implements Runnable {
	ObjectInputStream in;
	ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
	Croupier croupier = Croupier.getCroupier();
	ChatMessageLobby msg;
	Client_Model model;
	
	public ReadMsgFromServer (ObjectInputStream in, Client_Model model){
		this.in = in;	
		this.model=model;
	}

	@Override
	public void run() {

		GameObject obj;
		
		try {
			while ((obj = (GameObject) this.in.readObject()) !=null){

				switch (obj.getType()) {
				
				case ChatMessageLobby:
					ChatMessageLobby msg = (ChatMessageLobby) obj;					 
					String nameLobby = msg.getName();
					String textLobby = msg.getMsg();
					sl.getTextAreaChatLobby().appendText(nameLobby+": "+textLobby+"\n");
					sl.getTextAreaChatLobby().selectPositionCaret(sl.getTextAreaChatLobby().getText().length());	
					break;
				 
				case InformationObject:
					break;
				
					/**
					 * @author kab: handles incoming StartInformation with player statistics and sends to  tbl_playerStats in the bloody lobby
					 */
				case StartInformation:
					
					StartInformation playerStatistics = (StartInformation) obj;
					
					//wenn bereits ein Spieler mit dem glelichen Benutzernamen existiert, wird kein Eintritt in die Lobby gew�hrt
					if(playerStatistics.isBol_nameTaken()){
						
						Platform.runLater(new Runnable() {
							@Override 
					           public void run() {
								try{
				        	
				        	//verhindert das starten der lobby
			 				sl.getView_lobby().stop();
			 				
			 				//Restartet die lgoin view
			 				Stage stge_start = new Stage();
			 				Client_View_start view_start = new Client_View_start (stge_start, model);
			 				new Client_Controller_start(model, view_start);
			 				view_start.start();
			 				
			 				//gibt Meldung aus, dass die Username-Dieberei stattgefunden hat
							sl.setLbl_popUpMessage(new Label("Der Spielername ist zur Zeit vergeben."));		
							Stage popUp = new Stage();	
							popUp.setResizable(false);
							popUp.initModality(Modality.APPLICATION_MODAL);
				        	Client_View_popUp view = new Client_View_popUp (popUp, model);
				        	new Client_Controller_popUp(model, view); 
				        	view.start();
			 				
								}catch (NullPointerException e){
								}
								
							}
					      });
					
						playerStatistics = null;
						model.client.close();
					}
							
				
					
					sl.getAl_Statistics().clear();
					sl.getTbl_playerStats().getItems().clear();
		

					sl.add_AL_Statistics(playerStatistics.getListOfStartInformationObjects());
					sl.getTbl_playerStats().getItems().addAll(sl.getAl_Statistics());
				
					break;
					 
				case GameParty:
					GameParty newGame = (GameParty) obj;
					 
					Platform.runLater(new Runnable() {
				           @Override 
				           public void run() {
				            sl.addNewGame(newGame);
				            
				            if(newGame.getHost().getUsername().equals(model.getName())){
				            	sl.setCurrentGameParty(newGame);
				            	Stage playingStage = new Stage();			
				            	playingStage.initModality(Modality.APPLICATION_MODAL);
				            	sl.setIsHost(true);
						        Client_View_playingStage view_playingStage = new Client_View_playingStage (playingStage, model,newGame);
						        sl.setView_playingStage(view_playingStage);
						        new Client_Controller_playingStage(model, sl.getPlayingStage(), newGame); 
						        sl.getPlayingStage().start();
						        sl.getCreateGameView().stop();
						        
						        //adding the name of the host on the playing Stage and add the current GameParty to the ServiceLocatorCLient of the host
						        for(int i=0; i<newGame.getArrayListOfPlayers().size();i++){
						        	Label label = new Label(sl.getCurrentGameParty().getArrayListOfPlayers().get(i).getUsername());
						        	Label allPlayer = new Label("Spieler dieser Partie:");
									sl.getPlayingStage().vb_player.getChildren().addAll(allPlayer, label);
						        }
							}
				           }
				       });
					break;
				
				//updating the ListView of this client when he logs in (he needs to see all open games when he enters the lobby)	
				case UpdateLobby:
					
					UpdateLobby toUpdate = (UpdateLobby) obj;
					
					Iterator<GameParty> iter = toUpdate.getListOfOpenGames().iterator();
						Platform.runLater(new Runnable() {
					           @Override 
					           public void run() {
					        	   
					        	   while (iter.hasNext()){
					        		   sl.addNewGame(iter.next());
					        	   }
					           }

					       });	
					break;
			
				case JoinGameParty:
					JoinGameParty join=(JoinGameParty) obj;
					
					
					
					//updating the current GameParty object in the ServiceLocator for all players who have already joined this GameParty
					for (int i=0; i<join.getSelectedGameParty().getArrayListOfPlayers().size();i++){						
						try{
							if(join.getSelectedGameParty().getArrayListOfPlayers().get(i).getUsername().equals(model.getName())){
								sl.setCurrentGameParty(join.getSelectedGameParty());
							}							
						}catch (NullPointerException e){
							//NullPointerException caught
						}				
					}
					
					
					Platform.runLater(new Runnable() {

						@Override 
				           public void run() {	
							
							//determine the joining player and create his playing stage
							if(join.getUsername().equals(model.getName())){
								
								//setting the host as current player of the playing stage. We need this to save in case we will leave the party (new instance of a GameHistory object
								//where we will pass on the current player as parameter)
								sl.setCurrentPlayer_noOS_ofPlayingStage(sl.getCurrentGameParty().getHost());
								
								Stage playingStage = new Stage();			
					        	playingStage.initModality(Modality.APPLICATION_MODAL);
					        	sl.setIsHost(false);
					        	Client_View_playingStage view = new Client_View_playingStage (playingStage, model,join.getSelectedGameParty());
					        	sl.setView_playingStage(view);
					        	new Client_Controller_playingStage(model, sl.getPlayingStage(),join.getSelectedGameParty()); 
					        	sl.getPlayingStage().start();
							}
							
							//determine the players who have already joined the game. On their playing stages we have to add the username of the new player
							try{
								if(join.getSelectedGameParty().getID() == sl.getCurrentGameParty().getID()){
									sl.getPlayingStage().vb_player.getChildren().clear();	
									Label allPlayer = new Label("Spieler dieser Partie:");
									sl.getPlayingStage().vb_player.getChildren().add(allPlayer);
									
									for(int i =0; i<join.getSelectedGameParty().getArrayListOfPlayers().size();i++){
										Label label = new Label(join.getSelectedGameParty().getArrayListOfPlayers().get(i).getUsername());
										sl.getPlayingStage().vb_player.getChildren().add(label);
									}	
								}
							}catch (NullPointerException e){
								//NullPointerException caught for sl.getCurrentGameParty().getID()
							}
							
							//this method will update the number of joined players of this game on the ListView (Lobby) of EACH client. If the game is full, it will be removed from the ListView of every client
							sl.updateGameParty(join);
							
							
							
						}
				       });
									
					break;
				
					
				case CancelGame:
					CancelGame cancelObject = (CancelGame) obj;
					
					GameParty gamePartyToCancel = cancelObject.getGameParty();
					
					Platform.runLater(new Runnable() {

						@Override 
				           public void run() {
							
							try{
								//stop the playing stage ONLY for the players who have joined this GameParty
								if(gamePartyToCancel.getID() == sl.getCurrentGameParty().getID()){
									sl.setLbl_popUpMessage(new Label("Der Host hat das Spiel beendet."));
									sl.getPlayingStage().stop();
									sl.setCurrentGameParty(null);
									Stage popUp = new Stage();	
									popUp.setResizable(false);
									popUp.initModality(Modality.APPLICATION_MODAL);
						        	Client_View_popUp view = new Client_View_popUp (popUp, model);
						        	new Client_Controller_popUp(model, view); 
						        	view.start();
								}
							}catch (NullPointerException e){
								//NullPointerException caught
							}
							
							//remove the canceled game from the ListView of ALL clients
							sl.removeGame(gamePartyToCancel);
							
				           }
				      });
					
					break;
				
				case ChatMessagePlayingStage:			 	
					ChatMessagePlayingStage msg_obj = (ChatMessagePlayingStage) obj;
					
					String namePlayingStage = msg_obj.getName();
					String textPlayingStage = msg_obj.getMsg();
					sl.getTextAreaChatPlayingStage().appendText(namePlayingStage+": "+textPlayingStage+"\n");
					sl.getTextAreaChatPlayingStage().selectPositionCaret(sl.getTextAreaChatPlayingStage().getText().length());
					
					break;
					
				case GameHistory:
					GameHistory history = (GameHistory) obj;
					PlayerWithoutOS currentPlayer = history.getCurrentPlayer();
					
					switch(history.getHistoryType()){
					case PlayAction:
						sl.getTextAreaGameHistory().appendText(history.getText()); //to do: noch farblich abheben je player
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									sl.getLabelNumberOfActionsAndBuys().setText("Du bist an der Reihe: "+currentPlayer.getNumberOfActions()+" Aktionen, "+currentPlayer.getNumberOfBuys()+" Käufe");
								}else{
									sl.getLabelNumberOfActionsAndBuys().setText(currentPlayer.getUsername()+" ist an der Reihe: "+currentPlayer.getNumberOfActions()+" Aktionen, "+currentPlayer.getNumberOfBuys()+" Käufe");
								}							
					           }
					      });
						
						
						break;
						
					case PlayBuy:
	
						sl.getTextAreaGameHistory().appendText(history.getText()); //to do: noch farblich abheben je player
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									sl.getLabelNumberOfActionsAndBuys().setText("Du bist an der Reihe: "+currentPlayer.getNumberOfActions()+" Aktionen, "+currentPlayer.getNumberOfBuys()+" Käufe");
								}else{
									sl.getLabelNumberOfActionsAndBuys().setText(currentPlayer.getUsername()+" ist an der Reihe: "+currentPlayer.getNumberOfActions()+" Aktionen, "+currentPlayer.getNumberOfBuys()+" Käufe");
								}									

								sl.getLabelNumberOfActionsAndBuys().setText(currentPlayer.getUsername()+" ist an der Reihe: "+currentPlayer.getNumberOfActions()+" Aktionen, "+currentPlayer.getNumberOfBuys()+" Käufe");

					           }
					      });
						
						break;
						
					case EndAction:
						sl.getTextAreaGameHistory().appendText(history.getText()); //to do: noch farblich abheben je player
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									sl.getLabelNumberOfActionsAndBuys().setText("Du bist an der Reihe: "+currentPlayer.getNumberOfActions()+" Aktionen, "+currentPlayer.getNumberOfBuys()+" Käufe");
								}else{
									sl.getLabelNumberOfActionsAndBuys().setText(currentPlayer.getUsername()+" ist an der Reihe: "+currentPlayer.getNumberOfActions()+" Aktionen, "+currentPlayer.getNumberOfBuys()+" Käufe");
								}	
								
					           }
					      });
						break;
						
					case EndBuy:
						sl.getTextAreaGameHistory().appendText(history.getText()); //to do: noch farblich abheben je player
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
						
						if(history.getPlayerForGUIActivation().getUsername().equals(sl.getPlayer_noOS().getUsername())){
							sl.getButtonPlayActions().setDisable(false);
							sl.getButtonEndActions().setDisable(false);
						}
						PlayerWithoutOS playerForGUIActivation= history.getPlayerForGUIActivation();
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								
								if(playerForGUIActivation.getUsername().equals(sl.getPlayer_noOS().getUsername())){
									sl.getLabelNumberOfActionsAndBuys().setText("Du bist an der Reihe: "+playerForGUIActivation.getNumberOfActions()+" Aktionen, "+playerForGUIActivation.getNumberOfBuys()+" Käufe");
								}else{
									sl.getLabelNumberOfActionsAndBuys().setText(playerForGUIActivation.getUsername()+" ist an der Reihe: "+playerForGUIActivation.getNumberOfActions()+" Aktionen, "+playerForGUIActivation.getNumberOfBuys()+" Käufe");
								}
								
					           }
					      });
						
						break;
						
					case LeaveGame:
						System.out.println("test game is full "+history.getGameParty().isFull());
						System.out.println("test game has started "+history.getGameParty().getGameHasStarted());
						System.out.println("test anzahl spieler "+history.getGameParty().getNumberOfLoggedInPlayers());
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								
								if(history.getLeavingPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									sl.setCurrentGameParty(null);
									
									//check if the game has started. We have to make +1player on number of logged in players because we have already removed the player on client side
									if(history.getGameParty().getGameHasStarted()){
										sl.getPlayingStage().stop();
										
										//game party is full when player is leaving: he gets a defeat
										Label popUpMsg = new Label ("Du hast das Spiel verlassen und bekommst eine Niederlage");
										sl.setLbl_popUpMessage(popUpMsg);
										
										Stage popUp = new Stage();	
										popUp.setResizable(true);
										Client_View_popUp view = new Client_View_popUp (popUp, model);
										new Client_Controller_popUp(model, view); 
										view.start();
									}else{
										//leaving player won't get a defeat
										sl.getPlayingStage().stop();
										
										//leaving player won't get a defeat
										Label popUpMsg = new Label ("Du hast das Spiel verlassen");
										sl.setLbl_popUpMessage(popUpMsg);
										
										Stage popUp = new Stage();	
										popUp.setResizable(true);
										Client_View_popUp view = new Client_View_popUp (popUp, model);
										new Client_Controller_popUp(model, view); 
										view.start();
									}
									
								//else-clause is for all other players which are still ingame	
								}else{
									//first we check if there is only one player left. If yes: he will win the party
									if(history.getGameParty().getNumberOfLoggedInPlayers() == 1 && history.getGameParty().getGameHasStarted()){
										sl.getPlayingStage().stop();
										sl.setCurrentGameParty(null);
										Label popUpMsg = new Label ("Glückwunsch, du hast gewonnen!");
										sl.setLbl_popUpMessage(popUpMsg);
										Stage popUp = new Stage();	
										popUp.setResizable(true);
										Client_View_popUp view = new Client_View_popUp (popUp, model);
										new Client_Controller_popUp(model, view); 
										view.start();
									}else{
										//update the saved GameParty because one player has left the game
										sl.setCurrentGameParty(history.getGameParty());
										
										//clear first the player list and then set it updated
										sl.getPlayingStage().vb_player.getChildren().clear();
										for(int i =0; i<history.getGameParty().getArrayListOfPlayers().size();i++){
											Label label = new Label(history.getGameParty().getArrayListOfPlayers().get(i).getUsername());
											sl.getPlayingStage().vb_player.getChildren().add(label);
										}
									}
	
								}
								
								//last but not least: update the LisView of each client if the game hasn't started yet
								if (!history.getGameParty().getGameHasStarted()){
									sl.updateGamePartyAfterLeave(history.getGameParty());
								}
								
					           }
					      });
						
						break;

					}
					
					
								
				default:
				}
		
			}
		}  catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			
			try {
				model.getInput().close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				model.getOutput().close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				model.client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}

}
