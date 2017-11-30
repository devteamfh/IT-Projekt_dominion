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
import Dominion.appClasses.UpdateGameParty;
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
					
					sl.get_ol_PlayerStatistics().removeAll();
					sl.add_ol_PlayerStatistics(playerStatistics.getListOfStartInformationObjects());
	
					
					
					
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
					
				case UpdateGameParty:

					UpdateGameParty newUpdate = (UpdateGameParty) obj;
					
					Platform.runLater(new Runnable() {
				           @Override 
				           public void run() {
				        	   sl.updateGameParty(newUpdate.getGameParty());
				        	   
				           }
				       });
					break;
					
				case JoinGameParty:
					JoinGameParty join=(JoinGameParty) obj;
					GameParty gamePartyToJoin=join.getSelectedGameParty();
					
					Platform.runLater(new Runnable() {

						@Override 
				           public void run() {
				        	   Stage playingStage = new Stage();			
				        	   playingStage.initModality(Modality.APPLICATION_MODAL);
				        	   Client_View_playingStage view = new Client_View_playingStage (playingStage, model,false);
				        	   sl.setView_playingStage(view);
				        	   new Client_Controller_playingStage(model, sl.getPlayingStage(),gamePartyToJoin); 
				        	   sl.getPlayingStage().start();
						}
				       });
					
					break;
					
				case CancelGame:
					Platform.runLater(new Runnable() {

						@Override 
				           public void run() {
								sl.getPlayingStage().stop();	
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
