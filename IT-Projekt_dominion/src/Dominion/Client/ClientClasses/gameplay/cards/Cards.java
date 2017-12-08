package Dominion.Client.ClientClasses.gameplay.cards;

import java.util.ArrayList;
import java.util.LinkedList;

import javafx.scene.control.Label;
/**
 * @author kab: instanziert Spielkarten
 * 
 */
public class Cards {
	
	ArrayList<GameCard> al_allCards = new ArrayList<GameCard>();
	ArrayList<GameCard> al_communityActionCards = new ArrayList<GameCard>();
								
								//ActionCard Attributes
								//int costs, int adtnlActions, int adtnlBuys, int adtnlMoneyToSpend) 
	
	//ActionCard adventure	  = new ActionCard(new Label("adventure"),6,0,0,0);
	ActionCard basement  	  = new ActionCard(new Label("basement"),2,1,0,0);
	//ActionCard bureaucrat	  = new ActionCard(new Label("bureaucrat"),4,0,0,0); //draw 1silber
	//ActionCard cardback     = new ActionCard(new Label("cardback"),6,0,0,0);
	//ActionCard chancellor	  = new ActionCard(new Label("chancellor"),3,0,0,2);
	ActionCard  chapel 	      = new ActionCard(new Label("chapel"),2,0,0,0);
	//ActionCard councilmeeting = new ActionCard(new Label("councilmeeting"),5,1,0,0); //draw 4 cards, mitspieler draw 1 card
	//ActionCard curse		  = new ActionCard(new Label("curse"),0,0,0,0);  //curser -1 matchpoint
	//ActionCard duchy	      = new ActionCard(new Label("duchy"),6,0,0,0);
	//ActionCard estate		  = new ActionCard(new Label("estate"),6,0,0,0);
	//ActionCard feast	      = new ActionCard(new Label("feast"),4,0,0,0); //pick card that costs up to 5
	ActionCard forge		  = new ActionCard(new Label("forge"),4,0,0,0); // +3 karten
	ActionCard funfair		  = new ActionCard(new Label("funfair"),5,2,1,2);
	//ActionCard gardens      = new ActionCard(new Label("gardens"),4,0,0,0);  //pro 10 karten 1 match pint
	ActionCard laboratory	  = new ActionCard(new Label("laboratory"),5,1,0,0); // +2 Karten
	//ActionCard library	  = new ActionCard(new Label("library"),5,0,0,0);  //ziehen bis 7 karten
	ActionCard lumberjack 	  = new ActionCard(new Label("lumberjack"),3,1,0,2);
	ActionCard market		  = new ActionCard(new Label("market"),5,1,1,1);  //plus 1 karte
	ActionCard militia 		  = new ActionCard(new Label("militia"),4,0,0,2); //jeder legt karten ab bis nur noch 3 auf der hand
	ActionCard mine			  = new ActionCard(new Label("mine"),5,0,0,0); // entsorge eine geldkarte aus der hand, nimm eine geldkarte die bis zu 4 mehr kostet auf die hand
	ActionCard moat			  = new ActionCard(new Label("moat"),2,0,0,0); //+2 Karten angriff abwehren
	ActionCard moneylender	  = new ActionCard(new Label("moneylender"),4,0,0,0); //ewnn kupfer entwoftg wird +3
	ActionCard rebuilding	  = new ActionCard(new Label("rebuilding"),4,0,0,0); //entsorgen und eine nehmen die bis zu 2 mehr kostet
	//ActionCard spy		  = new ActionCard(new Label("spy"),4,1,0,0); //jeder spieler muss die oberte karte von seinem nachziehstapel aufdekcne.....
	//ActionCard thief		  = new ActionCard(new Label("thief"),4,0,0,0); //...
	//ActionCard throneroom   = new ActionCard(new Label("throneroom"),4,0,0,0); //..
	ActionCard village		  = new ActionCard(new Label("village"),3,2,0,0); //+1 karte
	//ActionCard waste        = new ActionCard(new Label("waste"),6,0,0,0);
	ActionCard witch 		  = new ActionCard(new Label("witch"),5,0,0,0); //+2 karten, jeder eine fluchkarte
	ActionCard workshop 	  = new ActionCard(new Label("workshop"),3,0,0,0); //+1 karte die bis zu vier mehr kostet
	
	
	public Cards() {         
		wrapCards();
		}


		public void wrapCards(){
			al_allCards.add(basement);
			al_allCards.add(chapel);
			al_allCards.add(forge);
			al_allCards.add(funfair);
			al_allCards.add(laboratory);
			al_allCards.add(lumberjack);
			al_allCards.add(market);
			al_allCards.add(militia);
			al_allCards.add(mine);
			al_allCards.add(moat);
			al_allCards.add(moneylender);
			al_allCards.add(rebuilding);
			al_allCards.add(village);
			al_allCards.add(witch);
			al_allCards.add(workshop);
		}
		
		public ArrayList<GameCard> getAl_allCards(){
			return al_allCards;
		}
		

		//hier sind die 10 community ActioncArdsd drin, welche random generiert werden sollen
		public ArrayList<GameCard> getCommunityActionCards(){
		for (int i = 0; i<10; i++){
		this.al_communityActionCards.add(al_allCards.get(i));
		}
		
		return al_communityActionCards;
				
			
		}
		
		
		
		
		
	}
