package Dominion.Client.ClientClasses.gameplay.cards;

import Dominion.Client.ClientClasses.ServiceLocatorClient;
import Dominion.Client.ClientClasses.gameplay.Croupier;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ActionCard extends GameCard{
		ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
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
					
					//wenn karte aus den community action cards gekauft wird
					if(isHoleCard() == false && croupier.isBuyMode() == true && costs <= croupier.getBuyPower() && croupier.getBuys() > 0){
					
						//finde heraus um welche karte es sich handelt und ob noch karten vorhanden sind
						for (int i = 0; i < croupier.getAl_communityActionCards().size(); i++){
							if (ac.equals(croupier.getAl_communityActionCards().get(i)) && croupier.getAl_stackSizeCommunityActionCards().get(i)>0) {
							
								//anzahl buy redurzieren
								croupier.setBuys(croupier.getBuys()-1);
					
								//buyPower reduzieren
								croupier.setBuyPower(croupier.getBuyPower()-ac.costs);
								
								//stack reduzieren      <--------------------------------------------------------------@joel
								Integer valueToChange = croupier.getAl_stackSizeCommunityActionCards().get(i);
								int newValue = Integer.parseInt(valueToChange.toString());
								newValue--;
								croupier.getAl_stackSizeCommunityActionCards().set(i, Integer.valueOf(newValue));
								//System.out.println("alte stackgrösse:"+valueToChange);
								//System.out.println("neue Stackgrösse:"+newValue);
							
								//gekaufte karte auf ablagestapel legen
								//System.out.println("alte Ablagestapelgrösse: "+croupier.getAblagestapel().size());
								ActionCard newCard = new ActionCard(ac.lbl_cardName,ac.costs,ac.adtnlActions,ac.adtnlBuys,ac.adtnlBuyPower);
								croupier.addObserver(newCard);
								croupier.addToAblagestapel(newCard);
								
									
							}
						}
				}
					
					
					
					//wenn ich die Karte in der Hand spielen darf:
					if(isHoleCard() == true && croupier.isActionMode() && croupier.getActions() > 0 ){		
					
					croupier.setBuyPower(croupier.getBuyPower()+adtnlBuyPower);
					croupier.setBuys(croupier.getBuys()+adtnlBuys);
					croupier.setActions(croupier.getActions()+adtnlActions);
					croupier.getHoleCards().remove(ac);
					croupier.addToAblagestapel(ac);
					

					}
					
					
					
					//GUI aktualisieren
					sl.getPlayingStage().updateGUI();
					//System.out.println("updategui gesendet");		
			}});
			}
					
					
		
		

		public int getPositionOnBoard() {
			return positionOnBoard;
		}
		public void setPositionOnBoard(int positionOnBoard) {
			this.positionOnBoard = positionOnBoard;
		}
		
		
		
}
