package Dominion.Server.ServerClasses;

import java.util.ArrayList;

import Dominion.appClasses.GameParty;
import Dominion.appClasses.Player;

/**
 * @author Joel Henz
 * this class is only needed on server side. It is the equivalent of the class GameParty, whose instances have to be sent by client and server. The instances of GamePartyOnServer
 * cannot be sent because they have an ArrayList of Player-objects (ObjectOutpuStream class is not serializable)
 */
public class GamePartyOnServer {
	
	private ArrayList <Player> playerList = new ArrayList <Player>();
	GameParty party;

	public GamePartyOnServer(GameParty party) {
		this.party=party;
	}
	
	public void addPlayer(Player player){
		this.playerList.add(player);
		this.party.addNewPlayer(player.getUsername());
	}
	
	public GameParty getGameParty(){
		return this.party;
	}
	

}
