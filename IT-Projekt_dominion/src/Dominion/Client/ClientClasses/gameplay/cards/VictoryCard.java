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
	
	StringBuilder strBuilderForTextArea = new StringBuilder();
	StringBuilder strBuilderForLabel = new StringBuilder();
	
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
				
				if(strBuilderForTextArea != null){
            		strBuilderForTextArea.delete(0, strBuilderForTextArea.length());
            	}
				
				if(strBuilderForLabel != null){
					strBuilderForLabel.delete(0, strBuilderForLabel.length());
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
					
					sl.getPlayer_noOS().increasePoints(pc.getMatchPoints());
					
					System.out.println("ablagestapel neu, karte gekauft und treasure cards verwendet");
	            	for(int i=0; i<croupier.getAblagestapel().size();i++){
	            		System.out.println(croupier.getAblagestapel().get(i).getLbl_cardName().getText());
	            	}
					
					strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" kauft eine "+pc.text_DE+"-Karte und gewinnt "+pc.getMatchPoints()+" Punkte\n");
					
					GameHistory history;
					
					if(croupier.getBuys()==0){
						croupier.setBuyMode(false);
		            	//set also buy power = 0 in case the player uses treasure cards but doesn't buy anything
		            	croupier.setBuyPower(0);
		            	
		            	sl.getButtonEndBuys().setDisable(true);
		            	
		            	croupier.removeHoleCards();
		            	
		            	System.out.println("ablagestapel neu turn beendet");
		            	for(int i=0; i<croupier.getAblagestapel().size();i++){
		            		System.out.println(croupier.getAblagestapel().get(i).getLbl_cardName().getText());
		            	}
		            	
						strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" beendet Kaufphase\n\n");
		        		
		        		//we will create the Label on playing stage later....because we first have to determine the next player in the sequence on server-side
		        		history = new GameHistory(strBuilderForTextArea.toString(), null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),pc.text_DE, GameHistory.HistoryType.EndBuy);
		        		
					}else{
						strBuilderForLabel.append("an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
						history = new GameHistory (strBuilderForTextArea.toString(),strBuilderForLabel.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),pc.text_DE, GameHistory.HistoryType.BuyPointCard);
						
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
					
				
				
				
				}
				
				//wenn bis zu 4 karten auf den müll geworfen werden können
				if(isHoleCard() == true && croupier.isTrashMode()){		
					
					//trash the card
					croupier.getHoleCards().remove(pc);
					strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" wirft eine "+pc.text_DE+"-Karte weg\n");
					croupier.increaseTrashedCards();
					
					if(croupier.getTrashCounter() ==1){
						croupier.setTrashMode(false);
						croupier.setTrashCounter(0);
						if(croupier.getActions()==0){
							croupier.setBuyMode(true);
							sl.getButtonEndActions().setDisable(true);
							sl.getButtonEndBuys().setDisable(false);
							strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" beendet Aktionsphase\n");
						}else{
							croupier.setActionMode(true);
							strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" hat noch weitere Aktionen\n");
						}
					}
					
					GameHistory history = new GameHistory(strBuilderForTextArea.toString(),strBuilderForLabel.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null, GameHistory.HistoryType.Trash);
					
					try {
						//sl.getPlayer_OS().getOut().reset();
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
