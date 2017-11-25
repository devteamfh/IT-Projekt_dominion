package Dominion.appClasses;

import javafx.scene.image.Image;

/**
 * @author David Steuri
 *
 */

public class VictoryCard {
	private int victoryPoints;
	private int costs;
	private int sizeOfStack;
	private String title;
	private Image victoryImg;

		
// Konstruktor für VictoryCard?
public VictoryCard(int VictoryPoints, int costs, int sizeOfStack, String title, Image victoryImg){
	this.victoryPoints = VictoryPoints;
	this.costs = costs;
	this.sizeOfStack = 10; // Muss auf dem Tisch 10 sein. Was passiert wenn gekauft?
	this.title = title;
	this.victoryImg = victoryImg;
	
	// Stimmt das so?
}
	

// Getter und Setter für VictoryPoints
public void setVictoryPoints(int victoryPoints){
	this.victoryPoints = victoryPoints;
	
}
public int getVictoryPoints(){
	return victoryPoints;
}
//Verkauf vom Stapel auf dem Tisch
public void buy(int sizeOfStack){
	this.sizeOfStack = sizeOfStack--;
	
}

//Getter und Setter für Title

public void setTitle(String title){
	this.title = title;
	
}
public String getTitle(){
	return title;
	
}

//Getter und Setter für Costs

public void setCosts(int costs){
	this.costs = costs;
	
}
public int getCosts(){
	return costs;
}

//Getter und Setter für Image
public void setVictoryImg(Image victoryImg){
	this.victoryImg = victoryImg;
}

public Image victoryImg(){
	return victoryImg;
}

// Auf Ablagestapel legen
public void throwOnDisCardPile(VictoryCard v){
	//Wohin?
	
}

public void trash(VictoryCard v){
	VictoryCard v1 = null;
	// Nicht klar wie...
}


}
