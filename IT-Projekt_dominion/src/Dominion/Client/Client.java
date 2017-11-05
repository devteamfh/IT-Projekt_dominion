package Dominion.Client;

import Dominion.Client.ClientClasses.Client_Controller_start;
import Dominion.Client.ClientClasses.Client_Model;
import Dominion.Client.ClientClasses.Client_View_start;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Joel Henz
 */
public class Client extends Application {
	
	private Client_View_start view;
	
	public static void main(String[] args) {		
		launch (args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		Client_Model model = new Client_Model();
        view = new Client_View_start(stage, model);
        new Client_Controller_start(model, view);

        view.start();
       
	}
}
