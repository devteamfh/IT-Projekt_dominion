package Dominion.Client.ClientClasses;

import java.util.ArrayList;

import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.View;
import Dominion.appClasses.GameParty;
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
import javafx.scene.layout.GridPane;
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
public class Client_View_playingStage extends View<Client_Model> {
	ServiceLocatorClient sl;
	Button endGameHost;
	Label [] labelArray;

	public Client_View_playingStage(Stage stage, Client_Model model, boolean isHost, GameParty party) {
		super(stage, model,isHost,party);
        stage.setTitle("Dominion - Spielplattform");
    }
	
	/**
     * @author Joel Henz
     */
	protected Scene create_GUI() {

	    sl = ServiceLocatorClient.getServiceLocator();  
	    
		BorderPane root = new BorderPane();
		
		VBox vb_player = new VBox();
		
		labelArray = new Label[super.party.getMaxNumberOfPlayers()];
		
		for(int i=0;i<labelArray.length;i++){
			labelArray[i] = new Label();
			vb_player.getChildren().add(labelArray[i]);
		}
		
		//if the host ends his game before the GameParty is full, the game will end for the host and all other clients and will disappear on the ListView "Spielübersicht" in the lobby. 
		//There will be no score for this GameParty. Once the GameParty is full, the GameParty will disappear on the ListView. While playing the game, each client is able to leave the GameParty. His score
		//will be evaluated as a loss.
		this.endGameHost = new Button ("Spiel abbrechen (Host)");

		root.setTop(vb_player);
		vb_player.setAlignment(Pos.TOP_RIGHT);
		vb_player.setPrefHeight(60.0);
		
		//setting the treasure and point cards to buy
		GridPane gp_left = new GridPane();
		
		Button province = new Button ("Province");
		Button duchy = new Button ("Duchy");
		Button estate = new Button ("Estate");
		Button curse = new Button ("Curse");
		
		Button gold = new Button ("Gold");
		Button silver = new Button ("Silver");
		Button copper = new Button ("Copper");
		
		GridPane.setConstraints(province, 0, 0);
		GridPane.setConstraints(duchy, 0, 1);
		GridPane.setConstraints(estate, 0, 2);
		GridPane.setConstraints(curse, 0, 3);
		
		GridPane.setConstraints(gold, 1, 0);
		GridPane.setConstraints(silver, 1, 1);
		GridPane.setConstraints(copper, 1, 2);
		
		gp_left.getChildren().addAll(province,duchy,estate,curse,gold,silver,copper);
		
		VBox vb_center = new VBox();
		GridPane gp_actionCards = new GridPane();
		
		Button action1 = new Button ("action1");
		Button action2 = new Button ("action1");
		Button action3 = new Button ("action1");
		Button action4 = new Button ("action1");
		Button action5 = new Button ("action1");
		Button action6 = new Button ("action1");
		Button action7 = new Button ("action1");
		Button action8 = new Button ("action1");
		Button action9 = new Button ("action1");
		Button action10 = new Button ("action1");
		
		GridPane.setConstraints(action1, 0, 0);
		GridPane.setConstraints(action2, 1, 0);
		GridPane.setConstraints(action3, 2, 0);
		GridPane.setConstraints(action4, 3, 0);
		GridPane.setConstraints(action5, 4, 0);
		GridPane.setConstraints(action6, 0, 1);
		GridPane.setConstraints(action7, 1, 1);
		GridPane.setConstraints(action8, 2, 1);
		GridPane.setConstraints(action9, 3, 1);
		GridPane.setConstraints(action10, 4, 1);
		
		gp_actionCards.getChildren().addAll(action1,action2,action3,action4,action5,action6,action7,action8,action9,action10);
		
		vb_center.getChildren().add(gp_actionCards);
		
		Label playedCards_label = new Label ("gespielte Karten");
		vb_center.getChildren().add(playedCards_label);
		
		HBox playedCards_hbox = new HBox();
		Button playedCard1 = new Button ("playedCard");
		Button playedCard2 = new Button ("playedCard");
		Button playedCard3 = new Button ("playedCard");
		
		playedCards_hbox.getChildren().addAll(playedCard1,playedCard2,playedCard3);
		vb_center.getChildren().add(playedCards_hbox);
		
		root.setLeft(gp_left);
		BorderPane.setMargin(gp_left, new Insets(0, 20, 0, 0));
		root.setCenter(vb_center);
		
		if(super.isHost){
			root.setBottom(endGameHost);
		}
		
		this.scene = new Scene (root, 800,800);

        return scene;
	}

}