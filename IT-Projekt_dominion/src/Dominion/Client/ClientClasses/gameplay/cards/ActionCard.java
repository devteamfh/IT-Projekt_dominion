package Dominion.Client.ClientClasses.gameplay.cards;

import java.io.IOException;

import Dominion.Client.ClientClasses.ServiceLocatorClient;
import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.appClasses.GameHistory;
import Dominion.appClasses.PlayerWithoutOS;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;



/** @author Adrian Widmer --
* @author Joel: only communication (creating GameHistory obj, sending them and doing the necessary work on server and client side after read)
*/
public class ActionCard extends GameCard{
		ServiceLocatorClient sl;
		int adtnlActions; // + Aktionen
		int adtnlBuys; // + Kaeufe
		int adtnlBuyPower; // + Geld
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
						
						// Karte wird aus den Community-Action-Cards gekauft 
						// Voraussetzungen: Kaufmodus ist aktiv <BuyMode>
						// Genuegend Geld ist vorhanden <BuyPower>
						// Anzahl verbleibende Kaeufe ist ausreichend <Buys>
						// Genuegend Karten sind vorhanden <StackSize>
						if(isHoleCard() == false && croupier.isBuyMode() && costs <= croupier.getBuyPower() && croupier.getBuys() > 0 && croupier.getStackSize(ac)>0){
							croupier.setStackSize(ac.getLbl_cardName().getText());
							
							if(sl.getCurrentGameParty().getGameHasEnded()==false){
								croupier.setBuys(croupier.getBuys()-1);
								
								// Kauf wird von verbleibender Anzahl Kaeufen abgezogen
								croupier.setBuyPower(croupier.getBuyPower()-ac.costs);
								
								// Gekaufte Karte wird auf den Ablagestapel gelegt
								ActionCard newCard = new ActionCard(ac.lbl_cardName,ac.costs,ac.adtnlActions,ac.adtnlBuys,ac.adtnlBuyPower,ac.text_DE);
								croupier.addObserver(newCard);
								croupier.addToAblagestapel(newCard);
								newCard.assignPicture(); 
								newCard.setHoleCard(true);				
								
				            	appendTextArea(sl.getPlayerName()+" kauft eine "+ac.text_DE+"-Karte\n");
								
								GameHistory history;
								
								if(croupier.getBuys()==0){
									croupier.setBuyMode(false);
					            	
							// Geld wird auf 0 gesetzt, falls der Spieler Geldkarten benutzt, aber nichts kauft
					            	croupier.setBuyPower(0);
					            			            	
					            	croupier.removeHoleCards();
					            	
					            	appendTextArea(sl.getPlayerName()+" beendet Kaufphase\n\n");
					        		
					        		// Das Label auf der Spiel-Stage wird spaeter generiert, weil zuerst der naechste Spieler bestimmt werden muss in der Sequenz serverseitig

					        		history = new GameHistory(sl.getStrBuilderTextArea().toString(), null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.lbl_cardName.getText(),ac.text_DE, GameHistory.HistoryType.EndBuy);

					        		
								}else{
									sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
									history = new GameHistory (sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.lbl_cardName.getText(),ac.text_DE, GameHistory.HistoryType.BuyNoPointCard);
									
								}
								
								

								try {
									// Reset durchfuehren
									sl.getPlayer_OS().getOut().reset();
									sl.getPlayer_OS().getOut().writeObject(history);
									sl.getPlayer_OS().getOut().flush();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
			
						}

						
						// Spieler darf die Karte auf der Hand spielen
						if(isHoleCard() && croupier.isActionMode() && croupier.getActions() > 0 ){		
							
						croupier.setActions(croupier.getActions()-1);
						
						croupier.setBuyPower(croupier.getBuyPower()+adtnlBuyPower);
						croupier.setBuys(croupier.getBuys()+adtnlBuys);
						croupier.setActions(croupier.getActions()+adtnlActions);
							
						croupier.getHoleCards().remove(ac);
						croupier.addToAblagestapel(ac);
						

						GameHistory history=null;
										
						// Aktion der Karte ausfuehren
						switch(ac.lbl_cardName.getText()){
						
						case "basement":
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und erhaelt 1 zusaetzliche Aktion.\nAusserdem kann er beliebig viele Karten tauschen\n");
							//sl.getStrBuilderLabel().append("Tausche so viele Karten wie\ndu möchtest!");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							
							croupier.setActionMode(false);
							croupier.setDiscardMode(true); // Wird wieder auf false gesetzt, sobald Action gespielt wurde
							
							// Diese Karte muessen die anderen Spieler nicht sehen, da sie durch diese Karte nicht betroffen sind
							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.lbl_cardName.getText(),null, GameHistory.HistoryType.PlayCard);
							
							try {
								sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
							// GUI wird aktualisiert
							sl.getPlayingStage().updateGUI();
							
							return;
							
						case "chapel":
							
							croupier.setActionMode(false);
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und darf bis 4 Karten aus der Hand wegwerfen\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							
							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.getLbl_cardName().getText(),ac.text_DE, GameHistory.HistoryType.PlayCard);
							
							try {
								sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
							// GUI wird aktualisiert
							sl.getPlayingStage().updateGUI();
							
							return;
							
						case "forge":
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und darf 3 neue Karten ziehen\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							
							// Aktion dieser Karte
							croupier.getNewHoleCards(3);
							
							if(croupier.getActions()==0){
								
								String card = "forge";
								
								history = createHistoryObjectForEndOfActions(card);
				        		
							}else{

								// Diese Karte muessen die anderen Spieler nicht sehen, da sie durch diese Karte nicht betroffen sind
								history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
								
							}
							
							break;
						
						case "funfair":
							
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: gewinnt 2 Aktionen, 1 Kauf und 2 Geld\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							
							if(croupier.getActions()==0){
								
								String card = "funfair";
								
								history = createHistoryObjectForEndOfActions(card);
				        		
							}else{

								// Diese Karte muessen die anderen Spieler nicht sehen, da sie durch diese Karte nicht betroffen sind
								history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
								
							}
							
							break;
							
						case "laboratory":
							
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: darf 2 Karten ziehen und gewinnt 1 Aktion\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							
							croupier.getNewHoleCards(2);
							
							if(croupier.getActions()==0){
								
								String card = "laboratory";
								
								history = createHistoryObjectForEndOfActions(card);
				        		
							}else{

								// Diese Karte muessen die anderen Spieler nicht sehen, da sie durch diese Karte nicht betroffen sind
								history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
								
							}
							
							break;
							
						case "lumberjack":
							
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: gewinnt 1 Kauf und 2 Geld\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							
							if(croupier.getActions()==0){
								
								String card = "lumberjack";
								
								history = createHistoryObjectForEndOfActions(card);
				        		
							}else{

								// Diese Karte muessen die anderen Spieler nicht sehen, da sie durch diese Karte nicht betroffen sind
								history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
								
							}
							
							break;
						
						case "market":
							
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: gewinnt je 1 Karte, Aktion, Kauf und Geld\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							
							croupier.getNewHoleCards(1);
							
							if(croupier.getActions()==0){
								
								String card = "market";
								
								history = createHistoryObjectForEndOfActions(card);
				        		
							}else{

								// Diese Karte muessen die anderen Spieler nicht sehen, da sie durch diese Karte nicht betroffen sind
								history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
								
							}
							
							break;
							
						case "militia":
							
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und erhaelt 2 Geld.\nZusaetzlich muessen seine Gegenspieler solange Karten ablegen,\nbis sie nur noch 3 Karten in der Hand haben\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							
							croupier.setActionMode(false);

							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.getLbl_cardName().getText(),null, GameHistory.HistoryType.PlayCard);
							
							break;
							
						case "mine":
							croupier.setActionMode(false);
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: darf eine Geldkarte entsorgen und eine neue erwerben,\ndie bis zu 3 Geld mehr kostet als die Weggeworfene.\nEr darf die erworbene Karte direkt auf die Hand nehmen\n");
							sl.getStrBuilderLabel().append("am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							
							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.getLbl_cardName().getText(),ac.text_DE, GameHistory.HistoryType.PlayCard);

							break;
							
						case "moat":
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und darf 2 neue Karten ziehen\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							
							// Aktion dieser Karte
							croupier.getNewHoleCards(2);
							
							if(croupier.getActions()==0){
								
								String card = "moat";
								
								history = createHistoryObjectForEndOfActions(card);
				        		
							}else{

								// Diese Karte muessen die anderen Spieler nicht sehen, da sie durch diese Karte nicht betroffen sind
								history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
								
							}
							
							break;						
							
						case "moneylender":
							croupier.setActionMode(false);
	
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: darf eine Kupferkarte von der Hand wegwerfen\nund kriegt dafuer +3 Geld\n");
							sl.getStrBuilderLabel().append("am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");

							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.getLbl_cardName().getText(),ac.text_DE, GameHistory.HistoryType.PlayCard);

							
							break;
							
						case "rebuilding":
							croupier.setActionMode(false);
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: darf eine Handkarte entsorgen\nund eine Neue erwerben, die bis zu 2 Geld\nmehr kostet als die Weggeworfene\n");
							sl.getStrBuilderLabel().append("am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");

							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.getLbl_cardName().getText(),ac.text_DE, GameHistory.HistoryType.PlayCard);

							try {
								sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
							// GUI wird aktualisiert
							sl.getPlayingStage().updateGUI();
							
							return;
							
						case "village": 
							
							croupier.getNewHoleCards(1);
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte: gewinnt 2 Aktionen und zieht 1 Karte nach\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							
							// Diese Karte muessen die anderen Spieler nicht sehen, da sie durch diese Karte nicht betroffen sind
							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.PlayCard);
												
							
							break;

							
						case "witch":
							croupier.getNewHoleCards(2);
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und zieht 2 Karten nach.\nZusaetzlich muessen alle Gegenspieler eine Fluchkarte aufnehmen\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							
							
							croupier.setActionMode(false);

							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.getLbl_cardName().getText(),null, GameHistory.HistoryType.PlayCard);

