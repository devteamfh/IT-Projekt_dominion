package Dominion.Client.ClientClasses.gameplay.cards;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ActionCard extends GameCard{
		
		int adtnlActions;
		int adtnlBuys;
		int adtnlBuyPower;

		public ActionCard(Label cardName, int costs, int adtnlActions, int adtnlBuys, int adtnlMoneyToSpend) {
			super(cardName);
			super.costs         = costs;	
			this.adtnlActions  = adtnlActions;
			this.adtnlBuys     = adtnlBuys;
			this.adtnlBuyPower = adtnlMoneyToSpend;
			this.addClickListener();
			
	}
		
		
		
		public void addClickListener(){
			
			addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent e) {
					
					if(isHoleCard() == false){
						croupier.setBuys(croupier.getBuys()-1);
						//decrement stacksize
						//add copy of object to ablagestapel 
					}
				
					
					}
			});
		
		}

		
		
		
		
}
