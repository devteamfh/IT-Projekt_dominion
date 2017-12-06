package Dominion.appClasses.Cards;

import java.util.ArrayList;


import javax.swing.ImageIcon;
import javax.swing.JButton;

import Dominion.appClasses.PlayerWithoutOS;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;


/**
 * @author David Steuri
 *
 */

//Switch Statement für verschiedene Karten

public class VictoryCard extends Button {
	private String title;					
	private ImageIcon victoryImg;						
	private int costs;						
	private int counter;
	private int points;
	private int sizeOfStack;						// TODO Was wenn Karte auf der Hand und nicht auf Deck? =0?
	private String typeOfVictory;					// TODO Fuer was ist diese Variable?
	private VictoryCardType type;
	private String player;

	public enum VictoryCardType {
		Estate, Duchy, Province
	}

	
	// Konstruktor
	public VictoryCard(VictoryCardType type) {
		super();
		this.type = type;
		switch(type){
        case Estate:
        	this.title = "Anwesen";
        	this.victoryImg = new ImageIcon("/IT-Projekt_dominion/Cards/Cards/Punkte/Punkte_01.jpg");
    		this.costs = 2;
    		//this.counter = 0;						// TODO Siehe Frage oben
    		//this.sizeOfStack = sizeOfStack;		// TODO Initial 0?
    		this.points = 1;
    		this.typeOfVictory = "Anwesen";		
            break;
        case Duchy:
        	this.title = "Herzogtum";
        	this.victoryImg = new ImageIcon("/IT-Projekt_dominion/Cards/Cards/Punkte/Punkte_03.jpg");
        	this.costs = 5;
        	this.points = 3;
        	this.typeOfVictory = "Herzogtum";
        	break;
        case Province:
        	this.title = "Provinz";
        	this.victoryImg = new ImageIcon("/IT-Projekt_dominion/Cards/Cards/Punkte/Punkte_06.jpg");
        	this.costs = 8;
        	this.points = 6;
        	this.typeOfVictory = "Provinz";       
        default:
            System.out.println("Karte konnte nicht erzeugt werden.");
        } 
	}
		
		
	// Getter und Setter für VictoryCard-Objekte
	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	
	public int getPoints(){
		return this.points;	
		}
	public void setPoints(int points){
		this.points = points;
	}
	
	public int getCosts(){
		return this.costs;
	}
	
	public void setCosts(int costs){
		this.costs = costs;
	}
	
	public void setVictoryImg(ImageIcon victoryImg){
		this.victoryImg = victoryImg;
	}
	
	public ImageIcon getVictoryImg(){
		return this.victoryImg;
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
	
	public String getTypeOfVictory(){
		return typeOfVictory;
	}
	
	public void setTypeOfVictory(String typeOfVictory){
		this.typeOfVictory = typeOfVictory;
	}
	
	public void setType(VictoryCardType type){
		this.type = type;
	}
	
	public VictoryCardType getType(){
		return type;
	}
	
	public String getPlayer(){
		return player;
	}
	
	public void setPlayer(String player) {
		this.player = player;
	}
	

	
	// Kauf
	public void buy(String player){
		// super.getNumberOfTreasure();
		this.player = player;
		
	
				
			//if (player.getActivity()==?)
			
			if (player.getNumberOfBuys()>=1) { // Kann der Spieler noch kaufen?
				if (player.getNumberOfTreasures()<=this.costs) {
					player.discardPile.add(new VictoryCard(type));
					player.decreaseNumberOfTreasures(this.costs);
					player.decreaseNumberOfBuys();
					// playerWOS.add.(new VictoryCard(); Neue Karte zur LinkedList!
				}
			}
	}
	
	public void trashCard(String player){	
		this.player = player;
	}
}
	
	/*
	// Spiel wird gestartet, Stapel für Estate
	public void setVictoryCardStackEstate(){
			VictoryCard [] stackEstate = new VictoryCard[10];
			for (int i=0; i < stackEstate.length; i++){
			stackEstate[i] = new VictoryCard(Estate);
			}
		}
	// Spiel wird gestartet, Stapel für Duchy
	public void setVictoryCardStackDuchy(){
			VictoryCard [] stackDuchy = new VictoryCard[10];
			for (int i=0; i < stackDuchy.length; i++){
			stackDuchy[i] = new VictoryCard(Duchy);
			}
		}
	// Spiel wird gestartet, Stapel für Province
	public void setVictoryCardStackProvince(){
			VictoryCard [] stackProvince = new VictoryCard[10];
			for (int i=0; i < stackProvince.length; i++){
			stackProvince[i] = new VictoryCard(Province);
			}
		}

	}
	}
	


/*
	public void setBtnVictoryCard(VictoryCard vbtn){
	this.vbtn=vbtn; 
    this.vlbl.getStyleClass().add("vlbl");
    this.vbtn.setGraphic(this.vlbl);
	}

	@Override
	public String toString(){
		String labelText = this.vlblText;
		return labelText;
		}
}




/*
   private VictoryCardType(int victoryPoints, int costs, String title, Image imgViewEstate){
    	this.victoryPoints = victoryPoints;
    	this.costs = costs;
    	this.title = title;
    	this.victoryImg = victoryImg;
    	
    }
    
   
   
		public void setVictoryPoints () {
        switch (victoryCard) {
        case Estate:
        	public int getVictoryPoints(){
        		return (int) points = victoryPoints+1;
        	}
        	
        default:
            public int getVictoryPoints(){
            	return 0;
            }
            break;
        }
	
	
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

 */