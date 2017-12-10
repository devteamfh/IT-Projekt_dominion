package Dominion.Client.ClientClasses;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.Client.ClientClasses.gameplay.cards.ActionCard;
import Dominion.Client.ClientClasses.gameplay.cards.GameCard;
import Dominion.Client.ClientClasses.gameplay.cards.MoneyCard;
import Dominion.Client.ClientClasses.gameplay.cards.ProvinceCard;
import Dominion.Client.abstractClasses.View;
import Dominion.appClasses.GameParty;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.misc.GC;


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
public class Client_View_playingStage extends View<Client_Model> implements Observer {
	ServiceLocatorClient sl;
	Croupier croupier;

	Button btn_close;
	
	ProvinceCard estate, duchy, province;
	MoneyCard copper, silver, gold, curse;
	
	ArrayList<GameCard> al_communityCards_left;
	
	Label buyPower;
	Label actions;
	Label buys;
	
	Label labeltest;
	
	Button provisorischCard1;
	Button provisorischCard2;
	Button provisorischCard3;
	
	Button provisorisch4;
	GameCard ac1;
	
	Button action2;
	
	Label stack;
	Label yourHand;
	
	VBox vb_player;
	
	TextField tf_messagePlayingStage;
    TextArea chatWindowPlayingStage;
    TextArea windowGameHistory;
    customButton btn_sendChatMsgPlayingStage;
    
    HBox tryUpdateshit;
    
	HBox hb_wrp_communityActionCardsBackRow;
	HBox hb_wrp_communityActionCardsFrontRow;
    

	public Client_View_playingStage(Stage stage, Client_Model model, GameParty party) {
		super(stage, model,party);
        stage.setTitle("Dominion - Spielplattform");
    }
	
