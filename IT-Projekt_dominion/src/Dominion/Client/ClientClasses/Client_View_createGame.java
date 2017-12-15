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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


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
    Label numberOfRounds; 
    Label numberOfPlayers;
    
    Label lbl_AnzahlSpieler;
    Label lbl_Spielmodus;
	
    Button btn_close;
    
	RadioButton btnRdo_twoPlayer;
	RadioButton btnRdo_threePlayer;
	RadioButton btnRdo_fourPlayer;
	
	RadioButton btnRdo_mode1;
	RadioButton btnRdo_mode2;
	
	Label lbl_twoPlayer;
	Label lbl_threePlayer;  
	Label lbl_fourPlayer; 

	Label lbl_mode1;
	Label lbl_mode2;
		final String INITIALERUNDENANZAHL = "20";
		Label lbl_iRunden;
	
	Button btn_iRundenPLUS;
	Button btn_iRundenMINUS;
	
	customButton btn_finish;

	public Client_View_createGame(Stage stage, Client_Model model) {
		super(stage, model);
        stage.setTitle("Dominion - neues Spiel erstellen");
    }
	
	/**
     * @author Joel Henz: Initial
     * @author KAB: Recreation with according graphics and styles
     *
     */
	
	protected Scene create_GUI() {

	    sl = ServiceLocatorClient.getServiceLocator();  
	    sl.setToggleForNumberOfPlayers();
	    sl.setToggleForEndOfGame();
	    sl.setTextFieldForRounds();


	    		
	    //Node-Leafs
		btn_close = new Button();
		btn_close.setPrefSize(35, 33);
	   	btn_close.getStyleClass().addAll("btn","btn_close_normal");
	   	btn_close.setAlignment(Pos.TOP_RIGHT);
			
	    lbl_AnzahlSpieler = new Label("Anzahl Spieler");
	    lbl_AnzahlSpieler.getStyleClass().add("h1");
	   
		    btnRdo_twoPlayer  =  new RadioButton("");
		    btnRdo_twoPlayer.getStyleClass().addAll("btn","btnRdo","btnRdo_active");
		    btnRdo_twoPlayer.getStyleClass().remove("radio-button");
		    btnRdo_twoPlayer.setSelected(true);
		    btnRdo_twoPlayer.setPrefSize(31, 30);
			    lbl_twoPlayer = new Label("Zwei Spieler");
			    lbl_twoPlayer.getStyleClass().add("lbl_menuPoint");


		    btnRdo_threePlayer =  new RadioButton("");
		    btnRdo_threePlayer.getStyleClass().addAll("btn","btnRdo");
		    btnRdo_threePlayer.getStyleClass().remove("radio-button");
		    btnRdo_threePlayer.setPrefSize(31, 30);
			    lbl_threePlayer = new Label("Drei Spieler");
				lbl_threePlayer.getStyleClass().add("lbl_menuPoint");
			
		    btnRdo_fourPlayer  =  new RadioButton("");
		    btnRdo_fourPlayer.getStyleClass().addAll("btn","btnRdo");
		    btnRdo_fourPlayer.getStyleClass().remove("radio-button");
		    btnRdo_fourPlayer.setPrefSize(31, 30);
			    lbl_fourPlayer = new Label("Vier Spieler");
				lbl_fourPlayer.getStyleClass().add("lbl_menuPoint");
		
			//ToggleGroup
			btnRdo_twoPlayer.setToggleGroup(sl.getToggleForNumberOfPlayers());
		    btnRdo_threePlayer.setToggleGroup(sl.getToggleForNumberOfPlayers());
		    btnRdo_fourPlayer.setToggleGroup(sl.getToggleForNumberOfPlayers());
				
				
				
			lbl_Spielmodus = new Label("Spielmodus");  
			lbl_Spielmodus.getStyleClass().add("h1");
				
				lbl_mode1  = new Label("Provinzkarten aufbrauchen");
				lbl_mode1.getStyleClass().add("lbl_menuPoint");
		
				lbl_iRunden = new Label(INITIALERUNDENANZAHL);
				lbl_iRunden.getStyleClass().add("lbl_iRunden");
			    btnRdo_mode1 = new RadioButton("");
			    btnRdo_mode1.getStyleClass().addAll("btn","btnRdo","btnRdo_active");
				btnRdo_mode1.getStyleClass().remove("radio-button");
				btnRdo_mode1.setSelected(true);
			    btnRdo_mode1.setPrefSize(31, 30);
			    
			    
			    lbl_mode2 = new Label("Rundenanzahl");
				lbl_mode2.getStyleClass().add("lbl_menuPoint");
				
			    btnRdo_mode2 = new RadioButton("");
			    btnRdo_mode2.getStyleClass().addAll("btn","btnRdo");
				btnRdo_mode2.getStyleClass().remove("radio-button");
			    btnRdo_mode2.setPrefSize(31, 30);
			    	btn_iRundenPLUS = new Button("");
			    	btn_iRundenPLUS.getStyleClass().addAll("btn","btn_PlusMinus","btn_Plus_clicked");
			    	btn_iRundenPLUS.setPrefSize(34,34);
				    
				    btn_iRundenMINUS = new Button("");
			    	btn_iRundenMINUS.getStyleClass().addAll("btn","btn_PlusMinus","btn_Minus_clicked");
			    	btn_iRundenMINUS.setPrefSize(34,34);

			    //ToggleGroup
			    btnRdo_mode1.setToggleGroup(sl.getToggleForEndOfGame());
		    	btnRdo_mode2.setToggleGroup(sl.getToggleForEndOfGame());

	    
	    btn_finish  = new customButton("Fertig");
	    btn_finish.getStyleClass().addAll("btn","btn_sendChatMsg");
	    btn_finish.setBtnTextEmpty(btn_finish);
	    btn_finish.setPrefSize(202, 40);
		    
	    
	    
	    //Nodebranches
	    BorderPane root = new BorderPane();

		
		 //X Button top right, wrapper hb_custom menue
	    HBox hb_custom_menue = new HBox();
	    HBox spacer =  new HBox();
	   	HBox.setHgrow(spacer, Priority.ALWAYS);
	   	
	   	hb_custom_menue.getChildren().addAll(spacer, btn_close);
	   	hb_custom_menue.setPadding(new Insets(5,5,5,0));		  
	   	
	   	
	   	
		    //Wrapper Content
		    HBox hb_wrapper_Content = new HBox();
		    hb_wrapper_Content.setPadding(new Insets(0,50,0,0));
		    
		    VBox vb_wrapper_properties = new VBox();
		    
			    //Boxes for menu "Anzahl Spieler"
			    VBox vb_wrapper_AnzahlSpieler = new VBox();
			    vb_wrapper_AnzahlSpieler.setPadding(new Insets(20,15,20,50));
			    
			    	//Boxes for Menu items
				    HBox hb_wrapper_2Players = new HBox();
				    hb_wrapper_2Players.getChildren().addAll(btnRdo_twoPlayer,lbl_twoPlayer);
				    lbl_twoPlayer.setPadding(new Insets(0,0,0,20));
					
				    HBox hb_wrapper_3Players = new HBox();
				    hb_wrapper_3Players.getChildren().addAll(btnRdo_threePlayer,lbl_threePlayer);
				    lbl_threePlayer.setPadding(new Insets(0,0,0,20));
    
				    HBox hb_wrapper_4Players = new HBox();
				    hb_wrapper_4Players.getChildren().addAll(btnRdo_fourPlayer,lbl_fourPlayer);
				    lbl_fourPlayer.setPadding(new Insets(0,0,0,20));
				    
				vb_wrapper_AnzahlSpieler.getChildren().addAll(hb_wrapper_2Players,hb_wrapper_3Players,hb_wrapper_4Players);
			    
				    
			    //Boxes for menu "Spielmodus"
			    VBox vb_wrapper_Spielmodus = new VBox();
			    vb_wrapper_Spielmodus.setPadding(new Insets(20,15,0,50));
			    
			    	//Boxes for Menu items
			    	HBox hb_wrapper_mode1 = new HBox();
			    	hb_wrapper_mode1.getChildren().addAll(btnRdo_mode1, lbl_mode1);
			    	lbl_mode1.setPadding(new Insets(0,0,0,20));
			    	
			    	HBox hb_wrapper_mode2 = new HBox();
		    			HBox hb_wrapper_iRunden = new HBox();
			    	hb_wrapper_mode2.getChildren().addAll(btnRdo_mode2, lbl_mode2, hb_wrapper_iRunden);
			    	lbl_mode2.setPadding(new Insets(0,0,0,20));
			    	
			    	VBox vb_wrapper_btn_finish = new VBox();
			    	vb_wrapper_btn_finish.getChildren().add(btn_finish);
			    	vb_wrapper_btn_finish.setPadding(new Insets(25,0,0,0));
			    	
			    		//Boxes 
			    		VBox vb_wrapper_btns_iRunden = new VBox();
			    			 vb_wrapper_btns_iRunden.getChildren().addAll(btn_iRundenPLUS,btn_iRundenMINUS);
			    			 vb_wrapper_btns_iRunden.setPadding(new Insets(10,70,0,15));
			    			 
			    			 
			    			 
			    		hb_wrapper_iRunden.getChildren().addAll(lbl_iRunden,vb_wrapper_btns_iRunden,vb_wrapper_btn_finish);


			    		hb_wrapper_iRunden.setPadding(new Insets(0,0,0,20));
			    		
			    vb_wrapper_Spielmodus.getChildren().addAll(hb_wrapper_mode1,hb_wrapper_mode2);
			  
			 //bringing the two menues Anzahl Spieler und Spielmodus together   
			 vb_wrapper_properties.getChildren().addAll(lbl_AnzahlSpieler,vb_wrapper_AnzahlSpieler,lbl_Spielmodus,vb_wrapper_Spielmodus);
			 
			 
	    //Wrap all content
	   hb_wrapper_Content.getChildren().addAll(vb_wrapper_properties);
	   hb_wrapper_Content.setPadding(new Insets(0,0,0,50));
		    	
			
		
		root.setTop(hb_custom_menue);
		root.setCenter(hb_wrapper_Content);
		root.getStyleClass().add("bg_createGame");
		
		this.scene = new Scene (root, 750, 410);
		scene.getStylesheets().add(getClass().getResource("/stylesheets/style_clientStart.css").toExternalForm());
	    stage.initStyle(StageStyle.TRANSPARENT);     
        return scene;
		
	}

}