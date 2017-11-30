package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;

import Dominion.appClasses.CancelGame;
import Dominion.appClasses.ChatMessageLobby;
import Dominion.appClasses.DeleteGameFromListView;
import Dominion.appClasses.GameObject;
import Dominion.appClasses.GameParty;
import Dominion.appClasses.JoinGameParty;
import Dominion.appClasses.StartInformation;
import Dominion.appClasses.UpdateLobby;
import javafx.application.Platform;
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
					String name = msg.getName();
					String text = msg.getMsg();
					sl.getTextAreaLobby().appendText(name+": "+text+"\n");
					sl.getTextAreaLobby().selectPositionCaret(sl.getTextAreaLobby().getText().length());	
					break;
				 
				case InformationObject:
					break;
				
					/**
					 * @author kab: handles incoming StartInformation with player statistics and sends to  tbl_playerStats in the bloody lobby
					 */
				case StartInformation:
					
					StartInformation playerStatistics = (StartInformation) obj;
					
				
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
					
					Platform.runLater(new Runnable() {

						@Override 
				           public void run() {
							//this method will update the number of joined players of this game. If the game is full, it will be removed from the ListView of every client
							sl.updateGameParty(join);	
							
							//determine the joining player and create his playing stage
							if(join.getUsername().equals(model.getName())){
								GameParty gamePartyToJoin = join.getSelectedGameParty();
								Stage playingStage = new Stage();			
					        	playingStage.initModality(Modality.APPLICATION_MODAL);
					        	Client_View_playingStage view = new Client_View_playingStage (playingStage, model,false);
					        	sl.setView_playingStage(view);
					        	new Client_Controller_playingStage(model, sl.getPlayingStage(),gamePartyToJoin); 
					        	sl.getPlayingStage().start();
							}
						}
				       });
									
					break;
					
				case CancelGame:
					CancelGame cancelObject = (CancelGame) obj;
					
					GameParty gamePartyToCancel = cancelObject.getGameParty();
					
					Platform.runLater(new Runnable() {

						@Override 
				           public void run() {
							
							for(int i=0; i<gamePartyToCancel.getArrayOfPlayers().length;i++){
								if(model.getName().equals(gamePartyToCancel.getArrayOfPlayers()[i])){
									System.out.println("test");
									sl.getPlayingStage().stop();
								}
							}

							sl.removeGame(gamePartyToCancel);
				           }
				      });	
				break;
				
				case DeleteGameFromListView:
					
					DeleteGameFromListView gameToDelete = (DeleteGameFromListView) obj;
					
					Platform.runLater(new Runnable() {

						@Override 
				           public void run() {
				        	   sl.removeGame(gameToDelete.getGamePartyToDelete());
						}
				       });
					
				break;
								
				default:
				}
		
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
