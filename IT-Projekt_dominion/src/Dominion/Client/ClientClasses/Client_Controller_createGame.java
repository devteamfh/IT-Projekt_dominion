package Dominion.Client.ClientClasses;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Iterator;

import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.Controller;
import Dominion.appClasses.ChatMessageLobby;
import Dominion.appClasses.GameObject;
import Dominion.appClasses.GameParty;
import Dominion.appClasses.Player;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC), Joel Henz (events)
 */
public class Client_Controller_createGame extends Controller<Client_Model, Client_View_createGame> {
    ServiceLocatorClient sl;
    Client_View_createGame view_createGame;
       
    /**
     * @author Joel Henz
     * */
    public Client_Controller_createGame(Client_Model model, Client_View_createGame view) {
        super(model, view);
        this.view_createGame = view;
        sl = ServiceLocatorClient.getServiceLocator();
        
        /**
         * @author Joel Henz
         * register ourselves to handle window-closing event
         * interrupts all client threads and closes all client sockets
         * */
        view.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
            	model.t1.interrupt();
               
                try {
					model.client.close();				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              
                Platform.exit();
            }
        }); 
        
       
        //ToggleGroup Change listener
        sl.getToggleForNumberOfPlayers().selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle toggle, Toggle new_toggle) {
            	
            	((Node) toggle).getStyleClass().remove("btnRdo_active");
             	((Node) new_toggle).getStyleClass().add("btnRdo_active");

              	}
        });

        sl.getToggleForEndOfGame().selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle toggle, Toggle new_toggle) {

                ((Node) toggle).getStyleClass().remove("btnRdo_active");
                ((Node) new_toggle).getStyleClass().add("btnRdo_active");

            }
        });



        //Button btn_iRundenPLUS
        view.btn_iRundenPLUS.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {

                        view.btn_iRundenPLUS.getStyleClass().addAll("btn_close_hover");
                        view.btn_iRundenPLUS.getStyleClass().remove("btn_close_normal");

                        //increase label iRunden
                        String str_iRunden = view.lbl_iRunden.getText();
                        int int_iRunden = Integer.parseInt(str_iRunden);
                            if (int_iRunden < 50) {
                                int_iRunden = int_iRunden + 1;
                                view.lbl_iRunden.setText(String.valueOf(int_iRunden));
                            }
                    }
                });

        //Button btn_iRundenMINUS
        view.btn_iRundenMINUS.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        view.btn_iRundenMINUS.getStyleClass().addAll("btn_close_hover");
                        view.btn_iRundenMINUS.getStyleClass().remove("btn_close_normal");

                        //decrease label iRunden

                        String str_iRunden = view.lbl_iRunden.getText();
                        int int_iRunden = Integer.parseInt(str_iRunden);
                            if (int_iRunden > 10) {
                                int_iRunden = int_iRunden - 1;
                                view.lbl_iRunden.setText(String.valueOf(int_iRunden));
                            }
                    }
                });
        
        
        
       /*
        
        view.btnRdo_twoPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	if (view.btnRdo_twoPlayer.equals(sl.getToggleForNumberOfPlayers().getSelectedToggle())) {
            	sl.setNumberOfPlayer(2);
            	view.btnRdo_twoPlayer.getStyleClass().add("btnRdo_active");
            	}
            }
        });
        
        view.btnRdo_threePlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if (view.btnRdo_threePlayer.equals(sl.getToggleForNumberOfPlayers().getSelectedToggle())) {
            	sl.setNumberOfPlayer(3);
            	view.btnRdo_threePlayer.getStyleClass().add("btnRdo_active");
            	}
            }
        });
        
        view.btnRdo_fourPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	sl.setNumberOfPlayer(4);
            }
        });
        
        */
        
        view.btnRdo_mode1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	view.sl.getTextFieldForRounds().clear();
            	view.sl.getTextFieldForRounds().setDisable(true);
            	sl.setSelectedMode("province");
            }
        });
        
        view.btnRdo_mode2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	view.sl.getTextFieldForRounds().setDisable(false);
            	sl.setSelectedMode("rounds");
            }
        });
        
      
        
     
        
        view.btn_finish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	String selectedMode = sl.getSelectedMode();
            	int numberOfPlayers = sl.getNumberOfPlayers();
            	String creator = model.playerName;
            	
            	GameParty newParty = new GameParty(selectedMode,creator,numberOfPlayers);

            	try {
					model.out.writeObject(newParty);
					model.out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	Stage playingStage = new Stage();			
            	playingStage.initModality(Modality.APPLICATION_MODAL);
		        Client_View_playingStage view_playingStage = new Client_View_playingStage (playingStage, model,true);
		        sl.setView_playingStage(view_playingStage);
		        new Client_Controller_playingStage(model, sl.getPlayingStage(), newParty); 
		        sl.getPlayingStage().start();
		        view_createGame.stop();        			
            }
        });
        
    }

	
}