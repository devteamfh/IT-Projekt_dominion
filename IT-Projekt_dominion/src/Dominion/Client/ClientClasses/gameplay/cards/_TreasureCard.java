package Dominion.Client.ClientClasses.gameplay.cards;

import java.awt.Button;
import javafx.scene.image.Image;

/**
 * @author Adrian Widmer
 */

public class _TreasureCard extends Button 		
{
	private String title;					
	private Image treasureImg;
	private int costs;						
	private int counter;					// TODO Was wird gezaehlt? Anzahl auf der Hand?
	private int treasureValue;				
	private int sizeOfStack;				// Was wenn Karte auf der Hand und nicht auf Deck? =0?
	private TreasureCardType type;
	
	public enum TreasureCardType				
	{
		Kupfer, Silber, Gold;
	}
	
	//constructor
	public _TreasureCard(TreasureCardType type) {
		super();
		this.type = type;
		switch(type){
        case Kupfer:
        	this.title = "Kupfer";
    		//this.treasureImg = ;					// TODO Pfad der Datei?
    		this.costs = 0;
    		this.treasureValue = 1; 
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		break;
        case Silber:
        	this.title = "Silber";
    		//this.treasureImg = ;					// TODO Pfad der Datei?
    		this.costs = 3;
    		this.treasureValue = 2; 
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		break;
        case Gold:
        	this.title = "Gold";
    		//this.treasureImg = ;					// TODO Pfad der Datei?
    		this.costs = 6;
    		this.treasureValue = 3; 
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		break;
       default:
            System.out.println("Karte konnte nicht erzeugt werden.");
        } 
	}

	//getter & setter
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public Image getTreasureImg() {
		return treasureImg;
	}

	public void setTreasureImg(Image treasureImg) {
		this.treasureImg = treasureImg;
	}
	
	public int getCosts() {
		return costs;
	}

	public void setCosts(int costs) {
		this.costs = costs;
	}

	public int getCounter() {
		return counter;	
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getTrasureValue() {
		return treasureValue;
	}

	public void setTrasureValue(int trasureValue) {
		this.treasureValue = trasureValue;
	}

	public int getSizeOfStack() {
		return sizeOfStack;
	}

	public void setSizeOfStack(int sizeOfStack) {
		this.sizeOfStack = sizeOfStack;
	}

	public TreasureCardType getType() {
		return type;
	}

	public void setType(TreasureCardType type) {
		this.type = type;
	}

	// Move card to DiscardPile
	public void throwOnDiscardPile()
		{
		// TODO Karte nach LinkedList discardPile verschieben
		}
	
	// Buy card
	public void buy()
		{
		// TODO analog ActionCard
		}
	
	// Trash card
	public void trashCard()				
	{
		// TODO Wo soll Karte hin? nur deleten?
	}
	
	//Play card
	public void playXy ()		
	{
		// TODO --> TreasureKarten sind nicht klickbar, sondern werden nur im Total angezeigt (numberOfTreasures) --> Vereinfacht Buy-Methode bei ActionCard
	}
	
}
