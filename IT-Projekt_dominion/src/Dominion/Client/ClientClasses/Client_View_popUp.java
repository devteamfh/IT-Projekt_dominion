package Dominion.Client.ClientClasses;

import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC Pattern)
 */

/**
 * @author: Initial: Joel (GUI without styling)
 * @author: Styling und Anordnung, Anpassung für allgemeine Verwendung kab
 *                                        
 */
public class Client_View_popUp extends View<Client_Model> {
    Label lbl;
	ServiceLocatorClient sl;
	customButton btn_ok;
	Button btn_close;

	public Client_View_popUp(Stage stage, Client_Model model) {
		super(stage, model);
        stage.setTitle("Dominion - Information");
    }
	

	/**
     * @author Joel Henz, kab
     */
	protected Scene create_GUI() {
		sl = ServiceLocatorClient.getServiceLocator();
		
		//Node-Leafs
		btn_close = new Button();
		btn_close.setPrefSize(35, 33);
	   	btn_close.getStyleClass().addAll("btn","btn_close_normal");
	   	btn_close.setAlignment(Pos.TOP_RIGHT);
	   	
		Label lbl_message = sl.getLbl_popUpMessage();
		lbl_message.getStyleClass().add("lbl_popUpMessage");
		lbl_message.setAlignment(Pos.TOP_CENTER);
	   	
		btn_ok = new customButton("ok");
	    btn_ok.getStyleClass().addAll("btn","btn_sendChatMsg");
	    btn_ok.setBtnTextEmpty(btn_ok);
	    btn_ok.setPrefSize(202, 40);
	    
	    
	    //Nodes	
		BorderPane root = new BorderPane();
		
			VBox vb_wrapper_content = new VBox();
			vb_wrapper_content.setPadding(new Insets(0,0,0,60));
			
			
				//X Button top right, wrapper hb_custom menue
			    HBox hb_custom_menue = new HBox();
			      
					HBox spacer =  new HBox();
					HBox.setHgrow(spacer, Priority.ALWAYS);
						   	
					hb_custom_menue.getChildren().addAll(spacer, btn_close);
				   	hb_custom_menue.setPadding(new Insets(5,5,5,0));	
			   	//----------------------------------------------------------//
			
			   	
			   	//Content
			   	VBox vb_wrapper_lbl_btn = new VBox();
			   		
			   		VBox vb_wrapper_lbl = new VBox();
			   			 vb_wrapper_lbl.getChildren().add(lbl_message);
			   			 vb_wrapper_lbl.setPadding(new Insets(5,0,0,0));
			   		
			   		VBox vb_wrapper_btn = new VBox();
			   			 vb_wrapper_btn.getChildren().add(btn_ok);
			   			 vb_wrapper_btn.setPadding(new Insets(20,0,0,300));
			   						
			   	vb_wrapper_lbl_btn.getChildren().addAll(vb_wrapper_lbl, vb_wrapper_btn);
			   	//---------------------------------------------------------------------//
			   	
			   	
		    vb_wrapper_content.getChildren().addAll(vb_wrapper_lbl_btn);
			 
			
		root.setTop(hb_custom_menue);
		root.setCenter(vb_wrapper_content);
		root.getStyleClass().add("bg_popUp");

		
		this.scene = new Scene (root, 600,180);
		scene.getStylesheets().add(getClass().getResource("style_clientStart.css").toExternalForm());
	    stage.initStyle(StageStyle.TRANSPARENT);   
	    
        return scene;
	}

}
