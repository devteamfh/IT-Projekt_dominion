package Dominion.Server.ServerClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import Dominion.appClasses.ChatMessage;
import Dominion.appClasses.GameObject;

/**
 * @author Joel Henz: 
 * the ClientHandler is a server-side Runnable.
 * Each connected client gets an ClientHandler thread. Each ClientHandler reads messages from his client and will send them to ALL clients by iterating through the ObjectOutputStream ArrayList.
 * This implementation (reading and writing with a Runnable) was inspired by youtube XXX
 */
public class ClientHandler implements Runnable {
	Socket s; 
	ArrayList <ObjectOutputStream> list;
	ObjectOutputStream out;
	ObjectInputStream in;
	
	public ClientHandler(Socket s, ArrayList <ObjectOutputStream> list){ 
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
				
		switch (obj.getType()) {
		 case ChatMessage:
			 ChatMessage msg = (ChatMessage) obj;
			 
			 Iterator<ObjectOutputStream> iter = this.list.iterator();
			 
			//sending the chat messages to all clients
				while (iter.hasNext()){
					ObjectOutputStream current = (ObjectOutputStream) iter.next();
					current.writeObject(msg);
					current.flush();			
				} 
			 break;
		 
		 case InformationObject:
			 break;

		 default:
		 }
	
	}
	
}
