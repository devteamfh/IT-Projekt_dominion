package Dominion.Client.ClientClasses.gameplay.cards;

import Dominion.Client.ClientClasses.gameplay.Croupier;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class StackSizeInfo {
	Croupier croupier = Croupier.getCroupier();
	Text t = new Text();
	Rectangle r = new Rectangle(26, 26);
    DropShadow e = new DropShadow();
	StackPane stackPane = new StackPane();
	HBox wrapper = new HBox();
	Group g = new Group();
	
	GameCard gc;
	int stackSize;
	
	public StackSizeInfo(GameCard gc, int stackSize){
		this.gc = gc;
		this.stackSize = stackSize;
		assignStackSizeInfo();
	}
	
	public void assignStackSizeInfo() {//Zeigt Stackanzal an
		
		t.setFill(Color.BLACK);
		t.setText(""+stackSize);
		t.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 20));
		t.setBoundsType(TextBoundsType.VISUAL);
		t.setCache(true);
	
	    r.setFill(Color.LIGHTGRAY);
	    r.setStroke(Color.BLACK);
	    r.setStrokeWidth(2);
		r.setFill(Color.GOLD);
		r.setCache(true);
		

	    e.setWidth(10);
	    e.setHeight(10);
	    e.setOffsetX(2);
	    e.setOffsetY(2);
	    e.setRadius(0);
	    r.setEffect(e);
	    	

		g.getChildren().add(r);
 	

		stackPane.getChildren().addAll(g,t);
		stackPane.setCache(true);
		
		wrapper.setAlignment(Pos.TOP_RIGHT);
		wrapper.getChildren().add(stackPane);
		wrapper.setPadding(new Insets(0,0,0,0));
		gc.setGraphic(wrapper);

}
	
	public void updateStackSizeInfo(){
		t.setText(""+croupier.getStackSize(gc));
		stackPane.getChildren().clear();
		stackPane.getChildren().addAll(g,t);
		wrapper.getChildren().clear();
		wrapper.getChildren().add(stackPane);
		gc.setGraphic(wrapper);
	}

	

}
