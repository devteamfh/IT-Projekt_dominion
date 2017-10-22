package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import Dominion.ServiceLocator;
import Dominion.appClasses.ChatMessage;
import Dominion.appClasses.GameObject;

/**
 * @author Joel Henz: 
 * this is the class for receiving chat messages from the server. If a client sends a chat message to server, the server will send that message to all clients
 * */
public class ReceiveMessagesFromServer implements Runnable {
	ObjectInputStream in;
	ServiceLocator sl = ServiceLocator.getServiceLocator();
	ChatMessage msg;
	
	public ReceiveMessagesFromServer (ObjectInputStream in){
		this.in = in;	
	}

	@Override
	public void run() {
		GameObject obj;
		
		try {
			while ((obj = (GameObject) this.in.readObject()) !=null){
				
				switch (obj.getType()) {
				 case ChatMessage:
					 ChatMessage msg = (ChatMessage) obj;					 
					 String name = msg.getName();
					 String text = msg.getMsg();
					 sl.getTextArea().appendText(name+": "+text+"\n");
					 sl.getTextArea().selectPositionCaret(sl.getTextArea().getText().length());				 
					 break;
				 
				 case InformationObject:
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
