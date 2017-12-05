package Dominion.appClasses;					// TODO mit Testklasse (Klon von Client_View_playingStage) bis 9.12.2017

/**
 * @author Adrian Widmer
 */

public class TreasureCard //extends Button 		// TODO Button importieren?
{
	
	private String title;					
	private String description;				
											// TODO Image als Variable?
	private int costs;						
	private int counter;					// TODO Was wird gez�hlt? Anzahl auf der Hand?
	private int trasureValue;				
	private int sizeOfStack;				// Was wenn karte auf der Hand und nicht auf Deck? =0?
	
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
		return trasureValue;
	}
	public void setTrasureValue(int trasureValue) {
		this.trasureValue = trasureValue;
	}
	public int getSizeOfStack() {
		return sizeOfStack;
	}
	public void setSizeOfStack(int sizeOfStack) {
		this.sizeOfStack = sizeOfStack;
	}

	public TreasureCard()					// TODO Verschiedene Konstruktoren notwendig? Hand oder Deck?
	{
	
	}
	
	public void throwOnDiscardPile()		// TODO
	{
	/* 1. Karte zu eingesetzte Karten verschieben
	 * 2. Karte aus Hand entfernen
	 */
	}
	
	public void buy()						// TODO
	/* Vorbedingung: Kaufbare Karten sind aktiviert, nicht kaufbare Karten sind deaktiviert
	 * 1. Abfragen costs der Deckkarte 
	 * 2. Abgleichen mit treasure Karten auf der Hand
	 * 3. Treasurekarten auf der hand abziehen
	 * 4. Actioncard auf gekaufte karten erg�nzen
	 */
	{
		
	}
	
	public void trashCard()				// TODO
	{
		//Wo soll Karte hin?
	}
		
	public void playXy ()		// TODO --> Pro Enum eine Methode?
	{
	/* 1. Switch Statement --> https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html
	 * 	
	 */
	}
	
}
