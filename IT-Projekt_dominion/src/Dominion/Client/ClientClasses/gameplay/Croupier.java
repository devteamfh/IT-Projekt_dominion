package Dominion.Client.ClientClasses.gameplay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.sound.midi.Synthesizer;

import com.sun.glass.ui.View;

import Dominion.Client.ClientClasses.Client_View_playingStage;
import Dominion.Client.ClientClasses.ServiceLocatorClient;
import Dominion.Client.ClientClasses.CustomButton;
import Dominion.Client.ClientClasses.gameplay.cards.Cards;
import Dominion.Client.ClientClasses.gameplay.cards.GameCard;
import Dominion.Server.ServerClasses.ServiceLocatorServer;
import Dominion.appClasses.GameHistory;
import Dominion.appClasses.PlayerWithoutOS;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

/**
 * @author kab: Croupier verwaltet alle Felder welche zum Gameplay n�tig sind
 * @author Joel: methods where mentioned
 * 
 */
public class Croupier  extends Observable {
	ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
    //Felder
	private static Croupier croupier;

	boolean actionMode = false;
	boolean buyMode    = false;
	boolean discardMode = false;
	boolean trashModeChapel=false;
	boolean trashModeMine = false;
	boolean modeForMine = false;
	boolean trashModeMoneylender =false;
	boolean trashModeRebuilding=false;
	boolean modeForRebuilding = false;
	boolean modeForWorkshop = false;
	boolean modeForAttack = false;
	boolean reactionMode = false;
	boolean modeForDiscardMilitia = false;
	boolean modeForCurseCard = false;
	int numberOfEmptyStacks=0;
	
	int buyPower = 0;
	int actions;
	int buys;
	int counterTrashedCardsModeChapel;
	int savedMCValueForMineMode;
	int savedCardValueForRebuildingMode;
	int discardCounter;
	int countReactions;
	PlayerWithoutOS currentPlayer;
	
	Label lbl_playerListStage = new Label();
	
	Label lbl_buyPower = new Label();
	Label lbl_actions  = new Label();
	Label lbl_buys     = new Label();
	

	//# of match cards and cost of match cards; 10
	int stackSizeEstate   = 10; int costsEstate  = 2; int pointsEstate =1;
	int stackSizeDuchy    = 10; int costsDuchy   = 5; int pointsDuchy =3;
	int stackSizeProvince = 10; int costsPovince = 8; int pointsProvince =6;
	int stackSizeCurse    = 10; int costsCurse   = 0; int pointsCurse = -1;
	
	//# of money cards and cost of money cards; 50
	int stackSizeCopper   = 50; int costsCopper  = 0; int buyPowerCopper = 1;
	int stackSizeSilver   = 50; int costsSilver  = 3; int buyPowerSilver = 2;
	int stackSizeGold     = 50; int costsGold    = 6; int buyPowerGold   = 3;
	
	//# of Community Action Cards on Board
	ArrayList<Integer> al_stackSizeCommunityActionCards = new ArrayList<Integer>();
	
	LinkedList<GameCard> ll_ablagestapel = new LinkedList<GameCard>();
	LinkedList<GameCard> ll_nachziehstapel = new LinkedList<GameCard>();
	LinkedList<GameCard> ll_holeCards = new LinkedList<GameCard>();
	
	ArrayList<GameCard> al_communityActionCards = new ArrayList<GameCard>();


	
	//Konstruktoren
    public static Croupier getCroupier() {
        if (croupier == null)
            croupier = new Croupier();
        return croupier;
    }
    
    public static void setCroupierNull(){
    	croupier=null;
    }
  
    private Croupier() {

    }
   
    
    //Methoden
	public Croupier(Observer o){   
	      this.addObserver(o);
		}

	public void setBuyPower(int buyPower) {
		this.buyPower = buyPower;
		
		setChanged();
		notifyObservers();	
    	sl.getPlayingStage().updateGUI(); 
	}
		
	public void setBuyPower() {
 		this.buyPower++;
 		setChanged();
		notifyObservers();
		}	
	
