package Dominion.Client.ClientClasses;

import java.io.IOException;

import Dominion.Client.abstractClasses.Controller;
import Dominion.appClasses.ChatMessageLobby;
import Dominion.appClasses.GameParty;
import Dominion.appClasses.JoinGameParty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC), Joel Henz (Events; where mentioned); kab (styling)
 */
public class Client_Controller_lobby extends Controller<Client_Model, Client_View_lobby> {
	private ServiceLocatorClient sl;
	private Client_View_lobby view;
	private Client_View_createGame view2;
	private GameParty joinGame;
       
    public Client_Controller_lobby(Client_Model model, Client_View_lobby view) {
        super(model, view);
        this.view = view;
        sl = ServiceLocatorClient.getServiceLocator();
      
        
        
        /* kab ausgeblendet da eigener close btn gemacht
        /**
         * @author Joel Henz
         * register ourselves to handle window-closing event
         * interrupts all client threads and closes all client sockets
         * *
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
        */
        
        
        
     // Button Close Style Hover und Action press (kab)
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
            
            	/**model.t1.interrupt();
                
                try {
					model.client.close();				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
              
                System.exit(0);
            }
            });
        
        
        
        
        
        
        
        /**
         * @author Joel Henz:
         * action event for the chat program (sending the message by clicking on "Senden" button)
         * */
        view.btn_sendChatMsg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                sendChatMessageToServer();
            }
        });
        
       	//Button enterGame verhalten
    	view.btn_sendChatMsg.addEventHandler(MouseEvent.MOUSE_ENTERED, 
    		    new EventHandler<MouseEvent>() {
    		        @Override public void handle(MouseEvent e) {
    		        	view.btn_sendChatMsg.getStyleClass().addAll("btn_sendChatMsg_hover");
    		        	view.btn_sendChatMsg.getStyleClass().remove("btn_sendChatMsg");
    		        }
    		});
     		
    		view.btn_sendChatMsg.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
    		        @Override public void handle(MouseEvent e) {
    		        	view.btn_sendChatMsg.getStyleClass().remove("btn_sendChatMsg_hover");
    		        	view.btn_sendChatMsg.getStyleClass().addAll("btn_sendChatMsg");
    		        }
    		});
        
       
        
        /**
         * @author Joel Henz
         * key event for the chat program (sending the chat message by pressing enter)
         * */
        view.scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	if (event.getCode().equals(KeyCode.ENTER)){
            		sendChatMessageToServer();	
            	}
            }
        }); 
        
        /**
         * @author Joel Henz
         *event for ListView mouse clicks
         * */
       sl.getListView().setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if(!sl.getListView().getSelectionModel().isEmpty()){
					view.btn_enterGame.setDisable(false);	
					joinGame = sl.getListView().getSelectionModel().getSelectedItem();
				}
				
			}
		});
       /**
        * @author Joel Henz
        * */
       view.btn_enterGame.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {

        	   view.btn_enterGame.setDisable(true);
        	   JoinGameParty gameToJoin = new JoinGameParty(joinGame,model.getName());
        	   
        	   try {
        		   sl.getPlayer_OS().getOut().reset();//in case a player joins a game, leaves and rejoins the same game party (after leaving it is possible that another player has joined game before the leaving player rejoins)
        		   sl.getPlayer_OS().getOut().writeObject(gameToJoin);
        		   sl.getPlayer_OS().getOut().flush();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           }
       });
       
       
       
		   	//Button enterGame verhalten
			view.btn_enterGame.addEventHandler(MouseEvent.MOUSE_ENTERED, 
				    new EventHandler<MouseEvent>() {
				        @Override public void handle(MouseEvent e) {
				        	view.btn_enterGame.getStyleClass().addAll("btn_view_hover");
				        	view.btn_enterGame.getStyleClass().remove("btn_view");
				        }
				});
		 		
				view.btn_enterGame.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
				        @Override public void handle(MouseEvent e) {
				        	view.btn_enterGame.getStyleClass().remove("btn_view_hover");
				        	view.btn_enterGame.getStyleClass().addAll("btn_view");
				        }
				});
        
        
        /**
         * @author Joel Henz
         *opens a new stage for creating the configs of a new game
         * */
        view.btn_newGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	Stage createNewGame = new Stage();
            	createNewGame.initModality(Modality.APPLICATION_MODAL);
            	                
		        view2 = new Client_View_createGame(createNewGame, model);
                new Client_Controller_createGame(model, view2);
                sl.setView_createGame(view2);
                sl.getCreateGameView().start();
            }
        });
        
	   	//Button enterGame verhalten
		view.btn_newGame.addEventHandler(MouseEvent.MOUSE_ENTERED, 
			    new EventHandler<MouseEvent>() {
			        @Override public void handle(MouseEvent e) {
			        	view.btn_newGame.getStyleClass().addAll("btn_view_hover");
			        	view.btn_newGame.getStyleClass().remove("btn_view");
			        }
			});
	 		
			view.btn_newGame.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			        @Override public void handle(MouseEvent e) {
			        	view.btn_newGame.getStyleClass().remove("btn_view_hover");
			        	view.btn_newGame.getStyleClass().addAll("btn_view");
			        }
			});
        
    
    
    }
       
    
    
    
    /**
     * @author Joel Henz: 
     * method for sending the chat messages
     * */
	protected void sendChatMessageToServer() {
		String name = model.getName();
		String msg = view.tf_message.getText();
		ChatMessageLobby cmsg = new ChatMessageLobby(name, msg);
		
		try {
			
 		   	sl.getPlayer_OS().getOut().writeObject(cmsg);
 		   	sl.getPlayer_OS().getOut().flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		view.tf_message.clear();
		view.tf_message.requestFocus();	
	}
	
}
