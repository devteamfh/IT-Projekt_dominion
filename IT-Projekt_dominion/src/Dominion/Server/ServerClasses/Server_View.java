package Dominion.Server.ServerClasses;

import Dominion.Server.abstractClasses.View;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class Server_View extends View<Server_Model> {
	Button connect;
	TextField port;
	TextField ip;
    Scene scene;

	public Server_View(Stage stage, Server_Model model) {
        super(stage, model);
        stage.setTitle("Server");
    }

	@Override
	protected Scene create_GUI() { 
	    
	    Label IPAdress = new Label ("IP Adresse:  ");
	    Label portNr = new Label ("Port:           ");
	    ip = new TextField();
	    port = new TextField();
	    
	    connect = new Button ("verbinden");
	    
	    BorderPane root = new BorderPane();
	    HBox hb1 = new HBox();
	    HBox hb2 = new HBox();
	    
	    hb1.getChildren().addAll(IPAdress, ip);
	    hb2.getChildren().addAll(portNr, port);
	    
	    HBox.setMargin(IPAdress, new Insets(0, 0, 20, 0));
	    HBox.setMargin(ip, new Insets(0, 0, 20, 0));
	    
	    
	    VBox vb = new VBox();
	    
	    vb.getChildren().addAll(hb1,hb2,connect);
	    VBox.setMargin(connect, new Insets(20, 0, 0, 0));

		root.setCenter(vb);

		this.scene = new Scene (root);

        return scene;
	}

}