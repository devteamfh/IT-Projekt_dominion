package Dominion.Server.ServerClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.event.ListSelectionEvent;

import com.sun.glass.ui.Cursor;

import Dominion.appClasses.CancelGame;
import Dominion.appClasses.ChatMessageLobby;
import Dominion.appClasses.ChatMessagePlayingStage;
import Dominion.appClasses.GameHistory;
import Dominion.appClasses.GameObject;
import Dominion.appClasses.GameParty;
import Dominion.appClasses.JoinGameParty;
import Dominion.appClasses.PlayerWithOS;
import Dominion.appClasses.PlayerWithoutOS;
import Dominion.appClasses.StartInformation;
import Dominion.appClasses.UpdateLobby;
import Dominion.appClasses.GameHistory.HistoryType;
import javafx.scene.input.TouchPoint;

/**
 * @author Joel Henz: 
 * the ClientHandler is a server-side Runnable.
 * Each connected client gets an ClientHandler thread. Each ClientHandler reads messages from his client (via InputStream of the socket which was accepted by the ServerSocket in class
 * "Server_Model" and was passed on as parameter to the CLientHandler instance) and can send them to all clients by iterating through the ObjectOutputStream ArrayList.
 *	@author kab: StartInformation
 */
public class ClientHandler implements Runnable {
	private Socket s; 
	private ArrayList <ObjectOutputStream> list;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private PlayerWithOS PWOS_thisPlayer;
	private StringBuilder strBuilder = new StringBuilder();

	private ServiceLocatorServer sl;
	
