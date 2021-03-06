package Dominion.Client.ClientClasses.gameplay.cards;

import java.io.File;
import java.io.IOException;


import Dominion.Client.ClientClasses.ServiceLocatorClient;
import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.appClasses.GameHistory;
import javafx.application.Platform;
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
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
		 * @author kab: MoneyCard --
		 * @author Joel: only communication (creating GameHistory obj, sending them and doing the necessary work on server and client side after read)
		 * 
		 * 
		 */
		public class MoneyCard extends GameCard{
			
			//buyPower of the actual treasure card
			int buyPower;
			ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
			
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
						
						if (e.getButton() == MouseButton.PRIMARY ){
							
							if(sl.getStrBuilderTextArea() != null){
								sl.getStrBuilderTextArea().delete(0, sl.getStrBuilderTextArea().length());
			            	}
							
							if(sl.getStrBuilderLabel() != null){
								sl.getStrBuilderLabel().delete(0, sl.getStrBuilderLabel().length());
			            	}

							
							//Wenn die karte aus den community cards gekauft wird
							if(isHoleCard() == false && croupier.isBuyMode() && costs <= croupier.getBuyPower() && croupier.getBuys() > 0 && croupier.getStackSize(mc) > 0){
								croupier.setStackSize(mc.getLbl_cardName().getText());
								if(sl.getCurrentGameParty().getGameHasEnded()==false){
									croupier.setBuys(croupier.getBuys()-1);
									//in case player has more than 1 buy
									croupier.setBuyPower(croupier.getBuyPower()-mc.costs);
									
									//croupier.setStackSize(mc.lbl_cardName.getText()); //stacksize von moneyCards wird um eins reduziert
			
									//gekaufte karte auf ablagestapel legen
									MoneyCard newCard = new MoneyCard(mc.lbl_cardName,mc.buyPower,mc.costs,mc.text_DE);

									croupier.addObserver(newCard);
									croupier.addToAblagestapel(newCard);
									newCard.assignPicture();

									newCard.setHoleCard(true);
									
									//send the buy information to server						
									appendTextArea(sl.getPlayer_noOS().getUsername()+" kauft eine "+mc.text_DE+"-Karte\n");

									GameHistory history;

									if(croupier.getBuys()==0){
										croupier.setBuyMode(false);
						            	//set also buy power = 0 in case the player uses treasure cards but doesn't buy anything
						            	croupier.setBuyPower(0);

						            	//sl.getButtonEndBuys().setDisable(true);

						            	croupier.removeHoleCards();



						            	appendTextArea(sl.getPlayer_noOS().getUsername()+" beendet Kaufphase und zieht 5 neue Karten\n\n");

					            		//Restliche Karten in h�nden werden auf ablagestapel gelegt
					            		//croupier.muckHoleCards();

					            		//es werden 5 neue Karten gezogen
					            		//croupier.drawHoleCards();


						        		//we will create the Label on playing stage later....because we first have to determine the next player in the sequence on server-side
						        		history = new GameHistory(sl.getStrBuilderTextArea().toString(), null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),mc.lbl_cardName.getText(),mc.text_DE, GameHistory.HistoryType.EndBuy);

									}else{
										sl.getStrBuilderLabel().append("am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
										appendTextArea(sl.getPlayer_noOS().getUsername()+" hat noch weitere Käufe\n");
										history = new GameHistory (sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),mc.getLbl_cardName().getText(),mc.text_DE, GameHistory.HistoryType.BuyNoPointCard);

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


							}


							//wenn ich die Karte in der Hand spielen darf:
							if(isHoleCard() == true && croupier.isBuyMode() && croupier.getBuys() > 0 ){

							croupier.setBuyPower(croupier.getBuyPower()+buyPower);
							croupier.getHoleCards().remove(mc);
							croupier.addToAblagestapel(mc);


							String textForTextArea = sl.getPlayer_noOS().getUsername()+" spielt "+mc.text_DE+"-Karte und gewinnt "+buyPower+" Geld\n";
							String textForLabel = "am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld";
							GameHistory history = new GameHistory(textForTextArea,textForLabel,sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);

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
								croupier.getHoleCards().remove(mc);
								croupier.addToAblagestapel(mc);
								croupier.increaseDiscardedCards();

								appendTextArea(sl.getPlayer_noOS().getUsername()+" legt eine "+mc.text_DE+" Karte ab\n");

								if(croupier.getHoleCards().isEmpty()){
									croupier.setDiscardMode(false);
									sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
									appendTextArea(sl.getPlayer_noOS().getUsername()+" beendet das Ablegen und darf "+croupier.getDiscrardCounter()+" Karten nachziehen\n");
									croupier.getNewHoleCards(croupier.getDiscrardCounter());
									croupier.setDiscardedCounter(0);

									if(croupier.getActions()==0){
										croupier.setBuyMode(true);
										appendTextArea(sl.getPlayer_noOS().getUsername()+" beendet Aktionsphase\n");
									}else{
										croupier.setActionMode(true);
										appendTextArea(sl.getPlayer_noOS().getUsername()+" hat noch weitere Aktionen\n");
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

							//wenn bis zu 4 karten auf den müll geworfen werden können (Kapelle)
							if(isHoleCard() == true && croupier.isTrashModeChapel()){

								//trash the card
								croupier.getHoleCards().remove(mc);
								appendTextArea(sl.getPlayer_noOS().getUsername()+" wirft eine "+mc.text_DE+"-Karte weg\n");
								croupier.increaseTrashedCards();

								if(croupier.getTrashCounter() ==4 || croupier.getHoleCards().isEmpty()){
									appendTextArea(sl.getPlayer_noOS().getUsername()+" beendet Wegwerfen\n");
									sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
									croupier.setTrashModeChapel(false);
									croupier.setTrashCounterModeChapel(0);
									if(croupier.getActions()==0){
										croupier.setBuyMode(true);

										appendTextArea(sl.getPlayer_noOS().getUsername()+" beendet Aktionsphase\n");

									}else{
										croupier.setActionMode(true);
										appendTextArea(sl.getPlayer_noOS().getUsername()+" hat noch weitere Aktionen\n");
									}
								}

								GameHistory history = new GameHistory(sl.getStrBuilderTextArea().toString(),null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.Trash);

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

							//wenn eine geldkarte entsorgt werden kann und dafür eine neue geldkarte gekauft werden kann, die bis zu 3 mehr kostet als die weggeworfene
							if(isHoleCard() == true && croupier.isTrashModeMine()){

								//trash the card
								croupier.getHoleCards().remove(mc);
								//save the value (f.e. he trashes a copper -> copper costs = 0 --> saved value 0+3 =3)
								croupier.setMCValueForMineMode(mc.costs);
								appendTextArea(sl.getPlayer_noOS().getUsername()+" wirft eine "+mc.text_DE+"-Karte weg\n");
								//croupier.increaseTrashedCards();

								croupier.setTrashModeMine(false);
								croupier.setModeForMine(true);

								GameHistory history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.Trash);

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

							//neue geldkarte erwerben im minen-modus und direkt auf die hand legen
							if(!isHoleCard() && croupier.isModeForMine() && costs <= croupier.getSavedMCValueForMineMode() && croupier.getStackSize(mc) > 0){
								//to do: custom button deaktivieren?
								MoneyCard newCard = new MoneyCard(mc.lbl_cardName,mc.buyPower,mc.costs,mc.text_DE);
								croupier.setStackSize(mc.getLbl_cardName().getText());

								if(sl.getCurrentGameParty().getGameHasEnded()==false){
									newCard.setHoleCard(true);
									newCard.assignPicture();

									croupier.addObserver(newCard);
									croupier.addToHoleCards(newCard);

									croupier.setModeForMine(false);
									//sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
									sl.getStrBuilderLabel().append(croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");

									appendTextArea(sl.getPlayer_noOS().getUsername()+" erwirbt eine "+newCard.text_DE+"-Karte und legt sie auf die Hand\n");

									GameHistory history=null;

									if(croupier.getActions()==0){
										croupier.setBuyMode(true);

							        	appendTextArea(sl.getPlayerName()+" beendet Aktionsphase\n");

										history = new GameHistory(sl.getStrBuilderTextArea().toString(), sl.getStrBuilderLabel().toString(), sl.getCurrentGameParty(),sl.getPlayer_noOS(),newCard.getLbl_cardName().getText(),null, GameHistory.HistoryType.MineModeEnd);
									}else{
										croupier.setActionMode(true);
										appendTextArea(sl.getPlayerName()+" hat noch weitere Aktionen\n");
										history = new GameHistory(sl.getStrBuilderTextArea().toString(), sl.getStrBuilderLabel().toString(), sl.getCurrentGameParty(),sl.getPlayer_noOS(),newCard.getLbl_cardName().getText(),null, GameHistory.HistoryType.MineModeEnd);
									}

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


							}

							//wenn eine Kupferkarte weggeworfen werden kann und man +3 Geld bekommt
							if(isHoleCard() == true && croupier.isTrashModeMoneylender() && getLbl_cardName().getText().equals("copper")){

								//trash the card
								croupier.getHoleCards().remove(mc);
								appendTextArea(sl.getPlayer_noOS().getUsername()+" wirft eine Kupfer-Karte weg und gewinnt +3 Geld\n");
								croupier.setBuyPower(croupier.getBuyPower()+3);
								sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
								croupier.setTrashModeMoneylender(false);

								if(croupier.getActions()==0){
									croupier.setBuyMode(true);
									appendTextArea(sl.getPlayer_noOS().getUsername()+" beendet Aktionsphase\n");
								}else{
									croupier.setActionMode(true);
									appendTextArea(sl.getPlayer_noOS().getUsername()+" hat noch weitere Aktionen\n");
								}

								sl.getStrBuilderLabel().append("am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");

								GameHistory history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),"moneylender",null, GameHistory.HistoryType.Trash);

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

							//1 Karte kann man trashend, dafür kann man eine beliebige Karte kaufen, die bis zu 2 mehr kostet als die weggeworfene
							if(isHoleCard() == true && croupier.isTrashModeRebuilding()){

								//trash the card
								croupier.getHoleCards().remove(mc);
								//save the value of the trashed card
								croupier.setCardValueForRebuildingMode(mc.costs);
								appendTextArea(sl.getPlayer_noOS().getUsername()+" wirft eine "+mc.text_DE+"-Karte weg\n");

								croupier.setTrashModeRebuilding(false);
								croupier.setModeForRebuilding(true);

								GameHistory history = new GameHistory(sl.getStrBuilderTextArea().toString(),null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.Trash);

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


							//neue Karte erwerben im Rebuild-Modus
							if(!isHoleCard() && croupier.isModeForRebuilding() && costs <= croupier.getCardValueForRebuildingMode() && croupier.getStackSize(mc) > 0){
								MoneyCard newCard = new MoneyCard(mc.lbl_cardName,mc.buyPower,mc.costs,mc.text_DE);
								croupier.setStackSize(mc.getLbl_cardName().getText());

								if(sl.getCurrentGameParty().getGameHasEnded()==false){
									croupier.addObserver(newCard);
									newCard.setHoleCard(true);
									croupier.addToAblagestapel(newCard);
									newCard.assignPicture();

									croupier.setModeForRebuilding(false);
									sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
									appendTextArea(sl.getPlayer_noOS().getUsername()+" erwirbt eine "+newCard.text_DE+"-Karte\n");

									GameHistory history=null;

									if(croupier.getActions()==0){
										croupier.setBuyMode(true);

							        	//sl.getButtonEndActions().setDisable(true);
							        	//sl.getButtonEndBuys().setDisable(false);

							        	appendTextArea(sl.getPlayerName()+" beendet Aktionsphase\n");

										history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),sl.getPlayer_noOS(),newCard.getLbl_cardName().getText(),null, GameHistory.HistoryType.RebuildingModeEnd);
									}else{
										croupier.setActionMode(true);
										appendTextArea(sl.getPlayerName()+" hat noch weitere Aktionen\n");
										history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),sl.getPlayer_noOS(),newCard.getLbl_cardName().getText(),null, GameHistory.HistoryType.RebuildingModeEnd);
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



							}

							//neue Karte erwerben im Workshop-Modus
							if(!isHoleCard() && croupier.isModeForWorkshop() && costs <= 4 && croupier.getStackSize(mc) > 0){
								MoneyCard newCard = new MoneyCard(mc.lbl_cardName,mc.buyPower,mc.costs,mc.text_DE);
								croupier.setStackSize(mc.getLbl_cardName().getText());

								if(sl.getCurrentGameParty().getGameHasEnded()==false){
									croupier.addObserver(newCard);
									croupier.addToAblagestapel(newCard);
									newCard.assignPicture();
									newCard.setHoleCard(true);

									croupier.setModeForWorkshop(false);
									sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
									appendTextArea(sl.getPlayer_noOS().getUsername()+" erwirbt eine "+newCard.text_DE+"-Karte\n");

									GameHistory history=null;

									if(croupier.getActions()==0){
										croupier.setBuyMode(true);

							        	appendTextArea(sl.getPlayerName()+" beendet Aktionsphase\n");

										history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),sl.getPlayer_noOS(),newCard.getLbl_cardName().getText(),null, GameHistory.HistoryType.WorkshopModeEnd);
									}else{
										croupier.setActionMode(true);
										appendTextArea(sl.getPlayerName()+" hat noch weitere Aktionen\n");
										history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),sl.getPlayer_noOS(),newCard.getLbl_cardName().getText(),null, GameHistory.HistoryType.WorkshopModeEnd);
									}

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

							}

							//discard Mode wenn ein Gegner eine Miliz-Karte gespielt hat
							if(isHoleCard() == true && croupier.isDiscardModeMilitia()){
								croupier.getHoleCards().remove(mc);
								croupier.addToAblagestapel(mc);

								appendTextArea(sl.getPlayer_noOS().getUsername()+" legt eine "+mc.text_DE+" Karte ab\n");
								GameHistory history=null;
								if(croupier.getHoleCards().size() == 3){
									croupier.setDiscardModeForMilitia(false);
									appendTextArea(sl.getPlayer_noOS().getUsername()+" beendet das Ablegen\n");
									history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),croupier.getCurrentPlayer(),null,null, GameHistory.HistoryType.Reaction);

								}else{
									history = new GameHistory(sl.getStrBuilderTextArea().toString(),null,sl.getCurrentGameParty(),null,null,null, GameHistory.HistoryType.Discard);
								}


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


							// GUI wird aktualisiert
							if(sl.getCurrentGameParty().getGameHasEnded()==false){
								try{
									sl.getPlayingStage().updateGUI();
								}catch (Exception e2){
									//
								}
							}

						}



						//bei rechtsklick bild �ffnen
						 if (e.getButton() == MouseButton.SECONDARY) {

							 ShowGameCard showGameCard = new ShowGameCard(mc);

						 }

					}



				});

			}

	private void appendTextArea(String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				sl.getStrBuilderTextArea().append(text);
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