	/**
     * @author Joel Henz, kab
     */
	protected Scene create_GUI() {
		croupier = Croupier.getCroupier();
		
		
		btn_close = new Button();
		btn_close.setPrefSize(35, 33);
	   	btn_close.getStyleClass().addAll("btn","btn_close_normal");
	   	btn_close.setAlignment(Pos.TOP_RIGHT);
	   	
		//X Button top right, wrapper hb_custom menue
	    HBox hb_custom_menue = new HBox();
	      
			HBox spacer =  new HBox();
			HBox.setHgrow(spacer, Priority.ALWAYS);
				   	
			hb_custom_menue.getChildren().addAll(spacer, btn_close);
		   	hb_custom_menue.setPadding(new Insets(5,5,5,0));	
	   	//----------------------------------------------------------//

		
		
		
		

		//____Community Cards links -> Lï¿½ndereien und Geld_____________________________________________//
		estate   = new ProvinceCard(new Label("estate"),croupier.getCostsEstate());
		duchy    = new ProvinceCard(new Label("duchy"),croupier.getCostsDuchy());
		province = new ProvinceCard(new Label("province"),croupier.getCostsPovince());
		
		copper = new MoneyCard(new Label("copper"),croupier.getBuyPowerCopper(),croupier.getCostsCopper());
		silver = new MoneyCard(new Label("silver"),croupier.getBuyPowerSilver(),croupier.getCostsSilver());
		gold   = new MoneyCard(new Label("gold"),croupier.getBuyPowerGold(),croupier.getCostsGold());
		
		curse = new MoneyCard(new Label("curse"),croupier.getBuyPowerCurse(),croupier.getCostsCurse());
		
		//
		al_communityCards_left = new ArrayList<GameCard>();	
		al_communityCards_left.add(estate);
		al_communityCards_left.add(duchy);
		al_communityCards_left.add(province);
		al_communityCards_left.add(copper);
		al_communityCards_left.add(silver);
		al_communityCards_left.add(gold);
		al_communityCards_left.add(curse);
		
		//add Observer, setSize
		GameCard gc;
		for (int i = 0; i < al_communityCards_left.size(); i++) {
			gc = al_communityCards_left.get(i);
			croupier.addObserver(gc);
			gc.setMinSize(110, 120);
		}
		//--------------------------------------------------------------------------------------------//
		
		
		//Allen CommunityActionCards einen observer hinzufügen
		for (GameCard ac : croupier.getAl_communityActionCards()){
		croupier.addObserver(ac);	
		}
				
		//community Action Cards in der Mitte mit  10 karten initialisieren
		croupier.prepareAL_stackSizeCommunityActionCards();
		
		

		tryUpdateshit = new HBox();
		
		//buyPower = new Label("11");
		
		tryUpdateshit.getChildren().add(croupier.getLbltest());		
	
		

		HBox hb_wrapper_buyPowerActionBuys = new HBox();
		//croupier.setSimpleIntegerPropertyBuyPower();
		//hb_wrapper_buyPowerActionBuys.getChildren().add(buyPower);
		
		
		
		
		
		
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
		this.provisorischCard1.setDisable(false);
		this.provisorischCard2 = new Button ("Karte prov");
		this.provisorischCard2.setDisable(false);
		this.provisorischCard3 = new Button ("Karte prov");
		this.provisorischCard3.setDisable(false);
		
		this.provisorisch4 = new Button("kjkjk");

		
		
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
		
		//if the host ends his game before the GameParty is full, the game will end for the host and all other clients and will disappear on the ListView "SpielÃ¼bersicht" in the lobby. 
		//There will be no score for this GameParty. Once the GameParty is full, the GameParty will disappear on the ListView. While playing the game, each client is able to leave the GameParty. His score
		//will be evaluated as a loss.
		sl.getButtonEndGameHost().setDisable(true);
		//by default this button is also deactivated
		sl.getButtonLeaveGamePlayer().setDisable(true);

		root.setTop(vb_player);
		vb_player.setAlignment(Pos.TOP_RIGHT);
		vb_player.setPrefHeight(60.0);
		
		//setting the treasure and point cards to buy
		GridPane gp_left = new GridPane();
		

		//Button curse = new Button ("Curse");
		
		gold.addClickListener();
		//Button silver = new Button ("Silver");
		//Button copper = new Button ("Copper");
		
		GridPane.setConstraints(province, 0, 0);
		GridPane.setConstraints(duchy, 0, 1);
		GridPane.setConstraints(estate, 0, 2);
		//GridPane.setConstraints(curse, 0, 3);
		
		GridPane.setConstraints(gold, 1, 0);
		GridPane.setConstraints(silver, 1, 1);
		GridPane.setConstraints(copper, 1, 2);
		
		gp_left.getChildren().addAll(province,duchy,estate,gold,silver,copper);
		
		VBox vb_center = new VBox();
		//GridPane gp_actionCards = new GridPane();
		
		

		//Croupier.getCroupier().addObserver(gc1);
		
		
		
		 hb_wrp_communityActionCardsBackRow = new HBox();
		 hb_wrp_communityActionCardsFrontRow = new HBox();
		
		
		hb_wrp_communityActionCardsBackRow.getChildren().addAll(croupier.getAl_communityActionCards().get(0),croupier.getAl_communityActionCards().get(1),croupier.getAl_communityActionCards().get(2),croupier.getAl_communityActionCards().get(3),croupier.getAl_communityActionCards().get(4));
		hb_wrp_communityActionCardsFrontRow.getChildren().addAll(croupier.getAl_communityActionCards().get(5),croupier.getAl_communityActionCards().get(6),croupier.getAl_communityActionCards().get(7),croupier.getAl_communityActionCards().get(8),croupier.getAl_communityActionCards().get(9));
		
	
		for (int i = 0;i<10;i++){
			croupier.getAl_communityActionCards().get(i).setPrefSize(300,200);
			}
		
		
		//gc1.setMinSize(100,200);
		

		
		vb_center.getChildren().addAll(hb_wrp_communityActionCardsBackRow,hb_wrp_communityActionCardsFrontRow);
		
		Label playedCards_label = new Label ("gespielte Karten");
		vb_center.getChildren().addAll(playedCards_label);
		
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
		hb_hand.getChildren().addAll(provisorischCard1, provisorischCard2,provisorischCard3);
		
		VBox vb_stack_endGameHost = new VBox();
		HBox hb_endGameHost_endGamePlayer = new HBox();

		hb_endGameHost_endGamePlayer.getChildren().addAll(sl.getButtonEndGameHost(),sl.getButtonLeaveGamePlayer());
		vb_stack_endGameHost.getChildren().addAll(tryUpdateshit,stack,hb_endGameHost_endGamePlayer);


		
		HBox.setMargin(vb_stack_endGameHost, new Insets(0, 100, 0, 0));
		HBox.setMargin(yourHand, new Insets(0, 20, 0, 0));
		HBox.setMargin(hb_hand, new Insets(0, 20, 0, 0));
		
		hb_stack_hand_endAction_endBuy.getChildren().addAll(vb_stack_endGameHost,yourHand,hb_hand,sl.getButtonPlayActions(),sl.getButtonPlayBuy(),sl.getButtonEndActions(),sl.getButtonEndBuys());
		hb_stack_hand_endAction_endBuy.setAlignment(Pos.TOP_CENTER);
		
		vb_bottom.getChildren().addAll(sl.getLabelNumberOfActionsAndBuys(),hb_stack_hand_endAction_endBuy);
		
		vb_bottom.setAlignment(Pos.TOP_CENTER);
		
		root.setTop(hb_custom_menue);
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
			sl.getButtonLeaveGamePlayer().setDisable(false);
		}
		
		this.scene = new Scene (root,1000,800);
		scene.getStylesheets().add(getClass().getResource("/stylesheets/style_playStage.css").toExternalForm());
	    //stage.initStyle(StageStyle.TRANSPARENT);   
		
		

		   scene.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
		        @Override
		        public void handle(MouseEvent mouseEvent) {
		            System.out.println("mouse click detected! " + mouseEvent.getSource());
		            croupier.setSimpleIntegerPropertyBuyPower();
		        	tryUpdateshit.getChildren().clear();
		        	tryUpdateshit.getChildren().addAll(croupier.getLbltest());
		        }
		    });
		
		
        return scene;
	}

	

	//im moment passiert hier nichts, später ev. löschen
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("obseverd");
		System.out.println("observed: "+Thread.currentThread());
		
		  /*  	
		  	Task task = new Task<Void>() {
		    	    @Override public Void call() {
		    	    	
		            	System.out.println(Thread.currentThread());
		               	tryUpdateshit.getChildren().clear();
				    	tryUpdateshit.getChildren().addAll(croupier.getLbltest());

		    	        return null;
		    	    }
		    	};*/
	
		//hb_wrp_communityActionCardsBackRow.getChildren().addAll(provisorisch4,ac1);
		
		//hb_wrp_communityActionCardsFrontRow.getChildren().addAll(croupier.getAl_communityActionCards().get(5),croupier.getAl_communityActionCards().get(6),croupier.getAl_communityActionCards().get(7),croupier.getAl_communityActionCards().get(8),croupier.getAl_communityActionCards().get(9));
		//stage.show();
	}
	
	
 }