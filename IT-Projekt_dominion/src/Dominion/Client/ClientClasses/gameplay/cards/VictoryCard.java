package Dominion.Client.ClientClasses.gameplay.cards;

import java.io.IOException;

import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.appClasses.GameHistory;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/** 
 *@author David: VictoryCard
* @author Joel: only communication (creating GameHistory obj, sending them and doing the necessary work on server and client side after read)
* 
* 
*/
public class VictoryCard extends GameCard{
	int matchPoints;
	final double PADDINGTOP   = -75;
	final double PADDINGRIGHT = 5;
	
	public VictoryCard(Label cardName, int costs, int points, String text_DE) {
		super(cardName,text_DE);
		super.costs = costs; // Kosten	
		this.matchPoints=points; // Punkte
		this.addClickListener();
	}
		
	public int getMatchPoints() {
		return matchPoints;
	}

	public void setMatchPoints(int matchPoints) {
		this.matchPoints = matchPoints;
	}


	VictoryCard pc = this;
	public void addClickListener(){
		
		addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent e) {
				
				if (e.getButton() == MouseButton.PRIMARY ){
					if(sl.getStrBuilderTextArea() != null){
						sl.getStrBuilderTextArea().delete(0, sl.getStrBuilderTextArea().length());
	            	}
					
					if(sl.getStrBuilderLabel() != null){
						sl.getStrBuilderLabel().delete(0, sl.getStrBuilderLabel().length());
	            	}
					
					// Karte wird aus Community gekauft
					if(isHoleCard() == false && croupier.isBuyMode() && costs <= croupier.getBuyPower() && croupier.getBuys() > 0 && croupier.getStackSize(pc) > 0){
						sl.getPlayer_noOS().increasePoints(pc.getMatchPoints());
						croupier.setStackSize(pc.getLbl_cardName().getText());
						
						if(sl.getCurrentGameParty().getGameHasEnded()==false){
							croupier.setBuys(croupier.getBuys()-1);
							
							//buyPower verkleinern
							croupier.setBuyPower(croupier.getBuyPower()-pc.costs);
							
							//gekaufte Karte auf den Ablagestapel legen
							VictoryCard newCard = new VictoryCard(pc.lbl_cardName,pc.costs,pc.getMatchPoints(),pc.text_DE);
							croupier.addObserver(newCard);
							croupier.addToAblagestapel(newCard);
							newCard.assignPicture(); 
							newCard.setHoleCard(true);
																									
							
			            	sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" kauft eine "+pc.text_DE+"-Karte und gewinnt "+pc.getMatchPoints()+" Punkte\n");
							
							GameHistory history;
							
							if(croupier.getBuys()==0){
								croupier.setBuyMode(false);
				            	//stellt buyPower auch auf 0 wenn kein Kauf getätigt wird
				            	croupier.setBuyPower(0);
				            	
				            	
				            	croupier.removeHoleCards();
				            	
				            	sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" beendet Kaufphase\n\n");
				        		
				        		//we will create the Label on playing stage later....because we first have to determine the next player in the sequence on server-side
				        		history = new GameHistory(sl.getStrBuilderTextArea().toString(), null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),pc.lbl_cardName.getText(),pc.text_DE, GameHistory.HistoryType.EndBuy);
				        		
							}else{
								sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
								history = new GameHistory (sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),pc.lbl_cardName.getText(),pc.text_DE, GameHistory.HistoryType.BuyPointCard);
								
							}
							
							

							try {
								//reset needed
								sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
						
						
						
								
					}

					
					//Karte auf der Hand spielbar oder nicht
					if(isHoleCard() == true && croupier.isBuyMode() && croupier.getBuys() > 0 ){	

					}
					
					// Karten auf den Ablagestapel werfen können und zudem die gleiche Anzahl Karten vom Nachziehstapel genommen werden können
					if(isHoleCard() == true && croupier.isDiscardMode()){		
						croupier.getHoleCards().remove(pc);
						croupier.addToAblagestapel(pc);
						croupier.increaseDiscardedCards();
						
						sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" legt eine "+pc.text_DE+" Karte ab\n");
						
						if(croupier.getHoleCards().isEmpty()){
							croupier.setDiscardMode(false);
							sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" beendet das Ablegen und darf "+croupier.getDiscrardCounter()+" Karten nachziehen\n");
							croupier.getNewHoleCards(croupier.getDiscrardCounter());
							croupier.setDiscardedCounter(0);
							
