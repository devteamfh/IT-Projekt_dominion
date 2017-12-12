package Dominion.Client.ClientClasses.gameplay.cards;

import java.util.Observable;
import java.util.Observer;

import Dominion.Client.ClientClasses.ServiceLocatorClient;
import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.appClasses.GameObject.ObjectType;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class GameCard extends Button implements Observer  {
	ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
	Croupier croupier;
	
	Label   lbl_cardName;
	boolean holeCard  = false;

	int costs;
	
	public GameCard(Label cardName) {
		super();
		this.lbl_cardName = cardName;
		croupier = Croupier.getCroupier();
		this.assignPicture();
	}
	
	GameCard gc = this;
	@Override
	public void update(Observable arg0, Object arg1) {							
		getStyleClass().remove("highlight");
		
		//Highlighte alle Karten im Kaufmodus, welche ich mit der aktuelln Buypower und buys kaufen kann
		if (croupier.isBuyMode() == true && croupier.getBuyPower() >= this.costs && !this.isHoleCard() && croupier.getBuys() > 0){
			this.getStyleClass().add("highlight");
		}
		
		//wenn stacksize auf 0, wird highlighting ebenfalls deaktiviert
		if (croupier.getStackSize(gc) == 0) {
			this.getStyleClass().remove("highlight");
		}
	
		//update das STackSize infoLabel auf der Karte
		
	}
		
	
	
	
	public void assignPicture(){
		this.getStyleClass().addAll("card",lbl_cardName.getText());
	}

	
	public boolean isHoleCard() {
		return holeCard;
	}

	public void setHoleCard(boolean holeCard) {
		this.holeCard = holeCard;
	}

	public Label getLbl_cardName() {
		return lbl_cardName;
	}

	public void setLbl_cardName(Label lbl_cardName) {
		this.lbl_cardName = lbl_cardName;
	}

	
	
}
