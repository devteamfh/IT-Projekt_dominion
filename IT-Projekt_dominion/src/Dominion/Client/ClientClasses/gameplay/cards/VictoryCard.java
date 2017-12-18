package Dominion.Client.ClientClasses.gameplay.cards;

import java.io.IOException;

import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.appClasses.GameHistory;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/** 
 *@author kab: VictoryCard
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
		//this.int_matchPoints = matchPoints;
		super.costs = costs;	
		this.matchPoints=points;
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
					
					//Wenn die karte aus den communty cards gekauft wird
					if(isHoleCard() == false && croupier.isBuyMode() && costs <= croupier.getBuyPower() && croupier.getBuys() > 0 && croupier.getStackSize(pc) > 0){
						croupier.setBuys(croupier.getBuys()-1);
						
						//buyPower reduzieren
						croupier.setBuyPower(croupier.getBuyPower()-pc.costs);
						
						//gekaufte karte auf ablagestapel legen
						VictoryCard newCard = new VictoryCard(pc.lbl_cardName,pc.costs,pc.getMatchPoints(),pc.text_DE);
						croupier.addObserver(newCard);
						croupier.addToAblagestapel(newCard);
						newCard.assignPicture(); 
						newCard.setHoleCard(true);
						
						//increase the points for this player within his current GameParty
						/**for(int i=0; i<sl.getCurrentGameParty().getArrayListOfPlayers().size();i++){
							if(sl.getPlayer_noOS().getUsername().equals(sl.getCurrentGameParty().getArrayListOfPlayers().get(i).getUsername())){
								sl.getCurrentGameParty().getArrayListOfPlayers().get(i).increasePoints(pc.getMatchPoints());
								break;
							}
						}*/
						
						sl.getPlayer_noOS().increasePoints(pc.getMatchPoints());
						
						System.out.println("ablagestapel neu, karte gekauft und treasure cards verwendet");
		            	for(int i=0; i<croupier.getAblagestapel().size();i++){
		            		System.out.println(croupier.getAblagestapel().get(i).getLbl_cardName().getText());
		            	}
						
		            	sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" kauft eine "+pc.text_DE+"-Karte und gewinnt "+pc.getMatchPoints()+" Punkte\n");
						
						GameHistory history;
						
						if(croupier.getBuys()==0){
							croupier.setBuyMode(false);
			            	//set also buy power = 0 in case the player uses treasure cards but doesn't buy anything
			            	croupier.setBuyPower(0);
			            	
			            	//sl.getButtonEndBuys().setDisable(true);
			            	
			            	croupier.removeHoleCards();
			            	
			            	System.out.println("ablagestapel neu turn beendet");
			            	for(int i=0; i<croupier.getAblagestapel().size();i++){
			            		System.out.println(croupier.getAblagestapel().get(i).getLbl_cardName().getText());
			            	}
			            	
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

					
					//wenn ich die Karte in der Hand spielen darf:
					if(isHoleCard() == true && croupier.isBuyMode() && croupier.getBuys() > 0 ){		
					
					//keine Aktion m�glich
					

					}
					
					//wenn karten auf den ablagestapel geworfen werden können und von gleicher anzahl vom nachziehstapel genommen werden können
					if(isHoleCard() == true && croupier.isDiscardMode()){		
						croupier.getHoleCards().remove(pc);
						croupier.addToAblagestapel(pc);
						croupier.increaseDiscardedCards();
						
						sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" legt eine "+pc.text_DE+" Karte ab\n");
						
						if(croupier.getHoleCards().isEmpty()){
							croupier.setDiscardMode(false);
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
							//sl.getPlayer_OS().getOut().reset();
							sl.getPlayer_OS().getOut().writeObject(history);
							sl.getPlayer_OS().getOut().flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
					}
					
					//wenn bis zu 4 karten auf den müll geworfen werden können
					if(isHoleCard() == true && croupier.isTrashModeChapel()){		
						
						//trash the card
						croupier.getHoleCards().remove(pc);
						sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" wirft eine "+pc.text_DE+"-Karte weg\n");
						croupier.increaseTrashedCards();
						
						//decrease the points
						sl.getPlayer_noOS().decreasePoints(pc.getMatchPoints());
						
						if(croupier.getTrashCounter() ==4 || croupier.getHoleCards().isEmpty()){
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" beendet Wegwerfen\n");
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
					
					//1 Karte kann man trashen, dafür kann man eine beliebige Karte kaufen, die bis zu 2 mehr kostet als die weggeworfene
					if(isHoleCard() == true && croupier.isTrashModeRebuilding()){		
						
						//trash the card
						croupier.getHoleCards().remove(pc);
						//save the value of the trashed card
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
					
					
					//neue Karte erwerben im Rebuild-Modus
					if(!isHoleCard() && croupier.isModeForRebuilding() && costs <= croupier.getCardValueForRebuildingMode()){	
						VictoryCard newCard = new VictoryCard(pc.lbl_cardName,pc.costs,pc.getMatchPoints(),pc.text_DE);
						croupier.addObserver(newCard);
						newCard.setHoleCard(true);
						croupier.addToAblagestapel(newCard);
						newCard.assignPicture();
						
						croupier.setModeForRebuilding(false);
						sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" erwirbt eine "+newCard.text_DE+"-Karte\n");
						
						sl.getPlayer_noOS().increasePoints(newCard.getMatchPoints());
						
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
					
					//neue Karte erwerben im Workshop-Modus
					if(!isHoleCard() && croupier.isModeForWorkshop() && costs <= 4){	
						VictoryCard newCard = new VictoryCard(pc.lbl_cardName,pc.costs,pc.getMatchPoints(),pc.text_DE);
						croupier.addObserver(newCard);
						newCard.setHoleCard(true);
						croupier.addToAblagestapel(newCard);
						newCard.assignPicture();
						
						sl.getPlayer_noOS().increasePoints(newCard.getMatchPoints());
						
						croupier.setModeForWorkshop(false);
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
					
					//discard Mode wenn ein Gegner eine Miliz-Karte gespielt hat
					if(isHoleCard() == true && croupier.isDiscardModeMilitia()){		
						croupier.getHoleCards().remove(pc);
						croupier.addToAblagestapel(pc);
						
						sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" legt eine "+pc.text_DE+" Karte ab\n");
						GameHistory history=null;
						if(croupier.getHoleCards().size() == 3){
							croupier.setDiscardModeForMilitia(false);
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" beendet das Ablegen\n");
							history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),croupier.getCurrentPlayer(),null,null, GameHistory.HistoryType.Reaction);
							
						}else{
							history = new GameHistory(sl.getStrBuilderTextArea().toString(),null,sl.getCurrentGameParty(),null,null,null, GameHistory.HistoryType.Discard);
						}

						
						try {
							//sl.getPlayer_OS().getOut().reset();
							sl.getPlayer_OS().getOut().writeObject(history);
							sl.getPlayer_OS().getOut().flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
					}
					
					//SPieler ist gewzungen, eine Fluch-Karte aufzunehmen
					if(!isHoleCard() == true && croupier.isModeForCurseCard() && getLbl_cardName().getText().equals("curse")){		
						VictoryCard newCard = new VictoryCard(pc.lbl_cardName,pc.costs,pc.getMatchPoints(),pc.text_DE);
						croupier.addObserver(newCard);
						croupier.addToAblagestapel(newCard);
						newCard.assignPicture(); 
						newCard.setHoleCard(true);
						
						sl.getPlayer_noOS().increasePoints(pc.getMatchPoints());
						
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
					
					//Gui aktualisieren
					sl.getPlayingStage().updateGUI();
					//System.out.println("updategui gesendet");	
				} 
				
				

				//bei rechtsklick bild �ffnen
				 if (e.getButton() == MouseButton.SECONDARY && pc.isHoleCard() == false) {
	                    System.out.println("consuming right release button in cm filter");
	                    
	                    Pane pane = new Pane();
	                    ImageView imgView = new ImageView();
	                    Image img = new Image(getClass().getResource("/img/cards/big/"+pc.lbl_cardName.getText()+".png").toExternalForm());
	                    imgView.setImage(img);
	                    pane.getChildren().add(imgView);
	                                       
	                    Stage stage = new Stage ();
	                    Scene scene = new Scene(pane,310,497);
	                   
	                    stage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	    					@Override public void handle(MouseEvent e1) { 
	    					stage.close();
	    					} 
	    					});
	                    
	                    stage.setScene(scene);
	            	    stage.initStyle(StageStyle.TRANSPARENT);   
	                    stage.show();
	                    }
				
				
				
				
				}
		});
	
	}



	
	
	
	

}
