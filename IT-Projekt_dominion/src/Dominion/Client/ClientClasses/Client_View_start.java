package Dominion.Client.ClientClasses;

import Dominion.Client.abstractClasses.View;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
 * @author Brad Richards (MVC Pattern)
 */

/**
 * @author: Initial: Joel (GUI without styling)
 * @author: Styling und Anordnung: kab
 * 
 * 			Aufbau: root--vb_wrapperContent<---hb_custom_menue (Schliessen Button) 
 *                                        ^----gp_wrapper      (Grid Pane mit Labels, Textfeldern, und Buttons)
 *                                        
 */

public class Client_View_start extends View<Client_Model> {
			customButton btn_connect;
			customButton btn_register;
			customButton btn_login;
			Button btn_close;
			
			TextField tf_port;
			TextField tf_ip;
			TextField tf_userName;
			TextField tf_password;
			
			static Label connectionResult = new Label();
		    
			Scene scene;
		
			public Client_View_start(Stage stage, Client_Model model) {
		        super(stage, model);
		        stage.setTitle("Dominion");
		        stage.setResizable(false);
		    }

	@Override
	protected Scene create_GUI() { 
	    
			Label lbl_IPAdress = new Label ("Server-IP:  ");
		    Label lbl_port     = new Label ("Port:       ");
		    Label lbl_userName = new Label ("Ihr Name:   ");
		    Label lbl_password = new Label ("Passwort:   ");
		       
		    tf_ip = new TextField("127.0.0.1");  
		    tf_port = new TextField("55"); 
		    tf_userName = new TextField();
		    tf_password = new TextField();
		    
		    lbl_IPAdress.getStyleClass().add("lbl_tfDescription");
		    lbl_port.getStyleClass().add("lbl_tfDescription");
		    lbl_userName.getStyleClass().add("lbl_tfDescription");
		    lbl_password.getStyleClass().add("lbl_tfDescription");
		    
		    btn_connect = new customButton ("Verbinden");
		    btn_register = new customButton ("Registrieren");
		    btn_login  = new customButton ("Einloggen");
		    
		    btn_connect.setBtnTextEmpty(btn_connect);
		    btn_register.setBtnTextEmpty(btn_register);
		    btn_login.setBtnTextEmpty(btn_login);

			btn_connect.getStyleClass().addAll("btn","btn_view");
			btn_register.getStyleClass().addAll("btn","btn_view");
			btn_login.getStyleClass().addAll("btn","btn_view");
			
			//Primï¿½re Panes initialisieren
		    VBox vb_wrapperContent = new VBox();		    
		    GridPane gp_wrapper = new GridPane();
		    
		    //Vbox fï¿½r Abstand zwischen tf IP-Adresse und tf Port
		    VBox vb_spacer = new VBox();			
			vb_spacer.getChildren().add(tf_ip);
			vb_spacer.setPadding(new Insets(0,0,7,0));
			
			//Wrapper mit lbls und tf befï¿½llen
			gp_wrapper.add(lbl_IPAdress, 0, 0); 
			gp_wrapper.add(lbl_port,     0, 1);
			gp_wrapper.add(lbl_userName, 2, 1);
			gp_wrapper.add(lbl_password, 4, 1);
			
			gp_wrapper.add(vb_spacer, 1, 0);
			gp_wrapper.add(tf_port,      1, 1); 
			gp_wrapper.add(tf_userName,  3, 1); 
			gp_wrapper.add(tf_password,  5, 1); 
			
			lbl_userName.setPadding(new Insets(0,0,0,20));
			lbl_password.setPadding(new Insets(0,0,0,20));
			
			
			//Buttons: Abstände festlegen und einfügen    
			btn_connect.setPrefSize(206, 54);
			btn_register.setPrefSize(206,54);
			btn_login.setPrefSize(206, 54);
		    
			HBox hb_BtnConnect = new HBox();
			HBox hb_BtnRegister = new HBox();
			HBox hb_BtnLogin = new HBox();

			hb_BtnConnect.getChildren().addAll(btn_connect);
			hb_BtnRegister.getChildren().add(btn_register);
			hb_BtnLogin.getChildren().add(btn_login);
			
			hb_BtnConnect.setPadding(new Insets(20,0,0,0));
			hb_BtnRegister.setPadding(new Insets(20,0,0,0));
			hb_BtnLogin.setPadding(new Insets(20,0,0,0));
			
			gp_wrapper.add(hb_BtnConnect, 1, 2);
			gp_wrapper.add(hb_BtnRegister, 3, 2);
			gp_wrapper.add(hb_BtnLogin, 5, 2);
			
			

		    //Close_Button top right, wrapper hb_custom menue
		    HBox hb_custom_menue = new HBox();
		      
		   	btn_close = new Button();
			btn_close.setPrefSize(35, 33);
		   	btn_close.getStyleClass().addAll("btn","btn_close_normal");
		   	btn_close.setAlignment(Pos.TOP_RIGHT);
		   	
		    HBox spacer =  new HBox();
		   	HBox.setHgrow(spacer, Priority.ALWAYS);
		   	
		   	hb_custom_menue.getChildren().addAll(spacer, btn_close);
		   	hb_custom_menue.setPadding(new Insets(20,15,0,0));		   	
		   	
		   	
		   	//Konfiguration Root-Node
		    BorderPane root = new BorderPane();
			root.getStyleClass().add("bg_login");
			
		    root.setPrefWidth(1200);
			root.setPrefHeight(600);
			
			root.setCenter(vb_wrapperContent);
 	

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

		    
		    
		   	//Alle Nodes Gruppieren
		    vb_wrapperContent.getChildren().addAll(hb_custom_menue, gp_wrapper); 
			gp_wrapper.setPadding(new Insets(280,50,0,150));
		    
		  
			//Scene erstellen und root, css und stagestyle zuordnen
			this.scene = new Scene (root);
			scene.getStylesheets().add(getClass().getResource("style_clientStart.css").toExternalForm());
		    stage.initStyle(StageStyle.TRANSPARENT); 
		    
			return scene;

	}
	
	// Fï¿½r Drag und Drop verschiebung: relative x und y Position herausfinden
	class Delta { double x, y; }

		}


