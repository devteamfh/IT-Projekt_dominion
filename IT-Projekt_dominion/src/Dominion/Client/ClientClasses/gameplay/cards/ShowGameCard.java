package Dominion.Client.ClientClasses.gameplay.cards;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ShowGameCard {

	public ShowGameCard(GameCard gc) {
		// TODO Auto-generated constructor stub
	
	
	
	 HBox hb = new HBox();
     hb.setPadding(new Insets(20,20,20,20));
     
     Pane p_wrapper_card = new Pane();
     p_wrapper_card.setPrefSize(330,527);
     
     hb.getChildren().add(p_wrapper_card);
     
     hb.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
     
     ImageView imgView = new ImageView();
     Image img = new Image(getClass().getResource("/img/cards/big/"+gc.lbl_cardName.getText()+".png").toExternalForm());
     imgView.setImage(img);
     imgView.setStyle("	-fx-effect: dropshadow( gaussian , black , 20 , 0.7 , 0 , 0 );");
     
     p_wrapper_card.getChildren().add(imgView);
     
     
     Stage stage = new Stage ();
     Scene scene = new Scene(hb,340,547);
     scene.setFill(null);
    
     stage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent e1) { 
			stage.close();
			} 
			});
     
     stage.setScene(scene);
	    stage.initStyle(StageStyle.TRANSPARENT);   

     stage.show();
}
	
	
	

}
