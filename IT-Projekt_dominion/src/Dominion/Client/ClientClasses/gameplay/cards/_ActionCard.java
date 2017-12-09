package Dominion.Client.ClientClasses.gameplay.cards;

import java.awt.Button;
import javafx.scene.image.Image;

/**
 * @author Adrian Widmer
 */
/*
public class ActionCard extends Button
{
	private String title;					
	private String description;				
	private Image actionImg;						
	private int costs;						
	private int counter;					
	private int sizeOfStack;						// TODO Was wenn Karte auf der Hand und nicht auf Deck? =0?
	private String typeOfAction;					// TODO Fuer was ist diese Variable?
	private ActionCardType type;
	private String player;
		
	public enum ActionCardType				
	{
		Kapelle, Keller, Burggraben, Dorf, Holzfaeller, Werkstatt, Geldverleiher, Miliz, Schmiede, Umbau, Mine, Markt, Laboratorium, Jahrmarkt, Hexe;
	}
	
	//Konstruktor mit Typ der Aktionskarte
	public ActionCard(ActionCardType type) {
		super();
		this.type = type;
		switch(type){
        case Kapelle:
        	this.title = "Kapelle";
        	this.description = "Entsorge bis zu 4 Karten aus deiner Hand.";
    		//this.actionImg = ;					// TODO Pfad der Datei?
    		this.costs = 2;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.typeOfAction = "Kapelle";		
            break;
        case Keller:
        	this.title = "Keller";
        	this.description = "+1 Aktion & Lege eine beliebig Anzahl Karten aus deiner Hand ab. Ziehe f�r jede abgelegte Karte eine Karte nach.";
    		//this.actionImg = ;					// TODO Pfad der Datei?
    		this.costs = 2;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.typeOfAction = "Keller";
            break;
        case Burggraben:
        	this.title = "Burggraben";
        	this.description = "+2 Karten & Wenn ein Mitspieler eine Angriffskarte ausspielt, darfst du diese Karte aus deiner Hand aufdecken. Der Angriff hat dann keine Wirkung auf dich.";
    		//this.actionImg = ;					// TODO Pfad der Datei?
    		this.costs = 2;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.typeOfAction = "Burggraben";
            break;
        case Dorf:
        	this.title = "Dorf";
        	this.description = "+1 Karte & +2 Aktionen";
    		//this.actionImg = ;					// TODO Pfad der Datei?
    		this.costs = 3;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.typeOfAction = "Dorf";
            break;
        case Holzfaeller:
        	this.title = "Holzfaeller";
        	this.description = "+1 Kauf & +2 Geld";
    		//this.actionImg = ;					// TODO Pfad der Datei?
    		this.costs = 3;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.typeOfAction = "Holzfaeller";
    		break;
        case Werkstatt:
        	this.title = "Werkstatt";
        	this.description = "Nimm dir eine Karte, die bis zu 4 Geld kostet.";
    		//this.actionImg = ;					// TODO Pfad der Datei?
    		this.costs = 3;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.typeOfAction = "Werkstatt";
    		break;
        case Geldverleiher:
        	this.title = "Geldverleiher";
        	this.description = "Entsorge ein Kupfer aus deiner Hand. Wenn du das machst: +3 Geld";
    		//this.actionImg = ;					// TODO Pfad der Datei?
    		this.costs = 4;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.typeOfAction = "Geldverleiher";
    		break;
        case Miliz:
        	this.title = "Miliz";
        	this.description = "+2 Geld & Jeder Mitspieler muss Karten ablegen, bis er nur noch 3 Karten auf der Hand hat.";
    		//this.actionImg = ;					// TODO Pfad der Datei?
    		this.costs = 4;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.typeOfAction = "Miliz";
    		break;
        case Umbau:
        	this.title = "Umbau";
        	this.description = "Entsorge eine Karte aus deiner Hand. Nimm dir eine Karte, die bis zu 2 Geld mehr kostet, als die entsorgte Karte.";
    		//this.actionImg = ;					// TODO Pfad der Datei?
    		this.costs = 4;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.typeOfAction = "Umbau";
    		break;
        case Mine:
        	this.title = "Mine";
        	this.description = "Entsorge eine Geldkarte aus deiner Hand. Nimm dir eine Geldkarte, die bis zu 3 Geld mehr kostet. Nimm diese Geldkarte sofort auf die Hand.";
    		//this.actionImg = ;					// TODO Pfad der Datei?
    		this.costs = 5;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.typeOfAction = "Mine";
    		break;
        case Markt:
        	this.title = "Markt";
        	this.description = "+1 Karte, +1 Aktion, +1 Kauf & +1 Geld";
    		//this.actionImg = ;					// TODO Pfad der Datei?
    		this.costs = 5;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.typeOfAction = "Markt";
    		break;
        case Laboratorium:
        	this.title = "Laboratorium";
        	this.description = "+2 Karten & +1 Aktion";
    		//this.actionImg = ;					// TODO Pfad der Datei?
    		this.costs = 5;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.typeOfAction = "Laboratorium";
    		break;
        case Jahrmarkt:
        	this.title = "Jahrmarkt";
        	this.description = "+2 Aktionen, +1 Kauf & +2 Geld";
    		//this.actionImg = ;					// TODO Pfad der Datei?
    		this.costs = 5;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.typeOfAction = "Jahrmarkt";
    		break;
        case Hexe:
        	this.title = "Hexe";
        	this.description = "+2 Karten & Jeder Mitspieler muss sich eine Fluckarte nehmen";
    		//this.actionImg = ;					// TODO Pfad der Datei?
    		this.costs = 5;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.typeOfAction = "Hexe";
    		break;
        default:
            System.out.println("Karte konnte nicht erzeugt werden.");
        } 
	}

	//Getter & Setter
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Image getActionImg() {
		return actionImg;
	}

	public void setActionImg(Image actionImg) {
		this.actionImg = actionImg;
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

	public int getSizeOfStack() {
		return sizeOfStack;
	}

	public void setSizeOfStack(int sizeOfStack) {
		this.sizeOfStack = sizeOfStack;
	}

	public String getTypeOfAction() {
		return typeOfAction;
	}

	public void setTypeOfAction(String typeOfAction) {
		this.typeOfAction = typeOfAction;
	}

	public ActionCardType getType() {
		return type;
	}

	public void setType(ActionCardType type) {
		this.type = type;
	}
	
	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	// Karte aus der Hand auf gespielte Karte werfen, ohne Aktion auszufuehren
	public void throwOnDiscardPile()		// TODO
		{
		// TODO Karte nach LinkedList discardPile verschieben --> Warten bis LinkedList in Klasse Player erstellt ist
		}

	// Karte kaufen
	public void buy(String player)
		{
		this.player = player;
		// TODO Vorbedingung: Kaufbare Karten sind aktiviert, nicht kaufbare Karten sind deaktiviert --> Stage?
		// Abfangen wie viele K�ufe offen sind
			if (player.getNumberOfBuys()>= 1) {
				// Abfangen, dass Preis der Karte kleiner oder gleich dem Geld auf Hand ist
				if (player.getNumberOfTreasures()<=this.costs) {
					// Dem Player die Kosten der Karte dem Geld auf der Hand abziehen
					player.decreaseNumberOfTreasures(this.costs);
					// Dem Player einen Kauf abziehen
					player.decreaseNumberOfBuys();
					// TODO Entsprechende TreasureCards zu LinkedList discardPile verschieben --> discardPile = gespielte Karten?
					// TODO Actioncard auf erworbene Karten anzeigen 
					// TODO Karte in LinkedList cardsOfDeck erzeugen
				} else {
					System.out.println("Zu wenig Geld auf der Hand umd diese Karte zu kaufen");
					}
		} else {
			System.out.println("Zu wenig offene K�ufe um einen Kauf auszuf�hren");
			}
		}
	
	// Karte wegwerfen (Aus der Hand und dem Deck entfernen)
	public void trashCard()				// TODO
		{
		// TODO Wo soll Karte hin? nur deleten? --> Meiner Meinung nach geh�rt dies in die Klasse Player (Objekt aus LinkedList entfernen)
		}
	
	// Aktionskarte spielen
	public void playCard (ActionCardType type, String player)
		{
		this.player = player;
		// Abfangen wie viele Aktionen offen sind
		if (player.getNumberOfActions()>= 1) {
			switch(type){
			case Kapelle:
				// TODO	4 Karten aus der Hand entsorgen. Was bringt das?
				break;
			case Keller:
				// +1 Aktion
				player.increaseNumberOfActions(1);
				// TODO Lege eine beliebige Anzahl Karten aus deiner Hand ab. Ziehe f�r jede abgelegte Karte eine Karte nach.
				break;
			case Burggraben:
				// TODO +2 Karten
				// TODO Wenn ein Mitspieler eine Angriffskarte ausspielt, darfst du diese Karte aus deiner Hand aufdecken. Der Angriff hat dan keine Wirkung auf dich.
				break;
			case Dorf:
				// TODO +1 Karte
				// +2 Aktionen
				player.increaseNumberOfActions(2);
				break;
			case Holzfaeller:
				// +1 Kauf
				player.increaseNumberOfBuys(1);
				// +2 Geld
				player.increaseNumberOfTreasures(2);
				break;
			case Werkstatt:
				// TODO Nimm dir eine Karte, die bis zu 4 Geld kostet.
				break;
			case Geldverleiher:
				// TODO Entsorge ein Kupfer aus deiner Hand. 
				//Wenn du das machst: +3 Geld
				player.increaseNumberOfTreasures(3);
				break;
			case Miliz:
				// +2 Geld
				player.increaseNumberOfTreasures(2);
				// TODO Jeder Spieler muss Karten ablegen, bis er nur noch 3 Karten auf der Hand hat.
				break;
			case Umbau:
				// TODO Entsorge eine Karte aus deiner Hand. Nimm dir eine Karte, die bis zu 2 Geld mehr kostet, als die entsorgte Karte.
				break;
			case Mine:
				// TODO Entsorge eine Geldkarte aus deiner Hand. Nimm dir eine Geldkarte, die bis zu 3 Geld mehr kostet. Nimm diese Karte sofort auf die Hand.
				break;
			case Markt:
				// TODO +1 Karte
				// +1 Aktion
				player.increaseNumberOfActions(1);
				// +1 Kauf
				player.increaseNumberOfBuys(1);
				// +1 Geld
				player.increaseNumberOfTreasures(1);
				break;
			case Laboratorium:
				// TODO +2 Karten
				// +1 Aktion
				player.increaseNumberOfActions(1);
				break;
			case Jahrmarkt:
				// +2 Aktionen
				player.increaseNumberOfActions(2);
				// +1 Kauf
				player.increaseNumberOfBuys(1);
				// +2 Geld
				player.increaseNumberOfTreasures(2);
				break;
			case Hexe:
				// TODO +2 Karten
				// TODO Jeder Mitspieler muss sich eine Fluchkarte nehmen
				break;
			default:
				System.out.println("Karte konnte nicht gespielt werden.");
			// Dem Spieler eine Aktion abziehen
			player.decreaseNumberOfActions();
			}
		} else {
			System.out.println("Zu wenig offene Aktionen um eine Aktion auszuf�hren");
		}
	}
}
*/