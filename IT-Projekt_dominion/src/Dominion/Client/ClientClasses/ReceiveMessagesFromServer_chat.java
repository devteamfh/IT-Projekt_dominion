package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import Dominion.ServiceLocator;
import Dominion.appClasses.ChatMessage;

/**
 * @author Joel Henz: 
 * this is the class for receiving chat messages from the server. If a client sends a chat message to server, the server will send that message to all clients
 * */
public class ReceiveMessagesFromServer_chat implements Runnable {
	ObjectInputStream in;
	ServiceLocator sl = ServiceLocator.getServiceLocator();
	ChatMessage msg;
	
	public ReceiveMessagesFromServer_chat (ObjectInputStream in){
		this.in = in;	
	}

	@Override
	public void run() {
		ChatMessage message;
	
		try {		
			while ((message = (ChatMessage) this.in.readObject()) !=null){
				String name = message.getName();
				String msg = message.getMsg();
				sl.getTextArea().appendText(name+": "+msg+"\n");
				sl.getTextArea().selectPositionCaret(sl.getTextArea().getText().length());
			}
			
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
