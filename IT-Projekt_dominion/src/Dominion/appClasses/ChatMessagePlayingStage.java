package Dominion.appClasses;


/**
 * @author Joel Henz
 */
public class ChatMessagePlayingStage extends GameObject {
	
	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	private String client;
	private String message;
	private GameParty party;
	// Generator for a unique message ID
	private static long messageID = 0;
	 
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public ChatMessagePlayingStage(String client, String message, GameParty party) {
		super(GameObject.ObjectType.ChatMessagePlayingStage);
		this. id = -1;
		this.client = client;
		this.message = message;
		this.party=party;
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
	
	public void setID2(long id){
		this.id=id;
	}
	   
	public long getID(){
		return this.id;
	}
	
	public GameParty getGameParty(){
		return this.party;
	}
	  
}