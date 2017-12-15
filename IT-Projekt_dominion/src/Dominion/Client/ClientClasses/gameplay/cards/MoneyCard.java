package Dominion.Client.ClientClasses.gameplay.cards;

import java.io.File;
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
			
			public MoneyCard(Label cardName, int buyPower, int costs) {
				super(cardName);
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

						
						//Wenn die karte aus den community cards gekauft wird
						if(isHoleCard() == false && croupier.isBuyMode() && costs <= croupier.getBuyPower() && croupier.getBuys() > 0 && croupier.getStackSize(mc) > 0){
							croupier.setBuys(croupier.getBuys()-1);
							//in case player has more than 1 buy
							croupier.setBuyPower(croupier.getBuyPower()-mc.costs);
							
							//croupier.setStackSize(mc.lbl_cardName.getText()); //stacksize von moneyCards wird um eins reduziert
	
							//gekaufte karte auf ablagestapel legen
							MoneyCard newCard = new MoneyCard(mc.lbl_cardName,mc.costs,mc.buyPower);
							croupier.addObserver(newCard);
							croupier.addToAblagestapel(newCard);
							newCard.assignPicture();
							newCard.setHoleCard(true);
							
							

							
							//send the buy information to server
							
							strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" kauft eine "+mc.getLbl_cardName().getText()+"-Karte\n");
							
							GameHistory history;
							
							if(croupier.getBuys()==0){
								croupier.setBuyMode(false);
				            	//set also buy power = 0 in case the player uses treasure cards but doesn't buy anything
				            	croupier.setBuyPower(0);
				            	
				            	sl.getButtonEndBuys().setDisable(true);
								strBuilderForTextArea.append(sl.getPlayer_noOS().getUsername()+" beendet Kaufphase\n\n");
				        		
			            		//Restliche Karten in h�nden werden auf ablagestapel gelegt
			            		croupier.muckHoleCards();
			            		
			            		//es werden 5 neue Karten gezogen
			            		croupier.drawHoleCards();
			            		
							
				        		//we will create the Label on playing stage later....because we first have to determine the next player in the sequence on server-side
				        		history = new GameHistory(strBuilderForTextArea.toString(), null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),mc.getLbl_cardName().getText(), GameHistory.HistoryType.EndBuy);
				        		
							}else{
								strBuilderForLabel.append("an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
								history = new GameHistory (strBuilderForTextArea.toString(),strBuilderForLabel.toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),mc.getLbl_cardName().getText(), GameHistory.HistoryType.BuyNoPointCard);
								
							}
							
							

							try {
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

						String textForTextArea = sl.getPlayer_noOS().getUsername()+" spielt "+mc.getLbl_cardName().getText()+"-Karte und gewinnt "+buyPower+" Geld\n";
						String textForLabel = "an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld";
						GameHistory history = new GameHistory(textForTextArea,textForLabel,sl.getCurrentGameParty(),sl.getPlayer_noOS(),null, GameHistory.HistoryType.PlayCard);
						
						try {
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
