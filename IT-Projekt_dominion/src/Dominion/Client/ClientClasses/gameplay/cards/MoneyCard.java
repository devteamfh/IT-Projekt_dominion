package Dominion.Client.ClientClasses.gameplay.cards;

import Dominion.Client.ClientClasses.gameplay.Croupier;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
		 * @author kab: MoneyCard
		 * 
		 * 
		 */
		public class MoneyCard extends GameCard{
		
			int buyPower;

			
			public MoneyCard(Label cardName, int buyPower, int costs) {
				super(cardName);
				this.lbl_cardName = cardName;
				this.buyPower = buyPower;
				this.costs    = costs;	
				this.addClickListener();
			}
		
			public int getBuyPower() {
				return buyPower;
			}
		
			public void setInt_buyPower(int buyPower) {
				this.buyPower = buyPower;
			}
		
	
			
			MoneyCard mc = this;
			public void addClickListener(){
				
				addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override public void handle(MouseEvent e) {
						
						if(isHoleCard() == false){
							croupier.setBuys(croupier.getBuys()-1);
							croupier.setStackSize(mc);
							System.out.println(croupier.getStackSizeCopper());
							//add copy of object to ablagestapel 
						}
						
						if(isHoleCard() == true){		
						croupier.setBuyPower(croupier.getBuyPower()+buyPower);
						//remove object form holeCards and send to ablagestapel 
						
						}
						
						}
				});
			
			}
	

	

}
