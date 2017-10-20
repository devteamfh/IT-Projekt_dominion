package Dominion.Client.ClientClasses;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;



public class test extends Application {

		  public static void main(String[] args) {
			  Application.launch(args);

		  }

		  @Override
		  public void start(Stage primaryStage) {
		    primaryStage.setTitle("BorderPane Test");
		    
		    //Creating StackPane
		    StackPane sp = new StackPane();
		   
		    Pane pane1 = new Pane();
		    Pane pane2 = new Pane();
		    Pane pane3 = new Pane();
		    Pane pane4 = new Pane();
		    Pane pane5 = new Pane();
		    

		    sp.getChildren().add(pane1);
		    sp.getChildren().add(pane2);
		    sp.getChildren().add(pane3);
		    sp.getChildren().add(pane4);
		    sp.getChildren().add(pane5);
		   
			pane1.getStyleClass().addAll("corner","cr_pane1");
			pane2.getStyleClass().addAll("corner","cr_pane2");
			pane3.getStyleClass().addAll("corner","cr_pane3");
			pane4.getStyleClass().addAll("corner","cr_pane4");
			pane5.getStyleClass().addAll("cr_pane5");


		    Scene scene = new Scene(sp,300,200);
			scene.getStylesheets().add(getClass().getResource("style_clientStart.css").toExternalForm());
		    primaryStage.setScene(scene);
		    
		    primaryStage.show();
		  }
		}
