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
		 * 
		 * 
		 */
		public class MoneyCard extends GameCard{
			
			//buyPower of the actual treasure card
			int buyPower;
			ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
			
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

						
						//Wenn die karte aus den communty cards gekauft wird
						if(isHoleCard() == false && croupier.isBuyMode() && costs <= croupier.getBuyPower() && croupier.getBuys() > 0 && croupier.getStackSize(mc) > 0){
							croupier.setBuys(croupier.getBuys()-1);
							

							//System.out.println("alte STackgr�sse: "+croupier.getStackSize(mc));
							croupier.setStackSize(mc); //stacksize von moneyCards wird um eins reduziert
							//System.out.println("neue STackgr�sse: "+croupier.getStackSize(mc));

							
							//gekaufte karte auf ablagestapel legen
							System.out.println("alte ablagestapelgr�sse: "+croupier.getAblagestapel().size());
							MoneyCard newCard = new MoneyCard(mc.lbl_cardName,mc.costs,mc.buyPower);
							croupier.addObserver(newCard);
							croupier.addToAblagestapel(newCard);
							System.out.println("neue ablagestapelgr�sse: "+croupier.getAblagestapel().size());
		

							/**croupier.setStackSize(mc); //stacksize von moneyCards wird um eins reduziert
							
							//buyPower reduzieren
							croupier.setBuyPower(croupier.getBuyPower()-mc.costs);

							
							//gekaufte karte auf ablagestapel legen
							MoneyCard newCard = new MoneyCard(mc.lbl_cardName,mc.costs,mc.buyPower);
							croupier.addObserver(newCard);
							croupier.addToAblagestapel(newCard);
							newCard.assignPicture(); */

						}

						
						//wenn ich die Karte in der Hand spielen darf:
						if(isHoleCard() == true && croupier.isBuyMode() && croupier.getBuys() > 0 && mc.getLbl_cardName().getText() != "curse"){		
						
						croupier.setBuyPower(croupier.getBuyPower()+buyPower);
						croupier.getHoleCards().remove(mc);
						croupier.addToAblagestapel(mc);
						
						//in this case we will send an empty string. Each client will append a new text line by reading the sender name and played card name in the class ReadMsgFromServer
						String text = "";
						
						GameHistory history = new GameHistory (text,sl.getCurrentGameParty(),sl.getPlayer_noOS(),mc.lbl_cardName.getText(),croupier.getActions(),croupier.getBuys(),croupier.getBuyPower(), GameHistory.HistoryType.PlayMoneyCard);
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
