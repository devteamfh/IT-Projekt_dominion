package Dominion.Server.ServerClasses;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import Dominion.ServiceLocator;
import Dominion.Server.abstractClasses.Model;
import Dominion.appClasses.Player;



/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class Server_Model extends Model { //creates the connection to the server
    Logger logger;
    
    ServerSocket server;
    
    ArrayList <ObjectOutputStream> list_clients_output;
	
	ServiceLocator sl = ServiceLocator.getServiceLocator();
	
	Thread clientThread;
	
	Database db;

    public Server_Model() {
        
    	sl = ServiceLocator.getServiceLocator();
        logger = sl.getLogger(); 
    }

    /**
     * @author Joel Henz
     */
	public boolean runServer(InetAddress addr, int portNr) {
		try {
			this.server = new ServerSocket (portNr,10,addr);
			list_clients_output = new ArrayList <ObjectOutputStream>();
			logger.info("Server gestartet"); 
			startDatabase(); // kab: kreiert und startet Datenbank und kreiert Tabelle
			return true;
		} catch (IOException e) {
			logger.info("Server konnte nicht gestartet werden");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @author kab: startet Datenbank und kreiert Tabelle
	*/	
		public void startDatabase(){	
		db = new Database();
        db.createNewDatabase();
        db.createTables();
		}
	
	
	/**
     * @author Joel Henz
     */
	public void listenToClient() {
		
		while (true){
			try {

				Socket client = this.server.accept();
										
				ObjectOutputStream os = new ObjectOutputStream (client.getOutputStream()); 
				list_clients_output.add(os); 
							
				clientThread = new Thread (new ClientHandler(client, list_clients_output,os)); 
				clientThread.start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}	
	
	public boolean runServer2() {
		
		try {
			InetAddress addr = InetAddress.getByName("127.0.0.1");
			this.server = new ServerSocket (8000,10,addr);
			list_clients_output = new ArrayList <ObjectOutputStream>();
			logger.info("Server gestartet"); 
			//startDatabase(); // kab: kreiert und startet Datenbank und kreiert Tabelle
			return true;
		} catch (IOException e) {
			logger.info("Server konnte nicht gestartet werden");
			e.printStackTrace();
			return false;
		}
	}
	
}
