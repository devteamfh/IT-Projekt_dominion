package Dominion.Client.ClientClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import Dominion.Client.ClientClasses.Client_View_start.Delta;
import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.Client.ClientClasses.gameplay.cards.ActionCard;
import Dominion.Client.ClientClasses.gameplay.cards.GameCard;
import Dominion.Client.ClientClasses.gameplay.cards.MoneyCard;
import Dominion.Client.ClientClasses.gameplay.cards.VictoryCard;
import Dominion.Client.abstractClasses.View;
import Dominion.appClasses.GameParty;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
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
public class Client_View_playingStage extends View<Client_Model> {

	ServiceLocatorClient sl;
	Croupier croupier;

	Scene scene;
	
	BorderPane root;
	
	Button btn_close;

	VictoryCard estate, duchy, province;
	MoneyCard copper, silver, gold;
	VictoryCard curse;

	ArrayList<GameCard> al_communityCards_left;
	ArrayList<GameCard> al_allStartingCards;

	VBox vb_player;
	
	Label lbl_playerList;
	
	HBox hb_wrapper_holeCards;
	
	Label lbl_descrBuyPower;
	Label lbl_descrBuys;
	Label lbl_descrActions;

	HBox hb_wrapper_stapel;
	Button dummyCardAblagestapel;
	Button nachziehstapel;
	
	
	VBox vb_wrapper_gameInformation_content;
	HBox hb_wrapper_ActionsBuysBuyPower;
	
	
	TextArea windowGameHistory;
	
	customButton btn_userInteraction;
	
	TextArea chatWindowPlayingStage;
	TextField tf_messagePlayingStage;
	customButton btn_sendChatMsgPlayingStage;


	
	Button provisorischCard1;
	Button provisorischCard2;
	Button provisorischCard3;

	Button provisorisch4;

	Button action2;

	Label stack;
	Label yourHand;



	public Client_View_playingStage(Stage stage, Client_Model model, GameParty party) {
		super(stage, model, party);
		stage.setTitle("Dominion - Spielplattform");
	}

