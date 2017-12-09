package Dominion.Client.ClientClasses.gameplay;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import com.sun.glass.ui.View;

import Dominion.Client.ClientClasses.Client_View_playingStage;
import Dominion.Client.ClientClasses.ServiceLocatorClient;
import Dominion.Client.ClientClasses.gameplay.cards.Cards;
import Dominion.Client.ClientClasses.gameplay.cards.GameCard;
import Dominion.Server.ServerClasses.ServiceLocatorServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * @author kab: Croupier verwaltet alle Felder welche zum Gameplay nötig sind
 * 
 * 
 */
public class Croupier  extends Observable {
	ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
    //Felderi
	private static Croupier croupier;
	
	boolean actionMode = true;
	boolean buyMode    = false;
	
	int buyPower = 0;
	int actions;
	int buys;
	
	//# of match cards and cost of match cards;
	int stackSizeEstate   = 10; int costsEstate  = 1;
	int stackSizeDuchy    = 10; int costsDuchy   = 3;
	int stackSizeProvince = 10; int costsPovince = 6;
	
	//# of money cards and cost of money cards;
	int stackSizeCopper   = 50; int costsCopper  = 0; int buyPowerCopper = 1;
	int stackSizeSilver   = 50; int costsSilver  = 3; int buyPowerSilver = 2;
	int stackSizeGold     = 50; int costsGold    = 6; int buyPowerGold   = 3;

	//# of Community Action Cards on Board
	ArrayList<Integer> al_stackSizeCommunityActionCards = new ArrayList<Integer>();

	
	LinkedList<GameCard> ll_ablageStapel = new LinkedList<GameCard>();
	LinkedList<GameCard> ll_nachziehStapel = new LinkedList<GameCard>();
	LinkedList<GameCard> ll_holeCards = new LinkedList<GameCard>();
	
	ArrayList<GameCard> al_communityActionCards = new ArrayList<GameCard>();

	
	//Konstruktoren
    public static Croupier getCroupier() {
        if (croupier == null)
            croupier = new Croupier();
        return croupier;
    }
  
    private Croupier() {

    }
   
    
    //Methoden
	public Croupier(Observer o){   
	      this.addObserver(o);
		}
	

			
	
	
	public void setStackSize(GameCard gc){

		switch (gc.getLbl_cardName().getText()) {
		
		case "estate":    setStackSizeEstate(getStackSizeEstate()-1);
			break;
		case "duchy":     setStackSizeDuchy(getStackSizeDuchy()-1);
			break;
		case "province":  setStackSizeProvince(getStackSizeProvince()-1);
			break;
		case "copper":    setStackSizeCopper(getStackSizeCopper()-1);
			break;
		case "silver":    setStackSizeSilver(getStackSizeSilver()-1);
			break;
		case "gold":      setStackSizeGold(getStackSizeGold()-1);
			break;
		}
	}
	
	public int getStackSize(GameCard gc){

	
		switch (gc.getLbl_cardName().getText()) {
		
		case "estate":    return getStackSizeEstate(); 
		case "duchy":     return getStackSizeDuchy(); 
		case "province":  return getStackSizeProvince(); 
		case "copper":    return getStackSizeCopper(); 
		case "silver":    return getStackSizeSilver(); 
		case "gold":      return getStackSizeGold(); 
		}
		return -1;
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

	public boolean isActionMode() {
		return actionMode;
	}


	public void setActionMode(boolean actionMode) {
		this.actionMode = actionMode;
		setChanged();
		notifyObservers();
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


	public void setBuyPower(int buyPower) {
		this.buyPower = buyPower;
		
	}
		
	public void setBuyPower() {
 		this.buyPower++;
 		setChanged();
		notifyObservers();
		}


	public int getActions() {
		return actions;
	}

	public void setActionPoints(int actionPoints) {
		this.actions = actionPoints;
	}

	public int getBuys() {
		return buys;
	}

	public void setBuys(int buys) {
		this.buys = buys;
	}

	
    public ArrayList<GameCard> getAl_communityActionCards() {
		return al_communityActionCards;
	}

	public void setAl_communityActionCards(ArrayList<GameCard> al_communityActionCards) {
		this.al_communityActionCards = al_communityActionCards;
	}
	

	public LinkedList<GameCard> getLl_ablageStapel() {
		return ll_ablageStapel;
	}

	public void setLl_ablageStapel(LinkedList<GameCard> ll_ablageStapel) {
		this.ll_ablageStapel = ll_ablageStapel;
	}

	public LinkedList<GameCard> getLl_nachziehStapel() {
		return ll_nachziehStapel;
	}

	public void setLl_nachziehStapel(LinkedList<GameCard> ll_nachziehStapel) {
		this.ll_nachziehStapel = ll_nachziehStapel;
	}

	public LinkedList<GameCard> getLl_holeCards() {
		return ll_holeCards;
	}

	public void setLl_holeCards(LinkedList<GameCard> ll_holeCards) {
		this.ll_holeCards = ll_holeCards;
	}
	
	//fügt eine Karte den hole cards hinzu
	public void drawHoleCard(GameCard gc){
		ll_holeCards.add(gc);
	}

	public ArrayList<Integer> getAl_stackSizeCommunityActionCards() {
		return al_stackSizeCommunityActionCards;
	}

	public void setAl_stackSizeCommunityActionCards(ArrayList<Integer> al_stackSizeCommunityActionCards) {
		this.al_stackSizeCommunityActionCards = al_stackSizeCommunityActionCards;
	}
	
	public void prepareAL_stackSizeCommunityActionCards(){
		for (int i = 0; i<10; i++){
			al_stackSizeCommunityActionCards.add(10);	
			}
	}
	
}
