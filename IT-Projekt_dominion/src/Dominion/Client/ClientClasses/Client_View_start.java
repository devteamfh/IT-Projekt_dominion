package Dominion.Client.ClientClasses;

import Dominion.Client.abstractClasses.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Client_View_start extends View<Client_Model> {
			Button btn_connect;
			Button btn_register;
			Button btn_login;
			
			TextField tf_port;
			TextField tf_ip;
			TextField tf_userName;
			TextField tf_password;
			
			static Label connectionResult = new Label();
		    
			Scene scene;
		
			public Client_View_start(Stage stage, Client_Model model) {
		        super(stage, model);
		        stage.setTitle("Dominion");
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
		    
		    btn_connect = new Button ("Verbinden");
		    btn_register = new Button ("Registrieren");
		    btn_login  = new Button ("Einloggen");
		    
		 
		    HBox hb_wrapperBtns = new HBox();
		    HBox hb_wrapperServerIP = new HBox();
		    VBox vb_wrapperContent = new VBox();
		   
		    GridPane gp_wrapper = new GridPane();
	
			gp_wrapper.add(lbl_IPAdress, 0, 0); 
			hb_wrapperServerIP.getChildren().add(tf_ip);
			gp_wrapper.add(hb_wrapperServerIP, 1, 0); 
			hb_wrapperServerIP.setPadding(new Insets(0,0,7,0));
	
			gp_wrapper.add(lbl_port, 0, 1);
			gp_wrapper.add(tf_port, 1, 1); 
	
	
			gp_wrapper.add(lbl_userName, 2, 1);
			gp_wrapper.add(tf_userName,  3, 1); 
			lbl_userName.setPadding(new Insets(0,0,0,20));
			tf_userName.setAlignment(Pos.CENTER_RIGHT);
			 
			 
			gp_wrapper.add(lbl_password, 4, 1);
			gp_wrapper.add(tf_password,  5, 1); 
			lbl_password.setPadding(new Insets(0,0,0,20));
			tf_password.setAlignment(Pos.CENTER_RIGHT);

			     
			btn_connect.setPrefSize(206, 20);
			btn_register.setPrefSize(212, 20);
			btn_login.setPrefSize(210, 20);
		     
			HBox hb_wrapperBtnRegister = new HBox();
		    
			hb_wrapperBtnRegister.getChildren().add(btn_register);
			hb_wrapperBtnRegister.setPadding(new Insets(0,20,0,20));
			
			
		    hb_wrapperBtns.getChildren().addAll(btn_connect,hb_wrapperBtnRegister,btn_login);
		    hb_wrapperBtns.setPadding(new Insets(10,0,0,0));
		    
		    
		   
		   
		    vb_wrapperContent.getChildren().addAll(gp_wrapper,hb_wrapperBtns); 
			vb_wrapperContent.setPadding(new Insets(200,30,0,30));
		    
		    BorderPane root = new BorderPane();
			root.setPrefWidth(735);
			root.setPrefHeight(400);

<<<<<<< HEAD
			root.setCenter(vb_wrapperContent);
			
=======
		//test kab  <- this line can be deleted
		//test kab  <- this line can be deleted line 2
		// test joel
		this.scene = new Scene (root);
>>>>>>> copyOfMaster_17.10.2017

	
			this.scene = new Scene (root);
			scene.getStylesheets().add(getClass().getResource("style_clientStart.css").toExternalForm());
			        return scene;
		}

		}
