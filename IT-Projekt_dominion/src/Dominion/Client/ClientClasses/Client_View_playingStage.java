package Dominion.Client.ClientClasses;

import java.util.ArrayList;
import java.util.Collections;
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
	ArrayList<GameCard> al_allStartingCards;
	
	VBox vb_player;
	

	HBox hb_wrapper_holeCards;
	
	Button provisorischCard1;
	Button provisorischCard2;
	Button provisorischCard3;
	
	Button provisorisch4;

	
	Button action2;
	
	Label stack;
	Label yourHand;
	

	
	TextField tf_messagePlayingStage;
    TextArea chatWindowPlayingStage;
    TextArea windowGameHistory;
    customButton btn_sendChatMsgPlayingStage;
    	
    BorderPane root;

    

    

	public Client_View_playingStage(Stage stage, Client_Model model, GameParty party) {
		super(stage, model,party);
        stage.setTitle("Dominion - Spielplattform");
    }
	
	/**
     * @author Joel Henz, kab
     */
	protected Scene create_GUI() {
		croupier = Croupier.getCroupier();
		
	    sl = ServiceLocatorClient.getServiceLocator();  
		
		root = new BorderPane();

		//_______________X Button top right__________________________//
		btn_close = new Button();
		btn_close.setPrefSize(35, 33);
	   	btn_close.getStyleClass().addAll("btn","btn_close_normal");
	   	btn_close.setAlignment(Pos.TOP_RIGHT);
	   	
		//if the host ends his game before the GameParty is full, the game will end for the host and all other clients and will disappear on the ListView "Spielübersicht" in the lobby. 
		//There will be no score for this GameParty. Once the GameParty is full, the GameParty will disappear on the ListView. While playing the game, each client is able to leave the GameParty. His score
		//will be evaluated as a loss.
		sl.getButtonEndGameHost().setDisable(true);
		//by default this button is also deactivated
		sl.getButtonLeaveGamePlayer().setDisable(true);
	   	
		//X Button top right, wrapper hb_custom menue
	    HBox hb_custom_menue = new HBox();
	      
			HBox spacer =  new HBox();
			HBox.setHgrow(spacer, Priority.ALWAYS);
				   	
			HBox hb_endGameHost_endGamePlayer = new HBox();
			
			hb_endGameHost_endGamePlayer.getChildren().addAll(sl.getButtonEndGameHost(),sl.getButtonLeaveGamePlayer());
			
			hb_custom_menue.getChildren().addAll(spacer,hb_endGameHost_endGamePlayer, btn_close);
		   	hb_custom_menue.setPadding(new Insets(5,5,5,0));	
		   	
		  //Only the will get the button for ending the game until the game isn't full (example 3 of 4 players -> host can end the game)
			if(sl.getIsHost()){
				sl.getButtonEndGameHost().setDisable(false);
			}else{
				//button activation if the the client isn't the host
				sl.getButtonLeaveGamePlayer().setDisable(false);
			}
			
	   	//----------------------------------------------------------//

		
		
	
		//____Community Cards links -> L�ndereien und Geld, Curse____________________________________________//
		
		//Community Cards auf der Linken seite Initialisieren	
		estate   = new ProvinceCard(new Label("estate"),croupier.getCostsEstate());
		duchy    = new ProvinceCard(new Label("duchy"),croupier.getCostsDuchy());
		province = new ProvinceCard(new Label("province"),croupier.getCostsPovince());
		
		copper = new MoneyCard(new Label("copper"),croupier.getBuyPowerCopper(),croupier.getCostsCopper());
		silver = new MoneyCard(new Label("silver"),croupier.getBuyPowerSilver(),croupier.getCostsSilver());
		gold   = new MoneyCard(new Label("gold"),croupier.getBuyPowerGold(),croupier.getCostsGold());
		
		curse = new MoneyCard(new Label("curse"),croupier.getBuyPowerCurse(),croupier.getCostsCurse());
		
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
			gc.setMinSize(120, 110);
		}
			
		
		//Branches
		HBox hb_wrapper_communityCards_Left = new HBox();
		hb_wrapper_communityCards_Left.setPadding(new Insets(0,20,20,20));
		
		VBox vb_wrapper_communityCards_Left_col1 = new VBox();
		VBox vb_wrapper_communityCards_Left_col2 = new VBox();
		 
		HBox hb_wrapper_province = new HBox();   hb_wrapper_province.getChildren().add(province);  hb_wrapper_province.setPadding(new Insets(0,5,5,0));
		HBox hb_wrapper_duchy    = new HBox();   hb_wrapper_duchy.getChildren().add(duchy); 	   hb_wrapper_duchy.setPadding(new Insets(0,5,5,0));
		HBox hb_wrapper_estate   = new HBox();   hb_wrapper_estate.getChildren().add(estate);  	   hb_wrapper_estate.setPadding(new Insets(0,5,5,0));
		HBox hb_wrapper_curse    = new HBox();   hb_wrapper_curse.getChildren().add(curse);	  	   hb_wrapper_curse.setPadding(new Insets(0,5,5,0)); 	
		HBox hb_wrapper_gold     = new HBox();   hb_wrapper_gold.getChildren().add(gold);	  	   hb_wrapper_gold.setPadding(new Insets(0,5,5,0));
		HBox hb_wrapper_silver   = new HBox();   hb_wrapper_silver.getChildren().add(silver);      hb_wrapper_silver.setPadding(new Insets(0,5,5,0));
		HBox hb_wrapper_copper   = new HBox();   hb_wrapper_copper.getChildren().add(copper);      hb_wrapper_copper.setPadding(new Insets(0,5,5,0));		
		
		vb_wrapper_communityCards_Left_col1.getChildren().addAll(hb_wrapper_province,hb_wrapper_duchy,hb_wrapper_estate,hb_wrapper_curse);
		vb_wrapper_communityCards_Left_col2.getChildren().addAll(hb_wrapper_gold, hb_wrapper_silver, hb_wrapper_copper);

		
		hb_wrapper_communityCards_Left.getChildren().addAll(vb_wrapper_communityCards_Left_col1, vb_wrapper_communityCards_Left_col2);
		
		//--------------------------------------------------------------------------------------------------------------------------------------//
		
		
		
		
		//____________CenterPane   communtiycard in der mitte und statusinformationen zum spiel (buypower, action etc)_________________________________________________________________________//
		
		//Allen CommunityActionCards einen observer hinzuf�gen
		for (GameCard ac : croupier.getAl_communityActionCards()){
		croupier.addObserver(ac);	
		}
				
		//community Action Cards in der Mitte mit 10 karten initialisieren
		croupier.prepareAL_stackSizeCommunityActionCards();
		
		for (int i = 0;i<10;i++){
			croupier.getAl_communityActionCards().get(i).setMinSize(200, 170);
			}
		
		//Leafs
		Label lbl_descrBuyPower = new Label("M�nzen");
		Label lbl_descrBuys     = new Label("K�ufe");
		Label lbl_descrActions  = new Label("Aktionen");
		
		
		
		//Branches
		VBox vb_wrapper_center = new VBox();
		vb_wrapper_center.setMinSize(1200, 600);
		vb_wrapper_center.setPadding(new Insets(0,50,0,30));
		
		HBox hb_wrp_communityActionCardsBackRow   = new HBox();
		hb_wrp_communityActionCardsBackRow.setPadding(new Insets(30,0,0,0));
		
		HBox hb_wrp_communityActionCardsFrontRow  = new HBox();
		
		HBox hb_wrapper_comntyACSlot0 = new HBox();   hb_wrapper_comntyACSlot0.getChildren().add(croupier.getAl_communityActionCards().get(0));  hb_wrapper_comntyACSlot0.setPadding(new Insets(0,5,5,0));
		HBox hb_wrapper_comntyACSlot1 = new HBox();   hb_wrapper_comntyACSlot1.getChildren().add(croupier.getAl_communityActionCards().get(1));  hb_wrapper_comntyACSlot1.setPadding(new Insets(0,5,5,0));
		HBox hb_wrapper_comntyACSlot2 = new HBox();   hb_wrapper_comntyACSlot2.getChildren().add(croupier.getAl_communityActionCards().get(2));  hb_wrapper_comntyACSlot2.setPadding(new Insets(0,5,5,0));
		HBox hb_wrapper_comntyACSlot3 = new HBox();   hb_wrapper_comntyACSlot3.getChildren().add(croupier.getAl_communityActionCards().get(3));  hb_wrapper_comntyACSlot3.setPadding(new Insets(0,5,5,0));
		HBox hb_wrapper_comntyACSlot4 = new HBox();   hb_wrapper_comntyACSlot4.getChildren().add(croupier.getAl_communityActionCards().get(4));  hb_wrapper_comntyACSlot4.setPadding(new Insets(0,5,5,0));
		HBox hb_wrapper_comntyACSlot5 = new HBox();   hb_wrapper_comntyACSlot5.getChildren().add(croupier.getAl_communityActionCards().get(5));  hb_wrapper_comntyACSlot5.setPadding(new Insets(0,5,5,0));
		HBox hb_wrapper_comntyACSlot6 = new HBox();   hb_wrapper_comntyACSlot6.getChildren().add(croupier.getAl_communityActionCards().get(6));  hb_wrapper_comntyACSlot6.setPadding(new Insets(0,5,5,0));
		HBox hb_wrapper_comntyACSlot7 = new HBox();   hb_wrapper_comntyACSlot7.getChildren().add(croupier.getAl_communityActionCards().get(7));  hb_wrapper_comntyACSlot7.setPadding(new Insets(0,5,5,0));
		HBox hb_wrapper_comntyACSlot8 = new HBox();   hb_wrapper_comntyACSlot8.getChildren().add(croupier.getAl_communityActionCards().get(8));  hb_wrapper_comntyACSlot8.setPadding(new Insets(0,5,5,0));
		HBox hb_wrapper_comntyACSlot9 = new HBox();   hb_wrapper_comntyACSlot9.getChildren().add(croupier.getAl_communityActionCards().get(9));  hb_wrapper_comntyACSlot9.setPadding(new Insets(0,5,5,0));
		
		hb_wrp_communityActionCardsBackRow.getChildren().addAll(hb_wrapper_comntyACSlot0,hb_wrapper_comntyACSlot1,hb_wrapper_comntyACSlot2,hb_wrapper_comntyACSlot3,hb_wrapper_comntyACSlot4);
		hb_wrp_communityActionCardsFrontRow.getChildren().addAll(hb_wrapper_comntyACSlot5,hb_wrapper_comntyACSlot6,hb_wrapper_comntyACSlot7,hb_wrapper_comntyACSlot8,hb_wrapper_comntyACSlot9);

		
		HBox hb_wrapper_playedCards_gameInformation = new HBox();
		
		HBox hb_playedCards = new HBox();
		
		VBox vb_wrapper_gameInformation = new VBox();
		vb_wrapper_gameInformation.setPadding(new Insets(50,0,0,0));
		vb_wrapper_gameInformation.setAlignment(Pos.CENTER_RIGHT);
		
	
		//buypower label und wert 
		HBox hb_wrapper_lblBuyPower = new HBox();
		hb_wrapper_lblBuyPower.getChildren().addAll(lbl_descrBuyPower, croupier.getLbl_buyPower());		

		sl.getLabelNumberOfActionsAndBuys().setText("warten bis Spiel voll ist...");
		
		//actions label und Wert
		HBox hb_wrapper_lblActions = new HBox();
		hb_wrapper_lblActions.getChildren().addAll(lbl_descrActions,croupier.getLbl_actions());
		
		//buys label und Wert
		HBox hb_wrapper_lblBuys = new HBox();
		hb_wrapper_lblBuys.getChildren().addAll(lbl_descrBuys,croupier.getLbl_buys());
		
		
		this.provisorischCard1 = new Button ("Buy Modus Aktivieren");
		this.provisorischCard2 = new Button ("Aktionsmodus Aktivieren");
		this.provisorischCard3 = new Button ("Geld Nachf�llen");
		
		vb_wrapper_gameInformation.getChildren().addAll(hb_wrapper_lblBuyPower,hb_wrapper_lblActions,hb_wrapper_lblBuys, sl.getLabelNumberOfActionsAndBuys(),provisorischCard1,provisorischCard2,provisorischCard3);   //  << l�schen: sl.getLabelNumberOfActionsAndBuys
		
		hb_wrapper_playedCards_gameInformation.getChildren().addAll(hb_playedCards,vb_wrapper_gameInformation);
		
		

		
		vb_wrapper_center.getChildren().addAll(hb_wrp_communityActionCardsBackRow,hb_wrp_communityActionCardsFrontRow,hb_wrapper_playedCards_gameInformation);
		//--------------------------------------------------------------------------------------------------------------------------------------//
		
		
		
		
		
			
		//____________Chat und Spielverlauf rechte Seite___________________________________________________________________________________________________//
		
		windowGameHistory = sl.getTextAreaGameHistory();
		windowGameHistory.setEditable(false);
		windowGameHistory.setStyle("-fx-opacity: 0.80;");
		
	    chatWindowPlayingStage = sl.getTextAreaChatPlayingStage();
		chatWindowPlayingStage.setEditable(false);
		chatWindowPlayingStage.setStyle("-fx-opacity: 0.80;");
		

		tf_messagePlayingStage = new TextField();
		tf_messagePlayingStage.setStyle("-fx-opacity: 0.80;");
		
		btn_sendChatMsgPlayingStage = new customButton("senden");
		btn_sendChatMsgPlayingStage.getStyleClass().addAll("btn","btn_sendChatMsg");
		btn_sendChatMsgPlayingStage.setBtnTextEmpty(btn_sendChatMsgPlayingStage);
		
		//Label label_gameHistory = new Label("Spielverlauf");
		//Label label_chat = new Label("Chat");
		
		
		VBox vb_wrapper_right = new VBox();
		vb_wrapper_right.setMinWidth(300);
		vb_wrapper_right.setMaxWidth(300);
		
		vb_wrapper_right.setPadding(new Insets(0,20,0,20));

		VBox vb_wrapper_spielverlauf_chat_btns = new VBox();
				
		vb_wrapper_spielverlauf_chat_btns.getChildren().addAll(windowGameHistory,chatWindowPlayingStage,tf_messagePlayingStage,btn_sendChatMsgPlayingStage);
		
		vb_wrapper_right.getChildren().addAll(vb_wrapper_spielverlauf_chat_btns);
		
		//--------------------------------------------------------------------------------------------------------------------------------------//
				
		
		
		
		//____________holecards contorlbuttons___________________________________________________________________________________________________//
		
		customButton blank = new customButton();
		blank.setMinSize(180, 240);
		customButton nachziehstapel = new customButton();
		nachziehstapel.setMinSize(180, 240);
		
		
		
		sl.setButtonPlayActions("Aktion spielen");
		sl.getButtonPlayActions().setDisable(true);
	
		sl.setButtonPlayBuy("Kauf spielen");
		sl.getButtonPlayBuy().setDisable(true);
		
		sl.setButtonEndActions("Aktionsphase beenden");
		sl.getButtonEndActions().setDisable(true);
		
		sl.setButtonEndBuys("Kaufphase beenden");
		sl.getButtonEndBuys().setDisable(true);
		

		
		HBox hb_wrapper_bottom = new HBox();
		hb_wrapper_bottom.setPadding(new Insets(0,20,20,20));
		
		
		//Ablage und Nachziehstapel
		HBox hb_wrapper_stapel = new HBox();
		hb_wrapper_stapel.setPadding(new Insets(0,50,0,0));
		HBox hb_wrapper_ablagestapel = new HBox();
		HBox hb_wrapper_nachziehstapel = new HBox();
		
		nachziehstapel.getStyleClass().addAll("card","cardback2");
		hb_wrapper_stapel.getChildren().addAll(blank,nachziehstapel);
		
		//wrapper hole Cards
		//HBox hb_wrapper_holeCards = new HBox();
		hb_wrapper_holeCards = new HBox();
		
		//holeCards initialisieren (7 copper, 3 estates)
		al_allStartingCards = new ArrayList<GameCard>();	
			
		for (int i = 0; i < 7; i++)
			al_allStartingCards.add(new MoneyCard(new Label("copper"),croupier.getBuyPowerCopper(),croupier.getCostsCopper()));	
			
		for (int i = 0; i <3; i++)
			al_allStartingCards.add(new ProvinceCard(new Label("estate"),croupier.getCostsEstate()));
			
		//alle Objekte in al_allStartingCards als "holeCards" true flaggieren und observer adden
		for (int i = 0; i < 10; i++) {
			al_allStartingCards.get(i).setHoleCard(true);
			croupier.addObserver(al_allStartingCards.get(i));
		}
		
		//Liste mischeln und 5 Karten in den nachziehstapel legen, und 5 karten in die hand legen
		Collections.shuffle(al_allStartingCards);
		for (int i = 0; i < 5; i++)
			croupier.addToHoleCards(al_allStartingCards.get(i));
		
		for (int i = 5; i < 10; i++)
			croupier.addToNachziehstapel(al_allStartingCards.get(i));
		
		
				//holeCards auf Board Anzeigen
				for (int i = 0; i < croupier.getHoleCards().size(); i++){
				GameCard gc1 = croupier.getHoleCards().get(i);
				hb_wrapper_holeCards.getChildren().add(gc1);
				gc1.setMinSize(180, 240);
				}
		
		//label anzahl nachziehkarten hier?= <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<		
				
				
		
		//Bereich nach holecards recht
		HBox hb_wrapper_gameController = new HBox();
		
		
		VBox vb_wrapper_controlButtons = new VBox();

		vb_wrapper_controlButtons.getChildren().addAll(sl.getButtonPlayActions(),sl.getButtonPlayBuy(),sl.getButtonEndActions(),sl.getButtonEndBuys());
		
		
	
		hb_wrapper_gameController.getChildren().addAll(vb_wrapper_controlButtons);
		
		HBox spacer2 =  new HBox();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		hb_wrapper_bottom.getChildren().addAll(hb_wrapper_stapel,hb_wrapper_holeCards,spacer2,hb_wrapper_gameController);
		
		
		//--------------------------------------------------------------------------------------------------------------------------------------//
		
		
		
		
		//____________Community Action Cards (Mitte)___________________________________________________________________________________________________//
				//--------------------------------------------------------------------------------------------------------------------------------------//
				
		
		
		
		
		
		
		gold.addClickListener();

		
	
	
		

		HBox hb_wrapper_buyPowerActionBuys = new HBox();
		//croupier.setSimpleIntegerPropertyBuyPower();
		//hb_wrapper_buyPowerActionBuys.getChildren().add(buyPower);
		
		
		
		
		
		

	  

		

		this.provisorischCard1.setDisable(false);

		this.provisorischCard2.setDisable(false);

		this.provisorischCard3.setDisable(false);
		
		this.provisorisch4 = new Button("kjkjk");

		
	
		
	

		
		vb_player = new VBox();
		


		root.setTop(vb_player);
		vb_player.setAlignment(Pos.TOP_RIGHT);
		vb_player.setPrefHeight(60.0);
		
		//setting the treasure and point cards to buy
		//GridPane gp_left = new GridPane();
		

		//Button curse = new Button ("Curse");

		//Button silver = new Button ("Silver");
		//Button copper = new Button ("Copper");
		
	/*	GridPane.setConstraints(province, 0, 0);
		GridPane.setConstraints(duchy, 0, 1);
		GridPane.setConstraints(estate, 0, 2);
		//GridPane.setConstraints(curse, 0, 3);
		
		GridPane.setConstraints(gold, 1, 0);
		GridPane.setConstraints(silver, 1, 1);
		GridPane.setConstraints(copper, 1, 2);
		
		gp_left.getChildren().addAll(province,duchy,estate,gold,silver,copper);
		*/
		
		
		VBox vb_center = new VBox();
		//GridPane gp_actionCards = new GridPane();
		
		

		//Croupier.getCroupier().addObserver(gc1);
		
		
		
		Label playedCards_label = new Label ("gespielte Karten");
		vb_center.getChildren().addAll(playedCards_label);
		
		HBox playedCards_hbox = new HBox();
		Button playedCard1 = new Button ("playedCard");
		Button playedCard2 = new Button ("playedCard");
		Button playedCard3 = new Button ("playedCard");
		
		
		
	
		
		
		
		playedCards_hbox.getChildren().addAll(playedCard1,playedCard2,playedCard3);
		vb_center.getChildren().add(playedCards_hbox);
		
		//VBox vb_bottom = new VBox();
		//vb_bottom.setPrefSize(800, 60);
		

		HBox hb_stack_hand_endAction_endBuy = new HBox();
		//hb_stack_hand_endAction_endBuy.setPrefSize(750, 120);
	
		/*HBox hb_hand = new HBox();
		hb_hand.getChildren().addAll(provisorischCard1, provisorischCard2,provisorischCard3);
		
		VBox vb_stack_endGameHost = new VBox();

		vb_stack_endGameHost.getChildren().addAll(tryUpdateshit,stack);


		
		HBox.setMargin(vb_stack_endGameHost, new Insets(0, 100, 0, 0));
		HBox.setMargin(yourHand, new Insets(0, 20, 0, 0));
		HBox.setMargin(hb_hand, new Insets(0, 20, 0, 0));
		
		hb_stack_hand_endAction_endBuy.getChildren().addAll(vb_stack_endGameHost,yourHand,hb_hand,sl.getButtonPlayActions(),sl.getButtonPlayBuy(),sl.getButtonEndActions(),sl.getButtonEndBuys());
		hb_stack_hand_endAction_endBuy.setAlignment(Pos.TOP_CENTER);
		
		vb_bottom.getChildren().addAll(sl.getLabelNumberOfActionsAndBuys(),hb_stack_hand_endAction_endBuy);
		
		vb_bottom.setAlignment(Pos.TOP_CENTER);
		*/
		root.setTop(hb_custom_menue);
		root.setLeft(hb_wrapper_communityCards_Left);
		root.setCenter(vb_wrapper_center);
		root.setRight(vb_wrapper_right);
		root.setBottom(hb_wrapper_bottom);
		

		this.scene = new Scene (root,1850,900);
		scene.getStylesheets().add(getClass().getResource("/stylesheets/style_playStage.css").toExternalForm());
	    //stage.initStyle(StageStyle.TRANSPARENT);   
		
		

		   scene.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
		        @Override
		        public void handle(MouseEvent mouseEvent) {
		           // System.out.println("mouse click detected! " + mouseEvent.getSource());
		            
		            //redraw community cards left
		            
   
		    		/*hb_wrapper_lblBuyPower.getChildren().clear();
		        	hb_wrapper_lblBuyPower.getChildren().addAll(lbl_descrBuyPower, croupier.getLbl_buyPower());
		        	
		    		hb_wrapper_lblActions.getChildren().clear();
		    		hb_wrapper_lblActions.getChildren().addAll(lbl_descrActions,croupier.getLbl_actions());
		    		
		    		hb_wrapper_lblBuys.getChildren().clear();
		    		hb_wrapper_lblBuys.getChildren().addAll(lbl_descrBuys,croupier.getLbl_buys());*/
		        }
		    });
		
		
        return scene;
	}

	
	
	public void updateGUI() {
		System.out.println("updategui erhalten");
		

		
		//Zeichne holeCards Neu
		hb_wrapper_holeCards.getChildren().clear();
		for (int i = 0; i < croupier.getHoleCards().size(); i++){
		GameCard gc1 = croupier.getHoleCards().get(i);
		hb_wrapper_holeCards.getChildren().add(gc1);
		gc1.setMinSize(180, 240);
		}
		
		
  
		
	};
	

	//im moment passiert hier nichts, sp�ter ev. l�schen
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