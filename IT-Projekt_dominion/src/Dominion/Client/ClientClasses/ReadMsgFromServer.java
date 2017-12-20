package Dominion.Client.ClientClasses;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JOptionPane;

import Dominion.Client.Client;
import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.Client.ClientClasses.gameplay.cards.Cards;
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
 * @author Joel Henz (if not commented by other): 
 * this is the class for receiving chat messages from the server. If a client sends a chat message to server, the server will send that message to all clients
 * */
public class ReadMsgFromServer implements Runnable {
	ObjectInputStream in;
	ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
	Croupier croupier = Croupier.getCroupier();
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
					System.out.println("gr�sse von zur�ckgsendetem playstatistics shit "+playerStatistics.getListOfStartInformationObjects().size());
					sl.getTbl_playerStats().getItems().addAll(sl.getAl_Statistics());				
					
					
					
					break;
					 
				case GameParty:
					
					GameParty newGame = (GameParty) obj;
					 
					Platform.runLater(new Runnable() {
				           @Override 
				           public void run() {
				        	//updating the ListView of each client   
				            sl.addNewGame(newGame);
				            
				            if(newGame.getHost().getUsername().equals(model.getName())){
				            	sl.setCurrentGameParty(newGame);
				            	
				            	Stage playingStage = new Stage();			
				            	playingStage.initModality(Modality.APPLICATION_MODAL);
				            	sl.setIsHost(true);
						        Client_View_playingStage view_playingStage = new Client_View_playingStage (playingStage, model,newGame);
						        sl.setView_playingStage(view_playingStage);
						        new Client_Controller_playingStage(model, sl.getPlayingStage(), newGame); 
						        sl.getCreateGameView().stop();
						        sl.getView_lobby().stop();
						        sl.getPlayingStage().start();
						        
						        //adding the name of the host on the playing Stage and add the current GameParty to the ServiceLocatorCLient of the host
						        for(int i=0; i<newGame.getArrayListOfPlayers().size();i++){
						        	Label label = new Label(sl.getCurrentGameParty().getArrayListOfPlayers().get(i).getUsername()+": "+sl.getCurrentGameParty().getArrayListOfPlayers().get(i).getPoints()+" Punkte");
						        	Label allPlayer = new Label("Spieler dieser Partie:");
									sl.getPlayingStage().vb_player.getChildren().addAll(allPlayer, label);
									allPlayer.getStyleClass().addAll("textBoard","boardTitle");
									label.getStyleClass().addAll("textBoard","textBoardInfo");
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
					
					JoinGameParty join = (JoinGameParty) obj;									

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
								
								Cards cards = new Cards();
				            	Croupier croupier = Croupier.getCroupier();
				            	
				            	//get the card set of this game party
				            	int numberOfCardSet = join.getSelectedGameParty().getNumberOfCardSet();
				            	croupier.setAl_communityActionCards(cards.getCardSetOfGameParty(numberOfCardSet));
				            	sl.setCroupier(croupier);
								
								//setting the host as current player of the playing stage. We need this to save in case we will leave the party (new instance of a GameHistory object
								//where we will pass on the current player as parameter)
								sl.setCurrentPlayer_noOS_ofPlayingStage(sl.getCurrentGameParty().getHost());
								
								Stage playingStage = new Stage();			
					        	playingStage.initModality(Modality.APPLICATION_MODAL);
					        	sl.setIsHost(false);
					        	Client_View_playingStage view = new Client_View_playingStage (playingStage, model,join.getSelectedGameParty());
					        	sl.setView_playingStage(view);
					        	new Client_Controller_playingStage(model, sl.getPlayingStage(),join.getSelectedGameParty()); 
					        	sl.getView_lobby().stop();
					        	sl.getPlayingStage().start();
							}
							
							//determine the players who have already joined the game. On their playing stages we have to add the username of the new player
							try{
								if(join.getSelectedGameParty().getID() == sl.getCurrentGameParty().getID()){
									sl.getPlayingStage().vb_player.getChildren().clear();	
									Label allPlayer = new Label("Spieler dieser Partie:");
						
									sl.getPlayingStage().vb_player.getChildren().add(allPlayer);
									allPlayer.getStyleClass().addAll("textBoard","boardTitle");
									for(int i =0; i<join.getSelectedGameParty().getArrayListOfPlayers().size();i++){
										Label label = new Label(join.getSelectedGameParty().getArrayListOfPlayers().get(i).getUsername()+": "+join.getSelectedGameParty().getArrayListOfPlayers().get(i).getPoints()+" Punkte");
										sl.getPlayingStage().vb_player.getChildren().add(label);
										label.getStyleClass().addAll("textBoard","textBoardInfo");
										
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
									
									
									sl.getPlayingStage().stop();
									
									//clear SL and croupier
									ServiceLocatorClient.setServiceLocatorClientNull();
									Croupier.setCroupierNull();
									
									//get a new SL with new resources
									sl = ServiceLocatorClient.getServiceLocator();
									croupier = Croupier.getCroupier();
									
									Label popUpMsg = new Label ("Der Host hat das Spiel beendet");
									sl.setLbl_popUpMessage(popUpMsg);
									Stage popUp = new Stage();	
									popUp.setResizable(true);
									Client_View_popUp viewPopup = new Client_View_popUp (popUp, model);
									new Client_Controller_popUp(model, viewPopup); 
					
									
									//recreate a lobby view
									Stage lobby_stage = new Stage();				
							        Client_View_lobby viewLobby = new Client_View_lobby(lobby_stage, model); 
							        sl.setView_lobby(viewLobby);
							        new Client_Controller_lobby(model, viewLobby); 
							        viewLobby.start();  
									
									viewPopup.start();
					
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
					
					/**ChatMessagePlayingStage currentMsgPlay = (ChatMessagePlayingStage) obj;
					ChatMessagePlayingStage msg_obj = new ChatMessagePlayingStage(currentMsgPlay.getName(), currentMsgPlay.getMsg(), currentMsgPlay.getGameParty());
					msg_obj.setID2(currentMsgPlay.getID());*/
					
					ChatMessagePlayingStage msg_obj = (ChatMessagePlayingStage) obj;
					
					String namePlayingStage = msg_obj.getName();
					String textPlayingStage = msg_obj.getMsg();
					sl.getTextAreaChatPlayingStage().appendText(namePlayingStage+": "+textPlayingStage+"\n");
					sl.getTextAreaChatPlayingStage().selectPositionCaret(sl.getTextAreaChatPlayingStage().getText().length());
					
					break;
					
				case GameHistory:
					
					GameHistory history =(GameHistory) obj;
										
					PlayerWithoutOS currentPlayer = history.getCurrentPlayer();
					
					switch(history.getHistoryType()){
						
					case EndAction:
						
						sl.getTextAreaGameHistory().appendText(history.getTextForTextArea()); //to do: noch farblich abheben je player
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									sl.getLabelNumberOfActionsAndBuys().setText("Du bist "+history.getTextForLabel());
								}else{
									sl.getLabelNumberOfActionsAndBuys().setText(currentPlayer.getUsername()+" ist "+history.getTextForLabel());
								}	
								
					           }
					      });
						break;
					
					//here we switch the player
					case EndBuy:
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								
								try{
									
									croupier.setStackSize(history.getGameCard_EN());

									if(history.getGameCard_EN().equals("estate") || history.getGameCard_EN().equals("duchy") || history.getGameCard_EN().equals("province") || history.getGameCard_EN().equals("curse")){
										
										sl.getPlayingStage().vb_player.getChildren().clear();	
										Label allPlayer = new Label("Spieler dieser Partie:");
										sl.getPlayingStage().vb_player.getChildren().add(allPlayer);
										allPlayer.getStyleClass().addAll("textBoard","boardTitle");
										
										
										for(int i =0; i<history.getGameParty().getArrayListOfPlayers().size();i++){
											Label label = new Label(history.getGameParty().getArrayListOfPlayers().get(i).getUsername()+": "+history.getGameParty().getArrayListOfPlayers().get(i).getPoints()+" Punkte");
											sl.getPlayingStage().vb_player.getChildren().add(label);
											label.getStyleClass().addAll("textBoard","textBoardInfo");
										}
									}
									
									
															           
								}catch(NullPointerException e){
									//we dont get a GameCard if player ends his buy phase without buying a card
								}
				
					           }
					      });
						
						
						sl.getTextAreaGameHistory().appendText(history.getTextForTextArea()); //to do: noch farblich abheben je player
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
						
						if(history.getPlayerForGUIActivation().getUsername().equals(sl.getPlayer_noOS().getUsername())){
							//its the next players turn, so prepare his stage and croupier
							
							Platform.runLater(new Runnable() {

								@Override 
						           public void run() {
									
									//sl.getButtonEndActions().setDisable(false);
									croupier.setActionMode(true);
									croupier.setActions(1);
									croupier.setBuys(1);
									croupier.setBuyPower(0);
									
						           }
						      });
						}

						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								
								if(history.getPlayerForGUIActivation().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
								}else{
									sl.getLabelNumberOfActionsAndBuys().setText(history.getPlayerForGUIActivation().getUsername()+" ist am Zug\n1 Aktionen, 1 Käufe, 0 Geld");
								}
								
					           }
					      });
						
						
						/**try{
							history = new GameHistory (currentHistory.getText(),currentHistory.getGameParty(),currentHistory.getCurrentPlayer(),GameHistory.HistoryType.EndBuy);
							history.setID2(currentHistory.getID());
							
							sl.getTextAreaGameHistory().appendText(history.getText()); //to do: noch farblich abheben je player
							sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
							
							if(history.getPlayerForGUIActivation().getUsername().equals(sl.getPlayer_noOS().getUsername())){
								//its the next players turn, so prepare his stage and croupier
								sl.getButtonEndActions().setDisable(false);
								croupier.setActionMode(true);
								croupier.setBuyMode(false);
								croupier.setActions(1);
								croupier.setBuys(1);
								croupier.setBuyPower(0);
							}
							Platform.runLater(new Runnable() {
								@Override 
						           public void run() {
									
									if(history.getPlayerForGUIActivation().getUsername().equals(sl.getPlayer_noOS().getUsername())){
										sl.getLabelNumberOfActionsAndBuys().setText("Du bist an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
									}else{
										sl.getLabelNumberOfActionsAndBuys().setText(history.getPlayerForGUIActivation().getUsername()+" ist an der Reihe: 1 Aktionen, 1 Käufe, 0 Geld");
									}
									
						           }
						      });
							
						}catch (NullPointerException e){
							//
						}*/
					
						break;
						
					case LeaveGame:
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								
								//first we have to update the ListView of each client if the game hasn't started yet. Remove also the leaving player
								if(!history.getGameParty().getGameHasStarted()){
									if(history.getLeavingPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
										
										handleLeavingPlayerNoDefeat();
									}else{
										//game hasn't started yet
										handleAllConnectedClients1(history);
									}

									
								}else{
									if(history.getLeavingPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
										handleLeavingPlayerWithDefeat();
									}else{
										//game has already started so we have to determine the next player in the sequence
										handleAllConnectedClients2(history);
									}
									
									
								}

					           }
					      });
						
						
						/**try{
							history = new GameHistory (currentHistory.getText(),currentHistory.getGameParty(),currentHistory.getCurrentPlayer(),GameHistory.HistoryType.LeaveGame);
							history.setID2(currentHistory.getID());
							
							System.out.println(history.getLeavingPlayer().getUsername());
							
							Platform.runLater(new Runnable() {
								@Override 
						           public void run() {
									
									//first we have to update the ListView of each client if the game hasn't started yet. Remove also the leaving player
									if(!history.getGameParty().getGameHasStarted()){
										if(history.getLeavingPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
											
											handleLeavingPlayerNoDefeat();
										}else{
											//game hasn't started yet
											handleAllConnectedClients1(history);
										}
										
									}else{
										if(history.getLeavingPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
											handleLeavingPlayerWithDefeat();
										}else{
											//game has already started so we have to determine the next player in the sequence
											handleAllConnectedClients2(history);
										}
										
										
									}
						           }
						      });
							
						}catch(NullPointerException e){
							//
						}*/
		
						//break case LeaveGame
						break;
						
					//this msg is written only by server (class ClientHandler in case "EndBuy" and if the number of max rounds is reached)
					case EndGame:
						
						
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								
								sl.getPlayingStage().stop();
								
								//clear SL and croupier
								ServiceLocatorClient.setServiceLocatorClientNull();
								Croupier.setCroupierNull();
								
								//get a new SL with new resources
								sl = ServiceLocatorClient.getServiceLocator();
								croupier = Croupier.getCroupier();
								
								Label popUpMsg = new Label (history.getTextForTextArea());
								sl.setLbl_popUpMessage(popUpMsg);
								Stage popUp = new Stage();	
								popUp.setResizable(true);
								Client_View_popUp viewPopup = new Client_View_popUp (popUp, model);
								new Client_Controller_popUp(model, viewPopup); 
				
								
								//recreate a lobby view
								Stage lobby_stage = new Stage();				
						        Client_View_lobby viewLobby = new Client_View_lobby(lobby_stage, model); 
						        sl.setView_lobby(viewLobby);
						        new Client_Controller_lobby(model, viewLobby); 
						        viewLobby.start();  
								
								viewPopup.start();
														

					           }
					      });
						
						break;
	
					case UpdateLobbyAfterLeave:
						
						/**history = new GameHistory (currentHistory.getText(),currentHistory.getGameParty(),currentHistory.getCurrentPlayer(),GameHistory.HistoryType.UpdateLobbyAfterLeave);
						history.setID2(currentHistory.getID());*/
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								sl.updateGamePartyAfterLeave(history.getGameParty());
								
					           }
					      });
						//break case UpdateLobbyAfterLeave
						break;
						
					case PlayCard:
						if(history.getGameCard_EN() !=null){
							switch(history.getGameCard_EN()){
							
							case "chapel":
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									
									Platform.runLater(new Runnable() {

										@Override 
								           public void run() {
											croupier.setTrashModeChapel(true);
											
								           }
								      });
									
									
								}
								//break case chapel
								break;
								
							case "mine":
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									Platform.runLater(new Runnable() {

										@Override 
								           public void run() {
											croupier.setTrashModeMine(true);
											
								           }
								      });
									
									
								}
								break;
								
							case "moneylender":
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									Platform.runLater(new Runnable() {

										@Override 
								           public void run() {
											croupier.setTrashModeMoneylender(true);
											
								           }
								      });
									
									
								}
								break;
								
							case "rebuilding":
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									Platform.runLater(new Runnable() {

										@Override 
								           public void run() {
											croupier.setTrashModeRebuilding(true);
											
								           }
								      });
									
									
								}
								break;
								
							case "workshop":
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									Platform.runLater(new Runnable() {

										@Override 
								           public void run() {
											croupier.setModeForWorkshop(true);
											
								           }
								      });
									
									
								}
								break;
								
							case "militia":
								
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									Platform.runLater(new Runnable() {

										@Override 
								           public void run() {
											sl.getLabelNumberOfActionsAndBuys().setText("Du bist "+history.getTextForLabel());											
											croupier.setModeForAttack(true);
											
								           }
								      });
									
									
								}else{
									
									Platform.runLater(new Runnable() {

										@Override 
								           public void run() {
											sl.getLabelNumberOfActionsAndBuys().setText(currentPlayer.getUsername()+" ist "+history.getTextForLabel());
											//check if the opponents got a reaction card (moat)
											boolean found = false;
											for(int i=0; i<croupier.getHoleCards().size();i++){
												if(croupier.getHoleCards().get(i).getLbl_cardName().getText().equals("moat")){
													found = true;										
												}
											}
											
											//we have to save the current player (attacker) so we can send here the current player of the history object
											croupier.saveCurrentPlayer(history.getCurrentPlayer());
											if(found){
												croupier.setReactionMode(true);	
												
												
											}else{ 
												//player doesnt have a moat card...he has to discard until he has 3 cards
												
												if(croupier.getHoleCards().size()<4){
													sl.getStrBuilderTextArea().delete(0, sl.getStrBuilderTextArea().length());
													sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" hat 3 oder weniger Karten und muss keine ablegen\n");
													GameHistory history2 = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),history.getCurrentPlayer(),null,null, GameHistory.HistoryType.Reaction);
													
													try {
														sl.getPlayer_OS().getOut().reset();
														sl.getPlayer_OS().getOut().writeObject(history2);
														sl.getPlayer_OS().getOut().flush();
													} catch (IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
												}else{
													croupier.setDiscardModeForMilitia(true);
												}
												
											}
								           }
								      });
									
																		
								}
								break;
								
							case "witch":
								
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									Platform.runLater(new Runnable() {

										@Override 
								           public void run() {
											sl.getLabelNumberOfActionsAndBuys().setText("Du bist "+history.getTextForLabel());											
											croupier.setModeForAttack(true);
											
								           }
								      });
									
									
								}else{
									
									Platform.runLater(new Runnable() {

										@Override 
								           public void run() {
											sl.getLabelNumberOfActionsAndBuys().setText(currentPlayer.getUsername()+" ist "+history.getTextForLabel());
											//check if the opponents got a reaction card (moat)
											boolean found = false;
											for(int i=0; i<croupier.getHoleCards().size();i++){
												if(croupier.getHoleCards().get(i).getLbl_cardName().getText().equals("moat")){
													found = true;										
												}
											}
											
											//we have to save the current player (attacker) so we can send here the current player of the history object
											croupier.saveCurrentPlayer(history.getCurrentPlayer());
											if(found){
												croupier.setReactionMode(true);	
												
												
											}else{ 
												//player doesnt have a moat card...he has to pick up a curse card
												
												croupier.setModeCurseCard(true);
																							
											}
								           }
								      });
									
																		
								}
								break;

							}
						}
						
						sl.getTextAreaGameHistory().appendText(history.getTextForTextArea()); //to do: noch farblich abheben je player
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									sl.getLabelNumberOfActionsAndBuys().setText("Du bist "+history.getTextForLabel());
								}else{
									sl.getLabelNumberOfActionsAndBuys().setText(currentPlayer.getUsername()+" ist "+history.getTextForLabel());
								}	
								
					           }
					      });
						
						/**try{
							if(!history.getGameCard_EN().equals("militia")){
								sl.getTextAreaGameHistory().appendText(history.getTextForTextArea()); //to do: noch farblich abheben je player
								sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
								
								Platform.runLater(new Runnable() {

									@Override 
							           public void run() {
										if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
											sl.getLabelNumberOfActionsAndBuys().setText("Du bist "+history.getTextForLabel());
										}else{
											sl.getLabelNumberOfActionsAndBuys().setText(currentPlayer.getUsername()+" ist "+history.getTextForLabel());
										}	
										
							           }
							      });
							}
						}catch (NullPointerException e){
							//
						}*/
						
						
						
						
						//break case PlayCard
						break;
					
					case BuyNoPointCard:
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								
								croupier.setStackSize(history.getGameCard_EN());
								
					           }
					      });
						
						sl.getTextAreaGameHistory().appendText(history.getTextForTextArea()); //to do: noch farblich abheben je player
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									sl.getLabelNumberOfActionsAndBuys().setText("Du bist "+history.getTextForLabel());
								}else{
									sl.getLabelNumberOfActionsAndBuys().setText(currentPlayer.getUsername()+" ist "+history.getTextForLabel());
								}	
								
					           }
					      });
						
						//break case BuyNoPointCard
						break;
						
					case BuyPointCard:
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								croupier.setStackSize(history.getGameCard_EN());
								
								//update the list of players (points) because there was a buy
								sl.setCurrentGameParty(history.getGameParty());
								
								//updating the player list with the points
								sl.getPlayingStage().vb_player.getChildren().clear();	
								Label allPlayer = new Label("Spieler dieser Partie:");
								sl.getPlayingStage().vb_player.getChildren().add(allPlayer);
								allPlayer.getStyleClass().addAll("textBoard","boardTitle");
								
								for(int i =0; i<history.getGameParty().getArrayListOfPlayers().size();i++){
									Label label = new Label(history.getGameParty().getArrayListOfPlayers().get(i).getUsername()+": "+history.getGameParty().getArrayListOfPlayers().get(i).getPoints()+" Punkte");
									sl.getPlayingStage().vb_player.getChildren().add(label);
									label.getStyleClass().addAll("textBoard","textBoardInfo");
									
								}
								
					           }
					      });
						
						sl.getTextAreaGameHistory().appendText(history.getTextForTextArea()); //to do: noch farblich abheben je player
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									sl.getLabelNumberOfActionsAndBuys().setText("Du bist "+history.getTextForLabel());
								}else{
									sl.getLabelNumberOfActionsAndBuys().setText(currentPlayer.getUsername()+" ist "+history.getTextForLabel());
								}	
								
					           }
					      });
						
						break;
						
					case Trash:
						sl.getTextAreaGameHistory().appendText(history.getTextForTextArea());
						
						try{
							
							if(history.getGameCard_EN().equals("estate") || history.getGameCard_EN().equals("duchy") || history.getGameCard_EN().equals("province") || history.getGameCard_EN().equals("curse")){
								Platform.runLater(new Runnable() {

									@Override 
							           public void run() {
										
										//update the list of players (points) because there was a buy
										sl.setCurrentGameParty(history.getGameParty());
										
										//updating the player list with the points
										sl.getPlayingStage().vb_player.getChildren().clear();	
										Label allPlayer = new Label("Spieler dieser Partie:");
										sl.getPlayingStage().vb_player.getChildren().add(allPlayer);
										allPlayer.getStyleClass().addAll("textBoard","boardTitle");
										
										for(int i =0; i<history.getGameParty().getArrayListOfPlayers().size();i++){
											Label label = new Label(history.getGameParty().getArrayListOfPlayers().get(i).getUsername()+": "+history.getGameParty().getArrayListOfPlayers().get(i).getPoints()+" Punkte");
											sl.getPlayingStage().vb_player.getChildren().add(label);
											label.getStyleClass().addAll("textBoard","textBoardInfo");
										}
										
							           }
							      });
							}
							
							if(history.getGameCard_EN().equals("moneylender")){
								Platform.runLater(new Runnable() {

									@Override 
							           public void run() {
										if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
											sl.getLabelNumberOfActionsAndBuys().setText("Du bist "+history.getTextForLabel());
										}else{
											sl.getLabelNumberOfActionsAndBuys().setText(currentPlayer.getUsername()+" ist "+history.getTextForLabel());
										}	
										
							           }
							      });
							}
							
							
							
						}catch (NullPointerException e){
							//catch if it wasn't trashed a point card
						}
						
						break;
						
					case MineModeEnd:
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								
								croupier.setStackSize(history.getGameCard_EN());
								
					           }
					      });
						
						sl.getTextAreaGameHistory().appendText(history.getTextForTextArea()); //to do: noch farblich abheben je player
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug\n"+history.getTextForLabel());
								}else{
									sl.getLabelNumberOfActionsAndBuys().setText(currentPlayer.getUsername()+" ist "+history.getTextForLabel());
								}	
								
					           }
					      });
						
						
						break;
						
					case RebuildingModeEnd:
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								
								croupier.setStackSize(history.getGameCard_EN());
								
					           }
					      });
						
						sl.getTextAreaGameHistory().appendText(history.getTextForTextArea()); //to do: noch farblich abheben je player
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
						
						if(history.getGameCard_EN().equals("estate") || history.getGameCard_EN().equals("duchy") || history.getGameCard_EN().equals("province") || history.getGameCard_EN().equals("curse")){
							Platform.runLater(new Runnable() {

								@Override 
						           public void run() {
									
									//update the list of players (points) because there was a buy
									sl.setCurrentGameParty(history.getGameParty());
									
									//updating the player list with the points
									sl.getPlayingStage().vb_player.getChildren().clear();	
									Label allPlayer = new Label("Spieler dieser Partie:");
									sl.getPlayingStage().vb_player.getChildren().add(allPlayer);
									allPlayer.getStyleClass().addAll("textBoard","boardTitle");
									
									for(int i =0; i<history.getGameParty().getArrayListOfPlayers().size();i++){
										Label label = new Label(history.getGameParty().getArrayListOfPlayers().get(i).getUsername()+": "+history.getGameParty().getArrayListOfPlayers().get(i).getPoints()+" Punkte");
										sl.getPlayingStage().vb_player.getChildren().add(label);
										label.getStyleClass().addAll("textBoard","textBoardInfo");
									}
									
						           }
						      });
						}
						
						
						break;
						
					case WorkshopModeEnd:
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								
								croupier.setStackSize(history.getGameCard_EN());
								
					           }
					      });
						
						sl.getTextAreaGameHistory().appendText(history.getTextForTextArea()); //to do: noch farblich abheben je player
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
						
						if(history.getGameCard_EN().equals("estate") || history.getGameCard_EN().equals("duchy") || history.getGameCard_EN().equals("province") || history.getGameCard_EN().equals("curse")){
							Platform.runLater(new Runnable() {

								@Override 
						           public void run() {
									
									//update the list of players (points) because there was a buy
									sl.setCurrentGameParty(history.getGameParty());
									
									//updating the player list with the points
									sl.getPlayingStage().vb_player.getChildren().clear();	
									Label allPlayer = new Label("Spieler dieser Partie:");
									sl.getPlayingStage().vb_player.getChildren().add(allPlayer);
									allPlayer.getStyleClass().addAll("textBoard","boardTitle");
									
									for(int i =0; i<history.getGameParty().getArrayListOfPlayers().size();i++){
										Label label = new Label(history.getGameParty().getArrayListOfPlayers().get(i).getUsername()+": "+history.getGameParty().getArrayListOfPlayers().get(i).getPoints()+" Punkte");
										sl.getPlayingStage().vb_player.getChildren().add(label);
										label.getStyleClass().addAll("textBoard","textBoardInfo");
									}
									
						           }
						      });
						}
						
						
						break;
						
					case Discard:
						
						sl.getTextAreaGameHistory().appendText(history.getTextForTextArea());
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
						
						
						break;
						
					case Reaction:
						sl.getTextAreaGameHistory().appendText(history.getTextForTextArea()); //to do: noch farblich abheben je player
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
						
						Platform.runLater(new Runnable() {

							@Override 
					           public void run() {
								if(history.getCurrentPlayer().getUsername().equals(sl.getPlayer_noOS().getUsername())){
									croupier.increaseReactionCounter();
									
									if(croupier.getReactionCounter() == (sl.getCurrentGameParty().getNumberOfLoggedInPlayers()-1)){
										croupier.setModeForAttack(false);
										croupier.setCounterReactions(0);
										
										GameHistory history2;
										
										if(croupier.getActions()==0){
											croupier.setBuyMode(true);
								        	sl.getStrBuilderTextArea().delete(0, sl.getStrBuilderTextArea().length());
								        	sl.getStrBuilderTextArea().append(sl.getPlayerName()+" beendet Aktionsphase\n");

											history2 = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.AttackEnd);
										}else{
											croupier.setActionMode(true);
											sl.getStrBuilderTextArea().append(sl.getPlayerName()+" hat noch weitere Aktionen\n");
											history2 = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.AttackEnd);
										}
										
										try {
											sl.getPlayer_OS().getOut().reset();
											sl.getPlayer_OS().getOut().writeObject(history2);
											sl.getPlayer_OS().getOut().flush();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
								
									}
									
								}
								
								try{
									if(history.getGameCard_EN().equals("curse")){
										croupier.setStackSize(history.getGameCard_EN());
										
										//update the list of players (points) because there was a buy
										sl.setCurrentGameParty(history.getGameParty());
										
										//updating the player list with the points
										sl.getPlayingStage().vb_player.getChildren().clear();	
										Label allPlayer = new Label("Spieler dieser Partie:");
										sl.getPlayingStage().vb_player.getChildren().add(allPlayer);
										allPlayer.getStyleClass().addAll("textBoard","boardTitle");
										
										for(int i =0; i<history.getGameParty().getArrayListOfPlayers().size();i++){
											Label label = new Label(history.getGameParty().getArrayListOfPlayers().get(i).getUsername()+": "+history.getGameParty().getArrayListOfPlayers().get(i).getPoints()+" Punkte");
											sl.getPlayingStage().vb_player.getChildren().add(label);
											label.getStyleClass().addAll("textBoard","textBoardInfo");
											
										}
										
									}
								}catch(NullPointerException e){
									//
								}
					
					           }
					      });
			
						break;
						
					case AttackEnd:
						sl.getTextAreaGameHistory().appendText(history.getTextForTextArea()); //to do: noch farblich abheben je player
						sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
		
						break;
						
						
					}
					
					//break case GameHistory
					break;
						
				default:
				}
		
			}
		}  catch (Exception e) {
			sl.getLogger().info("Exception catched:");
			e.printStackTrace();
			
			try {
				model.getInput().close();
				model.getOutput().close();
				model.client.close();
			}catch(Exception e2){
			e2.printStackTrace();
			}
					
			

		}

	}
	
	public void handleAllConnectedClients1(GameHistory history){
		history.getGameParty().removePlayer(history.getLeavingPlayer());
		//game hasn't started yet. We have to update the ListView of every client (-1 player)
		history.setNewType(GameHistory.HistoryType.UpdateLobbyAfterLeave);
		
		try {
			sl.getPlayer_OS().getOut().writeObject(history);
			sl.getPlayer_OS().getOut().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void handleAllConnectedClients2(GameHistory history){
		//remove first the leaving player
		history.getGameParty().removePlayer(history.getLeavingPlayer());

		//we check if there is only one player left. If yes: he will win the party (if the game has started)
		if(history.getGameParty().getNumberOfLoggedInPlayers() == 1 && history.getGameParty().getArrayListOfPlayers().get(0).getUsername().equals(sl.getPlayer_noOS().getUsername())){
			
			//sl.clearCurrentGameParty();
			
			Label popUpMsg = new Label ("Gl�ckwunsch, du hast gewonnen!");
			sl.setLbl_popUpMessage(popUpMsg);
			Stage popUp = new Stage();	
			popUp.setResizable(true);
			Client_View_popUp view = new Client_View_popUp (popUp, model);
			new Client_Controller_popUp(model, view); 
			view.start();
		}else{
			//read the GameHistory message
			sl.getTextAreaGameHistory().appendText(history.getTextForTextArea()); //to do: noch farblich abheben je player
			sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
			
			//update the saved GameParty because one player has left the game
			sl.setCurrentGameParty(history.getGameParty());
			
			//clear the player list and then set it updated
			sl.getPlayingStage().vb_player.getChildren().clear();
			for(int i =0; i<history.getGameParty().getArrayListOfPlayers().size();i++){
				Label label = new Label(history.getGameParty().getArrayListOfPlayers().get(i).getUsername());
				sl.getPlayingStage().vb_player.getChildren().add(label);
			}
			
			
		}
		
		//finally: activate the GUI of the next player and get his resources
		if(history.getPlayerForGUIActivation().getUsername().equals(sl.getPlayer_noOS().getUsername())){
			
			//its the next players turn, so prepare his stage and croupier
			croupier.setActionMode(true);
			croupier.setBuyMode(false);
			croupier.setActions(1);
			croupier.setBuys(1);
			croupier.setBuyPower(0);
			
			sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");

		}else{
			sl.getLabelNumberOfActionsAndBuys().setText(history.getPlayerForGUIActivation().getUsername()+" ist am Zug\n1 Aktionen, 1 K�ufe, 0 Geld");
		}
	}
	
	public void handleLeavingPlayerNoDefeat(){
		//sl.clearCurrentGameParty();
		
		//leaving player won't get a defeat
		Label popUpMsg = new Label ("Du hast das Spiel verlassen");
		sl.setLbl_popUpMessage(popUpMsg);
		
		Stage popUp = new Stage();	
		popUp.setResizable(true);
		Client_View_popUp view = new Client_View_popUp (popUp, model);
		new Client_Controller_popUp(model, view); 
		view.start();
	}
	
	public void handleLeavingPlayerWithDefeat(){
		//sl.clearCurrentGameParty();
		
		//game party is full when player is leaving: he gets a defeat
		Label popUpMsg = new Label ("Du hast das Spiel verlassen\nund verlierst!");
		sl.setLbl_popUpMessage(popUpMsg);
		
		Stage popUp = new Stage();	
		popUp.setResizable(true);
		Client_View_popUp view = new Client_View_popUp (popUp, model);
		new Client_Controller_popUp(model, view); 
		view.start();
	}

}