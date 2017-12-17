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
public class customButton extends Button implements Observer {
	Croupier croupier = Croupier.getCroupier();
	private customButton btn;
	private Label lbl;
	private String lblText;
	
	//constructor
	public customButton() {
		super();
	}
	
	public customButton(String s){
		super(s);
	}
	
	//Text Property leeren und daf�r direkt das label lbl verwenden
	public void setBtnTextEmpty(customButton btn){
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
		
	
	
	//TextEffekt dem Button hinzuf�gen
	/*id setLblTextEffect(customButton btn){
		this.btn = btn;
		this.lbl = (Label) this.btn.getGraphic();
	    lbl.setStyle("-fx-effect: dropshadow( one-pass-box , white , 1 , 0.0 , 1 , 0 )");		
	}
*/
	
	
	
	/*public void addStyleHandlerClickListener(){
		
		addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent e) {
		/*		if (getStyleClass().contains("clicked"))	{

				getStyleClass().add("button");
				
				}else {
				getStyleClass().add("clicked");
						}
				}
		});
		
	}*/

	/*
	public void setStyleClicked(){
		getStyleClass().add("clicked");
		
	}
	
	public void setStyleUnclicked(){
		getStyleClass().clear();
		getStyleClass().add("button");
	}
	
	
	public void setStyleBtnAdtnl(){
		getStyleClass().add("btn_adtnl");

	}
	*/
	
	
	/*	public void addStyleHandlerDropShadow(){
		DropShadow shadow = new DropShadow();
		
		//Adding the shadow when the mouse cursor is on
		addEventHandler(MouseEvent.MOUSE_ENTERED, 
		    new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent e) {
		        
		        }
		});
		
		//Removing the shadow when the mouse cursor is off
		addEventHandler(MouseEvent.MOUSE_EXITED, 
		    new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent e) {
		        	
		        }
		});
		}
		*/
	
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
		
		}
	
		
		
		
		
	}
		
		
