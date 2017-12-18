package Dominion.Client.ClientClasses.gameplay.cards;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import Dominion.Client.ClientClasses.ServiceLocatorClient;
import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.appClasses.GameHistory;
import Dominion.appClasses.GameObject.ObjectType;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.misc.GC;

public class GameCard extends Button implements Observer  {
	ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
	Croupier croupier = Croupier.getCroupier();
	
	Label   lbl_cardName;
	String text_DE;
	boolean holeCard  = false;
	
	int costs;

	StackSizeInfo stackSizeInfo;

	
	public GameCard(Label cardName, String text_DE) {
		super();
		this.lbl_cardName = cardName;
		this.text_DE=text_DE;
		this.assignPicture();
	}
	
	GameCard gc = this;
	
	@Override
	public void update(Observable arg0, Object arg1) {
		
		
		getStyleClass().remove("highlight");
		getStyleClass().remove("highlight2");
		getStyleClass().remove("highlight3");
		
		//Highlighte alle Karten im Kaufmodus, welche ich mit der aktuelln Buypower und buys kaufen kann
		if (croupier.isBuyMode() == true && croupier.getBuyPower() >= this.costs && !this.isHoleCard() && croupier.getBuys() > 0){
			this.getStyleClass().add("highlight");
		}
		
		//Highlighte alle Geldkarten im Kaufmodus
		if (croupier.isBuyMode() == true && this.isHoleCard() && this instanceof MoneyCard){
			this.getStyleClass().add("highlight");
		}
		
		//Highlighte alle Aktionskarten im Aktionsmodus
		if (croupier.isActionMode() == true && this.isHoleCard() && this instanceof ActionCard){
			this.getStyleClass().add("highlight");
		}
		
		//Highlighte alle HoleCards im Discardmodus
		if (croupier.isDiscardMode() == true && this.isHoleCard()){
			this.getStyleClass().add("highlight2");
		}
		
		//Highlighte alle HoleCards im TrashModus Kapelle
		if (croupier.isTrashModeChapel() == true && this.isHoleCard()){
			this.getStyleClass().add("highlight2");
		}
		
		//Highlighte alle MoneyCards im TrashModus Mine
		if (croupier.isTrashModeMine() == true && this instanceof MoneyCard && this.isHoleCard()){
			this.getStyleClass().add("highlight2");
		}
		
		//Highlighte alle Karten auf der Hand, welche im Umbaumodus weggeworfen werden können
		if (croupier.isTrashModeRebuilding() == true && this.isHoleCard()){
			this.getStyleClass().add("highlight2");
		}
		
		//Highlighte alle Kupferkarten auf der Hand, die gegen +3Geld weggeworfen werden können (ActionCard Geldverleiher)
		if (croupier.isTrashModeMoneylender() == true && this.isHoleCard() && this.getLbl_cardName().getText().equals("copper")){
			this.getStyleClass().add("highlight2");
		}
		
		//Highlighte alle MoneyCards, welche erworben werden können im Minen-Modus
		if (croupier.isModeForMine() == true && this instanceof MoneyCard && !this.isHoleCard() && this.costs <= croupier.getSavedMCValueForMineMode()){
			this.getStyleClass().add("highlight3");
		}
		
		//Highlighte alle Karten zum Kauf, welche erworben werden können im Rebuilding-Modus
		if (croupier.isModeForRebuilding() == true && !this.isHoleCard() && this.costs <= croupier.getCardValueForRebuildingMode()){
			this.getStyleClass().add("highlight3");
		}
		
		//Highlighte alle Karten zum Kauf, welche erworben werden können im Rebuilding-Modus
		if (croupier.isModeForWorkshop() == true && !this.isHoleCard() && this.costs <= 4){
			this.getStyleClass().add("highlight3");
		}
		
		//Highlighte alle Moat-Karten wenn ein Angriff gemacht worden ist
		if (croupier.isReactionMode() == true && this.isHoleCard() && this.getLbl_cardName().getText().equals("moat")){
			this.getStyleClass().add("highlight3");
		}
		
		//Highlighte alle Moat-Karten wenn ein Angriff gemacht worden ist
		if (croupier.isDiscardModeMilitia() == true && this.isHoleCard()){
			this.getStyleClass().add("highlight2");
		}
		
		//Highlighte alle Moat-Karten wenn ein Angriff gemacht worden ist
		if (croupier.isModeForCurseCard() == true && !this.isHoleCard() && this.getLbl_cardName().getText().equals("curse")){
			this.getStyleClass().add("highlight3");
		}
		
		//Highlight wegnehmen wenn Trash Modus wieder verlassen wird (Kapelle, Mine)
		//if ((croupier.isTrashModeChapel() == false && this.isHoleCard())   ||   (croupier.isTrashModeMine() == false && this.isHoleCard() && this instanceof MoneyCard)){
			//this.getStyleClass().remove("highlight2");
		//}
		
		//Highlight wegnehmen wenn Minen-Modus wieder verlassen wird
		//if ((croupier.isModeForMine() == false && !this.isHoleCard())  && this instanceof MoneyCard){
			//this.getStyleClass().remove("highlight3");
		//}
		
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
	
	public String getText_DE(){
		return this.text_DE;
	}

	public void setLbl_cardName(Label lbl_cardName) {
		this.lbl_cardName = lbl_cardName;
	}	
}
