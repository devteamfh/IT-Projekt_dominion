package Dominion.appClasses;

/**
 * @author Joel Henz
 * instances of this class are sent/read by client and server. The equivalent class of GameParty is GamePartyOnServer. Objects of this class are only used on server side (to store Player objects and ObjectOutputStream-objects) and will not be sent
 * while communication
 */
public class GameParty extends GameObject{
	
	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	
	private String selectedMode;
	private String host;
	private int numberOfMaxPlayers;
	private int numberOfLoggedInPlayers = 0;
	private String [] playersOfThisGameParty;
	private int rounds;
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public GameParty(String selectedMode, String host, int number){
		super(GameObject.ObjectType.GameParty);
		this.selectedMode=selectedMode;
		this.host=host;
		this.numberOfMaxPlayers=number;
		this.playersOfThisGameParty = new String [this.numberOfMaxPlayers];
		this. id = -1;	
	}
	
	public void setID(){
		if (this. id == -1){
			this. id = nextMessageID();
		}
	}
	
	public long getID(){
		return this.id;
	}
	
	public String toString(){
		if(this.withRounds()){
			return "Host: "+this.host+", "+this.selectedMode+": "+this.rounds+", "+this.numberOfLoggedInPlayers+" Spieler von "+this.numberOfMaxPlayers+" eingeloggt";
		}else{
			return "Host: "+this.host+", "+this.selectedMode+", "+this.numberOfLoggedInPlayers+" Spieler von "+this.numberOfMaxPlayers+" eingeloggt";
		}
	}
	
	public String getHost(){
		return this.host;
	}
	
	//adding the player who is entering an existing GameParty (choosing from the ListView in the lobby)
	public void addNewPlayer(String player){
		this.playersOfThisGameParty[numberOfLoggedInPlayers] = player;
		this.numberOfLoggedInPlayers++;
		//this.propertyOfnumberOfLoggedInPlayers.set(numberOfLoggedInPlayers);
	}
	
	public int getMaxNumberOfPlayers(){
		return this.numberOfMaxPlayers;
	}
	
	public int getNumberOfLoggedInPlayers(){
		return this.numberOfLoggedInPlayers;
	}
	
	public String[] getArrayOfPlayers(){
		return this.playersOfThisGameParty;
	}
	
	public boolean isFull(){
		return this.numberOfLoggedInPlayers == this.numberOfMaxPlayers;
	}
	
	public boolean withRounds(){
		return this.selectedMode.equals("Rundenanzahl");
	}
	
	public void setRounds (int rounds){
		this.rounds=rounds;
	}
	

}