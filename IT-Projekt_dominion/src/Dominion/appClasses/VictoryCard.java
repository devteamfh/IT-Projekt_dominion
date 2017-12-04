package Dominion.appClasses;

import java.util.ArrayList;


import javax.swing.ImageIcon;
import javax.swing.JButton;

import javafx.scene.control.Button;
import javafx.scene.control.Label;


/**
 * @author David Steuri
 *
 */

//Switch Statement für verschiedene Karten

public class VictoryCard extends Button {
	private VictoryCard vbtn;
	private Label vlbl;
	private String vlblText;
	public VictoryCard(VictoryCardType vbtn) {
		super();
	}
	public VictoryCard(String s) {
		super(s);
	}

	public enum VictoryCardType {
		Estate(1, 2, "Anwesen", new ImageIcon("/IT-Projekt_dominion/Cards/Cards/Punkte/Punkte_01.jpg")),
		Duchy(3, 5, "Herzogtum", new ImageIcon("/IT-Projekt_dominion/Cards/Cards/Punkte/Punkte_03.jpg")),
		Province(6, 8, "Provinz", new ImageIcon("/IT-Projekt_dominion/Cards/Cards/Punkte/Punkte_06.jpg"));

	private int victoryPoints;
	private int costs;
	private String title;
	private ImageIcon image;

	
	VictoryCardType(int victoryPoints, int costs, String title, ImageIcon image){
	this.victoryPoints = victoryPoints;
	this.costs = costs;
	this.title = title;
	this.image = image;
	
	}
	// Getter und Setter für VictoryCard-Objekte
	public int getVictoryPoints(){
		return this.victoryPoints;	
		}
	
	public int getCosts(){
		return this.costs;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public ImageIcon getImage(){
		return this.image;
	}
	
	
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