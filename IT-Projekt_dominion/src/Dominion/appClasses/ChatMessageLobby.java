package Dominion.appClasses;


/**
 * @author Joel Henz
 */
public class ChatMessageLobby extends GameObject {
	
	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	private String client;
	private String message;
	// Generator for a unique message ID
	private static long messageID = 0;
	
	private Object obj;
	 
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public ChatMessageLobby(String client, String message) {
		super(GameObject.ObjectType.ChatMessageLobby);
		this. id = -1;
		this.client = client;
		this.message = message;
	}
	  
	public String getName(){
		return this.client;
	}
	   
	public String getMsg (){
		return this.message;
	}
	   
	public void setID(){
		if (this. id == -1){
			this. id = nextMessageID();
		}
	}
	   
	public long getID(){
		return this.id;
	}
	  
}
