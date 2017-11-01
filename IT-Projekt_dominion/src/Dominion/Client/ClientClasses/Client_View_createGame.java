package Dominion.Client.ClientClasses;

import java.util.ArrayList;

import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC Pattern), Joel Henz (GUI)
 */
public class Client_View_createGame extends View<Client_Model> {
	ServiceLocatorClient sl;
    Label chooseTypeOfGame;
    Label numberOfRounds; //->Wert kann man nur eingeben, wenn Modus gewählt worden ist
    Label numberOfPlayers;
	
	RadioButton endOfGame1;
	RadioButton endOfGame2;
	
	RadioButton twoPlayer;
	RadioButton threePlayer;
	RadioButton fourPlayer;
	
	Button finish;
	Button cancel;

	public Client_View_createGame(Stage stage, Client_Model model) {
		super(stage, model);
        stage.setTitle("Dominion - neues Spiel erstellen");
    }
	
	/**
     * @author Joel Henz
     */
	protected Scene create_GUI() {

	    sl = ServiceLocatorClient.getServiceLocator();  
	    sl.setTextFieldForRounds();
	    sl.setToggleForEndOfGame();
	    sl.setToggleForNumberOfPlayers();
	    
		BorderPane root = new BorderPane();
		VBox vb1 = new VBox ();
		
		chooseTypeOfGame = new Label ("Spielmodus wählen:");
		endOfGame1 = new RadioButton ("Provinzkarten aufbrauchen");
		endOfGame2 = new RadioButton ("max. Anzahl Runden (höchstens 50)");
		

		endOfGame1.setToggleGroup(sl.getToggleForEndOfGame());
		endOfGame2.setToggleGroup(sl.getToggleForEndOfGame());
		
		numberOfRounds= new Label ("Anzahl max. Runden:");
		sl.getTextFieldForRounds().setDisable(true);
		
		numberOfPlayers= new Label ("Anzahl Spieler wählen:");
		twoPlayer = new RadioButton ("2 Spieler");
		twoPlayer.setToggleGroup(sl.getToggleForNumberOfPlayers());
		threePlayer = new RadioButton ("3 Spieler");
		threePlayer.setToggleGroup(sl.getToggleForNumberOfPlayers());
		fourPlayer = new RadioButton ("4 Spieler");
		fourPlayer.setToggleGroup(sl.getToggleForNumberOfPlayers());
		
		
		vb1.getChildren().addAll(chooseTypeOfGame,endOfGame1,endOfGame2,numberOfRounds,sl.getTextFieldForRounds(),numberOfPlayers,twoPlayer,threePlayer,fourPlayer);
		vb1.setMargin(endOfGame2, new Insets(0, 0, 30, 0));
		vb1.setMargin(sl.getTextFieldForRounds(), new Insets(0, 0, 30, 0));
		
		root.setCenter(vb1);
		
		finish = new Button ("Fertig");
		cancel = new Button ("Abbrechen");
		
		HBox hb1 = new HBox ();
		hb1.getChildren().addAll(finish,cancel);
		hb1.setMargin(finish, new Insets(0, 20, 0, 0));
		
		root.setBottom(hb1);
		
		this.scene = new Scene (root, 800,800);

        return scene;
	}

}