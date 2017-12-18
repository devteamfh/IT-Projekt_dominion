package Dominion.Client.ClientClasses.gameplay.cards;

import java.io.IOException;

import com.sun.prism.paint.Color;

import Dominion.Client.ClientClasses.ServiceLocatorClient;
import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.appClasses.GameHistory;
import Dominion.appClasses.PlayerWithoutOS;
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


/** @author kab: ActionCard
* @author Joel: only communication (creating GameHistory obj, sending them and doing the necessary work on server and client side after read)
* 
* 
*/
public class ActionCard extends GameCard{
		ServiceLocatorClient sl;
		int adtnlActions; //+ aktionen
		int adtnlBuys; //+ käufe
		int adtnlBuyPower; //+geld
		int positionOnBoard;


		public ActionCard(Label cardName, int costs, int adtnlActions, int adtnlBuys, int adtnlMoneyToSpend, String text_DE) {
			super(cardName,text_DE);
			super.costs        = costs;
			this.adtnlActions  = adtnlActions;
			this.adtnlBuys     = adtnlBuys;
			this.adtnlBuyPower = adtnlMoneyToSpend;
			sl = ServiceLocatorClient.getServiceLocator();
			this.addClickListener();
			
	}
		
		
		ActionCard ac = this;
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
						
						//wenn karte aus den community action cards gekauft wird (kaufmodus on, buypower und buys ausreichend, sowie gen�gend karten da
						if(isHoleCard() == false && croupier.isBuyMode() == true && costs <= croupier.getBuyPower() && croupier.getBuys() > 0 && croupier.getStackSize(ac)>0){
							
							croupier.setBuys(croupier.getBuys()-1);
							
							//buyPower reduzieren
							croupier.setBuyPower(croupier.getBuyPower()-ac.costs);
							
							//gekaufte karte auf ablagestapel legen
							ActionCard newCard = new ActionCard(ac.lbl_cardName,ac.costs,ac.adtnlActions,ac.adtnlBuys,ac.adtnlBuyPower,ac.text_DE);
							croupier.addObserver(newCard);
							newCard.setHoleCard(true);
							croupier.addToAblagestapel(newCard);
							newCard.assignPicture(); 
							
							
							System.out.println("ablagestapel neu, karte gekauft und treasure cards verwendet");
			            	for(int i=0; i<croupier.getAblagestapel().size();i++){
			            		System.out.println(croupier.getAblagestapel().get(i).getLbl_cardName().getText());
			            	}
							
			            	sl.getStrBuilderTextArea().append(sl.getPlayerName()+" kauft eine "+ac.text_DE+"-Karte\n");
							
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
				            	
				            	sl.getStrBuilderTextArea().append(sl.getPlayerName()+" beendet Kaufphase\n\n");
				        		
				        		//we will create the Label on playing stage later....because we first have to determine the next player in the sequence on server-side

				        		history = new GameHistory(sl.getStrBuilderTextArea().toString(), null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.lbl_cardName.getText(),ac.text_DE, GameHistory.HistoryType.EndBuy);

				        		
							}else{
								sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
								history = new GameHistory (sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.lbl_cardName.getText(),ac.text_DE, GameHistory.HistoryType.BuyNoPointCard);
								
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
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und gewinnt 1 Aktion.\nZusätzlich kann er beliebig viele Karten tauschen\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
							
							croupier.setActionMode(false);
							croupier.setDiscardMode(true); //achtung danach wieder auf false wenn action gemacht
							
							//not necessary that we send the card because the other players won't interact with this event
							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
							
							try {
								sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							//GUI aktualisieren
							sl.getPlayingStage().updateGUI();
							
							return;
							
						case "chapel":
							
							croupier.setActionMode(false);
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und darf bis 4 Karten aus der Hand wegwerfen\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
							
							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.getLbl_cardName().getText(),ac.text_DE, GameHistory.HistoryType.PlayCard);
							
							try {
								sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							//GUI aktualisieren
							sl.getPlayingStage().updateGUI();
							
							return;
							
						case "forge":
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und darf 3 neue Karten ziehen\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
							
							//specific action
							croupier.getNewHoleCards(3);
							
							if(croupier.getActions()==0){
								
								String card = "forge";
								
								history = createHistoryObjectForEndOfActions(card);
				        		
							}else{

								//not necessary that we send the card because the other players won't interact with this event
								history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
								
							}
							
							break;
						
						case "funfair":
							
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: gewinnt 2 Aktionen, 1 Kauf und 2 Geld\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
							
							if(croupier.getActions()==0){
								
								String card = "funfair";
								
								history = createHistoryObjectForEndOfActions(card);
				        		
							}else{

								//not necessary that we send the card because the other players won't interact with this event
								history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
								
							}
							
							break;
							
						case "laboratory":
							
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: darf 2 Karten ziehen und gewinnt 1 Aktion\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
							
							croupier.getNewHoleCards(2);
							
							if(croupier.getActions()==0){
								
								String card = "laboratory";
								
								history = createHistoryObjectForEndOfActions(card);
				        		
							}else{

								//not necessary that we send the card because the other players won't interact with this event
								history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
								
							}
							
							break;
							
						case "lumberjack":
							
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: gewinnt 1 Kauf und 2 Geld\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
							
							if(croupier.getActions()==0){
								
								String card = "lumberjack";
								
								history = createHistoryObjectForEndOfActions(card);
				        		
							}else{

								//not necessary that we send the card because the other players won't interact with this event
								history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
								
							}
							
							break;
						
						case "market":
							
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: gewinnt je 1 Karte, Aktion, Kauf und Geld\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
							
							croupier.getNewHoleCards(1);
							
							if(croupier.getActions()==0){
								
								String card = "market";
								
								history = createHistoryObjectForEndOfActions(card);
				        		
							}else{

								//not necessary that we send the card because the other players won't interact with this event
								history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
								
							}
							
							break;
							
						case "militia":
							
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und gewinnt 2 Geld.\nZusätzlich müssen seine Gegenspieler solange Karten ablegen,\nbis sie nur noch 3 Karten in der Hand haben\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
							
							croupier.setActionMode(false);

							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.getLbl_cardName().getText(),null, GameHistory.HistoryType.PlayCard);
							
							break;
							
						case "mine":
							croupier.setActionMode(false);
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: darf eine Geldkarte entsorgen und eine neue erwerben,\ndie bis zu 3 Geld mehr kostet als die Weggeworfene.\nEr darf die erworbene Karte direkt auf die Hand nehmen\n");
							sl.getStrBuilderLabel().append("an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
							
							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.getLbl_cardName().getText(),ac.text_DE, GameHistory.HistoryType.PlayCard);

							break;
							
						case "moat":
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und darf 2 neue Karten ziehen\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
							
							//specific action
							croupier.getNewHoleCards(2);
							
							if(croupier.getActions()==0){
								
								String card = "moat";
								
								history = createHistoryObjectForEndOfActions(card);
				        		
							}else{

								//not necessary that we send the card because the other players won't interact with this event
								history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
								
							}
							
							break;						
							
						case "moneylender":
							croupier.setActionMode(false);
	
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: darf eine Kupferkarte von der Hand wegwerfen\nund kriegt dafür +3 Geld\n");
							sl.getStrBuilderLabel().append("an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");

							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.getLbl_cardName().getText(),ac.text_DE, GameHistory.HistoryType.PlayCard);

							
							break;
							
