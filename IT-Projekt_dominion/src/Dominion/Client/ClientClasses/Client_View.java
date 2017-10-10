package Dominion.Client.ClientClasses;

import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
public class Client_View extends View<Client_Model> {
    Button send;
    TextField tf_message;

    TextArea ta;
    Scene scene;
    ServiceLocator sl = ServiceLocator.getServiceLocator();
    String PlayerName;
    Label name;
    Label yourSign;

	public Client_View(Stage stage, Client_Model model) {
		super(stage, model);
        stage.setTitle("Dominion");
    }
	
	/**
     * @author Joel Henz
     */
	protected Scene create_GUI() {

	    ServiceLocator sl = ServiceLocator.getServiceLocator();  
	    
		BorderPane root = new BorderPane();

		/**
	     * creating the GUI elements for the chat
	     */
		send = new Button("senden");
		tf_message = new TextField();
		name = new Label();

		name.setText("Ihr Name: "+model.getName());
		
		Label message = new Label();
		message.setText("Ihre Nachricht");
		sl.setTextArea();
		ta = sl.getTextArea();
		ta.setEditable(false);
		ta.setMaxWidth(1000);
		ta.setMaxHeight(400);
		
		/**
	     * auto resize when window is resized
	     */
		ta.prefWidthProperty().bind(root.widthProperty());
		ta.prefHeightProperty().bind(root.heightProperty());
		
		HBox hb1 = new HBox();
		hb1.getChildren().addAll(message,this.tf_message);
		
		root.setBottom(ta);
		VBox vb1 = new VBox();
		
		vb1.getChildren().addAll(this.name,hb1,send);
		VBox.setMargin(this.name, new Insets(0, 0, 20, 0));
		VBox.setMargin(hb1, new Insets(0, 0, 20, 0));
		VBox.setMargin(send, new Insets(0, 0, 40, 0));
//kjnlkjlkjlkj
		root.setLeft(vb1);
		
		BorderPane.setAlignment(send, Pos.CENTER_LEFT);

		this.scene = new Scene (root, 800,800);

        return scene;
	}

}