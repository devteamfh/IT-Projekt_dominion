package Dominion.Client.ClientClasses.gameplay.cards;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javafx.scene.control.Label;
/**
 * @author kab: instanziert Spielkarten
 * 
 */
public class Cards {
	
	ArrayList<GameCard> al_allCards = new ArrayList<GameCard>();
	ArrayList<GameCard> al_communityActionCards = new ArrayList<GameCard>();
	ArrayList<GameCard> cardSet1 = new ArrayList<GameCard>();
	ArrayList<GameCard> cardSet2 = new ArrayList<GameCard>();
	ArrayList<GameCard> cardSet3 = new ArrayList<GameCard>();
	ArrayList<GameCard> cardSet4 = new ArrayList<GameCard>();
	
	int numberOfCardSet;
	
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
		setCardSet(1);
		setCardSet(2);
		setCardSet(3);
		setCardSet(4);
		}


		public void wrapCards(){
			al_allCards.add(basement); //keller 2; +1 aktion und beliebig viele karten von der hand austauschen (discard)
			al_allCards.add(chapel); //kapelle 2; bis zu 4 karten auf den müll werfen --> button "wegwerfen" noch machen
			al_allCards.add(moat);//burggraben 2 (reaktion auf angriff); +2 karten und angriff abwehren sobald ein angriff erfolgt -->client liest vom sever z.b. dass ein gegenspieler hexe gespielt hat...nun wird burggraben aktiv falls auf der hand
			
			al_allCards.add(lumberjack);//holzfäller 3; +1kauf und +2 treasure
			al_allCards.add(village); //dorf 3; +1 karte und +2 aktionen
			al_allCards.add(workshop);//werkstatt 3; eine karte nehmen, die BIS ZU 4 kostet --> alle karten werden aktiviert in der aktionsphase
			
			
			al_allCards.add(forge); //schmiede 4; +3 karten
			al_allCards.add(rebuilding);//umbau 4; beliebige karte aus der hand auf den müll werfen. man kann dann eine karten erwerben, die BIS ZU 2 treasure mehr kostet als die weggeworfene karte
			al_allCards.add(moneylender);//geldverleiher 4; kupfer entsorgen, man kriegt +3 treasure
			al_allCards.add(militia);//miliz 4 (angriff); +2 treasure und jeder spieler muss solange karten ablegen (auf stapel gespielte karten) bis er nur noch 3 hand-karten hat
						
			al_allCards.add(funfair); //jahrmarkt 5; +2 aktionen und +1kauf und +2 treasure
			al_allCards.add(laboratory); //labor 5; +2 karten und +1aktion		
			al_allCards.add(market);//markt 5; +1aktion und +1kauf und +1karte und +1treasure		
			al_allCards.add(mine); //mine 5; geldkarte entsorgen: nimm eine geldkarte, die BIS ZU 3 treasure mehr kostet als die trashed geldkarte. man kann die geldkarte sofort in die hand nehmen
			al_allCards.add(witch);//hexe 5 (angriff); +2 karten und jeder gegenspieler muss eine fluchkarte nehmen
			
		}
		
		public ArrayList<GameCard> getAl_allCards(){
			return al_allCards;
		}
		

		//hier sind die 10 community ActioncArdsd drin, welche random generiert werden sollen
		/**public ArrayList<GameCard> getCommunityActionCards(){
		for (int i = 0; i<10; i++){
		this.al_communityActionCards.add(al_allCards.get(i));
		}
		
		return al_communityActionCards;
				
			
		}*/
		
		/**
		 * @author Joel Henz:
		 * create several Card Sets. Each GameParty reveives one of those Card Sets, chosen randomly
		 * 
		 */
		public void setCardSet (int cardSetNumber){
			switch(cardSetNumber){
			case 1://ohne angriff und abwehr
				cardSet1.add(basement);//
				cardSet1.add(chapel);
				
				cardSet1.add(lumberjack);
				
				cardSet1.add(forge);
				cardSet1.add(rebuilding);
				cardSet1.add(moneylender);
				
				cardSet1.add(funfair);
				cardSet1.add(laboratory);
				cardSet1.add(market);
				cardSet1.add(mine);
				
				break;
				
			case 2:
				cardSet2.add(chapel);
				cardSet2.add(moat);
				
				cardSet2.add(village);
				cardSet2.add(workshop);
				
				cardSet2.add(rebuilding);
				cardSet2.add(moneylender);
				cardSet2.add(militia);
				
				cardSet2.add(funfair);
				cardSet2.add(laboratory);
				cardSet2.add(witch);
				
				break;
				
			case 3:
				cardSet3.add(basement);
				cardSet3.add(moat);
				
				cardSet3.add(lumberjack);
				cardSet3.add(village);
				cardSet3.add(workshop);
				
				cardSet3.add(forge);
				cardSet3.add(militia);
				
				cardSet3.add(market);
				cardSet3.add(mine);
				cardSet3.add(witch);
				
				break;
				
			case 4:
				cardSet4.add(chapel);
				cardSet4.add(basement);
				
				cardSet4.add(lumberjack);
				cardSet4.add(village);
				cardSet4.add(workshop);
				
				cardSet4.add(forge);
				cardSet4.add(rebuilding);
				cardSet4.add(militia);
				
				cardSet4.add(market);
				cardSet4.add(witch);
				
			}
		}
		
		/**
		 * @author Joel Henz:
		 * the host will get a card set when creating a new game party
		 * 
		 */
		public ArrayList <GameCard> getRandomCardSet(){
			Random rand = new Random();
			int cardSet = rand.nextInt(4)+1;
			
			switch(cardSet){
			case 1:
				this.numberOfCardSet =1;
				return cardSet1;
				
			case 2:
				this.numberOfCardSet =2;
				return cardSet2;
				
			case 3:
				this.numberOfCardSet =3;
				return cardSet3;
				
			case 4:
				this.numberOfCardSet =4;
				return cardSet4;
				
			default:
				return null;
			}
			
			
		}
		
		/**
		 * @author Joel Henz:
		 * the number of the card set of a game party
		 */
		public int getNumberOfCardSet(){
			return this.numberOfCardSet;
		}
		
		public ArrayList <GameCard> getCardSetOfGameParty(int numberOfCardSet){
			
			switch(numberOfCardSet){
			case 1:
				return cardSet1;
				
			case 2:
				return cardSet2;
				
			case 3:
				return cardSet3;
				
			case 4:
				return cardSet4;
				
			default:
				return null;
			}
			
			
		}
		
		
		
		
		
	}