						case "rebuilding":
							croupier.setActionMode(false);
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: darf eine Handkarte entsorgen\nund eine Neue erwerben, die bis zu 2 Geld\nmehr kostet als die Weggeworfene\n");
							sl.getStrBuilderLabel().append("an der Reihe: "+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");

							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.getLbl_cardName().getText(),ac.text_DE, GameHistory.HistoryType.PlayCard);

							try {
								sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							//GUI aktualisieren
							sl.getPlayingStage().updateGUI();
							
							return;
							
						case "village": 
							
							croupier.getNewHoleCards(1);
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: gewinnt 2 Aktionen und zieht 1 Karte nach\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
							
							//not necessary that we send the card because the other players won't interact with this event
							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
												
							
							break;

							
						case "witch":
							croupier.getNewHoleCards(2);
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und zieht 2 Karten nach.\nZusätzlich müssen seine Gegenspieler eine Fluchkarte aufnehmen\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
							
							croupier.setActionMode(false);

							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.getLbl_cardName().getText(),null, GameHistory.HistoryType.PlayCard);

							break;
							
						case "workshop":
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und darf eine neue Karte erwerben, die bis zu 4 Geld kostet\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Käufe, "+croupier.getBuyPower()+" Geld");
							
							croupier.setActionMode(false);

							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.getLbl_cardName().getText(),null, GameHistory.HistoryType.PlayCard);
							
							try {
								sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							//GUI aktualisieren
							sl.getPlayingStage().updateGUI();
							
							return;
							
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
						
						
						//wenn karten auf den ablagestapel geworfen werden können und von gleicher anzahl vom nachziehstapel genommen werden können (Keller)
						if(isHoleCard() == true && croupier.isDiscardMode()){		
							croupier.getHoleCards().remove(ac);
							croupier.addToAblagestapel(ac);
							croupier.increaseDiscardedCards();
							
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" legt eine "+ac.text_DE+" Karte ab\n");
							
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
							croupier.getHoleCards().remove(ac);
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" wirft eine "+ac.text_DE+"-Karte weg\n");
							croupier.increaseTrashedCards();
							
							if(croupier.getTrashCounter() == 4 || croupier.getHoleCards().isEmpty()){
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
							
							GameHistory history = new GameHistory(sl.getStrBuilderTextArea().toString(),null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.Trash);
							
							try {
								//sl.getPlayer_OS().getOut().reset();
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
							croupier.getHoleCards().remove(ac);
							//save the value of the trashed card
							croupier.setCardValueForRebuildingMode(ac.costs);
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" wirft eine "+ac.text_DE+"-Karte weg\n");
							
							croupier.setTrashModeRebuilding(false);
							croupier.setModeForRebuilding(true);
							
							GameHistory history = new GameHistory(sl.getStrBuilderTextArea().toString(),null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.Trash);
							
							try {
								//sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						
						}
						
						
						//neue Karte erwerben im Rebuild-Modus
						if(!isHoleCard() && croupier.isModeForRebuilding() && costs <= croupier.getCardValueForRebuildingMode()){	
							ActionCard newCard = new ActionCard(ac.lbl_cardName,ac.costs,ac.adtnlActions,ac.adtnlBuys,ac.adtnlBuyPower,ac.text_DE);
							croupier.addObserver(newCard);
							newCard.setHoleCard(true);
							croupier.addToAblagestapel(newCard);
							newCard.assignPicture();
							
							croupier.setModeForRebuilding(false);
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
								//sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						
						}
						
						//neue Karte erwerben im Workshop-Modus
						if(!isHoleCard() && croupier.isModeForWorkshop() && costs <= 4){	
							ActionCard newCard = new ActionCard(ac.lbl_cardName,ac.costs,ac.adtnlActions,ac.adtnlBuys,ac.adtnlBuyPower,ac.text_DE);
							croupier.addObserver(newCard);
							newCard.setHoleCard(true);
							croupier.addToAblagestapel(newCard);
							newCard.assignPicture();
							
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
								//sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						
						}
						
						//neue Karte erwerben im Workshop-Modus
						if(isHoleCard() && croupier.isReactionMode() && getLbl_cardName().getText().equals("moat")){	
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" wehrt den Angriff ab\n");
							croupier.setReactionMode(false);
							GameHistory history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),croupier.getCurrentPlayer(),null,null, GameHistory.HistoryType.Reaction);
							
							try {
								//sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						
						}
						
						//discard Mode wenn ein Gegner eine Miliz-Karte gespielt hat
						if(isHoleCard() == true && croupier.isDiscardModeMilitia()){		
							croupier.getHoleCards().remove(ac);
							croupier.addToAblagestapel(ac);
							
							sl.getStrBuilderTextArea().append(sl.getPlayer_noOS().getUsername()+" legt eine "+ac.text_DE+" Karte ab\n");
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
						
						
					//GUI aktualisieren
					sl.getPlayingStage().updateGUI();
					//System.out.println("updategui gesendet");	
					} 
					

				//bei rechtsklick bild �ffnen
				 if (e.getButton() == MouseButton.SECONDARY) {
					 
					 ShowGameCard showGameCard = new ShowGameCard(ac);
					 
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
		
		public GameHistory createHistoryObjectForEndOfActions(String actionCase){
			
			croupier.setActionMode(false);
			croupier.setBuyMode(true);
        	
        	sl.getStrBuilderTextArea().append(sl.getPlayerName()+" beendet Aktionsphase\n");

			GameHistory history = new GameHistory(sl.getStrBuilderTextArea().toString(), sl.getStrBuilderLabel().toString(), sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.EndAction);
			return history;
		}


				
}