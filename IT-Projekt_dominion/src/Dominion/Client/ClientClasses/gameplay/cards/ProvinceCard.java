package Dominion.Client.ClientClasses.gameplay.cards;

import java.io.IOException;

import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.appClasses.GameHistory;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ProvinceCard extends GameCard{
	int matchPoints;

	
	public ProvinceCard(Label cardName, int costs) {
		super(cardName);
		//this.int_matchPoints = matchPoints;
		super.costs = costs;	
		this.addClickListener();
	}
	
	
	
	public int getMatchPoints() {
		return matchPoints;
	}



	public void setMatchPoints(int matchPoints) {
		this.matchPoints = matchPoints;
	}


	ProvinceCard pc = this;
	public void addClickListener(){
		
		addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent e) {
				
				//Wenn die karte aus den communty cards gekauft wird
				if(isHoleCard() == false && croupier.isBuyMode() && costs <= croupier.getBuyPower() && croupier.getBuys() > 0 && croupier.getStackSize(pc) > 0){
					croupier.setBuys(croupier.getBuys()-1);
					
					System.out.println("alte STackgrösse: "+croupier.getStackSize(pc));
					croupier.setStackSize(pc); //stacksize von moneyCards wird um eins reduziert
					System.out.println("neue STackgrösse: "+croupier.getStackSize(pc));
					
					//code ab hier dann in true teil unten
					//String text = sl.getPlayer_noOS().getUsername()+" spielt "+lbl_cardName.getText();
					//System.out.println(buyPower); // --> wird komischerweise 2x ausgefÃ¼hrt wenn klick auf gold, also buy power = 6
					GameHistory history = new GameHistory (sl.getCurrentGameParty(),sl.getPlayer_noOS(),pc.lbl_cardName.getText(),croupier.getBuyPower(), GameHistory.HistoryType.PlayMoneyCard);
					try {
						sl.getPlayer_OS().getOut().writeObject(history);
						sl.getPlayer_OS().getOut().flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					
					//gekaufte karte auf ablagestapel legen
					ProvinceCard newCard = new ProvinceCard(pc.lbl_cardName,pc.costs);
					croupier.addObserver(newCard);
					croupier.addToAblagestapel(newCard);		
				}

				
				//wenn ich die Karte in der Hand spielen darf:
				if(isHoleCard() == true && croupier.isBuyMode() && croupier.getBuys() > 0 ){		
				
				//keine Aktion möglich
				

				}
				
				//Gui aktualisieren
				sl.getPlayingStage().updateGUI();
				//System.out.println("updategui gesendet");		
				
				}
		});
	
	}
	
	
	
	
	

}
