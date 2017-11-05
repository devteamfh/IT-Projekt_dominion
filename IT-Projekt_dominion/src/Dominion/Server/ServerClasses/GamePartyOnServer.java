package Dominion.Server.ServerClasses;

import java.util.ArrayList;

import Dominion.appClasses.NewGameParty;
import Dominion.appClasses.Player;

public class GamePartyOnServer {
	
	private ArrayList <Player> playerListOfGame = new ArrayList <Player>();
	private int loggedInPlayers = 0;
	NewGameParty party;

	public GamePartyOnServer(NewGameParty party) {
		this.party = party;
	}
	
	public void addPlayer(Player player){
		this.playerListOfGame.add(player);
		this.loggedInPlayers++;
	}
	
	public NewGameParty getGameParty(){
		return this.party;
	}

}
