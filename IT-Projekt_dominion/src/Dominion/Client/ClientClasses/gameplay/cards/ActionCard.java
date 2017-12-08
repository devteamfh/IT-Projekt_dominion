package Dominion.Client.ClientClasses.gameplay.cards;

import javafx.scene.control.Label;

public class ActionCard extends GameCard{
		
		int adtnlActions;
		int adtnlBuys;
		int adtnlBuyPower;

		public ActionCard(Label cardName, int costs, int adtnlActions, int adtnlBuys, int adtnlMoneyToSpend) {
			super(cardName);
			this.lbl_cardName  = cardName;
			this.costs         = costs;	
			this.adtnlActions  = adtnlActions;
			this.adtnlBuys     = adtnlBuys;
			this.adtnlBuyPower = adtnlMoneyToSpend;
			
	}

		
		
		
		
}
