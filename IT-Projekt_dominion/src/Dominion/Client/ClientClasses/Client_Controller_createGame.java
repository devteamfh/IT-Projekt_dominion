package Dominion.Client.ClientClasses;

import java.io.IOException;

import Dominion.Client.ClientClasses.gameplay.Croupier;
import Dominion.Client.ClientClasses.gameplay.cards.Cards;
import Dominion.Client.abstractClasses.Controller;
import Dominion.appClasses.GameParty;
import Dominion.appClasses.PlayerWithoutOS;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.input.MouseEvent;


/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC), Joel Henz (events)
 */
 
public class Client_Controller_createGame extends Controller<Client_Model, Client_View_createGame> {
    private ServiceLocatorClient sl;
    private Client_View_createGame view_createGame;
    private String selectedGameMode;
    private int numberOfPlayers;
    private Croupier croupier = Croupier.getCroupier();
       
    /**
     * @author Joel Henz (lines 253 - 285)
     * @author Kab: styling
     * */
    public Client_Controller_createGame(Client_Model model, Client_View_createGame view) {
        super(model, view);
        this.view_createGame = view;
        sl = ServiceLocatorClient.getServiceLocator();
    
     // Button Close Style Hover und Action press
    	view.btn_close.addEventHandler(MouseEvent.MOUSE_ENTERED, 
      		    new EventHandler<MouseEvent>() {
      		        @Override public void handle(MouseEvent e) {
      		        	view.btn_close.getStyleClass().addAll("btn_close_hover");
      		        	view.btn_close.getStyleClass().remove("btn_close_normal");
      		        }
  		});
		      		
		      		
  		view.btn_close.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
  		        @Override public void handle(MouseEvent e) {
  		        	view.btn_close.getStyleClass().remove("btn_close_hover");
  		        	view.btn_close.getStyleClass().addAll("btn_close_normal");
  		        }
  		});
		        
        view.btn_close.setOnAction(new EventHandler<ActionEvent>() { 
            @Override
            public void handle(ActionEvent event) {
            	view.stop();
            	
            }
        });
        
        //ToggleGroup Change listener
        
        sl.getToggleForNumberOfPlayers().selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle toggle, Toggle new_toggle) {
            	
            	((Node) toggle).getStyleClass().remove( "btnRdo_active");
             	((Node) new_toggle).getStyleClass().add("btnRdo_active");

              	}
        });

        sl.getToggleForEndOfGame().selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle toggle, Toggle new_toggle) {

                ((Node) toggle).getStyleClass().remove( "btnRdo_active");
                ((Node) new_toggle).getStyleClass().add("btnRdo_active");

                if(view.btnRdo_mode2.isSelected()){
                	view.btn_iRundenPLUS.getStyleClass().remove("btn_Plus_clicked");
                	view.btn_iRundenPLUS.getStyleClass().add("btn_Plus");
                	view.btn_iRundenMINUS.getStyleClass().remove("btn_Minus_clicked");
                	view.btn_iRundenMINUS.getStyleClass().add("btn_Minus");
                }else{
                	view.btn_iRundenPLUS.getStyleClass().remove("btn_Plus");
                	view.btn_iRundenPLUS.getStyleClass().add("btn_Plus_clicked");
                	view.btn_iRundenMINUS.getStyleClass().remove("btn_Minus");
                	view.btn_iRundenMINUS.getStyleClass().add("btn_Minus_clicked");
                }
                
            }
        });

        
        

        //Buttons #Players
        numberOfPlayers = 2;
        view.btnRdo_twoPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {           	
            	numberOfPlayers = 2;
            }
        });
        
        view.btnRdo_threePlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {   	
            	numberOfPlayers = 3;
            }
        });
        
        view.btnRdo_fourPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	numberOfPlayers = 4;
            }
        });
        
       
        
        
        //Buttons Game Mode
        selectedGameMode = "Provinzkarten";
        view.btnRdo_mode1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	view.sl.getTextFieldForRounds().clear();
            	view.sl.getTextFieldForRounds().setDisable(true);
            	selectedGameMode = "Provinzkarten";
            }
        });
        
        view.btnRdo_mode2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	view.sl.getTextFieldForRounds().setDisable(false);
            	selectedGameMode = "Rundenanzahl";
        	
            }
        });
        
         
      
        //Button btn_iRundenPLUS
        view.btn_iRundenPLUS.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {

                        view.btn_iRundenPLUS.getStyleClass().addAll("btn_Plus_clicked");
                        //view.btn_iRundenPLUS.getStyleClass().remove("btn_close_normal");

                        //increase label iRunden
                        if(view.btnRdo_mode2.isSelected()){
                        String str_iRunden = view.lbl_iRunden.getText();
                        int int_iRunden = Integer.parseInt(str_iRunden);
                            if (int_iRunden < 50) {
                                int_iRunden = int_iRunden + 1;
                                view.lbl_iRunden.setText(String.valueOf(int_iRunden));
                            }
                        }
                    }
                });
        
        //Button btn_iRundenPLUS
        view.btn_iRundenPLUS.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {

                        view.btn_iRundenPLUS.getStyleClass().remove("btn_Plus_clicked");
                      
                    }
                });

        //Button btn_iRundenMINUS
        view.btn_iRundenMINUS.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        view.btn_iRundenMINUS.getStyleClass().add("btn_Minus_clicked");
                        view.btn_iRundenMINUS.getStyleClass().remove("btn_Minus");

                        //decrease label iRunden
                        String str_iRunden = view.lbl_iRunden.getText();

                        if(view.btnRdo_mode2.isSelected()){
                        int int_iRunden = Integer.parseInt(str_iRunden);
                            if (int_iRunden > 10) {
                                int_iRunden = int_iRunden - 1;
                                view.lbl_iRunden.setText(String.valueOf(int_iRunden));
                            }
                        }
                    }
                });
        
        
        //Button btn_iRundenMINUS
        view.btn_iRundenMINUS.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        view.btn_iRundenMINUS.getStyleClass().remove("btn_Minus_clicked");
                        view.btn_iRundenMINUS.getStyleClass().add("btn_Minus");

                    }
                });
     
        
        
        
        
     	//Button btn_finish verhalten (create game)
        
    	view.btn_finish.addEventHandler(MouseEvent.MOUSE_ENTERED, 
    		    new EventHandler<MouseEvent>() {
    		        @Override public void handle(MouseEvent e) {
    		        	view.btn_finish.getStyleClass().addAll("btn_sendChatMsg_hover");
    		        	view.btn_finish.getStyleClass().remove("btn_sendChatMsg");
    		        }
    		});
     		
    		view.btn_finish.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
    		        @Override public void handle(MouseEvent e) {
    		        	view.btn_finish.getStyleClass().remove("btn_sendChatMsg_hover");
    		        	view.btn_finish.getStyleClass().addAll("btn_sendChatMsg");
    		        }
    		});
        
    		
        view.btn_finish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	PlayerWithoutOS creator = new PlayerWithoutOS(model.playerName);
            	
            	Cards cards = new Cards();
            	
            	//get a card set (chosen randomly among 4 sets)
            	croupier.setAl_communityActionCards(cards.getRandomCardSet());
            	
            	sl.setCroupier(croupier);
            	
            	int numberOfCardSet = cards.getNumberOfCardSet();
   	
            	GameParty newParty = new GameParty(selectedGameMode,creator,numberOfPlayers,numberOfCardSet);

            	if(newParty.withRounds()){
            		int rounds = Integer.parseInt(view.lbl_iRunden.getText());
            		newParty.setRounds(rounds);
            	}

            	try {
            		sl.getPlayer_OS().getOut().writeObject(newParty);
            		sl.getPlayer_OS().getOut().flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}     			
            }
        });
        
    }

	
}