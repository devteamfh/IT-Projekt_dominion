package Dominion.Client.ClientClasses.gameplay.cards;

import java.io.IOException;

import Dominion.Client.ClientClasses.ServiceLocatorClient;
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


/** @author kab: ActionCard
* @author Joel: only communication (creating GameHistory obj, sending them and doing the necessary work on server and client side after read)
* 
* 
*/
public class ActionCard extends GameCard{
		ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
		int adtnlActions; //+ aktionen
		int adtnlBuys; //+ käufe
		int adtnlBuyPower; //+geld
		int positionOnBoard;
		StringBuilder strBuilderForTextArea = new StringBuilder();
		StringBuilder strBuilderForLabel = new StringBuilder();


		public ActionCard(Label cardName, int costs, int adtnlActions, int adtnlBuys, int adtnlMoneyToSpend, String text_DE) {
			super(cardName,text_DE);
			super.costs        = costs;
			this.adtnlActions  = adtnlActions;
			this.adtnlBuys     = adtnlBuys;
			this.adtnlBuyPower = adtnlMoneyToSpend;
			this.addClickListener();
			
	}
		
		
		ActionCard ac = this;
		public void addClickListener(){
			
			addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent e) {
					
					if(strBuilderForTextArea != null){
	            		strBuilderForTextArea.delete(0, strBuilderForTextArea.length());
	            	}
					
					if(strBuilderForLabel != null){
						strBuilderForLabel.delete(0, strBuilderForLabel.length());
	            	}
					
					//wenn karte aus den community action cards gekauft wird (kaufmodus on, buypower und buys ausreichend, sowie gen�gend karten da
					if(isHoleCard() == false && croupier.isBuyMode() == true && costs <= croupier.getBuyPower() && croupier.getBuys() > 0 && croupier.getStackSize(ac)>0){
						
						croupier.setBuys(croupier.getBuys()-1);
						
						//buyPower reduzieren
						croupier.setBuyPower(croupier.getBuyPower()-ac.costs);
						
						//gekaufte karte auf ablagestapel legen
						ActionCard newCard = new ActionCard(ac.lbl_cardName,ac.costs,ac.adtnlActions,ac.adtnlBuys,ac.adtnlBuyPower,ac.text_DE);
						croupier.addObserver(newCard);
						croupier.addToAblagestapel(newCard);
						newCard.assignPicture(); 
						
						
						System.out.println("ablagestapel neu, karte gekauft und treasure cards verwendet");
		            	for(int i=0; i<croupier.getAblagestapel().size();i++){
		            		System.out.println(croupier.getAblagestapel().get(i).getLbl_cardName().getText());
		            	}
						
						strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" kauft eine "+ac.text_DE+"-Karte\n");
						
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
			        		history = new GameHistory(strBuilderForTextArea.toString(), null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.text_DE, GameHistory.HistoryType.EndBuy);
			        		
						}else{
							strBuilderForLabel.append("an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
							history = new GameHistory (strBuilderForTextArea.toString(),strBuilderForLabel.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.text_DE, GameHistory.HistoryType.BuyNoPointCard);
							
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
										
						
						
						
						
						
						/**
								//anzahl buy redurzieren
								croupier.setBuys(croupier.getBuys()-1);
					
								//buyPower reduzieren
								croupier.setBuyPower(croupier.getBuyPower()-ac.costs);
								
								/**Integer valueToChange = croupier.getStackSize(ac);
								int newValue = Integer.parseInt(valueToChange.toString());
								newValue--;
								
								int i = croupier.getAl_communityActionCards().indexOf(ac);
								
								
								
								System.out.println("alte stackgr�sse:"+valueToChange);
								croupier.setAl_stackSizeCommunityActionCards(i);
								System.out.println("neue Stackgr�sse:"+newValue);
							
								//gekaufte karte auf ablagestapel legen
								//System.out.println("alte Ablagestapelgr�sse: "+croupier.getAblagestapel().size());
								ActionCard newCard = new ActionCard(ac.lbl_cardName,ac.costs,ac.adtnlActions,ac.adtnlBuys,ac.adtnlBuyPower);
								croupier.addObserver(newCard);
								croupier.addToAblagestapel(newCard);
								newCard.assignPicture(); */
								
						}
			
					
					//wenn ich die Karte in der Hand spielen darf:
					if(isHoleCard() == true && croupier.isActionMode() && croupier.getActions() > 0 ){		
						
					croupier.setActions(croupier.getActions()-1);
					
					croupier.setBuyPower(croupier.getBuyPower()+adtnlBuyPower);
					croupier.setBuys(croupier.getBuys()+adtnlBuys);
					croupier.setActions(croupier.getActions()+adtnlActions);
						
					croupier.getHoleCards().remove(ac);
					croupier.addToAblagestapel(ac);
					
					GameHistory history=null;
									
					//do the specific action
					switch(ac.lbl_cardName.getText()){
					
					case "basement":
						strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und gewinnt 1 Aktion.\nZusätzlich kann er beliebig viele Karten tauschen\n");
						strBuilderForLabel.append("an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
						
						croupier.setActionMode(false);
						croupier.setDiscardMode(true); //achtung danach wieder auf false wenn action gemacht
						
						//not necessary that we send the card because the other players won't interact with this event
						history = new GameHistory(strBuilderForTextArea.toString(),strBuilderForLabel.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null, GameHistory.HistoryType.PlayCard);
						
						break;
						
					case "chapel":
						strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und darf bis 4 Karten aus der Hand wegwerfen\n");
						strBuilderForLabel.append("an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
						
						croupier.setActionMode(false);

						history = new GameHistory(strBuilderForTextArea.toString(),strBuilderForLabel.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.text_DE, GameHistory.HistoryType.PlayCard);
						break;
						
					case "forge":
						strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und darf 3 neue Karten ziehen\n");
						strBuilderForLabel.append("an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
						
						croupier.getNewHoleCards(3);
						
						//not necessary that we send the card because the other players won't interact with this event
						history = new GameHistory(strBuilderForTextArea.toString(),strBuilderForLabel.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null, GameHistory.HistoryType.PlayCard);
						break;
					
					case "funfair":
						break;
						
					case "laboratory":
						break;
						
					case "lumberjack":
						break;
					
					case "market":
						break;
						
					case "militia":
						break;
						
					case "mine":
						break;
						
					case "moat":
						break;
						
					case "moneylender":
						break;
						
					case "rebuilding":
						break;
						
					case "village":
						break;
						
					case "witch":
						break;
						
					case "workshop":
						break;
						
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
					
					
					//wenn karten auf den ablagestapel geworfen werden können und von gleicher anzahl vom nachziehstapel genommen werden können
					if(isHoleCard() == true && croupier.isDiscardMode()){		
						
					
					
					
					}
					
					//wenn bis zu 4 karten auf den müll geworfen werden können
					if(isHoleCard() == true && croupier.isTrashMode()){		
						
						//trash the card
						croupier.getHoleCards().remove(ac);
						strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" wirft eine "+ac.text_DE+"-Karte weg\n");
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
					
					
				//GUI aktualisieren
				sl.getPlayingStage().updateGUI();
				//System.out.println("updategui gesendet");		
				
				
				//bei rechtsklick bild �ffnen
				 if (e.getButton() == MouseButton.SECONDARY && ac.isHoleCard() == false) {
	                    System.out.println("consuming right release button in cm filter");
	                    
	                    Pane pane = new Pane();
	                    ImageView imgView = new ImageView();
	                    Image img = new Image(getClass().getResource("/img/cards/big/"+ac.lbl_cardName.getText()+".png").toExternalForm());
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
			
			//anstatt clicker eher mouse over zum vergrössern
			/**if (e.getButton() == MouseButton.SECONDARY && ac.isHoleCard() == false) {
                System.out.println("consuming right release button in cm filter");
                
                Pane pane = new Pane();
                ImageView imgView = new ImageView();
                Image img = new Image(getClass().getResource("/img/cards/big/"+ac.lbl_cardName.getText()+".png").toExternalForm());
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
                }*/
			
			
			
		}
					
					
		
		

		public int getPositionOnBoard() {
			return positionOnBoard;
		}
		public void setPositionOnBoard(int positionOnBoard) {
			this.positionOnBoard = positionOnBoard;
		}



}