	public ClientHandler(Socket s, ArrayList <ObjectOutputStream> list, ObjectOutputStream out){ 
		this.s = s;
		this.list = list;
		this.out = out;
		sl = ServiceLocatorServer.getServiceLocator();
		try {
			this.in = new ObjectInputStream(s.getInputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {

		GameObject obj;
		
		try {
			while ((obj = (GameObject) this.in.readObject()) !=null){
				sendToAllClients (obj);			
			}
		} catch (IOException | ClassNotFoundException e) {
			sl.getLogger().info("Exception catched:");
			e.printStackTrace();
			list.remove(this.out);
			sl.getConnectedPlayers().remove(PWOS_thisPlayer);
			
			//verbindung des disconecteten client wird geschlossen
			try { out.close();
			  in.close();
			  s.close();
			} catch (IOException e1) {
					e1.printStackTrace();
				}
			
			//Spieler Statistik Tabelle in Lobby muss upgedated werden, da ein Spieler spiel verlassen aht
				
				//hier wird der disconnectede Player gelöscht:
				sl.removeStartInfoStatistics(PWOS_thisPlayer.getUsername());
				
				//hier werden die derzeitigen Playerstatistics an alle Clients gesendet
				updatePlayerStatistics();
	
		}
	}
	
	//ID for the serialized object is set here
	private void sendToAllClients(GameObject obj) throws IOException {
		
		//iterator for the OutPutStreams of all connected players in the lobby
		Iterator<ObjectOutputStream> iterOut = this.list.iterator();

		Iterator<GamePartyOnServer> iterGamePartyOnServer = sl.getGameListFromServer().iterator();
		
		Iterator<PlayerWithOS> iter_connectedPlayers = sl.getConnectedPlayers().iterator();
		
		switch (obj.getType()) {
		 case ChatMessageLobby:	
			 //we dont change the chat msg objects, so we can resend it without creating new objects
			ChatMessageLobby msg = (ChatMessageLobby) obj;

			//sending the chat messages to all clients
				while (iterOut.hasNext()){
					ObjectOutputStream current = (ObjectOutputStream) iterOut.next();
					current.writeObject(msg);
					current.flush();			
				} 
			 break;
		 
		 //handle a client who is creating a new game
		 case GameParty:
			 GameParty game = (GameParty) obj;
			 game.setID();
			 
			 if(game.withRounds()){
         		game.setRounds(game.getRounds());
         	}
			 
			 
			//on server-side we must store the ObjectOutpuStream within the Player object
			 GamePartyOnServer newGameOnServer = new GamePartyOnServer (game);
			 PlayerWithOS current_player = new PlayerWithOS (game.getHost().getUsername(), this.out);
			 newGameOnServer.addPlayer(current_player);
			 
			 sl.addNewGame(newGameOnServer);
			 
			 //sending the message to all clients so they can update their ListView; we must send the GameParty-object, not the GamePartyOnServer because the class
			 //ObjectOutputStream (instance variable of Player class) is not serializable
			 while (iterOut.hasNext()){
				ObjectOutputStream current = (ObjectOutputStream) iterOut.next();
				current.writeObject(newGameOnServer.getGameParty());
				current.flush();
			 }

			 break;
		 
		 //handle a client entering an existing game
		 case JoinGameParty:
			 JoinGameParty gameToJoin = (JoinGameParty) obj;			 
			 
			 GameParty selectedGame = gameToJoin.getSelectedGameParty();
			 long id = selectedGame.getID();
			 		 
			 PlayerWithOS newPlayerJoining = new PlayerWithOS (gameToJoin.getUsername(), this.out);
			 
			 //first we will add the joining player to the correct GamePartyOnServer			 
			 for (int i=0; i<sl.getGameListFromServer().size();i++){
				 if(id == sl.getGameListFromServer().get(i).getGameParty().getID()){
					 sl.getGameListFromServer().get(i).addPlayer(newPlayerJoining);
					 if(sl.getGameListFromServer().get(i).getGameParty().isFull()){
						 //game can begin 
						 sl.getGameListFromServer().get(i).getGameParty().setGameHasStarted(true);
					 }
					 //the updated JoinGameParty object will be sent to all clients
					 gameToJoin.setUpdatedGameParty(sl.getGameListFromServer().get(i).getGameParty());
					 
					 //break the for-loop because we have found the right GameParty
					 break;
				 }
			 }
			 
			 //now we send the JoinGameParty to all clients.
			 while(iterOut.hasNext()){			 
				 ObjectOutputStream current = (ObjectOutputStream) iterOut.next();
				 current.reset(); //the GameParty object within JoinGameParty has changed, so we need to reset
				 current.writeObject(gameToJoin);
				 current.flush();
			 }

			 break;
			 
		 //used for new logged in players to create the ListView with the open GameParties	 
		 case UpdateLobby:
			 UpdateLobby toUpdate = (UpdateLobby) obj;
			 toUpdate.setID();
			 
			 //the server searches all open gameparties, adds them to an ArrayList and sends this list (within an UpdateLobby-object) to the client which has made the request for updating his ListView
			 //(message was sent from client-side from class Client_View_lobby)
			 if(!sl.getGameListFromServer().isEmpty()){
				 ArrayList <GameParty> gamePartyListClient = new ArrayList <GameParty>();
				 
				 while (iterGamePartyOnServer.hasNext()){
					 GamePartyOnServer currentGamePartyOnServer = iterGamePartyOnServer.next();
					 
					 if(!(currentGamePartyOnServer.getGameParty().getNumberOfLoggedInPlayers() == currentGamePartyOnServer.getGameParty().getMaxNumberOfPlayers())){
						 gamePartyListClient.add(currentGamePartyOnServer.getGameParty());
					 }
				 }
			 
				 toUpdate.setListOfOpenGames(gamePartyListClient);

				 this.out.writeObject(toUpdate);
				 this.out.flush();				 
			 }
			 break;
			 
		 
		 //kab: the server reads the username of each connected player and stores them
		 case StartInformation:
			 StartInformation start = (StartInformation) obj;
			 start.setID();
			 
			 String username = start.getUsername();
			 String PW       = start.getPW();
			 int gamesPlayed = start.getGamesPlayed();
			 int gamesWon    = start.getGamesWon();
			 int gamesLost   = start.getGamesLost();
			 int winLooseRto = start.getWinLooseRatio();
			 String att6     = start.getAtt6();
			 String att7	 = start.getAtt7();
			 String att8	 = start.getAtt8();
			 String att9 	 = start.getAtt9();
			 
			 
			 //prï¿½fe ob bereits ein User mit dem Namen auf dem Server vorhanden ist. Falls ja, sende disconnect Aufforderung zurï¿½ck
			 while (iter_connectedPlayers.hasNext()){ 
				 PlayerWithOS current = iter_connectedPlayers.next();	 
				 if(current.getUsername().equals(username)){
					 start.setBol_nameTaken(true);
					 sl.getConnectedPlayers().remove(current);
					 
					 this.out.reset();
					 out.writeObject(start);
					 out.flush();
					 break; 
					 }
			 } if (start.isBol_nameTaken())  break;
			 
			 PlayerWithOS newPlayer = new PlayerWithOS (username, this.out);
			 this.PWOS_thisPlayer = newPlayer;
			 
			 sl.addConnectedPlayer(newPlayer);
		
			 sl.db_addPlayer(username, PW, gamesPlayed, gamesWon, gamesLost, winLooseRto, att6,att7,att8,att9); 
				 
			 //hier werden alle Start Ifno STatistics, die der Server mal erhalten hat in eine StartInfoSTatistics Array List hineingelegt
			 sl.addNewStartInfoStatistics(start);

			 //in das Objekt STartInformation wird die komplette Liste mit allen STart Info
			 //Statistics auf dem Server gelegt
			 start.setListOfStartInformationObjects(sl.get_al_AllStartInfoStatisitcsOnServer());
			 
			 while (iterOut.hasNext()){
					ObjectOutputStream current = (ObjectOutputStream) iterOut.next();
					current.reset();
					current.writeObject(start);
					current.flush();

	
				 }

			 
			
			 
			 
			 break;
			 
		 case CancelGame:

			 CancelGame cancel = (CancelGame) obj;
			 cancel.setID();
			 GameParty gamePartyToCancel = cancel.getGameParty();
			 
			 //searching the correspondent GamePartyOnServer to write to the players of this GameParty
			 long id2 = gamePartyToCancel.getID();
			 	 
			 
			 //sending the CancelGame object to all clients so the game will be removed from their ListViews
			 while (iterOut.hasNext()){
				 //no reset needed because we won't send this object again
					ObjectOutputStream current = (ObjectOutputStream) iterOut.next();
					current.reset();
					current.writeObject(obj);
					current.flush();
			 } 
			 
			 //remove also the GamePartyOnServer
			 for (int i=0; i<sl.getGameListFromServer().size();i++){
				 if(id2 == sl.getGameListFromServer().get(i).getGameParty().getID()){
					 sl.getGameListFromServer().remove(i);
					 break;
				 }
			 }
			 
			 //sende die PlayerInfo Statistics erneut an alle Clients, da sobald ein Game Gecanceld wurde, die lobby gestoppt wurde.
			 //ein wiedereröffnnen der lobby erforderd, dass die Palyer Statistics wieder gesendet werden
				updatePlayerStatistics();
			 
			 
		 break;
		 
		 case ChatMessagePlayingStage:			 
			//sending the chat messages to the players of the corresponding GameParty	
			 /**ChatMessagePlayingStage currentMsgPlay = (ChatMessagePlayingStage) obj;
			ChatMessagePlayingStage msg_obj = new ChatMessagePlayingStage(currentMsgPlay.getName(), currentMsgPlay.getMsg(), currentMsgPlay.getGameParty());
			msg_obj.setID2(currentMsgPlay.getID());*/
			 ChatMessagePlayingStage msg_obj = (ChatMessagePlayingStage) obj;
			 msg_obj.setID();
			 
			
			while(iterGamePartyOnServer.hasNext()){
				GamePartyOnServer current = iterGamePartyOnServer.next();
				
				if(current.getGameParty().getID() == msg_obj.getGameParty().getID()){
					for(int i=0; i<current.getPlayerList().size();i++){
						current.getPlayerList().get(i).getOut().writeObject(msg_obj);
						current.getPlayerList().get(i).getOut().flush();
					}
					break;
				}
			}
			break;
			
		 case GameHistory:
			 
			 GameHistory history =(GameHistory) obj;
			 history.setID();
			 
			 while(iterGamePartyOnServer.hasNext()){
					GamePartyOnServer current = iterGamePartyOnServer.next();
					
					//determine the corresponding GamePartyOnServer
					if(current.getGameParty().getID() == history.getGameParty().getID()){
						
						switch (history.getHistoryType()){
							
						case EndAction:
						
							//writing to all clients
							for (int i =0; i<current.getPlayerList().size();i++){
								//maybe reset needed because the GameParty object could have been changed (f.e. one player has left the game -> -1 player)
								//current.getPlayerList().get(i).getOut().reset();
								current.getPlayerList().get(i).getOut().writeObject(history);
								current.getPlayerList().get(i).getOut().flush();
							}
							//break case EndAction
							break;
							
						case EndBuy:

							//check first if there was a buy of a point card
							try{  

								if(history.getGameCard_EN().equals("estate") || history.getGameCard_EN().equals("duchy") || history.getGameCard_EN().equals("province") || history.getGameCard_EN().equals("curse")){
									//first search the corresponding GamePartyOnServer
									GamePartyOnServer current2=null;
									long id4 = history.getGameParty().getID();									
									for(int i =0; i<sl.getGameListFromServer().size();i++){
										if(id4 == (sl.getGameListFromServer().get(i).getGameParty().getID())){
											current2 = sl.getGameListFromServer().get(i);
											//break for loop
											break;
										}
									}
									
									//search the player who gained points
									String currentPlayer = history.getCurrentPlayer().getUsername();
									for(int i=0; i<current2.getGameParty().getArrayListOfPlayers().size();i++){
										if(currentPlayer.equals(current2.getGameParty().getArrayListOfPlayers().get(i).getUsername())){
											current2.getGameParty().getArrayListOfPlayers().get(i).setPoints(history.getCurrentPlayer().getPoints());
										}
									}
									
									//set the updated GameParty within GameHistory
									history.updateGameParty(current2.getGameParty());
									
								}
								
								
							}catch (NullPointerException e){
								//if the String GameCard is null
							}
							
							
							//determine the index of the current player
							int indexOfCurrentPlayer = determineIndexOfCurrentPlayer(history);
							int indexOfNextPlayer = determineIndexOfNextPlayer(indexOfCurrentPlayer,history);
							//we will save the name of the next player within the GameHistory object. On client side for each client it will be checked if this name corresponds to the name of the client. If yes we will
							//activate his GUI (and only his!)
							PlayerWithoutOS nextPlayer = history.getGameParty().getArrayListOfPlayers().get(indexOfNextPlayer);
							//set the name of the player whose GUI has to get activated
							history.setPlayerForGUIActivation(nextPlayer);							
							
							if(indexOfNextPlayer ==0){
								
								//a new round will begin because the last player in the sequence has finished his buy phase
								//in this method we also check if the max number of rounds is reached (when game party with mode "Rundenanzahl)...the game would end here
								prepareNewRound(history,nextPlayer);
								
							}else{
								strBuilder.append(history.getTextForTextArea());
								String update = "Spieler "+nextPlayer.getUsername()+" ist an der Reihe\n";
								strBuilder.append(update);
								history.updateTextForTextArea(strBuilder.toString());
								strBuilder.delete(0, strBuilder.length());
							}
							
							//writing to all clients
							for (int i =0; i<current.getPlayerList().size();i++){
								current.getPlayerList().get(i).getOut().reset();
								current.getPlayerList().get(i).getOut().writeObject(history);
								current.getPlayerList().get(i).getOut().flush();
							}
							
							//last but not least: delete the GameParty on server-side if the game has ended (defined number of rounds in GameMode "nach Runden" reached)
							if(history.getGameParty().getGameHasEnded()){
								for(int i=0; i<sl.getGameListFromServer().size();i++){
									if(history.getGameParty().getID() == sl.getGameListFromServer().get(i).getGameParty().getID()){
										sl.getGameListFromServer().remove(i);
										
										//kab: Player Statistik Tabelle auf allen Clients müssen auch noch upgedated werden
										updatePlayerStatistics();
										
									}
								}
							}
		
							//break case EndBuy
							break;
							
						case LeaveGame:
							
							//first: check if the leaving player is the current player and determine the next player in the sequence 
							if(history.getCurrentPlayer() !=null){
								//if the leaving player is also the current player we have to activate the GUI of the next player in the sequence
								indexOfCurrentPlayer = determineIndexOfCurrentPlayer(history);
								indexOfNextPlayer = determineIndexOfNextPlayer(indexOfCurrentPlayer,history);
								
								//we will save the name of the next player within the GameHistory object. On client side for each client it will be checked if this name corresponds to the name of the client. If yes we will
								//activate his GUI (and only his!)
								nextPlayer = history.getGameParty().getArrayListOfPlayers().get(indexOfNextPlayer);
								//set the name of the player whose GUI haas to get activated
								history.setPlayerForGUIActivation(nextPlayer);	
								
								if(indexOfNextPlayer ==0){
									
									//a new round will begin because the last player in the sequence has left the game
									//in this method we also check if the max number of rounds is reached (when game party with mode "Rundenanzahl)...the game would end here
									prepareNewRound(history,nextPlayer);
									
								}else{
									strBuilder.append(history.getTextForTextArea());
									String update = "Spieler "+nextPlayer.getUsername()+" ist an der Reihe\n";
									strBuilder.append(update);
									history.updateTextForTextArea(strBuilder.toString());
									strBuilder.delete(0, strBuilder.length());
								}
								
							}else{
								
							}
					
							//write to all players of the corresponding GameParty
							for (int i =0; i<current.getPlayerList().size();i++){
								//current.getPlayerList().get(i).getOut().reset();
								current.getPlayerList().get(i).getOut().writeObject(history);
								current.getPlayerList().get(i).getOut().flush();
							}
							
							//remove leaving player in the corresponding GamePartyOnServer
							long id3 = history.getGameParty().getID();
							for (int i=0; i<sl.getGameListFromServer().size();i++){
								 if(id3 == sl.getGameListFromServer().get(i).getGameParty().getID()){
									 sl.getGameListFromServer().get(i).removePlayer(history.getLeavingPlayer());
									 //the game ends automatically if there is only one player left (and the other players have left the game) -->remove GameParty on server-side
									 if(sl.getGameListFromServer().get(i).getPlayerList().size() == 1){
										 sl.getGameListFromServer().remove(i);
									 }

									 //break the for-loop because we have found the right GameParty
									 break;
								 }
							 }
							
							
							/**try{
								
								history = new GameHistory (currentHistory.getText(),currentHistory.getGameParty(),currentHistory.getCurrentPlayer(),GameHistory.HistoryType.LeaveGame);
								history.setID2(currentHistory.getID());
								//first: check if the leaving player is the current player and determine the next player in the sequence 
								if(history.getCurrentPlayer() !=null){
									//if the leaving player is also the current player we have to activate the GUI of the next player in the sequence
									int indexOfCurrentPlayer = determineIndexOfCurrentPlayer(history);
									int indexOfNextPlayer = determineIndexOfNextPlayer(indexOfCurrentPlayer,history);
									
									//we will save the name of the next player within the GameHistory object. On client side for each client it will be checked if this name corresponds to the name of the client. If yes we will
									//activate his GUI (and only his!)
									PlayerWithoutOS nextPlayer = history.getGameParty().getArrayListOfPlayers().get(indexOfNextPlayer);
									//set the name of the player whose GUI haas to get activated
									history.setPlayerForGUIActivation(nextPlayer);	
									
									if(indexOfNextPlayer ==0){
										
										//a new round will begin because the last player in the sequence has left the game
										//in this method we also check if the max number of rounds is reached (when game party with mode "Rundenanzahl)...the game would end here
										prepareNewRound(history,nextPlayer);
										
									}else{
										strBuilder.append(history.getText());
										String update = "Spieler "+nextPlayer.getUsername()+" ist an der Reihe\n";
										strBuilder.append(update);
										history.updateText(strBuilder.toString());
										strBuilder.delete(0, strBuilder.length());
									}
									
								}else{
									
								}
						
								//write to all players of the corresponding GameParty
								for (int i =0; i<current.getPlayerList().size();i++){
									//current.getPlayerList().get(i).getOut().reset();
									current.getPlayerList().get(i).getOut().writeObject(history);
									current.getPlayerList().get(i).getOut().flush();
								}
								
								//remove leaving player in the corresponding GamePartyOnServer
								long id3 = history.getGameParty().getID();
								for (int i=0; i<sl.getGameListFromServer().size();i++){
									 if(id3 == sl.getGameListFromServer().get(i).getGameParty().getID()){
										 sl.getGameListFromServer().get(i).removePlayer(history.getLeavingPlayer());
										 //the game ends automatically if there is only one player left (and the other players have left the game) -->remove GameParty on server-side
										 if(sl.getGameListFromServer().get(i).getPlayerList().size() == 1){
											 sl.getGameListFromServer().remove(i);
										 }

										 //break the for-loop because we have found the right GameParty
										 break;
									 }
								 }
							}catch (NullPointerException e){
								
							}*/
							
							
										

							//break case LeaveGame
							break;
							
						case UpdateLobbyAfterLeave:
							for (int i =0; i<sl.getConnectedPlayers().size();i++){
								sl.getConnectedPlayers().get(i).getOut().writeObject(history);
								sl.getConnectedPlayers().get(i).getOut().flush();
							}
							
							
							/**try{
								history = new GameHistory (currentHistory.getText(),currentHistory.getGameParty(),currentHistory.getCurrentPlayer(),GameHistory.HistoryType.UpdateLobbyAfterLeave);
								history.setID2(currentHistory.getID());
								//send the msg to all logged in players so their lobby will be updated
								for (int i =0; i<sl.getConnectedPlayers().size();i++){
									sl.getConnectedPlayers().get(i).getOut().writeObject(history);
									sl.getConnectedPlayers().get(i).getOut().flush();
								}
							}catch (NullPointerException e){
								//
							}*/
							
							
							
							//break case UpdateLobbyAfterLeave
							break;
							
						case PlayCard:
							
							for (int i =0; i<current.getPlayerList().size();i++){
								current.getPlayerList().get(i).getOut().reset();
								current.getPlayerList().get(i).getOut().writeObject(history);
								current.getPlayerList().get(i).getOut().flush();
							}

							
							break;
							
						case BuyNoPointCard:
							for (int i =0; i<current.getPlayerList().size();i++){
								current.getPlayerList().get(i).getOut().writeObject(history);
								current.getPlayerList().get(i).getOut().flush();
							}
							
							break;
							
						case BuyPointCard:
							if(history.getGameCard_EN().equals("estate") || history.getGameCard_EN().equals("duchy") || history.getGameCard_EN().equals("province") || history.getGameCard_EN().equals("curse")){
								//first search the corresponding GamePartyOnServer
								long id4 = history.getGameParty().getID();
								GamePartyOnServer current3=null;
								for(int i =0; i<sl.getGameListFromServer().size();i++){
									if(id4 == (sl.getGameListFromServer().get(i).getGameParty().getID())){
										current3 = sl.getGameListFromServer().get(i);
										//break for loop
										break;
									}
								}
								
								//search the player who gained points
								String currentPlayer = history.getCurrentPlayer().getUsername();

								for(int i=0; i<current3.getGameParty().getArrayListOfPlayers().size();i++){
									if(currentPlayer.equals(current3.getGameParty().getArrayListOfPlayers().get(i).getUsername())){
										current3.getGameParty().getArrayListOfPlayers().get(i).setPoints(history.getCurrentPlayer().getPoints());
									}
								}
								
								//set the updated GameParty within GameHistory
								history.updateGameParty(current3.getGameParty());
							}
							
							
							for (int i =0; i<current.getPlayerList().size();i++){
								current.getPlayerList().get(i).getOut().reset();
								current.getPlayerList().get(i).getOut().writeObject(history);
								current.getPlayerList().get(i).getOut().flush();
							}

							//break case
							break;
							
						case Trash:
							
							try{
								
								if(history.getGameCard_EN().equals("estate") || history.getGameCard_EN().equals("duchy") || history.getGameCard_EN().equals("province") || history.getGameCard_EN().equals("curse")){
									long id4 = history.getGameParty().getID();
									GamePartyOnServer current3=null;
									for(int i =0; i<sl.getGameListFromServer().size();i++){
										if(id4 == (sl.getGameListFromServer().get(i).getGameParty().getID())){
											current3 = sl.getGameListFromServer().get(i);
											//break for loop
											break;
										}
									}
									
									//search the player who gained points
									String currentPlayer = history.getCurrentPlayer().getUsername();

									for(int i=0; i<current3.getGameParty().getArrayListOfPlayers().size();i++){
										if(currentPlayer.equals(current3.getGameParty().getArrayListOfPlayers().get(i).getUsername())){
											current3.getGameParty().getArrayListOfPlayers().get(i).setPoints(history.getCurrentPlayer().getPoints());
										}
									}
									
									//set the updated GameParty within GameHistory
									history.updateGameParty(current3.getGameParty());
								}
								
							}catch (NullPointerException e){
								//
							}

							for (int i =0; i<current.getPlayerList().size();i++){
								current.getPlayerList().get(i).getOut().reset();
								current.getPlayerList().get(i).getOut().writeObject(history);
								current.getPlayerList().get(i).getOut().flush();
							}
							//break case Trash
							break;
							
						case MineModeEnd:
							for (int i =0; i<current.getPlayerList().size();i++){
								current.getPlayerList().get(i).getOut().writeObject(history);
								current.getPlayerList().get(i).getOut().flush();
							}
							
							break;
							
						case Discard:
							for (int i =0; i<current.getPlayerList().size();i++){
								current.getPlayerList().get(i).getOut().writeObject(history);
								current.getPlayerList().get(i).getOut().flush();
							}
							
							break;
							
						case RebuildingModeEnd:
							
							try{
								
								if(history.getGameCard_EN().equals("estate") || history.getGameCard_EN().equals("duchy") || history.getGameCard_EN().equals("province") || history.getGameCard_EN().equals("curse")){
									//first search the corresponding GamePartyOnServer
									long id4 = history.getGameParty().getID();
									GamePartyOnServer current3=null;
									for(int i =0; i<sl.getGameListFromServer().size();i++){
										if(id4 == (sl.getGameListFromServer().get(i).getGameParty().getID())){
											current3 = sl.getGameListFromServer().get(i);
											//break for loop
											break;
										}
									}
									
									//search the player who gained points
									String currentPlayer = history.getCurrentPlayer().getUsername();

									for(int i=0; i<current3.getGameParty().getArrayListOfPlayers().size();i++){
										if(currentPlayer.equals(current3.getGameParty().getArrayListOfPlayers().get(i).getUsername())){
											current3.getGameParty().getArrayListOfPlayers().get(i).setPoints(history.getCurrentPlayer().getPoints());
										}
									}
									
									//set the updated GameParty within GameHistory
									history.updateGameParty(current3.getGameParty());
								}
								
							}catch (NullPointerException e){
								//
							}
							
							for (int i =0; i<current.getPlayerList().size();i++){
								current.getPlayerList().get(i).getOut().writeObject(history);
								current.getPlayerList().get(i).getOut().flush();
							}
							
							break;	
							
						case WorkshopModeEnd:
							
							try{
								
								if(history.getGameCard_EN().equals("estate") || history.getGameCard_EN().equals("duchy") || history.getGameCard_EN().equals("province") || history.getGameCard_EN().equals("curse")){
									//first search the corresponding GamePartyOnServer
									long id4 = history.getGameParty().getID();
									GamePartyOnServer current3=null;
									for(int i =0; i<sl.getGameListFromServer().size();i++){
										if(id4 == (sl.getGameListFromServer().get(i).getGameParty().getID())){
											current3 = sl.getGameListFromServer().get(i);
											//break for loop
											break;
										}
									}
									
									//search the player who gained points
									String currentPlayer = history.getCurrentPlayer().getUsername();

									for(int i=0; i<current3.getGameParty().getArrayListOfPlayers().size();i++){
										if(currentPlayer.equals(current3.getGameParty().getArrayListOfPlayers().get(i).getUsername())){
											current3.getGameParty().getArrayListOfPlayers().get(i).setPoints(history.getCurrentPlayer().getPoints());
										}
									}
									
									//set the updated GameParty within GameHistory
									history.updateGameParty(current3.getGameParty());
								}
								
							}catch (NullPointerException e){
								//
							}
							
							for (int i =0; i<current.getPlayerList().size();i++){
								current.getPlayerList().get(i).getOut().reset();
								current.getPlayerList().get(i).getOut().writeObject(history);
								current.getPlayerList().get(i).getOut().flush();
							}
							
							break;
							
						case Reaction:
							
							try{
								if(history.getGameCard_EN().equals("curse")){
									//first search the corresponding GamePartyOnServer
									long id4 = history.getGameParty().getID();
									GamePartyOnServer current3=null;
									for(int i =0; i<sl.getGameListFromServer().size();i++){
										if(id4 == (sl.getGameListFromServer().get(i).getGameParty().getID())){
											current3 = sl.getGameListFromServer().get(i);
											//break for loop
											break;
										}
									}
									
									//search the player who gained points
									String currentPlayer = history.getPlayerForWitchMode().getUsername();

									for(int i=0; i<current3.getGameParty().getArrayListOfPlayers().size();i++){
										if(currentPlayer.equals(current3.getGameParty().getArrayListOfPlayers().get(i).getUsername())){
											current3.getGameParty().getArrayListOfPlayers().get(i).setPoints(history.getPlayerForWitchMode().getPoints());
										}
									}
									
									//set the updated GameParty within GameHistory
									history.updateGameParty(current3.getGameParty());
									
								}
							}catch (NullPointerException e){
								//
							}
																	
							for (int i =0; i<current.getPlayerList().size();i++){
								current.getPlayerList().get(i).getOut().reset();
								current.getPlayerList().get(i).getOut().writeObject(history);
								current.getPlayerList().get(i).getOut().flush();
							}
							
							break;
							
						case AttackEnd:
							
							for (int i =0; i<current.getPlayerList().size();i++){
								//current.getPlayerList().get(i).getOut().reset();
								current.getPlayerList().get(i).getOut().writeObject(history);
								current.getPlayerList().get(i).getOut().flush();
							}
							
							break;

						
						}

						//break while loop because corresponding GamePartyOnServer was found
						break;
					}
				}
			 //break case GameHistory
			 break;
		 
		 default:
		 }
	
	}
	
	private int determineIndexOfCurrentPlayer(GameHistory history){
		PlayerWithoutOS currentPlayer = history.getCurrentPlayer();
		int indexOfCurrentPlayer = -1;
		for (int i=0; i<history.getGameParty().getArrayListOfPlayers().size();i++){
			//check if the String of the current index i corresponds to the String sender. If it corresponds, we have found the index of the current player.
			if(currentPlayer.getUsername().equals(history.getGameParty().getArrayListOfPlayers().get(i).getUsername())){
				indexOfCurrentPlayer = i;
			}
		}
		return indexOfCurrentPlayer;
	}

	
	private int determineIndexOfNextPlayer (int indexOfCurrentPlayer,GameHistory history){
		if(indexOfCurrentPlayer == ( (history.getGameParty().getArrayListOfPlayers().size()) -1 )){
			//first player in the sequence will start again (begin of a new round)
			return 0;		
		}else{
			return indexOfCurrentPlayer+1;
		}
	}
	
	private PlayerWithoutOS determineWinner(GameParty party){
		//first search the corresponding GamePartyOnServer
		GamePartyOnServer currentParty =null;
		for(int i=0; i<sl.getGameListFromServer().size();i++){
			if(party.getID() == sl.getGameListFromServer().get(i).getGameParty().getID()){
				currentParty = sl.getGameListFromServer().get(i);
			}
		}
		
		PlayerWithoutOS winner =null;
		PlayerWithoutOS current = null;
		
		for(int i=0; i<currentParty.getGameParty().getArrayListOfPlayers().size();i++){
			current =currentParty.getGameParty().getArrayListOfPlayers().get(i);
			for(int i2=0; i2<currentParty.getGameParty().getArrayListOfPlayers().size();i2++){
				if(currentParty.getGameParty().getArrayListOfPlayers().get(i2).getPoints()>current.getPoints()){
					winner=currentParty.getGameParty().getArrayListOfPlayers().get(i2);
				}
			}
		}
		
		System.out.println(winner.getUsername());
		return winner;
		
		
		/**PlayerWithoutOS winner =null;
		PlayerWithoutOS current = null;
		
		for(int i=0; i<party.getArrayListOfPlayers().size();i++){
			current =party.getArrayListOfPlayers().get(i);
			for(int i2=0; i2<party.getArrayListOfPlayers().size();i2++){
				if(party.getArrayListOfPlayers().get(i2).getPoints()>current.getPoints()){
					winner=party.getArrayListOfPlayers().get(i2);
				}
			}
		}
		
		return winner;*/
	}
	
	private ArrayList <PlayerWithoutOS> determineLoser(PlayerWithoutOS winner, GameParty party){
		ArrayList <PlayerWithoutOS> loserList = cloneList(party.getArrayListOfPlayers());
		
		for(int i=0; i<loserList.size();i++){
			if(winner.getUsername().equals(loserList.get(i).getUsername())){
				loserList.remove(i);
				break;
			}
		}
		return loserList;
	}
	
	public ArrayList<PlayerWithoutOS> cloneList(ArrayList<PlayerWithoutOS> list) {
	    ArrayList<PlayerWithoutOS> clone = new ArrayList<PlayerWithoutOS>(list.size());
	    for(int i=0; i<list.size();i++){
	    	clone.add(new PlayerWithoutOS(list.get(i)));
	    }

	    return clone;
	}
	
	public void prepareNewRound(GameHistory history, PlayerWithoutOS nextPlayer){
		//first search the corresponding GamePartyOnServer
		GamePartyOnServer current=null;
		for(int i=0; i<sl.getGameListFromServer().size();i++){
			if(sl.getGameListFromServer().get(i).getGameParty().getID() == history.getGameParty().getID()){
				current = sl.getGameListFromServer().get(i);
				break;
			}
		}
		
		current.getGameParty().increasePlayedRounds();
		history.updateGameParty(current.getGameParty());
		
		//only checked if we play with number of rounds. The game will end if true
		if(history.getGameParty().withRounds() && history.getGameParty().getRoundCounter() == history.getGameParty().getRounds()){
			history.getGameParty().setGameHasEnded(true);
			history.clearText();
			
			PlayerWithoutOS winner = determineWinner(history.getGameParty());
			
			if(winner !=null){
				strBuilder.append("Gewinner ist: "+winner.getUsername()+"\n");
				
			
				
			//kab: es Müssen noch die Spielerstatistiken upgedated werden nach einem Spiel
				//@joel die methhode unten geht nur, wenn es nur einen gewinner gibt. wenn du jetzt in der zwischenzeit was umgebaut hast, dann muss ich meine methode dann auch noch anpassen
				
				//Finde heraus, welche Spieler so eben gespielt haben um dessen statistiken auf der lobby tabelle upzudaten
				Iterator<StartInformation> iterStatisticsOnServer = sl.get_al_AllStartInfoStatisitcsOnServer().iterator();
				while (iterStatisticsOnServer.hasNext()){
					StartInformation cursor = iterStatisticsOnServer.next();
					
					for (int i = 0; i < current.getGameParty().getArrayListOfPlayers().size();i++){
						//jedem Spieler wird ein gespieltes Spiel hinzugefügt
						if (current.getGameParty().getArrayListOfPlayers().get(i).getUsername().equals(cursor.getUsername())){
							cursor.setGamesPlayed(cursor.getGamesPlayed()+1);
							
							//Falls der cursor auf dem gewinner steht wird ein gewonnenes Spiel hinzugefügt
							if (winner.getUsername().equals(cursor.getUsername())){
								cursor.setGamesWon(cursor.getGamesWon()+1);
							} 
							//falls der cursor nicht auf dem gewinner steht, wird ein verlorenes Spiel hinzugefügt
							else {   
								cursor.setGamesLost(cursor.getGamesLost()+1);
							}
							//zum schluss aktualisieren wir noch das gewonnnene spiele in prozent verhältins
							
							cursor.setWinLooseRatio(cursor.getGamesWon()*100/cursor.getGamesPlayed());

						}
					
						
						
					
					}
				}
			
				
				
				ArrayList<PlayerWithoutOS> loser = determineLoser(winner,history.getGameParty());
				strBuilder.append("Verloren haben: ");
				for(int i=0; i<loser.size();i++){
					strBuilder.append(loser.get(i).getUsername()+",");
				}

			}else{
				strBuilder.append("Unentschieden!");

			}
			history.setNewType(GameHistory.HistoryType.EndGame);
			history.setWinner(winner); //is null when all players have the same number of points
			history.updateTextForTextArea(strBuilder.toString());
			strBuilder.delete(0, strBuilder.length());
			
			
		}else{
			strBuilder.append(history.getTextForTextArea());
			String update = "Runde "+(history.getGameParty().getRoundCounter()-1)+" abgeschlossen\n_________________\nSpieler "+nextPlayer.getUsername()+" ist an der Reihe\n";
			strBuilder.append(update);
			//history.getGameParty().increasePlayedRounds();
			history.updateTextForTextArea(strBuilder.toString());
			strBuilder.delete(0, strBuilder.length());
			
		}
		
	}
	
	private void updatePlayerStatistics(){
		
		 StartInformation updateStatistics = new StartInformation();
		 updateStatistics.setListOfStartInformationObjects(sl.get_al_AllStartInfoStatisitcsOnServer());
		
			try {
				Iterator<ObjectOutputStream> iterOut = this.list.iterator();
				 while (iterOut.hasNext()){
						ObjectOutputStream current = (ObjectOutputStream) iterOut.next();
						current.reset();
						current.writeObject(updateStatistics);
						current.flush();
					 }
								
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	
	
}
