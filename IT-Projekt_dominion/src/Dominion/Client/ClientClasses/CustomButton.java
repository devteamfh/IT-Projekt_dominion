package Dominion.Client.ClientClasses;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import Dominion.Client.ClientClasses.gameplay.Croupier;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;

/**
 * @author Beda Kaufmann
 */
public class CustomButton extends Button implements Observer {
	Croupier croupier = Croupier.getCroupier();
	private CustomButton btn;
	private Label lbl;
	private String lblText;
	
	//constructor
	public CustomButton() {
		super();
	}
	
	public CustomButton(String s){
		super(s);
	}
	
	//Text Property leeren und daf�r direkt das label lbl verwenden
	public void setBtnTextEmpty(CustomButton btn){
	this.btn=btn;
	this.lblText = this.btn.getText();
	this.lbl = new Label(this.lblText);
	this.btn.setText("");    
    this.lbl.getStyleClass().add("lbl");
    this.btn.setGraphic(this.lbl);
	}

	public void setBtnText(String newText) {
	this.lblText = newText;
	this.lbl = new Label(this.lblText);
	this.lbl.getStyleClass().add("lbl");
	this.btn.setGraphic(this.lbl);
	}
		
	
	
		//Label 
	@Override
		public String toString(){
			String labelText = this.lblText;
			return labelText;
			}

	public Label getLbl() {
		return lbl;
	}

	public void setLbl(Label lbl) {
		this.lbl = lbl;
	}

	@Override
	public void update(Observable o, Object arg) {
		
		//System.out.println("customButton has been observerd");
		
		if (croupier.isActionMode() == true) {
			//System.out.println("isactionmode observed true");

		
			
			Platform.runLater(new Runnable() {
		           @Override 
		           public void run() {
		   			lbl.setText("Aktionen beenden");
					btn.setGraphic(lbl);
		           }
			});	
		}
			
		if (croupier.isBuyMode() == true)		
			Platform.runLater(new Runnable() {
		           @Override 
		           public void run() {
		    			lbl.setText("Kaufen beenden");
		    			btn.setGraphic(lbl);	
		           }
			});
		
	
		if (croupier.isBuyMode() == false && croupier.isActionMode() == false)		
			Platform.runLater(new Runnable() {
		           @Override 
		           public void run() {
		    			lbl.setText("Gegner spielt");
		    			btn.setGraphic(lbl);	
		           }
			});
		
		
		if (croupier.isDiscardMode() == true) {
			//System.out.println("isactionmode observed true");

		
			
			Platform.runLater(new Runnable() {
		           @Override 
		           public void run() {
		   			lbl.setText("Tauschen beenden");
					btn.setGraphic(lbl);
		           }
			});	
		}
		
		if (croupier.isTrashModeMine() == true) {
			//System.out.println("isactionmode observed true");

		
			
			Platform.runLater(new Runnable() {
		           @Override 
		           public void run() {
		   			lbl.setText("Dominion");
					btn.setGraphic(lbl);
		           }
			});	
		}
		
		if (croupier.isModeForMine() == true) {
			//System.out.println("isactionmode observed true");

		
			
			Platform.runLater(new Runnable() {
		           @Override 
		           public void run() {
		   			lbl.setText("Dominion");
					btn.setGraphic(lbl);
		           }
			});	
		}
		
		if (croupier.isModeForWorkshop() == true) {
			//System.out.println("isactionmode observed true");

		
			
			Platform.runLater(new Runnable() {
		           @Override 
		           public void run() {
		   			lbl.setText("Dominion");
					btn.setGraphic(lbl);
		           }
			});	
		}
		
		if (croupier.isModeForRebuilding() == true) {
			//System.out.println("isactionmode observed true");

		
			
			Platform.runLater(new Runnable() {
		           @Override 
		           public void run() {
		   			lbl.setText("Dominion");
					btn.setGraphic(lbl);
		           }
			});	
		}
		
		if (croupier.isTrashModeChapel() == true) {
			//System.out.println("isactionmode observed true");

		
			
			Platform.runLater(new Runnable() {
		           @Override 
		           public void run() {
		   			lbl.setText("Wegwerfen beenden");
					btn.setGraphic(lbl);
		           }
			});	
		}
		
		if (croupier.isTrashModeMoneylender() == true) {
			//System.out.println("isactionmode observed true");

		
			
			Platform.runLater(new Runnable() {
		           @Override 
		           public void run() {
		   			lbl.setText("Dominion");
					btn.setGraphic(lbl);
		           }
			});	
		}
		
		if (croupier.isModeForAttack() == true) {
			//System.out.println("isactionmode observed true");

		
			
			Platform.runLater(new Runnable() {
		           @Override 
		           public void run() {
		   			lbl.setText("Dominion");
					btn.setGraphic(lbl);
		           }
			});	
		}
		
		if (croupier.isReactionMode()) {
			//System.out.println("isactionmode observed true");

		
			
			Platform.runLater(new Runnable() {
		           @Override 
		           public void run() {
		   			lbl.setText("Dominion");
					btn.setGraphic(lbl);
		           }
			});	
		}
		
		if (croupier.isDiscardModeMilitia()) {
			//System.out.println("isactionmode observed true");

		
			
			Platform.runLater(new Runnable() {
		           @Override 
		           public void run() {
		   			lbl.setText("Dominion");
					btn.setGraphic(lbl);
		           }
			});	
		}
		
		if (croupier.isModeForCurseCard()) {
			//System.out.println("isactionmode observed true");

		
			
			Platform.runLater(new Runnable() {
		           @Override 
		           public void run() {
		   			lbl.setText("Dominion");
					btn.setGraphic(lbl);
		           }
			});	
		}
		
		}
	
		
		
		
		
	}
		
		
