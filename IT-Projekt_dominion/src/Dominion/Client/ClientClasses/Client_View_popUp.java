package Dominion.Client.ClientClasses;

import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.View;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
	Button ok;

	public Client_View_popUp(Stage stage, Client_Model model) {
		super(stage, model);
        stage.setTitle("Dominion - Information");
    }
	
	
	/**
     * @author Joel Henz, kab
     */
	protected Scene create_GUI() {
		sl = ServiceLocatorClient.getServiceLocator();
		
		BorderPane root = new BorderPane();
		

		Label message = sl.getLbl_popUpMessage();
		message.getStyleClass().add("lbl_errMsg");
		ok = new Button("OK");
		
		message.setAlignment(Pos.CENTER);
		ok.setAlignment(Pos.CENTER);
		
		root.setTop(message);
		root.setCenter(ok);
		
		this.scene = new Scene (root, 400,100);
		scene.getStylesheets().add(getClass().getResource("style_clientStart.css").toExternalForm());

        return scene;
	}

}