package Dominion.Server.ServerClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import Dominion.appClasses.ChatMessage;

/**
 * @author Joel Henz: 
 * the ClientHandler is a server-side Runnable.
 * Each connected client gets an ClientHandler thread. Each ClientHandler reads messages from his client and will send them to ALL clients by iterating through the ObjectOutputStream ArrayList.
 * This implementation (reading and writing with a Runnable) was inspired by youtube XXX
 */
public class ClientHandler_chat implements Runnable {
	Socket s; 
	ArrayList <ObjectOutputStream> list;
	ObjectOutputStream out;
	ObjectInputStream in;
	
	public ClientHandler_chat(Socket s, ArrayList <ObjectOutputStream> list){ 
		this.s = s;
		this.list = list;
		try {
			this.in = new ObjectInputStream(s.getInputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {

		ChatMessage message;
		
		try {
			while ((message = (ChatMessage) this.in.readObject()) !=null){
				sendToAllClients (message);			
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//ID for the serialized object is set here
	private void sendToAllClients(ChatMessage message) throws IOException {
		Iterator<ObjectOutputStream> iter = this.list.iterator();
		message.setID();
		
		//sending the chat messages to all clients
		while (iter.hasNext()){
			ObjectOutputStream current = (ObjectOutputStream) iter.next();
			current.writeObject(message);
			current.flush();			
		}
		
	}
	
}
