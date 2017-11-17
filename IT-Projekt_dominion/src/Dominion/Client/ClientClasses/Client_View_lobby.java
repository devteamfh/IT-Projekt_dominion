package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.util.ArrayList;

import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.View;
import Dominion.appClasses.StartInformation;
import Dominion.appClasses.UpdateLobby;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC Pattern), Joel Henz (GUI)
 */
public class Client_View_lobby extends View<Client_Model> {
    Button btn_send;
    Button btn_profile;
    Button btn_newGame;
    Button btn_enterGame;
    Button btn_close;
    
    TextField tf_message;

    TextArea ta;
    Scene scene;
    ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
    String PlayerName;
    Label name;
    Label yourSign;

	public Client_View_lobby(Stage stage, Client_Model model) {
		super(stage, model);
        stage.setTitle("Dominion");
    }
	
	/**
     * @author Joel Henz
     */
	protected Scene create_GUI() {

	    ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();  
	    sl.setListView();
	    	
		//resources

		btn_send = new Button("senden");
		btn_profile = new Button ("Profil");
		btn_newGame = new Button ("neues Spiel");
		btn_enterGame = new Button ("Spiel beitreten");
		btn_enterGame.setDisable(true);
		
		
		tf_message = new TextField();
		name = new Label();
		name.setText("Ihr Name: "+model.getName());
		
		Label message = new Label();
		message.setText("Ihre Nachricht");
		ta = sl.getTextAreaLobby();
		ta.setEditable(false);
		ta.setMaxWidth(500);
		ta.setMaxHeight(200);
		
		//Primäre Nodes initialisieren		
				BorderPane root = new BorderPane();
				
				 //X Button top right, wrapper hb_custom menue
			    HBox hb_custom_menue = new HBox();
			      
			   	btn_close = new Button();
				btn_close.setPrefSize(35, 33);
			   	btn_close.getStyleClass().addAll("btn","btn_close_normal");
			   	btn_close.setAlignment(Pos.TOP_RIGHT);
			   	
			    HBox spacer =  new HBox();
			   	HBox.setHgrow(spacer, Priority.ALWAYS);
			   	
			   	hb_custom_menue.getChildren().addAll(spacer, btn_close);
			   	hb_custom_menue.setPadding(new Insets(20,15,0,0));		   	
			   	//************************************************************/
			   	
			   	//vbox als wrapper welcher in border pane center kommt
			    VBox vb_wrapperContent = new VBox();
				
			    
			    	//obere hälfte von vb_wrapperContent ( borderpane center )
				    HBox hb_wrapper_upperhalf = new HBox();
					hb_wrapper_upperhalf.setAlignment(Pos.TOP_LEFT);
					
					
				    //Inhalt upperhalf (TableList)
					//***********************************//
					//table kreieren//
					TableView <StartInformation> tablePlayers = new TableView<>();
					sl.getListView_StartInformation();
					
					TableColumn column00 = new TableColumn("User");
					column00.setMinWidth(50);
					column00.setCellValueFactory(new PropertyValueFactory<>("User"));
					
					TableColumn column01 = new TableColumn("pw");
					column01.setMinWidth(100);
					column01.setCellValueFactory(new PropertyValueFactory<>("pw"));
					
					tablePlayers.getColumns().addAll(column00,column01);
					
				    hb_wrapper_upperhalf.getChildren().addAll(tablePlayers, btn_profile,sl.getListView_StartInformation());
					sl.getListView_StartInformation().setPrefSize(600,200);
				    
					//abstand zwischen upper und lower-half
					hb_wrapper_upperhalf.setPadding(new Insets(0,0,30,0));
				    
					
					
					
					
				    
				    //Inhalt lowerhalf(gameList und Chatbox)
					//***********************************************//
					
				    HBox hb_wrapper_lowerhalf = new HBox();
				
				    //gameList und Controll Buttons gruppieren
				    VBox vb_wrapper_gameList = new VBox();
				    
				    HBox hb_wrapper_gameList_ControlBtns = new HBox();
				    hb_wrapper_gameList_ControlBtns.getChildren().addAll(btn_newGame,btn_enterGame);
				    
				    vb_wrapper_gameList.getChildren().addAll(sl.getListView(), hb_wrapper_gameList_ControlBtns);
			  
					sl.getListView().setPrefSize(600,200);
					
					
					//Chat Wrapper
					VBox vb_wrapper_chat = new VBox();
					HBox hb_wrapper_chat_control = new HBox();
					hb_wrapper_chat_control.getChildren().addAll(tf_message, btn_send);
					hb_wrapper_chat_control.setPadding(new Insets(10,10,0,0));
					    
					vb_wrapper_chat.getChildren().addAll(ta, hb_wrapper_chat_control);
					
					hb_wrapper_lowerhalf.getChildren().addAll(vb_wrapper_gameList, vb_wrapper_chat);
				   
					   

					//nodes zusammenbringen
					vb_wrapperContent.getChildren().addAll(hb_wrapper_upperhalf,hb_wrapper_lowerhalf);
					vb_wrapperContent.setPadding(new Insets(10,75,10,75));
					
					
					
					

				    
				  
				//Konfiguration Root-Node		
				root.setTop(hb_custom_menue);
				root.setCenter(vb_wrapperContent);
				root.getStyleClass().add("background_lobby");
		this.scene = new Scene (root, 800,800);

        return scene;
	}

}