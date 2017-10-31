package Dominion.Client;

import java.net.InetAddress;

import Dominion.Client.ClientClasses.Client_Controller_lobby;
import Dominion.Client.ClientClasses.Client_Controller_start;
import Dominion.Client.ClientClasses.Client_Model;
import Dominion.Client.ClientClasses.Client_View_lobby;
import Dominion.Client.ClientClasses.Client_View_start;
import Dominion.Client.ClientClasses.ServiceLocatorClient;
import Dominion.appClasses.GameObject;
import Dominion.appClasses.GameObject.ObjectType;
import Dominion.appClasses.Player;
import Dominion.appClasses.StartInformation;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Joel Henz
 */
public class Client extends Application {
	
	private Client_View_start view;
	private ServiceLocatorClient sl = ServiceLocatorClient.getServiceLocator();
	
	public static void main(String[] args) {		
		launch (args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		Client_Model model = new Client_Model();
        view = new Client_View_start(stage, model);
        new Client_Controller_start(model, view);

        view.start();
        
        /**InetAddress addr = InetAddress.getByName("127.0.0.1");
		int portNr = 8000;
    	//String name = view.tf_userName.getText();
    	//model.setName(name);
		
		String name = "joel";
    	model.setName(name);
		
		model.connectToServer(addr,portNr);	
		
		if(sl.getConnected()){
			model.setPlayer();
			StartInformation current = new StartInformation(model.getName());
			current.setID();
			
			GameObject obj= new GameObject (GameObject.ObjectType.StartInformation);
			obj.setID();
			
			obj = current;
			
			model.getOutput().writeObject(obj);
			model.getOutput().flush();
		}
    	
    	Stage playingStage = new Stage();				
        Client_View_lobby view2 = new Client_View_lobby(playingStage, model);
        new Client_Controller_lobby(model, view2); 
        view2.start();
        view.stop();*/
       
	}
	
	
	

}
