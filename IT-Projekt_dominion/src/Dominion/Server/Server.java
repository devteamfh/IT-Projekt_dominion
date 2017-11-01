package Dominion.Server;

import Dominion.Server.ServerClasses.Server_Controller;
import Dominion.Server.ServerClasses.Server_Model;
import Dominion.Server.ServerClasses.Server_View;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Joel Henz
 */
public class Server extends Application{
	
	private Server_View view;
	
	public static void main(String[] args) {
		launch (args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Server_Model model = new Server_Model();
        view = new Server_View(stage, model);
        new Server_Controller(model, view);

        view.start();
        
	}

	

}

