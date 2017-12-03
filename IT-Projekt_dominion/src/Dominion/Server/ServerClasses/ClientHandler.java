package Dominion.Server.ServerClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import Dominion.appClasses.CancelGame;
import Dominion.appClasses.ChatMessageLobby;
import Dominion.appClasses.ChatMessagePlayingStage;
import Dominion.appClasses.GameHistory;
import Dominion.appClasses.GameObject;
import Dominion.appClasses.GameParty;
import Dominion.appClasses.JoinGameParty;
import Dominion.appClasses.Player;
import Dominion.appClasses.StartInformation;
import Dominion.appClasses.UpdateLobby;
import javafx.scene.input.TouchPoint;

/**
 * @author Joel Henz: 
 * the ClientHandler is a server-side Runnable.
 * Each connected client gets an ClientHandler thread. Each ClientHandler reads messages from his client and can send them to all clients by iterating through the ObjectOutputStream ArrayList.
 */
public class ClientHandler implements Runnable {
	private Socket s; 
	private ArrayList <ObjectOutputStream> list;
	private ObjectOutputStream out;
	private ObjectInputStream in;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("exception");
		}
	}
	
	//ID for the serialized object is set here
	private void sendToAllClients(GameObject obj) throws IOException {
		
		//iterator for the OutPutStreams of all connected players in the lobby
		Iterator<ObjectOutputStream> iterOut = this.list.iterator();

		Iterator<GamePartyOnServer> iterGamePartyOnServer = sl.getGameListFromServer().iterator();
		
		Iterator<Player> iter_connectedPlayers = sl.getConnectedPlayers().iterator();
		
		switch (obj.getType()) {
		 case ChatMessageLobby:	
			//sending the chat messages to all clients
				while (iterOut.hasNext()){
					ObjectOutputStream current = (ObjectOutputStream) iterOut.next();
					current.writeObject(obj);
					current.flush();			
				} 
			 break;
		 
		 //handle a client who is creating a new game
		 case GameParty:
			 GameParty game = (GameParty) obj;
			 game.setID();
			 
			//on server-side we must store the ObjectOutpuStream within the Player object
			 GamePartyOnServer newGameOnServer = new GamePartyOnServer (game);
			 Player current_player = new Player (game.getHost(), this.out);
			 newGameOnServer.addPlayer(current_player);
			 
			 sl.addNewGame(newGameOnServer);
			 
			 //sending the message to all clients so they can update their ListView; we must send the GameParty-object, not the GamePartyOnServer because the class
			 //ObjectOutputStream (instance variable of Player class) is not serializable
			 while (iterOut.hasNext()){
				ObjectOutputStream current = (ObjectOutputStream) iterOut.next();
				current.writeObject(game);
				current.flush();
			 }

			 break;
		 
		 //handle a client entering an existing game
		 case JoinGameParty:
			 JoinGameParty gameToJoin = (JoinGameParty) obj;
			 gameToJoin.setID();
			 
			 GameParty selectedGame = gameToJoin.getSelectedGameParty();
			 long id = selectedGame.getID();
			 
			 
			 Player newPlayerJoining = new Player (gameToJoin.getUsername(), this.out);
			 
			 //first we will add the joining player to the correct GamePartyOnServer			 
			 for (int i=0; i<sl.getGameListFromServer().size();i++){
				 if(id == sl.getGameListFromServer().get(i).getGameParty().getID()){
					 sl.getGameListFromServer().get(i).addPlayer(newPlayerJoining);
					 //the updated JoinGameParty object will be sent to all clients
					 gameToJoin.setUpdatedGameParty(sl.getGameListFromServer().get(i).getGameParty());
				 }
			 }
			 
			 //now we send the JoinGameParty to all clients.
			 while(iterOut.hasNext()){			 
				 ObjectOutputStream current = (ObjectOutputStream) iterOut.next();
				 current.reset(); //reset is necessary so the clients will read the updated GameParty on switch case JoinGameParty
				 current.writeObject(gameToJoin);
				 current.flush();
			 }

			 break;
			 
		 case UpdateLobby:
			 UpdateLobby toUpdate = (UpdateLobby) obj;
			 toUpdate.setID();
			 
			 //the server searches all open gameparties, adds them to an ArrayList and sends this list (within an UpdateLobby-object) to the client which has made the request for updating his ListView
			 //(message was sent from client-side from class Client_View_lobby)
			 if(!sl.getGameListFromServer().isEmpty()){
				 ArrayList <GameParty> gamePartyListClient = new ArrayList <GameParty>();
				 //Iterator <GamePartyOnServer> iterGamePartyOnServer = sl.getGameListFromServer().iterator();
				 
				 while (iterGamePartyOnServer.hasNext()){
					 GamePartyOnServer currentGamePartyOnServer = iterGamePartyOnServer.next();
					 
					 if(!(currentGamePartyOnServer.getGameParty().getNumberOfLoggedInPlayers() == currentGamePartyOnServer.getGameParty().getMaxNumberOfPlayers())){
						 gamePartyListClient.add(currentGamePartyOnServer.getGameParty());
					 }
				 }
			 
				 toUpdate.setListOfOpenGames(gamePartyListClient);
				 
				 //this.out.reset();
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
			 
			 
			 //prüfe ob bereits ein User mit dem Namen auf dem Server vorhanden ist. Falls ja, sende disconnect Aufforderung zurück
			 while (iter_connectedPlayers.hasNext()){ 
				 Player current = iter_connectedPlayers.next();
					 if(current.getUsername().equals(username)){
					 start.setBol_nameTaken(true);
					
					 this.out.reset();
					 out.writeObject(start);
					 out.flush();
					 break; 
					 }
			 } if (start.isBol_nameTaken())  break;
			 
			 
			 
			 Player newPlayer = new Player (username, this.out);
			 
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
			 GameParty gamePartyToCancel = cancel.getGameParty();
			 
			 //searching the correspondent GamePartyOnServer to write to the players of this GameParty
			 long id2 = gamePartyToCancel.getID();
			 
			 //sending the CancelGame object to all clients so the game will be removed from their ListViews
			 while (iterOut.hasNext()){
					ObjectOutputStream current = (ObjectOutputStream) iterOut.next();
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
			 
			 
		 break;
		 
		 case ChatMessagePlayingStage:			 
			//sending the chat messages to the players of the corresponding GameParty	
			ChatMessagePlayingStage msg_obj = (ChatMessagePlayingStage) obj;
			
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
			 GameHistory history = (GameHistory) obj;
			 
			 while(iterGamePartyOnServer.hasNext()){
					GamePartyOnServer current = iterGamePartyOnServer.next();
					
					if(current.getGameParty().getID() == history.getGameParty().getID()){
						for(int i=0; i<current.getPlayerList().size();i++){
							if(history.getSwitchPlayer() == false){
								current.getPlayerList().get(i).getOut().writeObject(history);
								current.getPlayerList().get(i).getOut().flush();
							}else{
								
							}
						}
						break;
					}
				}
		 
		 default:
		 }
	
	}
	
	
	
}