							if(croupier.getActions()==0){
								croupier.setBuyMode(true);
								sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" beendet Aktionsphase\n");
							}else{
								croupier.setActionMode(true);
								sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" hat noch weitere Aktionen\n");
							}
							
						}
						
						GameHistory history = new GameHistory(sl.getStrBuilderTextArea().toString(),null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.Discard);
						
						try {
							//reset?
							sl.getPlayer_OS().getOut().reset();
							sl.getPlayer_OS().getOut().writeObject(history);
							sl.getPlayer_OS().getOut().flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
					}
					
					// bis zu 4 Karten auf den Müll werfen
					if(isHoleCard() == true && croupier.isTrashModeChapel()){		
						
						//Karte auf den Muell
						croupier.getHoleCards().remove(pc);
						sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" wirft eine "+pc.text_DE+"-Karte weg\n");
						croupier.increaseTrashedCards();
						
						//decrease the points
						sl.getPlayer_noOS().decreasePoints(pc.getMatchPoints());
						
						if(croupier.getTrashCounter() ==4 || croupier.getHoleCards().isEmpty()){
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" beendet Wegwerfen\n");
							sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							croupier.setTrashModeChapel(false);
							croupier.setTrashCounterModeChapel(0);
							if(croupier.getActions()==0){
								croupier.setBuyMode(true);

								sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" beendet Aktionsphase\n");

							}else{
								croupier.setActionMode(true);
								sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" hat noch weitere Aktionen\n");
							}
						}
						
						GameHistory history = new GameHistory (sl.getStrBuilderTextArea().toString(),null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),pc.lbl_cardName.getText(),pc.text_DE, GameHistory.HistoryType.Trash);
						
						try {
							sl.getPlayer_OS().getOut().reset();//reset necessary because we changed the points of the player
							sl.getPlayer_OS().getOut().writeObject(history);
							sl.getPlayer_OS().getOut().flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
					}
					
					//1 Karte kann man auf den Muell werfen
					// dann beliebige Karte kaufen, die bis zu 2 mehr kostet als die weggeworfene Karte
					if(isHoleCard() == true && croupier.isTrashModeRebuilding()){		
						
						//Karte auf den Muell
						croupier.getHoleCards().remove(pc);
						//Wert dieser Karte speichern
						croupier.setCardValueForRebuildingMode(pc.costs);
						sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" wirft eine "+pc.text_DE+"-Karte weg\n");
						
						croupier.setTrashModeRebuilding(false);
						croupier.setModeForRebuilding(true);
						
						sl.getPlayer_noOS().decreasePoints(pc.getMatchPoints());
						
						GameHistory history = new GameHistory(sl.getStrBuilderTextArea().toString(),null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.Trash);
						
						try {
							sl.getPlayer_OS().getOut().reset();
							sl.getPlayer_OS().getOut().writeObject(history);
							sl.getPlayer_OS().getOut().flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
					}
					
					
					//neue Karte kaufen im Rebuild-Modus
					if(!isHoleCard() && croupier.isModeForRebuilding() && costs <= croupier.getCardValueForRebuildingMode() && croupier.getStackSize(pc) > 0){	
						VictoryCard newCard = new VictoryCard(pc.lbl_cardName,pc.costs,pc.getMatchPoints(),pc.text_DE);
						croupier.setStackSize(pc.getLbl_cardName().getText());
						sl.getPlayer_noOS().increasePoints(newCard.getMatchPoints());
						
						if(sl.getCurrentGameParty().getGameHasEnded()==false){
							croupier.addObserver(newCard);
							newCard.setHoleCard(true);
							croupier.addToAblagestapel(newCard);
							newCard.assignPicture();
							
							croupier.setModeForRebuilding(false);
							sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" erwirbt eine "+newCard.text_DE+"-Karte\n");
														
							
							GameHistory history=null;
							
							if(croupier.getActions()==0){
								croupier.setBuyMode(true);
					        	
					        	sl.getStrBuilderTextArea().append(sl.getPlayerName()+" beendet Aktionsphase\n");

								history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),sl.getPlayer_noOS(),newCard.getLbl_cardName().getText(),null, GameHistory.HistoryType.RebuildingModeEnd);
							}else{
								croupier.setActionMode(true);
								sl.getStrBuilderTextArea().append(sl.getPlayerName()+" hat noch weitere Aktionen\n");
								history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),sl.getPlayer_noOS(),newCard.getLbl_cardName().getText(),null, GameHistory.HistoryType.RebuildingModeEnd);
							}
															
							try {
								sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
						
					
					}
					
					//neue Karte kaufen im Workshop-Modus
					if(!isHoleCard() && croupier.isModeForWorkshop() && costs <= 4 && croupier.getStackSize(pc) > 0){	
						VictoryCard newCard = new VictoryCard(pc.lbl_cardName,pc.costs,pc.getMatchPoints(),pc.text_DE);
						croupier.setStackSize(pc.getLbl_cardName().getText());
						sl.getPlayer_noOS().increasePoints(newCard.getMatchPoints());
						
						if(sl.getCurrentGameParty().getGameHasEnded()==false){
							croupier.addObserver(newCard);
							newCard.setHoleCard(true);
							croupier.addToAblagestapel(newCard);
							newCard.assignPicture();
							
							croupier.setModeForWorkshop(false);
							sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" erwirbt eine "+newCard.text_DE+"-Karte\n");
							
							GameHistory history=null;
							
							if(croupier.getActions()==0){
								croupier.setBuyMode(true);
					        	
					        	sl.getStrBuilderTextArea().append(sl.getPlayerName()+" beendet Aktionsphase\n");

								history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),sl.getPlayer_noOS(),newCard.getLbl_cardName().getText(),null, GameHistory.HistoryType.WorkshopModeEnd);
							}else{
								croupier.setActionMode(true);
								sl.getStrBuilderTextArea().append(sl.getPlayerName()+" hat noch weitere Aktionen\n");
								history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),sl.getPlayer_noOS(),newCard.getLbl_cardName().getText(),null, GameHistory.HistoryType.WorkshopModeEnd);
							}
															
							try {
								sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
						
					
					}
					
					//discard Mode, falls ein Gegner eine Miliz-Karte gespielt hat
					if(isHoleCard() == true && croupier.isDiscardModeMilitia()){		
						croupier.getHoleCards().remove(pc);
						croupier.addToAblagestapel(pc);
						
						sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" legt eine "+pc.text_DE+" Karte ab\n");
						GameHistory history=null;
						System.out.println(croupier.getHoleCards().size());
						if(croupier.getHoleCards().size() == 3){
							croupier.setDiscardModeForMilitia(false);
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" beendet das Ablegen\n");
							history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),croupier.getCurrentPlayer(),null,null, GameHistory.HistoryType.Reaction);
							
						}
						else{
							history = new GameHistory(sl.getStrBuilderTextArea().toString(),null,sl.getCurrentGameParty(),null,null,null, GameHistory.HistoryType.Discard);
						}

						
						try {
							sl.getPlayer_OS().getOut().reset();
							sl.getPlayer_OS().getOut().writeObject(history);
							sl.getPlayer_OS().getOut().flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
					}
					
					//Spieler ist gewzungen, eine Fluch-Karte aufzunehmen
					if(!isHoleCard() == true && croupier.isModeForCurseCard() && getLbl_cardName().getText().equals("curse") && croupier.getStackSize(pc) > 0){		
						VictoryCard newCard = new VictoryCard(pc.lbl_cardName,pc.costs,pc.getMatchPoints(),pc.text_DE);
						croupier.setStackSize(pc.getLbl_cardName().getText());
						sl.getPlayer_noOS().increasePoints(newCard.getMatchPoints());
						
						if(sl.getCurrentGameParty().getGameHasEnded()==false){
							croupier.addObserver(newCard);
							croupier.addToAblagestapel(newCard);
							newCard.assignPicture(); 
							newCard.setHoleCard(true);							
							
			            	sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" kann nicht abwehren und nimmt eine Fluchkarte\n");
			            	croupier.setModeCurseCard(false);
			            	
							GameHistory history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),croupier.getCurrentPlayer(),newCard.getLbl_cardName().getText(),null, GameHistory.HistoryType.Reaction);
			            	history.setPlayerForWitchMode(sl.getPlayer_noOS());

							try {
								//reset needed
								sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
			
					}
					
					// GUI wird aktualisiert
					if(sl.getCurrentGameParty().getGameHasEnded()==false){
						try{
							sl.getPlayingStage().updateGUI();
						}catch (Exception e2){
							//
						}
					}
				} 
				
				

				//bei rechtsklick bild oeffnen
				 if (e.getButton() == MouseButton.SECONDARY) {
	           
					 ShowGameCard showGameCard = new ShowGameCard(pc);			
				
				 }
				
				}
		});
	
	}



	
	
	
	

}
