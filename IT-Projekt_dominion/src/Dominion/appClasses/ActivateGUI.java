package Dominion.appClasses;

public class ActivateGUI {
	boolean activateGUI;
	String player;
	
	public ActivateGUI(boolean activateGUI, String player){
		this.activateGUI=activateGUI;
		this.player=player;
	}
	
	public boolean getActivateGUI(){
		return this.activateGUI;
	}
	
	public String getPlayer(){
		return this.player;
	}
}
