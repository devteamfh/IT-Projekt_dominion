package Dominion.Client.ClientClasses.gameplay.cards;

import Dominion.Client.ClientClasses.ServiceLocatorClient;
import Dominion.Client.ClientClasses.gameplay.Croupier;
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

public class ActionCard extends GameCard{
		ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
		int adtnlActions;
		int adtnlBuys;
		int adtnlBuyPower;
		int positionOnBoard;


		public ActionCard(Label cardName, int costs, int adtnlActions, int adtnlBuys, int adtnlMoneyToSpend) {
			super(cardName);
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
					
					//wenn karte aus den community action cards gekauft wird (kaufmodus on, buypower und buys ausreichend, sowie genügend karten da
					if(isHoleCard() == false && croupier.isBuyMode() == true && costs <= croupier.getBuyPower() && croupier.getBuys() > 0 && croupier.getStackSize(ac)>0){
										
								//anzahl buy redurzieren
								croupier.setBuys(croupier.getBuys()-1);
					
								//buyPower reduzieren
								croupier.setBuyPower(croupier.getBuyPower()-ac.costs);
								
								//stack reduzieren      <--------------------------------------------------------------@joel
								Integer valueToChange = croupier.getStackSize(ac);
								int newValue = Integer.parseInt(valueToChange.toString());
								newValue--;
								
								int i = croupier.getAl_communityActionCards().indexOf(ac);
								
								System.out.println("alte stackgrösse:"+valueToChange);
								croupier.setAl_stackSizeCommunityActionCards(i);
								System.out.println("neue Stackgrösse:"+newValue);
							
								//gekaufte karte auf ablagestapel legen
								//System.out.println("alte Ablagestapelgrösse: "+croupier.getAblagestapel().size());
								ActionCard newCard = new ActionCard(ac.lbl_cardName,ac.costs,ac.adtnlActions,ac.adtnlBuys,ac.adtnlBuyPower);
								croupier.addObserver(newCard);
								croupier.addToAblagestapel(newCard);
								newCard.assignPicture(); 
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
				
				
				//bei rechtsklick bild öffnen
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
		}
					
					
		
		

		public int getPositionOnBoard() {
			return positionOnBoard;
		}
		public void setPositionOnBoard(int positionOnBoard) {
			this.positionOnBoard = positionOnBoard;
		}



}
