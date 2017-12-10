package Dominion.Server.ServerClasses;

import java.util.ArrayList;

import Dominion.appClasses.GameParty;
import Dominion.appClasses.PlayerWithOS;
import Dominion.appClasses.PlayerWithoutOS;
import Dominion.appClasses.StartInformation;

/**
 * @author Joel Henz
 * this class is only needed on server side. It is the equivalent of the class GameParty, whose instances have to be sent by client and server. The instances of GamePartyOnServer
 * cannot be sent because they have an ArrayList of Player-objects (ObjectOutpuStream class is not serializable)
 */
public class GamePartyOnServer {
	
	private ArrayList <PlayerWithOS> playerList = new ArrayList <PlayerWithOS>();
	private GameParty party;

	
	public GamePartyOnServer(GameParty party) {
		this.party=party;
	}
	
	public void addPlayer(PlayerWithOS player_OS){
		PlayerWithoutOS player_noOS = new PlayerWithoutOS(player_OS.getUsername());
		//add the player on server side
		this.playerList.add(player_OS);
		//add player to the GameParty (for client side)
		this.party.addNewPlayer(player_noOS);
	}
	
	public void removePlayer(PlayerWithoutOS player){
		//remove the player from the GamePartyOnServer object
		for(int i=0; i<this.playerList.size();i++){
			if(this.playerList.get(i).getUsername().equals(player.getUsername())){
				this.playerList.remove(i);
			}
		}
		
		//remove the player from the GameParty object
		this.party.removePlayer(player);
		
		for(int i=0; i<this.playerList.size();i++){
		}
				
	}
	
	public GameParty getGameParty(){
		return this.party;
	}
	
	public ArrayList <PlayerWithOS> getPlayerList(){
		return this.playerList;
	}
	
	public void setCurrentPlayer(){
		
	}
	

	

}
