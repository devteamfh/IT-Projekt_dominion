package Dominion.Client.ClientClasses.gameplay.cards;

import java.util.Observable;
import java.util.Observer;

import Dominion.Client.ClientClasses.ServiceLocatorClient;
import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.appClasses.GameObject.ObjectType;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import sun.misc.GC;

public class GameCard extends Button implements Observer  {
	ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
	Croupier croupier = Croupier.getCroupier();
	
	Label   lbl_cardName;
	boolean holeCard  = false;
	
	int costs;

	StackSizeInfo stackSizeInfo;

	
	public GameCard(Label cardName) {
		super();
		this.lbl_cardName = cardName;
		this.assignPicture();
	}
	
	GameCard gc = this;
	@Override
	public void update(Observable arg0, Object arg1) {
		
		
		getStyleClass().remove("highlight");
		
		//Highlighte alle Karten im Kaufmodus, welche ich mit der aktuelln Buypower und buys kaufen kann
		if (croupier.isBuyMode() == true && croupier.getBuyPower() >= this.costs && !this.isHoleCard() && croupier.getBuys() > 0){
			this.getStyleClass().add("highlight");
		}
		
		//Highlighte alle Geldkarten im Kaufmodus
		if (croupier.isBuyMode() == true && this.isHoleCard() && this instanceof MoneyCard){
			this.getStyleClass().add("highlight");
		}
		
		//wenn stacksize auf 0, wird highlighting ebenfalls deaktiviert
		if (croupier.getStackSize(gc) == 0) {
			this.getStyleClass().remove("highlight");
		}
	
		//update das STackSize infoLabel auf der Karte		
		if (this.stackSizeInfo != null){
			
			stackSizeInfo.updateStackSizeInfo();
				
		}
		
		
	}
		
	
	public void assignStackSizeInfo(){
		stackSizeInfo = new StackSizeInfo(gc,croupier.getStackSize(gc));
	}

	
	
	
	public void assignPicture(){
		if (this.holeCard == false)
			this.getStyleClass().addAll("card",lbl_cardName.getText());
		else 
			this.getStyleClass().addAll("card",lbl_cardName.getText()+"_big");
	}
	
	
	public boolean isHoleCard() {
		return holeCard;
	}

	public void setHoleCard(boolean holeCard) {
		this.holeCard = holeCard;
	}

	public Label getLbl_cardName() {
		return lbl_cardName;
	}

	public void setLbl_cardName(Label lbl_cardName) {
		this.lbl_cardName = lbl_cardName;
	}





	
	
}
