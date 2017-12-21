package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.util.ArrayList;

import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.View;
import Dominion.appClasses.PlayerWithOS;
import Dominion.appClasses.PlayerWithoutOS;
import Dominion.appClasses.StartInformation;
import Dominion.appClasses.UpdateLobby;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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


public class Client_View_lobby extends View<Client_Model> {
    CustomButton btn_sendChatMsg;
    CustomButton btn_newGame;
    CustomButton btn_enterGame;
    Button btn_close;
    
    TextField tf_message;

    TextArea chatWindow;
    Scene scene;
    ServiceLocatorClient sl;
    String PlayerName;
    Label name;
    Label yourSign;

    TableView <StartInformation> tbl_playerStats;
    
	public Client_View_lobby(Stage stage, Client_Model model) {
		super(stage, model);
        stage.setTitle("Dominion");
    }
	
	/**
     * @author Joel Henz initial
     * @author Beda Kaufmann final view
     */
	
	protected Scene create_GUI() {

	    sl = ServiceLocatorClient.getServiceLocator();  
	    sl.setListView();
		sl.getListView().setPrefSize(730,200);
		sl.getListView().setStyle("-fx-opacity: 0.80;");
		
		PlayerWithoutOS player = new PlayerWithoutOS(model.getName());
		
		sl.setPlayerName(model.getName());
		sl.setPlayer_noOS(player);
		sl.setPlayer_OS(model.getPlayer_OS());

		btn_newGame = new CustomButton ("neues Spiel");
		btn_newGame.getStyleClass().addAll("btn","btn_view");
		btn_newGame.setBtnTextEmpty(btn_newGame);
	    btn_newGame.setPrefSize(206, 54);
		
		btn_enterGame = new CustomButton ("Spiel beitreten");
		btn_enterGame.getStyleClass().addAll("btn","btn_view");
		btn_enterGame.setBtnTextEmpty(btn_enterGame);
	    btn_enterGame.setPrefSize(206,54);
		btn_enterGame.setDisable(true);
		
		//CHAT
		chatWindow = sl.getTextAreaChatLobby();
		chatWindow.setEditable(false);
		chatWindow.setPrefSize(820, 150);
		chatWindow.setStyle("-fx-opacity: 0.80;");
		
		tf_message = new TextField();
		tf_message.setPrefSize(450,40);
		tf_message.setStyle("-fx-opacity: 0.80;");
		
		btn_sendChatMsg = new CustomButton("senden");
		btn_sendChatMsg.getStyleClass().addAll("btn","btn_sendChatMsg");
		btn_sendChatMsg.setBtnTextEmpty(btn_sendChatMsg);
		btn_sendChatMsg.setPrefSize(202, 40);
		
		
		name = new Label();
		name.setText("Ihr Name: "+model.getName());
		Label message = new Label();
		message.setText("Ihre Nachricht");
		
		tf_message.setText("Ihre Nachricht");
		
		//Prim�rre Nodes initialisieren		
				
		BorderPane root = new BorderPane();
	    /*______________________________________________________________________________________________*/				    

		
		
				//Root BorderPane top
				//***********************************//
		
				 //X Button top right, wrapper hb_custom menue
			    HBox hb_custom_menue = new HBox();
			      
			   	btn_close = new Button();
				btn_close.setPrefSize(35, 33);
			   	btn_close.getStyleClass().addAll("btn","btn_close_normal");
			   	btn_close.setAlignment(Pos.TOP_RIGHT);
			   	
			    HBox spacer =  new HBox();
			   	HBox.setHgrow(spacer, Priority.ALWAYS);
			   	
			   	hb_custom_menue.getChildren().addAll(spacer, btn_close);
			   	hb_custom_menue.setPadding(new Insets(20,15,150,0));		   	

			   	

				//Root BorderPane Center
				//***********************************//
			    VBox vb_wrapperContent = new VBox();
			    vb_wrapperContent.setPadding(new Insets(0,30,0,30));
			    /*______________________________________________________________________________________________*/				    

							
					
				    //Inhalt upperhalf (TableList)
					//***********************************//
			    
			    
			    	HBox hb_wrapper_upperhalf = new HBox();
			    	hb_wrapper_upperhalf.setAlignment(Pos.TOP_LEFT);
				    /*______________________________________________________________________________________________*/				    

			    	
					
					//table kreieren//
					
					
			    	tbl_playerStats = sl.getTbl_playerStats();
					TableColumn<StartInformation,String> column00 = new TableColumn<StartInformation,String>("Spieler");
					column00.setMinWidth(200);
					column00.setCellValueFactory(new PropertyValueFactory<>("username"));
					
					TableColumn<StartInformation,Integer> column01 = new TableColumn<StartInformation,Integer>("Gespielte Spiele");
					column01.setMinWidth(150);
					column01.setCellValueFactory(new PropertyValueFactory<>("gamesPlayed"));
					
					TableColumn<StartInformation,Integer> column02= new TableColumn<StartInformation,Integer>("Gewonnene Spiele");
					column02.setMinWidth(150);
					column02.setCellValueFactory(new PropertyValueFactory<>("gamesWon"));
					
					TableColumn<StartInformation,Integer> column03 = new TableColumn<StartInformation,Integer>("Verlorene Spiele");
					column03.setMinWidth(150);
					column03.setCellValueFactory(new PropertyValueFactory<>("gamesLost"));
					
					TableColumn<StartInformation,Double> column04 = new TableColumn<StartInformation,Double>("Winner in %");
					column04.setMinWidth(150);
					column04.setCellValueFactory(new PropertyValueFactory<>("winLooseRatio"));
				
					TableColumn<StartInformation,Integer> column05 = new TableColumn<StartInformation,Integer>("Rang");
					column05.setMinWidth(250);
					column05.setCellValueFactory(new PropertyValueFactory<>("att6"));
					
					tbl_playerStats.getColumns().addAll(column00,column01,column02,column03,column04,column05);
					tbl_playerStats.setPrefSize(1250, 200);
					tbl_playerStats.setStyle("-fx-opacity: 0.80;");
					

					/*Upperhalf zusammenf�hren*/
					hb_wrapper_upperhalf.getChildren().addAll(tbl_playerStats);
				    /*______________________________________________________________________________________________*/				    
				    /************************************************************************************************/				    

				    
				    
				    
					//abstand zwischen upper und lower-half
					hb_wrapper_upperhalf.setPadding(new Insets(0,0,30,0));
				    
						
					
					
				    
				    //Inhalt lowerhalf(gameList und Chatbox)
					//***********************************************//
					
				    HBox hb_wrapper_lowerhalf = new HBox();
				
				    
				    //gameList und Controll Buttons gruppieren
				    VBox vb_wrapper_gameList = new VBox();

				    /*______________________________________________________________________________________________*/				    
					HBox hb_wrapper_btnNewGame    = new HBox();
					HBox hb_wrapper_btnEnterGame = new HBox();

					hb_wrapper_btnNewGame.getChildren().add(btn_newGame);
					hb_wrapper_btnEnterGame.getChildren().add(btn_enterGame);
					
				    HBox hb_wrapper_gameListControlBtns = new HBox();
				    hb_wrapper_gameListControlBtns.getChildren().addAll(hb_wrapper_btnNewGame,hb_wrapper_btnEnterGame);
					
					hb_wrapper_btnNewGame.setPadding(new Insets(10,0,0,40));
					hb_wrapper_btnEnterGame.setPadding(new Insets(10,0,0,60));
				    vb_wrapper_gameList.setPadding(new Insets(0,20,0,0));
				    
				    
				    vb_wrapper_gameList.getChildren().addAll(sl.getListView(), hb_wrapper_gameListControlBtns);
				    /*______________________________________________________________________________________________*/					  
					
				    
				    
				    
					//Chat und ChatControls gruppieren
					VBox vb_wrapper_chat = new VBox();
					
					/*______________________________________________________________________________________________*/				
					HBox hb_wrapper_chatControls = new HBox();
					HBox hb_wrapper_btnSendChatMsg = new HBox();
					HBox hb_wrapper_tfMessage = new HBox();
					
					hb_wrapper_tfMessage.getChildren().add(tf_message);
					hb_wrapper_btnSendChatMsg.getChildren().add(btn_sendChatMsg);

					hb_wrapper_chatControls.getChildren().addAll(hb_wrapper_tfMessage, hb_wrapper_btnSendChatMsg);
						
					hb_wrapper_btnSendChatMsg.setPadding(new Insets(0,0,0,10));
					hb_wrapper_chatControls.setPadding(new Insets(10,0,0,0));   
					
					vb_wrapper_chat.getChildren().addAll(chatWindow, hb_wrapper_chatControls);
					/*______________________________________________________________________________________________*/		
					
					
					
					/*Lowerhalf zusammenf�hren*/
					hb_wrapper_lowerhalf.getChildren().addAll(vb_wrapper_gameList, vb_wrapper_chat);
				    /*______________________________________________________________________________________________*/				    
				    /************************************************************************************************/				    


					
					/*Upper und Lowerhalf zusammenf�hren*/
					vb_wrapperContent.getChildren().addAll(hb_wrapper_upperhalf,hb_wrapper_lowerhalf);
					vb_wrapperContent.setPadding(new Insets(10,75,10,75));
				    /*______________________________________________________________________________________________*/				    
				    /************************************************************************************************/				    

					
					
				  
					//Konfiguration Root-Node		
					root.setTop(hb_custom_menue);
					root.setCenter(vb_wrapperContent);
					root.getStyleClass().add("bg_lobby");
				

					
					
				    /**
					  * @author kab: Fenster via Drag und Drop verschieben
					  */
					/*______________________________________________________________________________________________*/				    
				    Delta dragDelta = new Delta();
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
				    /*______________________________________________________________________________________________*/				    

	    
	    UpdateLobby toUpdate = new UpdateLobby();
	    
	    try {
	    	sl.getPlayer_OS().getOut().writeObject(toUpdate);
	    	sl.getPlayer_OS().getOut().flush();
			//model.out.writeObject(toUpdate);
			//model.out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	   

		this.scene = new Scene (root, 1400,800);
		scene.getStylesheets().add(getClass().getResource("/stylesheets/style_clientStart.css").toExternalForm());
	
	    stage.initStyle(StageStyle.TRANSPARENT);     
        return scene;
        
      		
	}
	
	
	// Fï¿½r Drag und Drop verschiebung: relative x und y Position herausfinden
		class Delta { double x, y; }

}