package Dominion.Client.ClientClasses;

import Dominion.Client.abstractClasses.View;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Client_View_start extends View<Client_Model> {
	Button connect;
	TextField port;
	TextField ip;
	TextField insertName;
	static Label connectionResult = new Label();
    Scene scene;

	public Client_View_start(Stage stage, Client_Model model) {
        super(stage, model);
        stage.setTitle("Client");
    }

	@Override
	protected Scene create_GUI() { 
	    
	    Label IPAdress = new Label ("IP Adresse:  ");
	    Label portNr = new Label ("Port:            ");
	    Label name = new Label ("Ihr Name:    ");
	    
	    ip = new TextField();
	    port = new TextField();
	    insertName = new TextField();
	    
	    connect = new Button ("verbinden");
	    
	    BorderPane root = new BorderPane();
	    HBox hb1 = new HBox();
	    HBox hb2 = new HBox();
	    HBox hb3 = new HBox();
	    
	    hb1.getChildren().addAll(IPAdress, ip);
	    hb2.getChildren().addAll(portNr, port);
	    hb3.getChildren().addAll(name,insertName);
	    
	    HBox.setMargin(IPAdress, new Insets(0, 0, 20, 0));
	    HBox.setMargin(ip, new Insets(0, 0, 20, 0));
	    
	    HBox.setMargin(portNr, new Insets(0, 0, 20, 0));
	    HBox.setMargin(port, new Insets(0, 0, 20, 0));
	    
	    
	    VBox vb = new VBox();
	    
	    vb.getChildren().addAll(hb1,hb2,hb3,connect,connectionResult);
	    VBox.setMargin(connect, new Insets(20, 0, 0, 0));
	    VBox.setMargin(connectionResult, new Insets(20, 0, 0, 0));

		root.setCenter(vb);
		root.setPrefWidth(300);

		//test kab  <- this line can be deleted
		
		
		this.scene = new Scene (root);

        return scene;
	}

}