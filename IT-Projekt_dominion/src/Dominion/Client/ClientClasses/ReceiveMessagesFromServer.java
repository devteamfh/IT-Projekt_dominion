package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import Dominion.ServiceLocator;
import Dominion.appClasses.ChatMessageLobby;
import Dominion.appClasses.GameObject;
import Dominion.appClasses.GameParty;
import javafx.application.Platform;

/**
 * @author Joel Henz: 
 * this is the class for receiving chat messages from the server. If a client sends a chat message to server, the server will send that message to all clients
 * */
public class ReceiveMessagesFromServer implements Runnable {
	ObjectInputStream in;
	ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
	ChatMessageLobby msg;
	
	public ReceiveMessagesFromServer (ObjectInputStream in){
		this.in = in;	
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
					 
				 case GameParty:
					 GameParty newGame = (GameParty) obj;
					 System.out.println(newGame.toString());
					 
					 Platform.runLater(new Runnable() {
				            @Override 
				            public void run() {
				            	sl.addNewGame(newGame);
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
