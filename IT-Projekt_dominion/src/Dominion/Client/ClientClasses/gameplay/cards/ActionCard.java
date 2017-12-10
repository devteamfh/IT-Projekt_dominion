package Dominion.Client.ClientClasses.gameplay.cards;

import Dominion.Client.ClientClasses.gameplay.Croupier;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ActionCard extends GameCard{
		
		int adtnlActions;
		int adtnlBuys;
		int adtnlBuyPower;
		int positionOnBoard;



		public ActionCard(Label cardName, int costs, int adtnlActions, int adtnlBuys, int adtnlMoneyToSpend) {
			super(cardName);
			super.costs         = costs;	
			this.adtnlActions  = adtnlActions;
			this.adtnlBuys     = adtnlBuys;
			this.adtnlBuyPower = adtnlMoneyToSpend;
			this.addClickListener();
			
	}
		
		
		ActionCard ac = this;
		public void addClickListener(){
			
			addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent e) {
					
					
					if(isHoleCard() == false && croupier.isBuyMode() == true && costs <= croupier.getBuyPower()){
					
						for (int i = 0; i < croupier.getAl_communityActionCards().size(); i++){
							if (ac.equals(croupier.getAl_communityActionCards().get(i)) && croupier.getAl_stackSizeCommunityActionCards().get(i)>0) {
							
								//anzahl buy redurzieren
								croupier.setBuys(croupier.getBuys()-1);
								System.out.println("bought");
								
								//buyPower reduzieren
								croupier.setBuyPower(croupier.getBuyPower()-ac.costs);
								
								//stack reduzieren
								Integer valueToChange = croupier.getAl_stackSizeCommunityActionCards().get(i);
								int newValue = Integer.parseInt(valueToChange.toString());
								newValue--;
								croupier.getAl_stackSizeCommunityActionCards().set(i, Integer.valueOf(newValue));
								System.out.println(valueToChange);
								System.out.println(newValue);
							
								//gekaufte karte auf ablagestapel legen
								croupier.addToAblagestapel(ac);
								System.out.println(croupier.getLl_ablageStapel().size());
								
								//GUI aktualisieren
								//croupier.
								
							}
						}
				}
			}});
			}
					
					
		
		

		public int getPositionOnBoard() {
			return positionOnBoard;
		}
		public void setPositionOnBoard(int positionOnBoard) {
			this.positionOnBoard = positionOnBoard;
		}
		
		
		
}
