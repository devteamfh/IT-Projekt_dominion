package Dominion.appClasses;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;


/**
 * @author Joel Henz, David Steuri
 * this player class doesn't save the ObjectOutputStreams(OS) -->used on client side
 */
public class PlayerWithoutOS implements Serializable {

	private static final long serialVersionUID = 1;
	// Data included in a message
	private long id;
	// Generator for a unique message ID
	private static long messageID = 0;
	
	private int points;
	private int numberOfActions;
	private int numberOfBuys;
	private int numberOfTreasures;
	private String username;
	// private Object<ActionCard> card; Wie kann ich die Objekte (ActionCard usw. zur LinkedList hinzufügen?
	public LinkedList<Object> discardPile = new LinkedList<Object>();
	public LinkedList<Object> playedCards = new LinkedList<Object>();
	public LinkedList<Object> cardsToPlay = new LinkedList<Object>();
	
	Random rand = new Random();
	
	//Konstruktor Player
	public PlayerWithoutOS(long id, String username, int points, int numberOfActions, int numberOfBuys, int numberOfTreasure, LinkedList<Object> discardPile, LinkedList<Object> playedCards, LinkedList<Object> cardsToPlay){
		this.id=-1;
		this.username = username;
		this.points = 3;
		this.numberOfActions = 1;
		this.numberOfBuys = 1;
		this.numberOfTreasures = 0;
		this.discardPile = discardPile.addAll(new VictoryCard("Estate")), new VictoryCard("Estate"), new VictoryCard("Estate")),
			new TreasureCard("Kupfer"),new TreasureCard("Kupfer"),new TreasureCard("Kupfer"),new TreasureCard("Kupfer"),new TreasureCard("Kupfer"),new TreasureCard("Kupfer"),new TreasureCard("Kupfer"); //Import ActionCard?
		this.playedCards = null;
		this.cardsToPlay = null;
	}
	
	private static long nextMessageID() {		
		return messageID++;
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
	
	public int getNumberOfActions(){
		return this.numberOfActions;
	}
	
	public void increaseNumberOfActions(int i){
		this.numberOfActions = this.numberOfActions+i;
	}
	
	public void decreaseNumberOfActions(){
		this.numberOfActions--;
	}
	
	public int getNumberOfBuys(){
		return this.numberOfBuys;
	}
	
	public void increaseNumberOfBuys(int i){
		this.numberOfBuys = this.numberOfBuys+i;
	}
	
	public void decreaseNumberOfBuys(){
		this.numberOfBuys--;
	}
	
	public int getNumberOfTreasures(){
		return this.numberOfTreasures;
	}
	
	public void increaseNumberOfTreasures(int i){
		this.numberOfTreasures = this.numberOfTreasures+i;
	}
	
	public void decreaseNumberOfTreasures(int i){
		this.numberOfTreasures = numberOfTreasures-i;
	}
	
	public void setPoints(int points){
		this.points = points;
		
	}
	
	public int getPoints(){
		return points;
	}
	
	// Nachziehen vom Ablagestapel auf den Spielstapel
	public void setCardsToPlay(LinkedList<Object> cardsToPlay){
		this.cardsToPlay = cardsToPlay;
		if (cardsToPlay.size() < 5){
			int pull = 5-(int)cardsToPlay.size();
			while (pull == 5) {
				Object c = discardPile.removeFirst();
				cardsToPlay.add(c);
				pull++;
			}
		}
		else{
			System.out.println("Sie können spielen");
		}
	}
	
	// Spielkarten anzeigen
	public Object getCardsToPlay(){
		for (Object element : cardsToPlay){
			//System.out.println(element);
			// korrekt wäre evtl? Oder Objekt direkt?
			if (element.equals(ActionCard)){
				getActionCardImg();				
			}
			else {
				if (element.equals(TreasureCard))
					getTreasureCardImg();
				else  {
				return img = getVictoryCardImg();
				}
			}
		}

	}

}