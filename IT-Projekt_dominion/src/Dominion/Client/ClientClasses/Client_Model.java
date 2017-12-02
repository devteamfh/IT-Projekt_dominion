package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.Model;
import Dominion.appClasses.Player;

/**
 * MVC Pattern:
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC), Joel Henz (resources, methods)
 */
public class Client_Model extends Model {	
    ServiceLocatorClient sl;
    Socket client;
    private ObjectInputStream in;
    ObjectOutputStream out;    
    Thread t1;    
    String playerName;    
    private InetAddress addr;
    private int port;    
    boolean connected = false;
  
    /**
     * @author Joel Henz
     */
    public Client_Model() {     
    	sl = ServiceLocatorClient.getServiceLocator();
    }
       
    /**
     * @author Joel Henz: 
     * each client gets a separate Socket, ObjectInputStream, ObjectOutputStream and thread for: the chat
     */
	public void connectToServer(InetAddress addr,int port) {
		try {
			
			
			this.client = new Socket(addr, port);
			this.in = new ObjectInputStream (this.client.getInputStream());
			this.out = new ObjectOutputStream (this.client.getOutputStream());
			sl.getLogger().info("Netzwerkverbindung hergestellt");
			Client_View_start.connectionResult.setText("Netzwerkverbindung hergestellt");
			this.connected = true;
			
			/** 
		     * this thread will read the messages from the server
		     */
			t1 = new Thread (new ReadMsgFromServer(this.getInput(),this));
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
     * getter and setter for resources
     */	
	public void setName(String name){
		this.playerName = name;
	}
	
	public String getName(){
		return this.playerName;
	}
		
	public ObjectInputStream getInput(){
		return this.in;
	}
	
	public ObjectOutputStream getOutput(){
		return this.out;
	}
	
	public Socket getSocket (){
		return this.client;
	}
	
}
