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
 * @author Brad Richards (MVC Pattern)
 */

/**
 * @author: Initial: Joel (GUI without styling)
 * @author: Styling und Anordnung: kab
 *                                        
 */
public class Client_View_playingStage extends View<Client_Model> {
	ServiceLocatorClient sl;
	Label stack;
	Label yourHand;
	
	VBox vb_player;
	
	Button provisorischCard1;
	Button provisorischCard2;
	Button provisorischCard3;
	
	TextField tf_messagePlayingStage;
    TextArea chatWindowPlayingStage;
    TextArea windowGameHistory;
    customButton btn_sendChatMsgPlayingStage;

	public Client_View_playingStage(Stage stage, Client_Model model, GameParty party) {
		super(stage, model,party);
        stage.setTitle("Dominion - Spielplattform");
    }
	
	/**
     * @author Joel Henz
     */
	protected Scene create_GUI() {

	    sl = ServiceLocatorClient.getServiceLocator();  
	    
	    chatWindowPlayingStage = sl.getTextAreaChatPlayingStage();
		chatWindowPlayingStage.setEditable(false);
		chatWindowPlayingStage.setPrefSize(820, 150);
		chatWindowPlayingStage.setStyle("-fx-opacity: 0.80;");
		
		windowGameHistory = sl.getTextAreaGameHistory();
		windowGameHistory.setEditable(false);
		windowGameHistory.setPrefSize(820, 150);
		windowGameHistory.setStyle("-fx-opacity: 0.80;");
		
		tf_messagePlayingStage = new TextField();
		tf_messagePlayingStage.setPrefSize(450,40);
		tf_messagePlayingStage.setStyle("-fx-opacity: 0.80;");
		
		btn_sendChatMsgPlayingStage = new customButton("senden");
		btn_sendChatMsgPlayingStage.getStyleClass().addAll("btn","btn_sendChatMsg");
		btn_sendChatMsgPlayingStage.setBtnTextEmpty(btn_sendChatMsgPlayingStage);
		btn_sendChatMsgPlayingStage.setPrefSize(202, 40);
		
		Label label_gameHistory = new Label("Spielverlauf");
		Label label_chat = new Label("Chat");
		this.stack = new Label("hier kommt der Hauptstapel");
		this.yourHand = new Label("deine Hand");
		
		this.provisorischCard1 = new Button ("Karte prov");
		this.provisorischCard1.setDisable(true);
		this.provisorischCard2 = new Button ("Karte prov");
		this.provisorischCard2.setDisable(true);
		this.provisorischCard3 = new Button ("Karte prov");
		this.provisorischCard3.setDisable(true);
		
		sl.setButtonPlayActions("Aktion spielen");
		sl.getButtonPlayActions().setDisable(true);
		
		sl.setButtonPlayBuy("Kauf spielen");
		sl.getButtonPlayBuy().setDisable(true);
		
		sl.setButtonEndActions("Aktionsphase beenden");
		sl.getButtonEndActions().setDisable(true);
		
		sl.setButtonEndBuys("Kaufphase beenden");
		sl.getButtonEndBuys().setDisable(true);
		
		VBox vb_right = new VBox();
		
		vb_right.setPrefHeight(350);
		vb_right.setPrefWidth(400);
		
		HBox hb_chatButtonAndTextField = new HBox();
		hb_chatButtonAndTextField.setPrefSize(200, 100);
		
		hb_chatButtonAndTextField.getChildren().addAll(tf_messagePlayingStage,btn_sendChatMsgPlayingStage);
		
		vb_right.getChildren().addAll(label_gameHistory,windowGameHistory,label_chat,hb_chatButtonAndTextField,chatWindowPlayingStage);
		
		BorderPane root = new BorderPane();
		
		vb_player = new VBox();
		
		//if the host ends his game before the GameParty is full, the game will end for the host and all other clients and will disappear on the ListView "Spielübersicht" in the lobby. 
		//There will be no score for this GameParty. Once the GameParty is full, the GameParty will disappear on the ListView. While playing the game, each client is able to leave the GameParty. His score
		//will be evaluated as a loss.
		sl.getButtonEndGameHost().setDisable(true);
		//by default this button is also deactivated
		sl.getButtonEndGamePlayer().setDisable(true);

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
		
		VBox vb_bottom = new VBox();
		vb_bottom.setPrefSize(1000, 60);
		sl.getLabelNumberOfActionsAndBuys().setText("warten bis Spiel voll ist...");
		HBox hb_stack_hand_endAction_endBuy = new HBox();
		hb_stack_hand_endAction_endBuy.setPrefSize(750, 120);
		
		HBox hb_hand = new HBox();
		hb_hand.getChildren().addAll(provisorischCard1,provisorischCard2,provisorischCard3);
		
		VBox vb_stack_endGameHost = new VBox();
		HBox hb_endGameHost_endGamePlayer = new HBox();
		hb_endGameHost_endGamePlayer.getChildren().addAll(sl.getButtonEndGameHost(),sl.getButtonEndGamePlayer());
		vb_stack_endGameHost.getChildren().addAll(stack,hb_endGameHost_endGamePlayer);
		
		HBox.setMargin(vb_stack_endGameHost, new Insets(0, 100, 0, 0));
		HBox.setMargin(yourHand, new Insets(0, 20, 0, 0));
		HBox.setMargin(hb_hand, new Insets(0, 20, 0, 0));
		
		hb_stack_hand_endAction_endBuy.getChildren().addAll(vb_stack_endGameHost,yourHand,hb_hand,sl.getButtonPlayActions(),sl.getButtonPlayBuy(),sl.getButtonEndActions(),sl.getButtonEndBuys());
		hb_stack_hand_endAction_endBuy.setAlignment(Pos.TOP_CENTER);
		
		vb_bottom.getChildren().addAll(sl.getLabelNumberOfActionsAndBuys(),hb_stack_hand_endAction_endBuy);
		
		vb_bottom.setAlignment(Pos.TOP_CENTER);
		
		root.setLeft(gp_left);
		BorderPane.setMargin(gp_left, new Insets(0, 20, 0, 0));
		root.setCenter(vb_center);
		root.setRight(vb_right);
		root.setBottom(vb_bottom);
		
		//Only the will get the button for ending the game until the game isn't full (example 3 of 4 players -> host can end the game)
		if(sl.getIsHost()){
			sl.getButtonEndGameHost().setDisable(false);
		}else{
			//button activation if the the client isn't the host
			sl.getButtonEndGamePlayer().setDisable(false);
		}
		
		this.scene = new Scene (root, 800,800);

        return scene;
	}

}