							break;
							
						case "workshop":
							appendTextArea(sl.getPlayer_noOS().getUsername()+" spielt "+ac.text_DE+"-Karte und darf eine neue Karte erwerben, die bis zu 4 Geld kostet\n");
							sl.getStrBuilderLabel().append("am Zug\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
							
							croupier.setActionMode(false);

							history = new GameHistory(sl.getStrBuilderTextArea().toString(),sl.getStrBuilderLabel().toString(),sl.getCurrentGameParty(),sl.getPlayer_noOS(),ac.getLbl_cardName().getText(),null, GameHistory.HistoryType.PlayCard);
							
							try {
								sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
							// GUI wird aktualisiert
							sl.getPlayingStage().updateGUI();
							
							return;
							
						}
						
						
						
						try {
							sl.getPlayer_OS().getOut().reset();
							sl.getPlayer_OS().getOut().writeObject(history);
							sl.getPlayer_OS().getOut().flush();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						
						}
						
						// Karten koennen auf den Ablagestapel geworfen werden und anschliessend kann dieselbe Anzahl vom Nachziehstapel gezogen werden <Keller>
						if(isHoleCard() && croupier.isDiscardMode()){		
							croupier.getHoleCards().remove(ac);
							croupier.addToAblagestapel(ac);
							croupier.increaseDiscardedCards();
							
							appendTextArea(sl.getPlayer_noOS().getUsername()+" legt eine "+ac.text_DE+" Karte ab\n");
							
							if(croupier.getHoleCards().isEmpty()){
								croupier.setDiscardMode(false);
								appendTextArea(sl.getPlayer_noOS().getUsername()+" beendet das Ablegen und darf "+croupier.getDiscrardCounter()+" Karten nachziehen\n");
								sl.getLabelNumberOfActionsAndBuys().setText("Du bist am Zug:\n"+croupier.getActions()+" Aktionen, "+croupier.getBuys()+" Kaeufe, "+croupier.getBuyPower()+" Geld");
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
								e1.printStackTrace();
							}
						
						}
							
							
						
						// Bis zu 4 Karten koennen weggeworfen werden
						if(isHoleCard() && croupier.isTrashModeChapel()){		
							
							// Karten wegwerfen
							croupier.getHoleCards().remove(ac);

							appendTextArea(sl.getPlayer_noOS().getUsername()+" wirft eine "+ac.text_DE+"-Karte weg\n");
							croupier.increaseTrashedCards();
							
							if(croupier.getTrashCounter() == 4 || croupier.getHoleCards().isEmpty()){
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
								e1.printStackTrace();
							}
						

						}
						
						// Eine Karte kann weggeworfen werden und eine beliebige Karte, welche bis zu 2 Geld mehr kostet als die Weggeworfene, kann gekauft werden
						if(isHoleCard() && croupier.isTrashModeRebuilding()){		
							
							// Karte wegwerfen
							croupier.getHoleCards().remove(ac);
							
							// Geldwert der weggeworfenen Karte speichern
							croupier.setCardValueForRebuildingMode(ac.costs);
							appendTextArea(sl.getPlayer_noOS().getUsername()+" wirft eine "+ac.text_DE+"-Karte weg\n");
							
							croupier.setTrashModeRebuilding(false);
							croupier.setModeForRebuilding(true);
							
							GameHistory history = new GameHistory(sl.getStrBuilderTextArea().toString(),null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.Trash);
							
							try {
								//reset?
								sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						
						}
						
						
						// Neue Karte kann erworben werden (Rebuild-Modus)
						if(!isHoleCard() && croupier.isModeForRebuilding() && costs <= croupier.getCardValueForRebuildingMode() && croupier.getStackSize(ac)>0){	
							ActionCard newCard = new ActionCard(ac.lbl_cardName,ac.costs,ac.adtnlActions,ac.adtnlBuys,ac.adtnlBuyPower,ac.text_DE);
							croupier.setStackSize(ac.getLbl_cardName().getText());
							
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
						        	
						        	appendTextArea(sl.getPlayerName()+" beendet Aktionsphase\n");

									history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),sl.getPlayer_noOS(),newCard.getLbl_cardName().getText(),null, GameHistory.HistoryType.RebuildingModeEnd);
								}else{
									croupier.setActionMode(true);
									appendTextArea(sl.getPlayerName()+" hat noch weitere Aktionen\n");
									history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),sl.getPlayer_noOS(),newCard.getLbl_cardName().getText(),null, GameHistory.HistoryType.RebuildingModeEnd);
								}
																
								try {
									//reset?
									sl.getPlayer_OS().getOut().reset();
									sl.getPlayer_OS().getOut().writeObject(history);
									sl.getPlayer_OS().getOut().flush();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
							
												
						}
						
						// Neue Karte kann erworben werden (Workshop-Modus)
						if(!isHoleCard() && croupier.isModeForWorkshop() && costs <= 4 && croupier.getStackSize(ac)>0){	
							ActionCard newCard = new ActionCard(ac.lbl_cardName,ac.costs,ac.adtnlActions,ac.adtnlBuys,ac.adtnlBuyPower,ac.text_DE);
							croupier.setStackSize(ac.getLbl_cardName().getText());
							
							if(sl.getCurrentGameParty().getGameHasEnded()==false){
								croupier.addObserver(newCard);
								newCard.setHoleCard(true);
								croupier.addToAblagestapel(newCard);
								newCard.assignPicture();
								
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
									e1.printStackTrace();
								}
							}
													
						
						}
						
						// Neue Karte kann erworben werden (Workshop-Modus)
						if(isHoleCard() && croupier.isReactionMode() && getLbl_cardName().getText().equals("moat")){	
							appendTextArea(sl.getPlayer_noOS().getUsername()+" wehrt den Angriff ab\n");
							croupier.setReactionMode(false);
							GameHistory history = new GameHistory(sl.getStrBuilderTextArea().toString(), null, sl.getCurrentGameParty(),croupier.getCurrentPlayer(),null,null, GameHistory.HistoryType.Reaction);
							
							try {
								//reset?
								sl.getPlayer_OS().getOut().reset();
								sl.getPlayer_OS().getOut().writeObject(history);
								sl.getPlayer_OS().getOut().flush();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						
						}
						
						// Karten ablegen, wenn ein Gegenspieler eine Miliz-Karte gespielt hat (Discard-Modus)
						if(isHoleCard() && croupier.isDiscardModeMilitia()){		
							croupier.getHoleCards().remove(ac);
							croupier.addToAblagestapel(ac);
							
							appendTextArea(sl.getPlayer_noOS().getUsername()+" legt eine "+ac.text_DE+" Karte ab\n");
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
					//sl.getPlayingStage().updateGUI();
					} 
					

				// Mit einem Rechtsklick wird das Bild geoeffnet
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

	private void appendTextArea(String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				sl.getStrBuilderTextArea().append(text);
			}
		});
	}
		
		public GameHistory createHistoryObjectForEndOfActions(String actionCase){
			
			croupier.setActionMode(false);
			croupier.setBuyMode(true);
        	
        	appendTextArea(sl.getPlayerName()+" beendet Aktionsphase\n");

			GameHistory history = new GameHistory(sl.getStrBuilderTextArea().toString(), sl.getStrBuilderLabel().toString(), sl.getCurrentGameParty(),sl.getPlayer_noOS(),null,null, GameHistory.HistoryType.EndAction);
			return history;
		}


				
}
