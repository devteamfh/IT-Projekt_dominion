package Dominion.Client.ClientClasses;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;

/**
 * @author Beda Kaufmann
 */
public class customButton extends Button {
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
		
		

}