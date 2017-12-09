package Dominion.Client.ClientClasses.gameplay.cards;

import Dominion.Client.ClientClasses.gameplay.Croupier;
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