	/**
	 * @author Joel Henz, kab
	 */
	protected Scene create_GUI() {
		
		croupier = Croupier.getCroupier();
		sl = ServiceLocatorClient.getServiceLocator();

		root = new BorderPane();

		
		
		
		// _______________X Button top right__________________________//
		btn_close = new Button();
		btn_close.setPrefSize(35, 33);
		btn_close.getStyleClass().addAll("btn", "btn_close_normal");
		btn_close.setAlignment(Pos.TOP_RIGHT);

		// if the host ends his game before the GameParty is full, the game will
		// end for the host and all other clients and will disappear on the
		// ListView "Spielübersicht" in the lobby.
		// There will be no score for this GameParty. Once the GameParty is
		// full, the GameParty will disappear on the ListView. While playing the
		// game, each client is able to leave the GameParty. His score
		// will be evaluated as a loss.
		//sl.getButtonEndGameHost().setDisable(true);
		// by default this button is also deactivated
		//sl.getButtonLeaveGamePlayer().setDisable(true);

		// X Button top right, wrapper hb_custom menue
		HBox hb_custom_menue = new HBox();

		HBox spacer = new HBox();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		//HBox hb_endGameHost_endGamePlayer = new HBox();

		//hb_endGameHost_endGamePlayer.getChildren().addAll(sl.getButtonEndGameHost(), sl.getButtonLeaveGamePlayer());

		hb_custom_menue.getChildren().addAll(spacer, btn_close);
		hb_custom_menue.setPadding(new Insets(15, 0, 0, 0));

		// Only the will get the button for ending the game until the game isn't
		// full (example 3 of 4 players -> host can end the game)
		//if (sl.getIsHost()) {
		//	sl.getButtonEndGameHost().setDisable(false);
		//} else {
			// button activation if the the client isn't the host
		//	sl.getButtonLeaveGamePlayer().setDisable(false);
		//}

		// ----------------------------------------------------------//

		
		
		

		//_________Community Cards auf der Linken seite Initialisieren___________//
		estate   = new VictoryCard(new Label("estate"),croupier.getCostsEstate(),croupier.getPointsEstate(), "Anwesen");
		duchy    = new VictoryCard(new Label("duchy"),croupier.getCostsDuchy(),croupier.getPointsDuchy(), "Herzogtum");
		province = new VictoryCard(new Label("province"),croupier.getCostsPovince(),croupier.getPointsProvince(), "Provinz");
		curse =    new VictoryCard(new Label("curse"),croupier.getCostsCurse(),croupier.getPointsCurse(), "Fluch");
		
		copper = new MoneyCard(new Label("copper"),croupier.getBuyPowerCopper(),croupier.getCostsCopper(),"Kupfer");
		silver = new MoneyCard(new Label("silver"),croupier.getBuyPowerSilver(),croupier.getCostsSilver(), "Silber");
		gold   = new MoneyCard(new Label("gold"),croupier.getBuyPowerGold(),croupier.getCostsGold(), "Gold");
		
		//curse = new MoneyCard(new Label("curse"),croupier.getBuyPowerCurse(),croupier.getCostsCurse(), "Fluch");


		al_communityCards_left = new ArrayList<GameCard>();
		al_communityCards_left.add(estate);
		al_communityCards_left.add(duchy);
		al_communityCards_left.add(province);
		al_communityCards_left.add(copper);
		al_communityCards_left.add(silver);
		al_communityCards_left.add(gold);
		al_communityCards_left.add(curse);

		// add Observer, setSize
		GameCard gc;
		for (int i = 0; i < al_communityCards_left.size(); i++) {
			gc = al_communityCards_left.get(i);
			croupier.addObserver(gc);
			gc.setMinSize(120, 110);
			gc.assignStackSizeInfo();
		}

		// Branches
		HBox hb_wrapper_communityCards_Left = new HBox();
		hb_wrapper_communityCards_Left.setPadding(new Insets(0, 15, 0, 50));

		VBox vb_wrapper_communityCards_Left_col1 = new VBox();
		VBox vb_wrapper_communityCards_Left_col2 = new VBox();

		HBox hb_wrapper_province = new HBox();
		hb_wrapper_province.getChildren().add(province);
		hb_wrapper_province.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_duchy = new HBox();
		hb_wrapper_duchy.getChildren().add(duchy);
		hb_wrapper_duchy.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_estate = new HBox();
		hb_wrapper_estate.getChildren().add(estate);
		hb_wrapper_estate.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_curse = new HBox();
		hb_wrapper_curse.getChildren().add(curse);
		hb_wrapper_curse.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_gold = new HBox();
		hb_wrapper_gold.getChildren().add(gold);
		hb_wrapper_gold.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_silver = new HBox();
		hb_wrapper_silver.getChildren().add(silver);
		hb_wrapper_silver.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_copper = new HBox();
		hb_wrapper_copper.getChildren().add(copper);
		hb_wrapper_copper.setPadding(new Insets(0, 5, 5, 0));

		vb_wrapper_communityCards_Left_col1.getChildren().addAll(hb_wrapper_province, hb_wrapper_duchy,
				hb_wrapper_estate, hb_wrapper_curse);
		vb_wrapper_communityCards_Left_col2.getChildren().addAll(hb_wrapper_gold, hb_wrapper_silver, hb_wrapper_copper);

		hb_wrapper_communityCards_Left.getChildren().addAll(vb_wrapper_communityCards_Left_col1,
				vb_wrapper_communityCards_Left_col2);

		// --------------------------------------------------------------------------------------------------------------------------------------//

		
		
		
		
		
		// ____________CenterPane communtiycard in der mitte und  statusinformationen zum spiel (buypower, action__________________________________________________//

		// community Action Cards in der Mitte mit 10 karten initialisieren
		croupier.prepareAL_stackSizeCommunityActionCards();

		// Allen CommunityActionCards einen observer hinzuf�gen
		for (GameCard ac : croupier.getAl_communityActionCards()) {
			croupier.addObserver(ac);
			ac.setMinSize(200, 165);
			ac.assignStackSizeInfo();
		}

		lbl_descrBuyPower = new Label(" Kaufkraft ");
		lbl_descrBuys = new Label(" K�ufe ");
		lbl_descrActions = new Label(" Aktionen ");

		// Branches
		VBox vb_wrapper_center = new VBox();
		vb_wrapper_center.setMinSize(700, 600);
		vb_wrapper_center.setPadding(new Insets(0, 0, 0, 25));

		HBox hb_wrp_communityActionCardsBackRow = new HBox();
		hb_wrp_communityActionCardsBackRow.setPadding(new Insets(30, 0, 0, 0));

		HBox hb_wrp_communityActionCardsFrontRow = new HBox();

		HBox hb_wrapper_comntyACSlot0 = new HBox();
		hb_wrapper_comntyACSlot0.getChildren().add(croupier.getAl_communityActionCards().get(0));
		hb_wrapper_comntyACSlot0.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_comntyACSlot1 = new HBox();
		hb_wrapper_comntyACSlot1.getChildren().add(croupier.getAl_communityActionCards().get(1));
		hb_wrapper_comntyACSlot1.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_comntyACSlot2 = new HBox();
		hb_wrapper_comntyACSlot2.getChildren().add(croupier.getAl_communityActionCards().get(2));
		hb_wrapper_comntyACSlot2.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_comntyACSlot3 = new HBox();
		hb_wrapper_comntyACSlot3.getChildren().add(croupier.getAl_communityActionCards().get(3));
		hb_wrapper_comntyACSlot3.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_comntyACSlot4 = new HBox();
		hb_wrapper_comntyACSlot4.getChildren().add(croupier.getAl_communityActionCards().get(4));
		hb_wrapper_comntyACSlot4.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_comntyACSlot5 = new HBox();
		hb_wrapper_comntyACSlot5.getChildren().add(croupier.getAl_communityActionCards().get(5));
		hb_wrapper_comntyACSlot5.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_comntyACSlot6 = new HBox();
		hb_wrapper_comntyACSlot6.getChildren().add(croupier.getAl_communityActionCards().get(6));
		hb_wrapper_comntyACSlot6.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_comntyACSlot7 = new HBox();
		hb_wrapper_comntyACSlot7.getChildren().add(croupier.getAl_communityActionCards().get(7));
		hb_wrapper_comntyACSlot7.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_comntyACSlot8 = new HBox();
		hb_wrapper_comntyACSlot8.getChildren().add(croupier.getAl_communityActionCards().get(8));
		hb_wrapper_comntyACSlot8.setPadding(new Insets(0, 5, 5, 0));
		HBox hb_wrapper_comntyACSlot9 = new HBox();
		hb_wrapper_comntyACSlot9.getChildren().add(croupier.getAl_communityActionCards().get(9));
		hb_wrapper_comntyACSlot9.setPadding(new Insets(0, 5, 5, 0));

		hb_wrp_communityActionCardsBackRow.getChildren().addAll(hb_wrapper_comntyACSlot0, hb_wrapper_comntyACSlot1,
				hb_wrapper_comntyACSlot2, hb_wrapper_comntyACSlot3, hb_wrapper_comntyACSlot4);
		hb_wrp_communityActionCardsFrontRow.getChildren().addAll(hb_wrapper_comntyACSlot5, hb_wrapper_comntyACSlot6,
				hb_wrapper_comntyACSlot7, hb_wrapper_comntyACSlot8, hb_wrapper_comntyACSlot9);

		
		
		HBox hb_wrapper_playedCards_gameInformation = new HBox();
		hb_wrapper_playedCards_gameInformation.setPadding(new Insets(20,0,20,0));

		HBox hb_playedCards = new HBox();
		hb_playedCards.setMinSize(250, 150);

		
		
		
		HBox hb_wrapper_gameInformation = new HBox();
		hb_wrapper_gameInformation.getStyleClass().add("bg_gameInfo");
		hb_wrapper_gameInformation.setMinSize(550, 100);
		hb_wrapper_gameInformation.setPadding(new Insets(20,0,0,30));

			vb_wrapper_gameInformation_content = new VBox();
		
		
				//Leafes gameInformation
				sl.getLabelNumberOfActionsAndBuys().setText("warten bis Spiel voll ist...");
				
						//LabelStyling
						sl.getLabelNumberOfActionsAndBuys().getStyleClass().add("h1");						
										
			vb_wrapper_gameInformation_content.getChildren().addAll(
					sl.getLabelNumberOfActionsAndBuys());
					
									
							
		hb_wrapper_gameInformation.getChildren().add(vb_wrapper_gameInformation_content);    

		
	
		hb_wrapper_playedCards_gameInformation.getChildren().addAll(hb_playedCards, hb_wrapper_gameInformation);

		vb_wrapper_center.getChildren().addAll(hb_wrp_communityActionCardsBackRow, hb_wrp_communityActionCardsFrontRow,
				hb_wrapper_playedCards_gameInformation);
		// --------------------------------------------------------------------------------------------------------------------------------------//

		
		
		
		
		// ____________Spielverlauf Rechterhand__________________________________________________________________//
		
		vb_player = new VBox();
		vb_player.setPadding(new Insets(0,0,0,10));
		vb_player.setAlignment(Pos.TOP_LEFT);
		vb_player.setMaxHeight(130);
		vb_player.setMinHeight(130);
		
		
	
		windowGameHistory = sl.getTextAreaGameHistory();
		windowGameHistory.setPrefHeight(300);
		windowGameHistory.setEditable(false);
		windowGameHistory.getStyleClass().addAll("text-area","textBoardInfo");

	

		btn_userInteraction = new customButton("Dominion");
		btn_userInteraction.setBtnTextEmpty(btn_userInteraction);
		btn_userInteraction.getStyleClass().addAll("btn","btn_userInteraction");
		btn_userInteraction.getLbl().getStyleClass().add("lbl_btnUserInteraction"); //�berschreibt Stylezuweisung in customButton
		btn_userInteraction.setPrefSize(350, 89);
		croupier.addObserver(btn_userInteraction);
		
		
		
		VBox vb_wrapper_right = new VBox();
		vb_wrapper_right.setMinWidth(400);
		vb_wrapper_right.setMaxWidth(400);
		vb_wrapper_right.setPadding(new Insets(0,30,0,0));
		
		
			HBox hb_wrapper_windowGameHistory = new HBox();
			hb_wrapper_windowGameHistory.getChildren().add(windowGameHistory);
			
			HBox hb_wrapper_btn_userInteraction = new HBox();
			hb_wrapper_btn_userInteraction.setPadding(new Insets(15,0,0,10));
			hb_wrapper_btn_userInteraction.getChildren().add(btn_userInteraction);
			
			
			
			
			
			
			
										
		VBox vb_wrapper_spielverlauf_btn = new VBox();
		vb_wrapper_spielverlauf_btn.setPadding(new Insets(0,0,0,0));
		vb_wrapper_spielverlauf_btn.getChildren().addAll(vb_player, hb_wrapper_windowGameHistory, hb_wrapper_btn_userInteraction);

		vb_wrapper_right.getChildren().addAll(vb_wrapper_spielverlauf_btn);
		// --------------------------------------------------------------------------------------------------------------------------------------//
		
		
		
		
		// ____________holecards  contorlbuttons   chat Window__________________________________________________________________//

		dummyCardAblagestapel = new Button();
		dummyCardAblagestapel.setMinSize(160, 260);
		dummyCardAblagestapel.getStyleClass().add("dummyCardAblagestapel");
		
		nachziehstapel = new Button();
		nachziehstapel.setMinSize(160, 260);

		HBox hb_wrapper_bottom = new HBox();
		hb_wrapper_bottom.setPadding(new Insets(-50, 10, 0, 50)); 
		hb_wrapper_bottom.setMinHeight(500);
		hb_wrapper_bottom.setMaxHeight(500);

		// Ablage und Nachziehstapel
		hb_wrapper_stapel = new HBox();
		hb_wrapper_stapel.setPadding(new Insets(0, 50, 0, 0));
		HBox hb_wrapper_ablagestapel = new HBox();
		hb_wrapper_ablagestapel.setPadding(new Insets(0,0,0,0));
		HBox hb_wrapper_nachziehstapel = new HBox();

		nachziehstapel.getStyleClass().addAll("card", "cardback2");
		hb_wrapper_stapel.getChildren().addAll(dummyCardAblagestapel, nachziehstapel);

		// wrapper hole Cards
		hb_wrapper_holeCards = new HBox();
		hb_wrapper_holeCards.setMaxWidth(800);



		// holeCards initialisieren (7 copper, 3 estates)
		al_allStartingCards = new ArrayList<GameCard>();

		for (int i = 0; i < 7; i++)
			al_allStartingCards.add(new MoneyCard(new Label("copper"),croupier.getBuyPowerCopper(),croupier.getCostsCopper(),"Kupfer"));	
			
		for (int i = 0; i <3; i++)
			al_allStartingCards.add(new VictoryCard(new Label("estate"),croupier.getCostsEstate(),croupier.getPointsEstate(), "Anwesen"));
			

		// alle Objekte in al_allStartingCards als "holeCards" true flaggieren
		// und observer adden
		
		for (int i = 0; i < 10; i++) {
			al_allStartingCards.get(i).setHoleCard(true);
			croupier.addObserver(al_allStartingCards.get(i));
			al_allStartingCards.get(i).assignPicture();
		}

		// Liste mischeln und 5 Karten in den nachziehstapel legen, und 5 karten
		// in die hand legen
		Collections.shuffle(al_allStartingCards);
		for (int i = 0; i < 5; i++)
			croupier.addToHoleCards(al_allStartingCards.get(i));

		for (int i = 5; i < 10; i++)
			croupier.addToNachziehstapel(al_allStartingCards.get(i));

		// holeCards auf Board Anzeigen
		for (int i = 0; i < croupier.getHoleCards().size(); i++) {
			GameCard gc1 = croupier.getHoleCards().get(i);
			hb_wrapper_holeCards.getChildren().add(gc1);
			gc1.setPrefSize(160, 260);
		}
		
		
		

		// Bereich nach holecards recht mit Chat Fenster
		
		chatWindowPlayingStage = sl.getTextAreaChatPlayingStage();
		chatWindowPlayingStage.setEditable(false);
		chatWindowPlayingStage.getStyleClass().add("boardChat");
	

		tf_messagePlayingStage = new TextField();
		tf_messagePlayingStage.setStyle("-fx-opacity: 0.60;");
		tf_messagePlayingStage.setPrefSize(250, 40);


		btn_sendChatMsgPlayingStage = new customButton("senden");
		btn_sendChatMsgPlayingStage.getStyleClass().addAll("btn", "btn_sendChatMsg");
		btn_sendChatMsgPlayingStage.setBtnTextEmpty(btn_sendChatMsgPlayingStage);
		btn_sendChatMsgPlayingStage.setPrefSize(95, 40);
		
		
		VBox vb_wrapper_chat = new VBox();
		vb_wrapper_chat.setMinSize(335, 250);
		vb_wrapper_chat.setMaxSize(335, 250);
		vb_wrapper_chat.setPadding(new Insets(0,20,0,-50));
		
			HBox hb_wrapper_chatInput_btnSend = new HBox();
			
				HBox hb_wrapper_tf_messagePlayingStage = new HBox();
				hb_wrapper_tf_messagePlayingStage.getChildren().add(tf_messagePlayingStage);
				
				HBox hb_wrapper_btn_sendChatMsgPlayingStage = new HBox();
				hb_wrapper_btn_sendChatMsgPlayingStage.setPadding(new Insets(0,0,0,5));
				hb_wrapper_btn_sendChatMsgPlayingStage.getChildren().add(btn_sendChatMsgPlayingStage);
			
			
			hb_wrapper_chatInput_btnSend.getChildren().addAll(hb_wrapper_tf_messagePlayingStage,hb_wrapper_btn_sendChatMsgPlayingStage);
			hb_wrapper_chatInput_btnSend.setPadding(new Insets(5,0,0,0));
		
		vb_wrapper_chat.getChildren().addAll(chatWindowPlayingStage,hb_wrapper_chatInput_btnSend);
		
		
		
		
		

	
		
		
		
		VBox vb_wrapper_provisorische_buttons = new VBox();
		this.provisorischCard1 = new Button("Buy Modus Aktivieren");
		this.provisorischCard2 = new Button("Aktionsmodus Aktivieren");
		this.provisorischCard3 = new Button("Geld Nachf�llen");
		vb_wrapper_provisorische_buttons.getChildren().addAll(provisorischCard1,provisorischCard2,provisorischCard3);
		
		HBox spacer2 = new HBox();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		hb_wrapper_bottom.getChildren().addAll(hb_wrapper_stapel, hb_wrapper_holeCards,
				vb_wrapper_provisorische_buttons,
				spacer2,  vb_wrapper_chat);

		// --------------------------------------------------------------------------------------------------------------------------------------//

		// ____________Community Action Cards (Mitte)__________________________________//
		// --------------------------------------------------------------------------------------------------------------------------------------//

		gold.addClickListener();

		HBox hb_wrapper_buyPowerActionBuys = new HBox();
		// croupier.setSimpleIntegerPropertyBuyPower();
		// hb_wrapper_buyPowerActionBuys.getChildren().add(buyPower);

		this.provisorischCard1.setDisable(false);

		this.provisorischCard2.setDisable(false);

		this.provisorischCard3.setDisable(false);

		this.provisorisch4 = new Button("kjkjk");

		

		VBox vb_center = new VBox();
		// GridPane gp_actionCards = new GridPane();

		// Croupier.getCroupier().addObserver(gc1);

		Label playedCards_label = new Label("gespielte Karten");
		vb_center.getChildren().addAll(playedCards_label);

		HBox playedCards_hbox = new HBox();
		Button playedCard1 = new Button("playedCard");
		Button playedCard2 = new Button("playedCard");
		Button playedCard3 = new Button("playedCard");

		playedCards_hbox.getChildren().addAll(playedCard1, playedCard2, playedCard3);
		vb_center.getChildren().add(playedCards_hbox);

		
		
		 /**
		  * @author kab: Fenster via Drag und Drop verschieben
		  */
	    final Delta dragDelta = new Delta();
	    root.setOnMousePressed(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	        // record a delta distance for the drag and drop operation.
	        dragDelta.x = stage.getX() - mouseEvent.getScreenX();
	        dragDelta.y = stage.getY() - mouseEvent.getScreenY();
	        scene.setCursor(Cursor.MOVE);
	      }
	    });
	    root.setOnMouseReleased(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	        scene.setCursor(Cursor.DEFAULT);
	      }
	    });
	    root.setOnMouseDragged(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	        stage.setX(mouseEvent.getScreenX() + dragDelta.x);
	        stage.setY(mouseEvent.getScreenY() + dragDelta.y);
	      }
	    });
		
		
		

	
		root.setTop(hb_custom_menue);
		root.setLeft(hb_wrapper_communityCards_Left);
		root.setCenter(vb_wrapper_center);
		root.setRight(vb_wrapper_right);
		root.setBottom(hb_wrapper_bottom);
		root.setPadding(new Insets(0,20,0,0));
		root.getStyleClass().add("bg");
		
		this.scene = new Scene(root, 1850, 900);
		scene.getStylesheets().add(getClass().getResource("/stylesheets/style_playStage.css").toExternalForm());
		stage.initStyle(StageStyle.TRANSPARENT);
	
		return scene;
	}

	//F�rr Drag und Drop verschiebung: relative x und y Position herausfinden
	class Delta { double x, y; }

		

	public void updateGUI() {

		// Zeichne holeCards Neu
		hb_wrapper_holeCards.getChildren().clear();
		for (int i = 0; i < croupier.getHoleCards().size(); i++) {
			GameCard gc1 = croupier.getHoleCards().get(i);
			hb_wrapper_holeCards.getChildren().add(gc1);
			gc1.setPrefSize(160, 260);
			gc1.setMaxWidth(160);

			
		// Zeichne Ablagekarte neu
		hb_wrapper_stapel.getChildren().clear();
		dummyCardAblagestapel.getStyleClass().clear();
		try {
			String cssSelector = new String(croupier.getAblagestapel().peekLast().getLbl_cardName().getText()+"_big");
			
			dummyCardAblagestapel.getStyleClass().addAll("card",cssSelector);
			hb_wrapper_stapel.getChildren().addAll(dummyCardAblagestapel,nachziehstapel);	
		} catch(Exception e){
			hb_wrapper_stapel.getChildren().addAll(this.dummyCardAblagestapel,nachziehstapel);	
			}			
		}

	

		
	}

	public ArrayList<GameCard> getAl_communityCards_left() {
		return al_communityCards_left;
	}

	

}




