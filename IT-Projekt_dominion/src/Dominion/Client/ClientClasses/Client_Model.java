package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.Model;

/**
 * MVC Pattern:
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class Client_Model extends Model {	
    ServiceLocator sl;
    Socket client_chat;
    ObjectInputStream in;
    ObjectOutputStream out;    
    Thread t1;    
    String PlayerName;    
    InetAddress addr;
    int port;    
    boolean connected = false;
  
    /**
     * @author Joel Henz
     */
    public Client_Model() {     
    	sl = ServiceLocator.getServiceLocator();
        ServiceLocator.getServiceLocator().increaseLoggedPlayer();
    }
       
    /**
     * @author Joel Henz: 
     * each client gets a separate Socket, ObjectInputStream, ObjectOutputStream and thread for: the chat
     */
	public void connectToServer(InetAddress addr,int port) {
		try {
			this.client_chat = new Socket(addr, port);
			this.in = new ObjectInputStream (this.client_chat.getInputStream());
			this.out = new ObjectOutputStream (this.client_chat.getOutputStream());
			sl.getLogger().info("Netzwerkverbindung hergestellt");
			Client_View_start.connectionResult.setText("Netzwerkverbindung hergestellt");
			connected = true;
			
			/** 
		     * this thread will read the messages from the server
		     */
			t1 = new Thread (new ReceiveMessagesFromServer(this.getInput_chat()));
	        t1.start();			
		} catch (ConnectException e) {
			sl.getLogger().info("Netzwerkverbindung konnte nicht hergestellt werden");
			Client_View_start.connectionResult.setText("Netzwerkverbindung konnte nicht hergestellt werden");
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}
	}
	
	/**
     * @author Joel Henz: 
     * getter and setter
     */	
	public void setName(String name){
		this.PlayerName = name;
	}
	
	public String getName(){
		return this.PlayerName;
	}
		
	public ObjectInputStream getInput_chat(){
		return this.in;
	}
	
}
