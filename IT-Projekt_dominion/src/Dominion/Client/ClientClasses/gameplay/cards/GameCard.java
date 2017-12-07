package Dominion.Client.ClientClasses.gameplay.cards;

import java.util.Observable;
import java.util.Observer;

import Dominion.Client.ClientClasses.gameplay.Croupier;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class GameCard extends Button implements Observer {
	Croupier croupier;
	
	Label   lbl_cardName;
	boolean holeCard  = false;

	
	int  costs = 5;

	
	public GameCard(Label cardName) {
		super();
		this.lbl_cardName = cardName;
		croupier = Croupier.getCroupier();
		this.assignPicture();
	}
	
	
	@Override
	public void update(Observable arg0, Object arg1) {
		
		//Aktiviert Karten für den Kaufmodus. (in hole und auf deck, werden mehr geldkarten (moneytoSpend) geklickt, wird es möglich, andere Geldkarten zu kaufen
		if (croupier.isBuyMode() == true && croupier.getBuyPower() >= this.costs && this instanceof ActionCard){
			this.setDisable(false);
		}
		
		
		//Aktiviert holeCards für Aktionsmodus
		if (croupier.isActionMode() == true && this.isHoleCard() && this instanceof ActionCard){
			this.setDisable(false);
		}
		
		if (croupier.getStackSize(this) == 0) {
		this.setDisable(true);
		}
		
		
		/*test observable
		 *
		System.out.println("notifyed");
		if (croupier.isActionMode() == true && croupier.getCoinsSpent() >= this.int_costs ){
			
			System.out.println("croupier changed to action mode and the costs is lower than the coins Spent");
		}*/
	
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
