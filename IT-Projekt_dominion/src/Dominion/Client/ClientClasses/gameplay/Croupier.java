package Dominion.Client.ClientClasses.gameplay;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import Dominion.Client.ClientClasses.ServiceLocatorClient;
import Dominion.Client.ClientClasses.gameplay.cards.Cards;
import Dominion.Client.ClientClasses.gameplay.cards.GameCard;
import Dominion.Server.ServerClasses.ServiceLocatorServer;

public class Croupier extends Observable {
	ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
    //Felder
	private static Croupier croupier;
	
	boolean actionMode = false;
	boolean buyMode    = false;
	
	int buyPower;
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
	

	
	
	
	

}
