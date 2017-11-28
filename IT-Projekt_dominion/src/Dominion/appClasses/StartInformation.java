package Dominion.appClasses;

import java.util.ArrayList;

/**
 * @author Joel Henz
 */
public class StartInformation extends GameObject {
	
	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	private ArrayList<StartInformation> listOfStatrtInformation = new ArrayList <StartInformation>();
	
	private String 	username;
	private String 	PW;
	private int 	gamesPlayed;
	private int 	gamesWon;
	private int		gamesLost;
	private int 	winLooseRatio;
	private String  att6;
	private String  att7;
	private String  att8;
	private String  att9;
	
	private static long nextMessageID() {		
		return messageID++;
	}
	
	public StartInformation(String username) {
		super(GameObject.ObjectType.StartInformation);
		this.username = username;
		
	}
	
	public void setID(){
		if (this. id == -1){
			this. id = nextMessageID();
		}
	}

	
	public long getID(){
		return this.id;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getPW(){
		return this.PW;
	}
	
	

	public int getLoggedInPlayers() {
		// TODO Auto-generated method stub
		return 0;
	}
	

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}

	public int getGamesWon() {
		return gamesWon;
	}

	public void setGamesWon(int gamesWon) {
		this.gamesWon = gamesWon;
	}

	public int getGamesLost() {
		return gamesLost;
	}

	public void setGamesLost(int gamesLost) {
		this.gamesLost = gamesLost;
	}

	public int getWinLooseRatio() {
		return winLooseRatio;
	}

	public void setWinLooseRatio(int winLooseRatio) {
		this.winLooseRatio = winLooseRatio;
	}

	public String getAtt6() {
		return att6;
	}

	public void setAtt5(String att6) {
		this.att6 = att6;
	}

	public String getAtt7() {
		return att7;
	}

	public void setAtt7(String att7) {
		this.att7 = att7;
	}

	public String getAtt8() {
		return att8;
	}

	public void setAtt8(String att8) {
		this.att8 = att8;
	}

	public String getAtt9() {
		return att9;
	}

	public void setAtt9(String att9) {
		this.att9 = att9;
	}

	
	//wtf?
	public void setListOfStartInformationObjects(ArrayList <StartInformation> list){
		this.listOfStatrtInformation = list;
	}
	
	public ArrayList <StartInformation> getListOfStartInformationObjects(){
		return this.listOfStatrtInformation;
	}
	


}
