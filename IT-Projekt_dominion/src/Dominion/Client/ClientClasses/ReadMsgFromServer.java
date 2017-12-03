package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JOptionPane;

import Dominion.Client.Client;
import Dominion.Server.ServerClasses.GamePartyOnServer;
import Dominion.appClasses.CancelGame;
import Dominion.appClasses.ChatMessageLobby;
import Dominion.appClasses.ChatMessagePlayingStage;
import Dominion.appClasses.GameHistory;
import Dominion.appClasses.GameObject;
import Dominion.appClasses.GameParty;
import Dominion.appClasses.JoinGameParty;
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
					
					//wenn bereits ein Spieler mit dem glelichen Benutzernamen existiert, wird kein Eintritt in die Lobby gewährt
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
							sl.setLbl_popUpMessage(new Label("Ein Spieler mit dem gleichen Benutzernamen ist bereits angemeldet"));		
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
						System.gc();
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
				            
				            if(newGame.getHost().equals(model.getName())){
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
						        for(int i=0; i<newGame.getArrayOfPlayers().length;i++){
						        	sl.getPlayingStage().labelArray[i].setText(newGame.getArrayOfPlayers()[i]);
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
					for (int i=0; i<join.getSelectedGameParty().getArrayOfPlayers().length;i++){						
						try{
							if(join.getSelectedGameParty().getArrayOfPlayers()[i].equals(model.getName())){
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
									for(int i =0; i<join.getSelectedGameParty().getArrayOfPlayers().length;i++){
										sl.getPlayingStage().labelArray[i].setText(sl.getCurrentGameParty().getArrayOfPlayers()[i]);
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
					sl.getTextAreaGameHistory().appendText(history.getText()); //to do: noch farblich abheben je player
					sl.getTextAreaGameHistory().selectPositionCaret(sl.getTextAreaGameHistory().getText().length());
								
				default:
				}
		
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
