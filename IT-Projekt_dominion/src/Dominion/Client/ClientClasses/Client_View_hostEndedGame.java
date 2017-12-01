package Dominion.Client.ClientClasses;

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
 * @author: Styling und Anordnung: kab
 *                                        
 */
public class Client_View_hostEndedGame extends View<Client_Model> {
	
	Button ok;


	public Client_View_hostEndedGame(Stage stage, Client_Model model) {
		super(stage, model);
        stage.setTitle("Dominion - Host hat Spiel beendet");
    }
	
	/**
     * @author Joel Henz
     */
	protected Scene create_GUI() {
		BorderPane root = new BorderPane();
		
		Label message = new Label("Der Host hat das Spiel beendet. Gehen Sie mit 'OK' zurück in die Lobby");
		ok = new Button("OK");
		
		message.setAlignment(Pos.CENTER);
		ok.setAlignment(Pos.CENTER);
		
		root.setTop(message);
		root.setCenter(ok);
		
		this.scene = new Scene (root, 400,100);

        return scene;
	}

}