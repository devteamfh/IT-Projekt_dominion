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
	

	@Override
	public void update(Observable arg0, Object arg1) {							
		System.out.println("object has been observed");
		getStyleClass().remove("highlight");
		
		//Highlighte alle Karten im Kaufmodus, welche ich mit der aktuelln Buypower kaufen kann
		if (croupier.isBuyMode() == true && croupier.getBuyPower() >= this.costs && !this.isHoleCard() && croupier.getBuys() > 0){
			this.getStyleClass().add("highlight");
		}
	}

		
		/*
		 * 
		 * 
		 * 	  Platform.runLater(new Runnable(){
	         

				@Override
	              public void run() {
					getStyleClass().remove("highlight");
	              }
	          });
	          */
		
	


		/* final Task task;
	        task = new Task<Void>() {
	            @Override
	            protected Void call() throws Exception {
	   
	            		
	            		System.out.println("jaja");
	                return null;
	            }
	        };
		
		new Thread(task).start();
		*/
		
		
		
		
		//Aktiviert Karten für den Kaufmodus. (in hole und auf deck, werden mehr geldkarten (moneytoSpend) geklickt, wird es möglich, andere Geldkarten zu kaufen
		//GameCard gc = this;
		 //else {
		
		
			


		
		
		
		//Aktiviert holeCards für Aktionsmodus
	//	if (croupier.isActionMode() == true && this.isHoleCard() && this instanceof ActionCard){

//		}else 
		
	//	if (croupier.getStackSize(this) == 0) {

		//}
		
		
		/*test observable
		 *
		System.out.println("notifyed");
		if (croupier.isActionMode() == true && croupier.getCoinsSpent() >= this.int_costs ){
			
			System.out.println("croupier changed to action mode and the costs is lower than the coins Spent");
		}*/
		
		
	
	//}
	
	
	
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
