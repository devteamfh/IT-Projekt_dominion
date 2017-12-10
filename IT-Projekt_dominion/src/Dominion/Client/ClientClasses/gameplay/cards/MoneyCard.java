package Dominion.Client.ClientClasses.gameplay.cards;

import java.io.IOException;

import Dominion.Client.ClientClasses.ServiceLocatorClient;
import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.appClasses.GameHistory;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
		 * @author kab: MoneyCard
		 * 
		 * 
		 */
		public class MoneyCard extends GameCard{
			
			//buyPower of the actual treasure card
			int buyPower;
			ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();

			
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
							
							//code ab hier dann in true teil unten
							//String text = sl.getPlayer_noOS().getUsername()+" spielt "+lbl_cardName.getText();
							croupier.setBuyPower(croupier.getBuyPower()+buyPower);
							System.out.println(buyPower); // --> wird komischerweise 2x ausgeführt wenn klick auf gold, also buy power = 6
							GameHistory history = new GameHistory (sl.getCurrentGameParty(),sl.getPlayer_noOS(),mc.lbl_cardName.getText(),croupier.getBuyPower(), GameHistory.HistoryType.PlayMoneyCard);
							try {
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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
