package Dominion.appClasses;

public class GameParty {
	
	private String seletcedMode;
	private String creator;
	private int numberOfPlayers;
	
	public GameParty(String selectedMode, String creator, int number){
		this.seletcedMode=selectedMode;
		this.creator=creator;
		this.numberOfPlayers=number;
		
	}

}
