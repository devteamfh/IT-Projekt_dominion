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
import Dominion.appClasses.GameObject;
import Dominion.appClasses.GameParty;
import Dominion.appClasses.JoinGameParty;
import Dominion.appClasses.Player;
import Dominion.appClasses.StartInformation;
import Dominion.appClasses.UpdateGameParty;
import Dominion.appClasses.UpdateLobby;
import javafx.scene.input.TouchPoint;

/**
 * @author Joel Henz: 
 * the ClientHandler is a server-side Runnable.
 * Each connected client gets an ClientHandler thread. Each ClientHandler reads messages from his client and will send them to ALL clients by iterating through the ObjectOutputStream ArrayList.
 */
public class ClientHandler implements Runnable {
	Socket s; 
	ArrayList <ObjectOutputStream> list;
	ObjectOutputStream out;
	ObjectInputStream in;

	ServiceLocatorServer sl;
	
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
		}
	}
	
	//ID for the serialized object is set here
	private void sendToAllClients(GameObject obj) throws IOException {
		
		//iterator for the OutPutStreams of all connected players in the lobby
		Iterator<ObjectOutputStream> iterOut = this.list.iterator();
		
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
				current.reset();
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
			 Iterator <GamePartyOnServer> iterGameParty = sl.getGameListFromServer().iterator();
			 
			 GamePartyOnServer currentGame=null;
			 
			 while(iterGameParty.hasNext()){				 
				 
				 currentGame = iterGameParty.next();
				 if(currentGame.getGameParty().getID()== id){
					 currentGame.addPlayer(newPlayerJoining);
					 break;
				 }
			 }			 

			 //now we have to update the number of connected players to this GameParty (update on the ListView of each client which is already in the lobby)
			 UpdateGameParty newUpdate = new UpdateGameParty (currentGame.getGameParty());
			 newUpdate.setID();
			 
			 while(iterOut.hasNext()){			 
				 ObjectOutputStream current = (ObjectOutputStream) iterOut.next();
				 current.reset();
				 current.writeObject(newUpdate);
				 current.flush();
			 }
			 
			 //finally we have to send a JoinGameParty object only to the joining client for creating his playing stage
			 this.out.reset();
			 this.out.writeObject(gameToJoin);
			 this.out.flush();
			 

			 break;
			 
		 case UpdateLobby:
			 UpdateLobby toUpdate = (UpdateLobby) obj;
			 toUpdate.setID();
			 
			 //the server searches all open gameparties, adds them to an ArrayList and sends this list (within an UpdateLobby-object) to the client which has made the request for updating his ListView
			 ///(message was send on client-side from class Client_View_lobby)
			 if(!sl.getGameListFromServer().isEmpty()){
				 ArrayList <GameParty> gamePartyListClient = new ArrayList <GameParty>();
				 Iterator <GamePartyOnServer> iterGamePartyOnServer = sl.getGameListFromServer().iterator();
				 
				 while (iterGamePartyOnServer.hasNext()){
					 GamePartyOnServer currentGamePartyOnServer = iterGamePartyOnServer.next();
					 
					 if(!(currentGamePartyOnServer.getGameParty().getLoggedInPlayers() == currentGamePartyOnServer.getGameParty().getMaxNumberOfPlayers())){
						 gamePartyListClient.add(currentGamePartyOnServer.getGameParty());
					 }
				 }
			 
				 toUpdate.setListOfOpenGames(gamePartyListClient);
				 
				 this.out.reset();
				 this.out.writeObject(toUpdate);
				 this.out.flush();				 
			 }
			 break;
		 
		 //the server reads the username of each connected player and stores them
		 case StartInformation:
			 StartInformation start = (StartInformation) obj;
			 start.setID();
			 
			 String username = start.getUsername();
			 String PW       = start.getPW();
			 
			 Player newPlayer = new Player (username, this.out);
			 
			 sl.addConnectedPlayer(newPlayer);
			 
			 sl.db_addPlayer(username, PW, "att2", "att3", "att4", "att5", "att6", "att7", "att8", "att9");
			 
			 
			 break;
			 
		 case CancelGame:
			 CancelGame cancel = (CancelGame) obj;
			 GameParty gamePartyToCancel = cancel.getGameParty();
			 
			 //searching the correspondent GamePartyOnServer to write to the players of this GameParty
			 long id2 = gamePartyToCancel.getID();
			 Iterator <GamePartyOnServer> iterGameParty2 = sl.getGameListFromServer().iterator();
			 
			 GamePartyOnServer currentGame2=null;
			 
			 while(iterGameParty2.hasNext()){				 
				 
				 currentGame2 = iterGameParty2.next();
				 if(currentGame2.getGameParty().getID()== id2){
					 break;
				 }
			 }
			 
			 //writing the CancelGame object to all players of the GameParty
			 for(int i=0; i < currentGame2.getPlayerList().size(); i++){
				 Player current = currentGame2.getPlayerList().get(i);
				 current.getOut().writeObject(cancel);
				 current.getOut().flush();
				// current.getOut().reset();
			 }
			 
			 
		 default:
		 }
	
	}
	
}
