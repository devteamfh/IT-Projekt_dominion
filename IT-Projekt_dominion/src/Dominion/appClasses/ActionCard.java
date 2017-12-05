package Dominion.appClasses;

import java.awt.Button;					// https://docs.oracle.com/javase/8/docs/api/
import java.awt.Image;					// https://docs.oracle.com/javase/8/docs/api/

/**
 * @author Adrian Widmer
 */

<<<<<<< HEAD
public class ActionCard extends Button
=======
public class ActionCard //extends Button 		// TODO Button importieren? TODO Refactoring? --> Abstrakte Klasse?
>>>>>>> master
{
	private String title;					
	private String description;				
	private Image img;						
	private int costs;						
	private int counter;					// TODO Was wird gez�hlt? --> l�schen
	private int sizeOfStack;				// TODO Was wenn karte auf der Hand und nicht auf Deck? =0?
	private String typeOfAction;			// TODO f�r was ist diese variable?
	public enum actionCardType				
	{
<<<<<<< HEAD
		Kapelle, Keller, Burggraben, Dorf, Holzfaeller, Werkstatt, Geldverleiher, Miliz, Schmiede, Umbau, Mine, Markt, Laboratorium, Jahrmarkt, Hexe;
=======
		Kapelle, Keller, Burggraben, Dorf, Holzfäller, Werkstatt, Geldverleiher, Miliz, Schmiede, Umbau, Mine, Markt, Laboratorium, Jahrmarkt, Hexe;
>>>>>>> master
	}
	
	public String getTypeOfAction() {
		return typeOfAction;
	}

	public void setTypeOfAction(String typeOfAction) {
		this.typeOfAction = typeOfAction;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}


	public void setCosts(int costs) {
		this.costs = costs;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void setSizeOfStack(int sizeOfStack) {
		this.sizeOfStack = sizeOfStack;
	}
	
	public ActionCard()						// TODO Verschiedene Konstruktoren notwendig? Hand oder Deck?
	{
	
	}
	
	public void throwOnDiscardPile()		// TODO
	{
	
	/* 1. Karte zu eingesetzte Karten verschieben --> LinkedList discardPile
	 * 2. weitere aktion?
	 */
	}
	
	public void buy()						// TODO
	/* Vorbedingung: Kaufbare Karten sind aktiviert, nicht kaufbare Karten sind deaktiviert UND numberOfBuys >= 1
	 * 1. costs der ActionCard <= numberOfTreasures 
	 * 2. Abgleichen mit treasure Karten auf der Hand
	 * 3. Treasurekarten auf der hand abziehen
	 * 4. Actioncard auf gekaufte karten erg�nzen --> Objekt erzeugen
	 */
	{
		
	}
	
	public void trashCard()				// TODO
	{
		//Wo soll Karte hin? analog eingesetzte Karten
	}
		
	public void playXy ()		// TODO --> Pro Enum eine Methode?
	{
	/* 1. Switch Statement --> https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html
	 * 	
	 */
	}
	
}