	public void setStackSize(String str_lbl_cardName){
				
		//ist es eine action Karte links (province, curse, money?)
		switch (str_lbl_cardName) {
		
		case "estate":    setStackSizeEstate(stackSizeEstate-1);
		if(stackSizeEstate == 0){
			this.numberOfEmptyStacks++;
		}
			break;
		case "duchy":     setStackSizeDuchy(stackSizeDuchy-1);
		if(stackSizeDuchy == 0){
			this.numberOfEmptyStacks++;
		}
			break;
		case "province":  setStackSizeProvince(stackSizeProvince-1);
			break;
		case "copper":    setStackSizeCopper(stackSizeCopper-1);
		if(stackSizeCopper == 0){
			this.numberOfEmptyStacks++;
		}
			break;
		case "silver":    setStackSizeSilver(stackSizeSilver-1);
		if(stackSizeSilver == 0){
			this.numberOfEmptyStacks++;
		}
			break;
		case "gold":      setStackSizeGold(stackSizeGold-1);
		if(stackSizeGold == 0){
			this.numberOfEmptyStacks++;
		}
			break;
		case "curse": 	  setStackSizeCurse(stackSizeCurse-1);
		if(stackSizeCurse == 0){
			this.numberOfEmptyStacks++;
		}
			break;
		}
		
		//dann ist es eine Community Action Card
		for (int i = 0; i < getAl_communityActionCards().size();i++){
			//welche  hier u 1 reduziert wird
			if (getAl_communityActionCards().get(i).getLbl_cardName().getText().equals(str_lbl_cardName)){
			this.al_stackSizeCommunityActionCards.set(i,al_stackSizeCommunityActionCards.get(i)-1);
			if(this.al_stackSizeCommunityActionCards.get(i)==0){
				this.numberOfEmptyStacks++;
				
			}
			break;
			 }
		}
		
		
		/**
		 * @author Joel: 
		 */
		if((this.numberOfEmptyStacks==3 || this.stackSizeProvince ==0) && sl.getCurrentGameParty().getSelectedMode().equals("Provinzkarten")){
			sl.getCurrentGameParty().setGameHasEnded(true);
			GameHistory history = new GameHistory(null,sl.getCurrentGameParty(),sl.getPlayer_noOS(),str_lbl_cardName,GameHistory.HistoryType.EndGameModeProvince);
			try {
				sl.getPlayer_OS().getOut().reset();
				sl.getPlayer_OS().getOut().writeObject(history);
				sl.getPlayer_OS().getOut().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			setChanged();
			notifyObservers();
	    	sl.getPlayingStage().updateGUI();
		}
		
	}
	
	public int getStackSize(GameCard gc){
		
		//ist es eine action Karte links (province, curse, money?)
		switch (gc.getLbl_cardName().getText()) {
		
		case "estate":    return getStackSizeEstate(); 
		case "duchy":     return getStackSizeDuchy(); 
		case "province":  return getStackSizeProvince(); 
		case "copper":    return getStackSizeCopper(); 
		case "silver":    return getStackSizeSilver(); 
		case "gold":      return getStackSizeGold(); 
		case "curse":	  return getStackSizeCurse();
		}
		//dann ist es eine Community Action Card
		if (getAl_communityActionCards().contains(gc)) {
			int i = getAl_communityActionCards().indexOf(gc);
			return getAl_stackSizeCommunityActionCards().get(i);
		}
		return -1;
	}


	
	//Legt die Karten von den Hole Cards zum Ablagestapel (am Ende jeder Buyphase)
		public void muckHoleCards() {
			for (int i = ll_holeCards.size()-1; i >= 0; i--){
				addToAblagestapel(ll_holeCards.get(i));
				ll_holeCards.remove(i);
			}	
		}
		
		//Zieht 5 Karten vom Nachziehstapel zu den Hole Cards am Anfang jeder Spielphase
		public void drawHoleCards(){
			if (ll_nachziehstapel.size() < 5){
				putAblagestapelToNachziehstapel();
			}
			
			for (int i = 5; i > 0; i--){
				ll_holeCards.add(ll_nachziehstapel.pop());
			   	sl.getPlayingStage().updateGUI();
			}	
		}
		
		//mischt den Ablagestapel durch und f�gt die Karten dem Nachziehstapel hinzu
		public void putAblagestapelToNachziehstapel(){
			Collections.shuffle(ll_ablagestapel);
			
			while(ll_ablagestapel.size() != 0)
				ll_nachziehstapel.add(ll_ablagestapel.pop());
		}
		
	public int getStackSizeEstate() {
		return stackSizeEstate;
	}

	public void setStackSizeEstate(int stackSizeEstate) {
		this.stackSizeEstate = stackSizeEstate;
	}

	public int getCostsEstate() {
		return costsEstate;
	}

	public void setCostsEstate(int costsEstate) {
		this.costsEstate = costsEstate;
	}

	public int getStackSizeDuchy() {
		return stackSizeDuchy;
	}

	public void setStackSizeDuchy(int stackSizeDuchy) {
		this.stackSizeDuchy = stackSizeDuchy;
	}

	public int getCostsDuchy() {
		return costsDuchy;
	}

	public void setCostsDuchy(int costsDuchy) {
		this.costsDuchy = costsDuchy;
	}

	public int getStackSizeProvince() {
		return stackSizeProvince;
	}

	public void setStackSizeProvince(int stackSizeProvince) {
		this.stackSizeProvince = stackSizeProvince;
	}

	public int getCostsPovince() {
		return costsPovince;
	}

	public void setCostsPovince(int costsPovince) {
		this.costsPovince = costsPovince;
	}

	public int getStackSizeCopper() {
		return stackSizeCopper;
	}

	public void setStackSizeCopper(int stackSizeCopper) {
		this.stackSizeCopper = stackSizeCopper;
	}

	public int getCostsCopper() {
		return costsCopper;
	}

	public void setCostsCopper(int costsCopper) {
		this.costsCopper = costsCopper;
	}

	public int getBuyPowerCopper() {
		return buyPowerCopper;
	}

	public void setBuyPowerCopper(int buyPowerCopper) {
		this.buyPowerCopper = buyPowerCopper;
	}

	public int getStackSizeSilver() {
		return stackSizeSilver;
	}

	public void setStackSizeSilver(int stackSizeSilver) {
		this.stackSizeSilver = stackSizeSilver;
	}

	public int getCostsSilver() {
		return costsSilver;
	}

	public void setCostsSilver(int costsSilver) {
		this.costsSilver = costsSilver;
	}

	public int getBuyPowerSilver() {
		return buyPowerSilver;
	}

	public void setBuyPowerSilver(int buyPowerSilver) {
		this.buyPowerSilver = buyPowerSilver;
	}

	public int getStackSizeGold() {
		return stackSizeGold;
	}

	public void setStackSizeGold(int stackSizeGold) {
		this.stackSizeGold = stackSizeGold;
	}

	public int getCostsGold() {
		return costsGold;
	}

	public void setCostsGold(int costsGold) {
		this.costsGold = costsGold;
	}

	public int getBuyPowerGold() {
		return buyPowerGold;
	}

	public void setBuyPowerGold(int buyPowerGold) {
		this.buyPowerGold = buyPowerGold;
	}
	
	public int getPointsEstate(){
		return this.pointsEstate;
	}
	
	public int getPointsDuchy(){
		return this.pointsDuchy;
	}
	
	public int getPointsProvince(){
		return this.pointsProvince;
	}

	public boolean isActionMode() {
		return actionMode;
	}

	public void setActionMode(boolean actionMode) {
		
		this.actionMode = actionMode;
		setChanged();
		notifyObservers();
	}

	public boolean isDiscardMode(){
		return this.discardMode;
	}

	public void setDiscardMode(boolean discardMode){
		this.discardMode = discardMode;
		setChanged();
		notifyObservers();
	}

	public boolean isTrashModeChapel(){
		return this.trashModeChapel;
	}
	
	public void setTrashModeChapel(boolean trashMode){
		this.trashModeChapel = trashMode;
		setChanged();
		notifyObservers();
	}
	
	public boolean isTrashModeMoneylender(){
		return this.trashModeMoneylender;
	}
	
	public void setTrashModeMoneylender(boolean trashMode){
		this.trashModeMoneylender = trashMode;
		setChanged();
		notifyObservers();
	}
	
	public boolean isTrashModeRebuilding(){
		return this.trashModeRebuilding;
	}
	
	public void setTrashModeRebuilding(boolean trashMode){
		this.trashModeRebuilding = trashMode;
		setChanged();
		notifyObservers();
	}
	
	public boolean isTrashModeMine(){
		return this.trashModeMine;
	}
	
	public void setTrashModeMine(boolean trashMode){
		this.trashModeMine = trashMode;
		setChanged();
		notifyObservers();
	}
	
	public boolean isModeForMine(){
		return this.modeForMine;
	}
	
	public void setModeForMine(boolean mode){
		this.modeForMine = mode;
		setChanged();
		notifyObservers();
	}
	
	public boolean isModeForRebuilding(){
		return this.modeForRebuilding;
	}
	
	public void setModeForRebuilding(boolean mode){
		this.modeForRebuilding = mode;
		setChanged();
		notifyObservers();
	}
	
	public boolean isModeForWorkshop(){
		return this.modeForWorkshop;
	}
	
	public void setModeForWorkshop(boolean mode){
		this.modeForWorkshop = mode;
		setChanged();
		notifyObservers();
	}
	
	public boolean isReactionMode(){
		return this.reactionMode;
	}
	
	public void setReactionMode(boolean mode){
		this.reactionMode = mode;
		setChanged();
		notifyObservers();
	}
	
	public boolean isModeForAttack(){
		return this.modeForAttack;
	}
	
	public void setModeForAttack(boolean mode){
		this.modeForAttack = mode;
		setChanged();
		notifyObservers();
	}
	
	public boolean isDiscardModeMilitia(){
		return this.modeForDiscardMilitia;
	}
	
	public void setDiscardModeForMilitia(boolean mode){
		this.modeForDiscardMilitia = mode;
		setChanged();
		notifyObservers();
	}
	
	public boolean isModeForCurseCard(){
		return this.modeForCurseCard;
	}
	
	public void setModeCurseCard(boolean mode){
		this.modeForCurseCard = mode;
		setChanged();
		notifyObservers();
	}
		
	public void setTrashCounterModeChapel(int counter){
		this.counterTrashedCardsModeChapel = counter;
	}


	public boolean isBuyMode() {
		return buyMode;
	}


	public void setBuyMode(boolean buyMode) {
		this.buyMode = buyMode;
		setChanged();
		notifyObservers();
	}

	public int getBuyPower() {
		return buyPower;

	}


	public int getActions() {
		return actions;
	}

	public void setActions(int actionPoints) {
		this.actions = actionPoints;
		setChanged();
		notifyObservers();
    	sl.getPlayingStage().updateGUI();
	}

	public int getBuys() {
		return buys;
	}

	public void setBuys(int buys) {
		this.buys = buys;
		setChanged();
		notifyObservers();
    	sl.getPlayingStage().updateGUI();
	}

	
    public ArrayList<GameCard> getAl_communityActionCards() {
		return al_communityActionCards;
	}

	public void setAl_communityActionCards(ArrayList<GameCard> al_communityActionCards) {
		this.al_communityActionCards = al_communityActionCards;
	}
	
	//    ----- ABLAGESTAPEL-------
	public LinkedList<GameCard> getAblagestapel() {
		return ll_ablagestapel;
	}
	//hinzuf�gen
	public void addToAblagestapel(GameCard gc){
		gc.setHoleCard(true);
		this.ll_ablagestapel.add(gc);	
	}
	
	
	//    ----- Nachziehstapel-------
	public LinkedList<GameCard> getNachziehstapel() {
		return ll_nachziehstapel;
	}
	//hinzuf�gen
	public void addToNachziehstapel(GameCard gc) {
		gc.setHoleCard(true);
		this.ll_nachziehstapel.add(gc);
	}
	//    ----- Karten in der Hand-------	
	public LinkedList<GameCard> getHoleCards() {
		return ll_holeCards;
	}
	//hinzuf�gen
	public void addToHoleCards(GameCard gc) {
		gc.setHoleCard(true);
		this.ll_holeCards.add(gc);
	}
	
	public ArrayList<Integer> getAl_stackSizeCommunityActionCards() {
		return al_stackSizeCommunityActionCards;
	}

	public void setAl_stackSizeCommunityActionCards(ArrayList<Integer> al_stackSizeCommunityActionCards) {
		this.al_stackSizeCommunityActionCards = al_stackSizeCommunityActionCards;
		setChanged();
		notifyObservers();
    	sl.getPlayingStage().updateGUI();
	}
	
	/*public void setAl_stackSizeCommunityActionCards(int i) {
		this.al_stackSizeCommunityActionCards.set(i,al_stackSizeCommunityActionCards.get(i)-1);
		setChanged();
		notifyObservers();
    */
	
	// Initiale # communityActionCards
	public void prepareAL_stackSizeCommunityActionCards(){
		for (int i = 0; i<10; i++){
			al_stackSizeCommunityActionCards.add(10);	
			}
	}

	public int getStackSizeCurse() {
		return stackSizeCurse;
	}

	public void setStackSizeCurse(int stackSizeCurse) {
		this.stackSizeCurse = stackSizeCurse;
	}

	public int getCostsCurse() {
		return costsCurse;
	}

	public void setCostsCurse(int costsCurse) {
		this.costsCurse = costsCurse;
	}

	public int getPointsCurse() {
		return pointsCurse;
	}

	public void setPointsCurse(int pointsCurse) {
		this.pointsCurse = pointsCurse;
	}

	public Label getLbl_actions() {
		lbl_actions.setText(""+Integer.valueOf(actions));
	return lbl_actions;	
	}
	
	public Label getLbl_buyPower() {
		lbl_buyPower.setText(""+Integer.valueOf(buyPower)); 
		return lbl_buyPower;
	}

	public Label getLbl_buys() {
		lbl_buys.setText(""+Integer.valueOf(buys));
		return lbl_buys;
	}
	
	public void clearHoleCards(){
		this.ll_holeCards.clear();

	}
	
	/**
	 * @author Joel: 
	 clear all croupier resources so the player will start with new resources when he joins a new game party*/
	public void clear(){
		Croupier.croupier=null;
		
	}
	
	/**
	 * @author Joel: 
	 after a players turn*/
	public void removeHoleCards(){
		while(!this.getHoleCards().isEmpty()){
    		this.getAblagestapel().add(this.getHoleCards().poll());
    	}
		this.getNewHoleCards(5);
		
	}
	
	/**
	 * @author Joel: 
	 */
	public void getNewHoleCards (int number){
		//get new cards from "Nachziehstapel"
    	if(!(this.getNachziehstapel().size()<number)){
    		for(int i=0; i<number;i++){
    			this.getHoleCards().add(this.getNachziehstapel().poll());
    		}
    	}else{
    		int count=0;
    		//draw cards from Nachziehstapel until it is empty
    		while(!this.getNachziehstapel().isEmpty()){
    			this.getHoleCards().add(this.getNachziehstapel().poll());
    			count++;
    			
    		}
    		
    		
    		//shuffle the "Ablagestapel"
    		Collections.shuffle(this.getAblagestapel());
    		//add the whole Ablagestapel to Nachziehstapel
    		
    		while(!this.getAblagestapel().isEmpty()){
    			this.getNachziehstapel().add(this.getAblagestapel().poll());

    		}
    		
    		if(this.getNachziehstapel().size()<5){
    			while(!this.getNachziehstapel().isEmpty()){
    				this.getHoleCards().add(this.getNachziehstapel().poll());
    			}
    		}else{
    			//adding the last cards
        		int numberOfDrawsNachziehstapel = number-count;
        		for(int i=0; i<numberOfDrawsNachziehstapel;i++){
        			this.getHoleCards().add(this.getNachziehstapel().poll());
        		}
    		}
    		
    	}
		
		
		sl.getPlayingStage().updateGUI();
	}
	
	public void increaseTrashedCards(){
		this.counterTrashedCardsModeChapel++;
	}
	
	public int getTrashCounter(){
		return this.counterTrashedCardsModeChapel;
	}


	public Label getLbl_playerListStage() {
		return lbl_playerListStage;
	}

	public void setLbl_playerListStage(Label lbl_playerListStage) {
		this.lbl_playerListStage = lbl_playerListStage;
	}

	public void setMCValueForMineMode(int costs_MC){
		this.savedMCValueForMineMode = costs_MC+3;
	}
	
	public int getSavedMCValueForMineMode(){
		return this.savedMCValueForMineMode;
	}
	
	public void setCardValueForRebuildingMode(int costs){
		this.savedCardValueForRebuildingMode = costs+2;
	}
	
	public int getCardValueForRebuildingMode(){
		return this.savedCardValueForRebuildingMode;
	}
	
	public void increaseDiscardedCards(){
		this.discardCounter++;
	}
	
	public int getDiscrardCounter(){
		return this.discardCounter;
	}

	public void setDiscardedCounter(int value){
		this.discardCounter=value;
	}
	
	public void increaseReactionCounter(){
		this.countReactions++;
	}
	
	public int getReactionCounter(){
		return this.countReactions;
	}
	
	public void setCounterReactions(int value){
		this.countReactions=value;
	}
	
	public void saveCurrentPlayer(PlayerWithoutOS currentPlayer){
		this.currentPlayer=currentPlayer;
	}
	
	public PlayerWithoutOS getCurrentPlayer(){
		return this.currentPlayer;
	}
	
	public int getNumberOfEmptyStacks(){
		return this.numberOfEmptyStacks;
	}
	
}
