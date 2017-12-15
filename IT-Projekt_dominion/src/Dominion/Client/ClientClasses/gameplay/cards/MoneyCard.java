package Dominion.Client.ClientClasses.gameplay.cards;

import java.io.File;
import java.io.IOException;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

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
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
		 * @author kab: MoneyCard
		 * @author Joel: only communication (creating GameHistory obj, sending them and doing the necessary work on server and client side after read)
		 * 
		 * 
		 */
		public class MoneyCard extends GameCard{
			
			//buyPower of the actual treasure card
			int buyPower;
			ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
			StringBuilder strBuilderForTextArea = new StringBuilder();
			StringBuilder strBuilderForLabel = new StringBuilder();
			
			public MoneyCard(Label cardName, int buyPower, int costs, String text_DE) {
				super(cardName,text_DE);
				this.lbl_cardName = cardName;
				this.buyPower = buyPower;
				this.costs    = costs;	
				
				this.addClickListener();
			}
		
			

			
			MoneyCard mc = this;
			public void addClickListener(){
				
				addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override public void handle(MouseEvent e) {
						
						//System.out.println("moneycard clicked");
						
						if(strBuilderForTextArea != null){
		            		strBuilderForTextArea.delete(0, strBuilderForTextArea.length());
		            	}
						
						if(strBuilderForLabel != null){
							strBuilderForLabel.delete(0, strBuilderForLabel.length());
		            	}

						
						//Wenn die karte aus den communty cards gekauft wird
						if(isHoleCard() == false && croupier.isBuyMode() && costs <= croupier.getBuyPower() && croupier.getBuys() > 0 && croupier.getStackSize(mc) > 0){
							croupier.setBuys(croupier.getBuys()-1);
							//in case player has more than 1 buy
							croupier.setBuyPower(croupier.getBuyPower()-mc.costs);
							
							//gekaufte karte auf ablagestapel legen
							MoneyCard newCard = new MoneyCard(mc.lbl_cardName,mc.buyPower,mc.costs,mc.text_DE);
							croupier.addObserver(newCard);
							croupier.addToAblagestapel(newCard);
							newCard.assignPicture();

							newCard.setHoleCard(true);
							System.out.println("neue ablagestapelgr�sse: "+croupier.getAblagestapel().size());
							
							
							System.out.println("add zum ablagestapel gekaufte karte: "+mc.text_DE);
							System.out.println("ablagestapel neu:////////////////////");
							for(int i=0; i<croupier.getAblagestapel().size();i++){
			            		System.out.println(croupier.getAblagestapel().get(i).getLbl_cardName().getText());
			            	}
							
							//send the buy information to server
							
							strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" kauft eine "+mc.text_DE+"-Karte\n");
							
							GameHistory history;
							
							if(croupier.getBuys()==0){
								croupier.setBuyMode(false);
				            	//set also buy power = 0 in case the player uses treasure cards but doesn't buy anything
				            	croupier.setBuyPower(0);
				            	
				            	sl.getButtonEndBuys().setDisable(true);
				            	
				            	System.out.println("Nachziehstapel bevor neue Hand ausgeteilt wird/////////////////");
				            	for(int i=0; i<croupier.getNachziehstapel().size();i++){
				            		System.out.println(croupier.getNachziehstapel().get(i).getLbl_cardName().getText());
				            	}
				            	
				            	croupier.removeHoleCards();
				            	
				            	
				            	
								strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" beendet Kaufphase und zieht 5 neue Karten\n\n");
				        		
			            		//Restliche Karten in h�nden werden auf ablagestapel gelegt
			            		croupier.muckHoleCards();
			            		
			            		//es werden 5 neue Karten gezogen
			            		croupier.drawHoleCards();
			            		
								
				        		//we will create the Label on playing stage later....because we first have to determine the next player in the sequence on server-side
				        		history = new GameHistory(strBuilderForTextArea.toString(), null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),mc.text_DE, GameHistory.HistoryType.EndBuy);
				        		
							}else{
								strBuilderForLabel.append("an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
								strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" hat noch weitere Käufe\n");
								history = new GameHistory (strBuilderForTextArea.toString(),strBuilderForLabel.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),mc.text_DE, GameHistory.HistoryType.BuyNoPointCard);
								
							}
							
							

							try {
								sl.getPlayer_OS().getOut().reset(); //noch testen ohne reset
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}

						
						//wenn ich die Karte in der Hand spielen darf:
						if(isHoleCard() == true && croupier.isBuyMode() && croupier.getBuys() > 0 && mc.getLbl_cardName().getText() != "curse"){	

						croupier.setBuyPower(croupier.getBuyPower()+buyPower);
						croupier.getHoleCards().remove(mc);
						croupier.addToAblagestapel(mc);
						
						System.out.println("add zum ablagestapel gespielte karte: "+mc.text_DE);
						System.out.println("ablagestapel neu:////////////////////");
						for(int i=0; i<croupier.getAblagestapel().size();i++){
		            		System.out.println(croupier.getAblagestapel().get(i).getLbl_cardName().getText());
		            	}
						

						String textForTextArea = sl.getPlayer_noOS().getUsername()+" spielt "+mc.text_DE+"-Karte und gewinnt "+buyPower+" Geld\n";
						String textForLabel = "an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld";
						GameHistory history = new GameHistory(textForTextArea,textForLabel,sl.getCurrentGameParty(),sl.getPlayer_noOS(),null, GameHistory.HistoryType.PlayCard);
						
						try {
							sl.getPlayer_OS().getOut().reset(); //noch testen ohne reset
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
							croupier.getHoleCards().remove(mc);
							strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" wirft eine "+mc.text_DE+"-Karte weg\n");
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
						 if (e.getButton() == MouseButton.SECONDARY && mc.isHoleCard() == false) {
			                    System.out.println("consuming right release button in cm filter");
			                    
			                    Pane pane = new Pane();
			                    ImageView imgView = new ImageView();
			                    Image img = new Image(getClass().getResource("/img/cards/big/"+mc.lbl_cardName.getText()+".png").toExternalForm());
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
	
			public int getBuyPower() {
				return buyPower;
			}
		
			public void setInt_buyPower(int buyPower) {
				this.buyPower = buyPower;
			}


	

}
