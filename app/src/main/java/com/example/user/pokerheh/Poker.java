package com.example.user.pokerheh;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


enum Suit {
 Clubs(0), Diamonds(1), Hearts(2), Spades(3);
int sui;
	private Suit(int sui){
		this.sui=sui;
	}
}

enum Value{
two(2), three(3), four(4), five(5), six(6), seven(7), eight(8), nine(9), ten(10),
jack(11), queen(12), king(13), ace(14);

int weight;
private Value(int weight){
	this.weight=weight;
}
}
enum CombinationTitle{
	none,couple, doublecouple, set, straight, flash, fullhouse,
	foak, straightflash, royalflash;
}
class Combination implements Comparable<Combination>{
	CombinationTitle name;
	private int weight;
	private int majority;
	
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getMajority() {
		return majority;
	}
	public void setMajority(int majority) {
		this.majority = majority;
	}
	@Override
	public String toString() {
		return ""+name+" "+majority+" "+weight;
	}
	@Override
	public int compareTo(Combination o) {
		if(this.majority>o.majority){
			return -1;
		}
		if(this.majority<o.majority){
			return 1;
		}
		if(this.majority==o.majority){
			if(this.weight>o.weight) return -1;
			if(this.weight<o.weight) return 1;
		}
		return 0;
	}
}

class FTR{
	ArrayList<Card> ftr= new ArrayList<Card>(5);
	public FTR(){
	}
	public Card getcard(int num){
		return ftr.get(num);
	}
	public void ftradd(Card card){
		ftr.add(card);
	}
	public ArrayList<Card> getftr(){
		return ftr;
	}
	public int ftrsize(){
		return ftr.size();
	}
	public void clear(){
		ftr.clear();
	}
}

class Card implements Comparable<Card>{
	Suit suit;
	Value value;
	public Card(Suit suit, Value value) {
	this.suit = suit;
	this.value = value;
	}
	@Override
	public String toString() {
	return "" + suit + " | " + value;
	}
	
	@Override
	public int compareTo(Card o) {
		if(this.value.weight>o.value.weight){
			return -1;
		}
		else{
			if(this.value.weight<o.value.weight)
				return 1;
			else return 0;
		}
		
	}
}

class Deck{
	List<Card> cards= new ArrayList<Card>(52);
    Combination combination;
	public Deck(){
	super();
	reset();

	}
	private void reset() {
	cards.clear();
	for (Suit suit : Suit.values())
	for (Value value : Value.values())
	cards.add(new Card(suit, value));

	}
	@Override
	public String toString() {
	return "" + cards;
	}
	public void shuffle() {
	Collections.shuffle(cards);

	}
	public Card GetCard(){
	Card card= cards.get(0);
	cards.remove(0);
	return card;
	}
	public void clear(){
		cards.clear();
	}
}

class Hand{
    Card card1;
	Card card2;
	Combination combination;
	public Hand(Card card1, Card card2) {
		super();
		this.card1 = card1;
		this.card2 = card2;
	}
	@Override
	public String toString() {
		return card1+" , "+card2;
	}

}


class Game{
	
	int lastbet, totalbet, summbet;
	boolean end=false;
	
	public int goFurther(ArrayList<Player> players, int[] solutions){
		int playerscount=0;
		for(int i=0; i<players.size(); i++){
			if((!players.get(i).fold)&&(players.get(i).getWallet()!=0)) playerscount++;
		}
		if(playerscount<2) return 2;
		else {
			boolean done=true;
			for(int i=0; i<players.size(); i++){
				if(((players.get(i).bet!=lastbet)&&(players.get(i).getWallet()>0)&&(lastbet>0)&&(!players.get(i).fold))||(solutions[i]==-1)){
					done=false;
				}
			}
			if(done)return 1;
			else return 0;
		}
	}

	public void makeBlinds(ArrayList<Player> players, int i, int j){
		if(players.get(i).getWallet()<5){
			players.get(i).bet=players.get(i).getWallet();
			players.get(i).setWallet(i);
		}
		else{
			players.get(i).bet=5;
			players.get(i).setWallet(players.get(i).getWallet()-5);
		}
		lastbet=players.get(i).bet;

		if(players.get(j).getWallet()<10){
			players.get(j).bet=players.get(j).getWallet();
			players.get(j).setWallet(j);
			if(players.get(j).bet>lastbet) lastbet=players.get(j).bet;
		}
		else{
			players.get(j).bet=10;
			players.get(j).setWallet(players.get(j).getWallet()-10);
			lastbet=10;
		}
		totalbet=players.get(i).bet+players.get(j).bet;
	}
	
	public void endgame(ArrayList<Player> players, FTR ftr){
		for(int j=0; j<players.size(); j++){
			if(!players.get(j).fold){
				WhichCombination wc1=new WhichCombination(players.get(j).getHand().card1,players.get(j).getHand().card2,ftr.getftr());
				Combination comb=new Combination();
				players.get(j).getHand().combination=wc1.whichCombination(comb);
			}
		}
		ArrayList<Combination> finalcomb=new ArrayList<Combination>(players.size());
		for(int j=0; j<players.size(); j++ ){
			if(!players.get(j).fold) finalcomb.add(players.get(j).getHand().combination);
		}
		Collections.sort(finalcomb);
		int max=finalcomb.get(0).getMajority()*20+finalcomb.get(0).getWeight();
		int countwin=0;
		int playerscount=0;
		for(int j=0; j<players.size(); j++){
			if(!players.get(j).fold){
				if(players.get(j).getHand().combination.getMajority()*20+players.get(j).getHand().combination.getWeight()==max){
					players.get(j).Status='w';
					countwin++;
				}
				else players.get(j).Status='l';
				playerscount++;
			}
		}
		summbet=Math.round(summbet/countwin);
		boolean show=true;
		if(playerscount==1) show=false;
		for(int j=0; j<players.size(); j++){
			   if (players.get(j).Status=='w') players.get(j).setWallet(players.get(j).getWallet()+summbet);
		}
		if(show){
			for(int i=0; i<players.size(); i++){
				if(!players.get(i).fold) players.get(i).cshow=true;
			}
		}
		end=true;
	}
}


abstract class Player{
	public char Status;
	protected int wallet;
	protected Hand hand;
	public int bet=0;
	public boolean fold=false, cshow=false;
	public Player(int wallet) {
		super();
		this.wallet = wallet;
	}

	public int getWallet() {
		return wallet;
	}

	public void setWallet(int wallet) {
		this.wallet = wallet;
	}

	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}
	public abstract int AImakeBet(int lastbet, int cardnumber);
	public abstract int HumanmakeBet(int lastbet, int playersolution, int raiseby);
}

class HumanPlayer extends Player{

	public HumanPlayer(int wallet) {
		super(wallet);
	}
    @Override
	public int HumanmakeBet(int lastbet,int playersolution, int raiseby) {
       switch(playersolution){
       case 0:{
    	fold=true;
    	bet+=0;
    	return 0;
       }
       case 1:{
    	   wallet-=(lastbet-bet);
    	   bet+=lastbet-bet;
    	   return 1;
       }
       case 2:{
    	   bet+=0;
    	   return 2;
       }
       case 3:{
		   wallet+=bet;
		   wallet-=raiseby;
    	   bet=raiseby;
		   return 3;
       }
       }
       return 0;
	}


	@Override
	public int AImakeBet(int lastbet, int cardnumber) {
		return 0;
	}
	
	
}

class AIPlayer extends Player{

	public AIPlayer(int wallet) {
		super(wallet);
	}

	public int solution(int lastbet, int cardnumber){
		boolean ablecheck;
		if(bet==lastbet) ablecheck=true;
		else ablecheck=false;
		int maj=hand.combination.getMajority();
		if(cardnumber==2){
			if(maj>0){
				double k=Math.random();
				if(k>0.4) return 3;
				else{
					if(ablecheck) return 2;
					else return 1;
				}
			}
			else{
				if(ablecheck) return 2;
				else return 1;
			}
			
		}
		if(cardnumber==5){
			if(maj>6){
				double k=Math.random();
				if(k>0.6) return 3;
				else{
					if(ablecheck) return 2;
					else return 1;
				}
			}
			if((maj>=4)&&(maj<=6)){
				double k=Math.random();
				if(k>0.7) return 3;
				else{
					if(ablecheck) return 2;
					else return 1;
				}
			}
			if((maj>=2)&&(maj<=3)){
				double k=Math.random();
				if(k>0.8) return 3;
				else{
					if(ablecheck) return 2;
					else return 1;
				}
			}
			else{
				double k=Math.random();
				if(k>0.85) return 3;
				if((k>=0.4)&&(k<=0.85)){
					if(ablecheck) return 2;
					else return 1;
				}
				else{
					if(ablecheck) return 2;
					else return 0;
				}
			}
		}
		else{
			if(maj>6){
				double k=Math.random();
				if(k>0.6) return 3;
				else{
					if(ablecheck) return 2;
					else return 1;
				}
			}
			if((maj>=4)&&(maj<=6)){
				double k=Math.random();
				if(k>0.7) return 3;
				else{
					if(ablecheck) return 2;
					else return 1;
				}
			}
			if((maj>=2)&&(maj<=3)){
				double k=Math.random();
				if(k>0.8) return 3;
				else{
					if(ablecheck) return 2;
					else return 1;
				}
			}
			else{
				double k=Math.random();
				if(k>0.85) return 3;
				if((k >= 0.4) && (k <= 0.85)) {
					if(ablecheck) return 2;
					else return 1;
				}
				else{
					if(ablecheck) return 2;
					else return 0;
				}
			}
		}

	}

	public int AImakeBet(int lastbet, int cardnumber) {
		int sol = solution(lastbet, cardnumber);
		switch(sol){
		case 0:{
			fold=true;
			bet+=0;
			return(sol);
		}
		case 1:{
			if(wallet>lastbet-bet){
				wallet-=(lastbet-bet);
				bet+=(lastbet-bet);
				return sol;
			}
			else{
				bet+=wallet;
				wallet=0;
				return sol;
			}
		}
		case 2:{
			bet+=0;
			return sol;
		}
		case 3:{
			if(lastbet==0){
				if(wallet>10){
					wallet-=10;
					bet=10;
					return sol;
				}
				else{
					bet=wallet;
					wallet=0;
					return sol;
				}
			}
			else{
				if(wallet>lastbet*2-bet){
					wallet-=(lastbet*2-bet);
					bet=lastbet*2;
					return sol;
				}
				else{
					bet+=wallet;
					wallet=0;
					return sol;
				}
			}			
		}
		}
		
		return sol;
	}

	@Override
	public int HumanmakeBet(int lastbet, int playersolution, int raiseby) {
		return 0;
	}
	
}

class WhichCombination{
	int cweight=0;
	List<Card> handcards= new ArrayList<Card>(7);
	public WhichCombination(Card card1,Card card2,ArrayList<Card> ftr){
		handcards.add(card1);
		handcards.add(card2);
		for(int i=0; i<ftr.size(); i++){
			handcards.add(ftr.get(i));
		}
		Collections.sort(handcards);
	}
	public boolean ifCouple2(){
		if(handcards.get(0).value==handcards.get(1).value) return true;
		return false;
	}
	///////////////////////////////////////////
	public boolean ifRoyalFlash5(){
		Card[] hcards=new Card[5];
		for(int i=0; i<5; i++){
			hcards[i]=handcards.get(i);
		}
		int flag=0;
		for(int i1=0; i1<hcards.length-1; i1++){
			if((hcards[i1].suit!=hcards[i1+1].suit)||(hcards[i1].value.weight!=hcards[i1+1].value.weight+1))
				flag=1;
		}
		if((flag==0)&&(hcards[0].value.weight==14)){
			return true;
		}
		return false;
	}
	
	public boolean ifStraightFlash5(){
		Card[] hcards=new Card[5];
		for(int i=0; i<5; i++){
			hcards[i]=handcards.get(i);
		}
		int flag=0;
		for(int i1=0; i1<hcards.length-1; i1++){
			if((hcards[i1].suit!=hcards[i1+1].suit)||(hcards[i1].value.weight!=hcards[i1+1].value.weight+1))
				flag=1;
		}
		if(flag==0){
			return true;
		}
		return false;
	}
	
	public boolean ifFoak5(){
		Card[] hcards=new Card[5];
		for(int i=0; i<5; i++){
			hcards[i]=handcards.get(i);
		}
		int flag=0;
		if(((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[1].value.weight==hcards[2].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight))||
				(hcards[1].value.weight==hcards[2].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
			flag=0;
		}
		else flag=1;
		if(flag==0){
			return true;
		}
		return false;
	}
	
	public boolean ifFullhouse5(){
		Card[] hcards=new Card[5];
		for(int i=0; i<5; i++){
			hcards[i]=handcards.get(i);
		}
		int flag=0;
		if(((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[1].value.weight==hcards[2].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight))||
				(hcards[0].value.weight==hcards[1].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
			flag=0;
		}
		else flag=1;
		if(flag==0){
			return true;
		}
		return false;
	}
	
	public boolean ifFlash5(){
		Card[] hcards=new Card[5];
		for(int i=0; i<5; i++){
			hcards[i]=handcards.get(i);
		}
		int flag=0;
		if((hcards[0].suit==hcards[1].suit)&&(hcards[1].suit==hcards[2].suit)&&(hcards[2].suit==hcards[3].suit)&&(hcards[3].suit==hcards[4].suit)){
		flag=0;	
		}
		else flag=1;
		if (flag==0){
			return true;
		}
		return false;
	}
	
	public boolean ifStraight5(){
		Card[] hcards=new Card[5];
		for(int i=0; i<5; i++){
			hcards[i]=handcards.get(i);
		}
		int flag=0;
		for(int i1=0; i1<hcards.length-1; i1++){
			if(hcards[i1].value.weight!=hcards[i1+1].value.weight+1){
				flag=1;
			}
		}
		if (flag==0){
			return true;
		}
		else {
			if(hcards[0].value.weight==11&&hcards[1].value.weight==5&&hcards[2].value.weight==4&&
					hcards[3].value.weight==3&&hcards[4].value.weight==2){return true;}
		}
		return false;
	}
	
	public boolean ifSet5(){
		Card[] hcards=new Card[5];
		for(int i=0; i<5; i++){
			hcards[i]=handcards.get(i);
		}
		if((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[1].value.weight==hcards[2].value.weight)){
			return true;
		}
		if((hcards[1].value.weight==hcards[2].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight)){
			return true;
		}
	    if((hcards[2].value.weight==hcards[3].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
			return true;
	    }
	    return false;
	}
	
	public boolean ifDoubleCouple5(){
		Card[] hcards=new Card[5];
		for(int i=0; i<5; i++){
			hcards[i]=handcards.get(i);
		}
		if((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight)){
			return true;
		}
		if((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
			return true;
		}
		if((hcards[1].value.weight==hcards[2].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
			return true;
		}
		return false;
	}
	
	public boolean ifCouple5(){
		Card[] hcards=new Card[5];
		for(int i=0; i<5; i++){
			hcards[i]=handcards.get(i);
		}
		if(hcards[0].value.weight==hcards[1].value.weight){
	    	return true;
	    }
	    if(hcards[1].value.weight==hcards[2].value.weight){
	    	return true;
	    }
	    if(hcards[2].value.weight==hcards[3].value.weight){
	    	return true;
	    }
	    if(hcards[3].value.weight==hcards[4].value.weight){
	    	return true;
	    }
	    return false;
	}
	///////////////////////////////////////////
	public boolean ifRoyalFlash6(){
		Card[] hcards=new Card[5];
		for(int i=handcards.size()-1; i>=0; i--){
			int l=0;
			for(int k=0; k<6; k++){
				if(l!=i){
					hcards[l]=handcards.get(k);
					l++;
				}
			}
			int flag=0;
			for(int i1=0; i1<hcards.length-1; i1++){
				if((hcards[i1].suit!=hcards[i1+1].suit)||(hcards[i1].value.weight!=hcards[i1+1].value.weight+1))
					flag=1;
			}
			if((flag==0)&&(hcards[0].value.weight==14)){
				return true;
			}
		}
		return false;
	}
	
	public boolean ifStraightFlash6(){
		Card[] hcards=new Card[5];
		for(int i=handcards.size()-1; i>=0; i--){
			int l=0;
			for(int k=0; k<6; k++){
				if(l!=i){
					hcards[l]=handcards.get(k);
					l++;
				}
			}
			int flag=0;
			for(int i1=0; i1<hcards.length-1; i1++){
				if((hcards[i1].suit!=hcards[i1+1].suit)||(hcards[i1].value.weight!=hcards[i1+1].value.weight+1))
					flag=1;
			}
			if(flag==0){
				return true;
			}
		}
		return false;
	}
	
	public boolean ifFoak6(){
		Card[] hcards=new Card[5];
		for(int i=handcards.size()-1; i>=0; i--){
			int l=0;
			for(int k=0; k<6; k++){
				if(l!=i){
					hcards[l]=handcards.get(k);
					l++;
				}
			}
			int flag=0;
			if(((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[1].value.weight==hcards[2].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight))||
					(hcards[1].value.weight==hcards[2].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
				flag=0;
			}
			else flag=1;
			if(flag==0){
				return true;
			}
		}
		return false;
	}
	
	public boolean ifFullhouse6(){
		Card[] hcards=new Card[5];
		for(int i=handcards.size()-1; i>=0; i--){
			int l=0;
			for(int k=0; k<6; k++){
				if(l!=i){
					hcards[l]=handcards.get(k);
					l++;
				}
			}
			int flag=0;
			if(((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[1].value.weight==hcards[2].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight))||
					(hcards[0].value.weight==hcards[1].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
				flag=0;
			}
			else flag=1;
			if(flag==0){
				return true;
			}
		}
		return false;
	}
	
	public boolean ifFlash6(){
		Card[] hcards=new Card[5];
		for(int i=handcards.size()-1; i>=0; i--){
			int l=0;
			for(int k=0; k<6; k++){
				if(l!=i){
					hcards[l]=handcards.get(k);
					l++;
				}
			}
			int flag=0;
			if((hcards[0].suit==hcards[1].suit)&&(hcards[1].suit==hcards[2].suit)&&(hcards[2].suit==hcards[3].suit)&&(hcards[3].suit==hcards[4].suit)){
			flag=0;	
			}
			else flag=1;
			if (flag==0){
				return true;
			}
		}
		return false;
	}
	
	public boolean ifStraight6(){
		Card[] hcards=new Card[5];
		for(int i=handcards.size()-1; i>=0; i--){
			int l=0;
			for(int k=0; k<6; k++){
				if(l!=i){
					hcards[l]=handcards.get(k);
					l++;
				}
			}
			int flag=0;
			for(int i1=0; i1<hcards.length-1; i1++){
				if(hcards[i1].value.weight!=hcards[i1+1].value.weight+1){
					flag=1;
				}
			}
			if (flag==0){
				return true;
			}
			else{
				if(hcards[0].value.weight==11&&hcards[1].value.weight==5&&hcards[2].value.weight==4&&
						hcards[3].value.weight==3&&hcards[4].value.weight==2){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean ifSet6(){
		Card[] hcards=new Card[5];
		for(int i=handcards.size()-1; i>=0; i--){
			int l=0;
			for(int k=0; k<6; k++){
				if(l!=i){
					hcards[l]=handcards.get(k);
					l++;
				}
			}
			if((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[1].value.weight==hcards[2].value.weight)){
				return true;
			}
			if((hcards[1].value.weight==hcards[2].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight)){
				return true;
			}
		    if((hcards[2].value.weight==hcards[3].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
				return true;
		    }
		}
		return false;
	}
	
	public boolean ifDoubleCouple6(){
		Card[] hcards=new Card[5];
		for(int i=handcards.size()-1; i>=0; i--){
			int l=0;
			for(int k=0; k<6; k++){
				if(l!=i){
					hcards[l]=handcards.get(k);
					l++;
				}
			}
			if((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight)){
				return true;
			}
			if((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
				return true;
			}
			if((hcards[1].value.weight==hcards[2].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
				return true;
			}
		}
		return false;
	}
	
	public boolean ifCouple6(){
		Card[] hcards=new Card[5];
		for(int i=handcards.size()-1; i>=0; i--){
			int l=0;
			for(int k=0; k<6; k++){
				if(l!=i){
					hcards[l]=handcards.get(k);
					l++;
				}
			}
			if(hcards[0].value.weight==hcards[1].value.weight){
		    	return true;
		    }
		    if(hcards[1].value.weight==hcards[2].value.weight){
		    	return true;
		    }
		    if(hcards[2].value.weight==hcards[3].value.weight){
		    	return true;
		    }
		    if(hcards[3].value.weight==hcards[4].value.weight){
		    	return true;
		    }
		}
		return false;
	}
	
	///////////////////////////////////////////
	public boolean ifRoyalFlash(){
		int k;
		Card[] hcards=new Card[5];
		for(int i=handcards.size()-1; i>=1; i--){
			for(int j=i-1; j>=0; j--){
				k=0;
				for(int l=0; l<=handcards.size()-1; l++){
					if((l!=i)&&(l!=j)){
						hcards[k]=handcards.get(l);
						k++;
					}
										
				}
				int flag=0;
				for(int i1=0; i1<hcards.length-1; i1++){
					if((hcards[i1].suit!=hcards[i1+1].suit)||(hcards[i1].value.weight!=hcards[i1+1].value.weight+1))
						flag=1;
				}
				if((flag==0)&&(hcards[0].value.weight==14)){
					for(int t=0; t<hcards.length; t++){
						cweight+=hcards[t].value.weight;
					}
					return true;
				}
			}			
		}
		return false;
	}
	
	public boolean ifStraightFlash(){
		Card[] hcards=new Card[5];
		int k;
		for(int i=handcards.size()-1; i>=1; i--){
			for(int j=i-1; j>=0; j--){
				k=0;
				for(int l=0; l<=handcards.size()-1; l++){
					if((l!=i)&&(l!=j)){
						hcards[k]=handcards.get(l);
						k++;
					}	
				}
				int flag=0;
				for(int i1=0; i1<hcards.length-1; i1++){
					if((hcards[i1].suit!=hcards[i1+1].suit)||(hcards[i1].value.weight!=hcards[i1+1].value.weight+1))
						flag=1;
				}
				if(flag==0){
					for(int t=0; t<hcards.length; t++){
						cweight+=hcards[t].value.weight;
					}
					return true;
				}
			}			
		}
		return false;
	}
	
	public boolean ifFoak(){
		Card[] hcards=new Card[5];
		int k;
		for(int i=handcards.size()-1; i>=1; i--){
			for(int j=i-1; j>=0; j--){
				k=0;
				for(int l=0; l<=handcards.size()-1; l++){
					if((l!=i)&&(l!=j)){
						hcards[k]=handcards.get(l);
						k++;
					}	
				}
				int flag=0;
				if(((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[1].value.weight==hcards[2].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight))||
						(hcards[1].value.weight==hcards[2].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
					flag=0;
				}
				else flag=1;
				if(flag==0){
					cweight=hcards[1].value.weight*4;
					return true;
				}
			}			
		}
		return false;
	}
	
	public boolean ifFullhouse(){
		Card[] hcards=new Card[5];
		int k;
		for(int i=handcards.size()-1; i>=1; i--){
			for(int j=i-1; j>=0; j--){
				k=0;
				for(int l=0; l<=handcards.size()-1; l++){
					if((l!=i)&&(l!=j)){
						hcards[k]=handcards.get(l);
						k++;
					}
				}
				int flag=0;
				if(((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[1].value.weight==hcards[2].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight))||
						(hcards[0].value.weight==hcards[1].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
					flag=0;
				}
				else flag=1;
				if(flag==0){
					for(int t=0; t<hcards.length; t++){
						cweight+=hcards[t].value.weight;
					}
					return true;
				}
			}			
		}
		return false;
	}
	
	public boolean ifFlash(){
		Card[] hcards=new Card[5];
		int k;
		for(int i=handcards.size()-1; i>=1; i--){
			for(int j=i-1; j>=0; j--){
				k=0;
				for(int l=0; l<=handcards.size()-1; l++){
					if((l!=i)&&(l!=j)){
						hcards[k]=handcards.get(l);
						k++;
					}	
				}
				int flag=0;
				if((hcards[0].suit==hcards[1].suit)&&(hcards[1].suit==hcards[2].suit)&&(hcards[2].suit==hcards[3].suit)&&(hcards[3].suit==hcards[4].suit)){
				flag=0;	
				}
				else flag=1;
				if (flag==0){
					for(int t=0; t<hcards.length; t++){
						cweight+=hcards[t].value.weight;
					}
					return true;
				}
			}			
		}
		return false;
	}
	
	public boolean ifStraight(){
		Card[] hcards=new Card[5];
		int k;
		for(int i=handcards.size()-1; i>=1; i--){
			for(int j=i-1; j>=0; j--){
				k=0;
				for(int l=0; l<=handcards.size()-1; l++){
					if((l!=i)&&(l!=j)){
						hcards[k]=handcards.get(l);
						k++;
					}	
				}
				int flag=0;
				for(int i1=0; i1<hcards.length-1; i1++){
					if(hcards[i1].value.weight!=hcards[i1+1].value.weight+1){
						flag=1;
					}
				}
				if (flag==0){
					for(int t=0; t<hcards.length; t++){
						cweight+=hcards[t].value.weight;
					}
					return true;
				}
				else{
					if(hcards[0].value.weight==11&&hcards[1].value.weight==5&&hcards[2].value.weight==4&&
							hcards[3].value.weight==3&&hcards[4].value.weight==2){
						for(int t=0; t<hcards.length; t++){
							cweight+=hcards[t].value.weight;
						}
						return true;
					}
				}
			}			
		}
		return false;
	}

	public boolean ifSet(){
		Card[] hcards=new Card[5];
		int k;
		for(int i=handcards.size()-1; i>=1; i--){
			for(int j=i-1; j>=0; j--){
				k=0;
				for(int l=0; l<=handcards.size()-1; l++){
					if((l!=i)&&(l!=j)){
						hcards[k]=handcards.get(l);
						k++;
					}	
				}
				
				if((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[1].value.weight==hcards[2].value.weight)){
					cweight=hcards[0].value.weight+hcards[1].value.weight+hcards[2].value.weight;
					return true;
				}
				if((hcards[1].value.weight==hcards[2].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight)){
					cweight=hcards[1].value.weight+hcards[2].value.weight+hcards[3].value.weight;
					return true;
				}
			    if((hcards[2].value.weight==hcards[3].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
			    	cweight=hcards[2].value.weight+hcards[3].value.weight+hcards[4].value.weight;
					return true;
			    }
		    }			
		}
		return false;
	}
	
	public boolean ifDoubleCouple(){
		Card[] hcards=new Card[5];
		int k;
		for(int i=handcards.size()-1; i>=1; i--){
			for(int j=i-1; j>=0; j--){
				k=0;
				for(int l=0; l<=handcards.size()-1; l++){
					if((l!=i)&&(l!=j)){
						hcards[k]=handcards.get(l);
						k++;
					}	
				}
				if((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[2].value.weight==hcards[3].value.weight)){
					cweight=hcards[0].value.weight*2+hcards[2].value.weight*2;
					return true;
				}
				if((hcards[0].value.weight==hcards[1].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
					cweight=hcards[0].value.weight*2+hcards[3].value.weight*2;
					return true;
				}
				if((hcards[1].value.weight==hcards[2].value.weight)&&(hcards[3].value.weight==hcards[4].value.weight)){
					cweight=hcards[1].value.weight*2+hcards[3].value.weight*2;
					return true;
				}
		    }			
		}
		return false;
	}
	
	public boolean ifCouple(){
		Card[] hcards=new Card[5];
		int k;
		for(int i=handcards.size()-1; i>=1; i--){
			for(int j=i-1; j>=0; j--){
				k=0;
				for(int l=0; l<=handcards.size()-1; l++){
					if((l!=i)&&(l!=j)){
						hcards[k]=handcards.get(l);
						k++;
					}
				}
			    if(hcards[0].value.weight==hcards[1].value.weight){
			    	cweight=hcards[0].value.weight*2;
			    	return true;
			    }
			    if(hcards[1].value.weight==hcards[2].value.weight){
			    	cweight=hcards[1].value.weight*2;
			    	return true;
			    }
			    if(hcards[2].value.weight==hcards[3].value.weight){
			    	cweight=hcards[2].value.weight*2;
			    	return true;
			    }
			    if(hcards[3].value.weight==hcards[4].value.weight){
			    	cweight=hcards[3].value.weight*2;
			    	return true;
			    }
		    }			
		}
		return false;
	}
	
	public Combination whichCombination(Combination comb){
	if(handcards.size()==7){	
		if(ifRoyalFlash()){
			comb.name=CombinationTitle.royalflash;
			comb.setMajority(10);
			comb.setWeight(cweight);
			return comb;
		}
		if(ifStraightFlash()){
			comb.name=CombinationTitle.straightflash;
			comb.setMajority(9);
			comb.setWeight(cweight);
			return comb;
		}
		if(ifFoak()){
			comb.name=CombinationTitle.foak;
			comb.setMajority(8);
			comb.setWeight(cweight);
			return comb;
		}
		if(ifFullhouse()){
			comb.name=CombinationTitle.fullhouse;
			comb.setMajority(7);
			comb.setWeight(cweight);
			return comb;
		}
		if(ifFlash()){
			comb.name=CombinationTitle.flash;
			comb.setMajority(6);
			comb.setWeight(cweight);
			return comb;
		}
		if(ifStraight()){
			comb.name=CombinationTitle.straight;
			comb.setMajority(5);
			comb.setWeight(cweight);
			return comb;
		}
		if(ifSet()){
			comb.name=CombinationTitle.set;
			comb.setMajority(4);
			comb.setWeight(cweight);
			return comb;
		}
		if(ifDoubleCouple()){
			comb.name=CombinationTitle.doublecouple;
			comb.setMajority(3);
			comb.setWeight(cweight);
			return comb;
		}
		if(ifCouple()){
			comb.name=CombinationTitle.couple;
			comb.setMajority(2);
			comb.setWeight(cweight);
			return comb;
		}
		comb.name=CombinationTitle.none;
		comb.setMajority(0);
		return comb;	
	}
	if(handcards.size()==6){
		if(ifRoyalFlash6()){
			comb.name=CombinationTitle.royalflash;
			comb.setMajority(10);
			return comb;
		}
		if(ifStraightFlash6()){
			comb.name=CombinationTitle.straightflash;
			comb.setMajority(9);
			return comb;
		}
		if(ifFoak6()){
			comb.name=CombinationTitle.foak;
			comb.setMajority(8);
			return comb;
		}
		if(ifFullhouse6()){
			comb.name=CombinationTitle.fullhouse;
			comb.setMajority(7);
			return comb;
		}
		if(ifFlash6()){
			comb.name=CombinationTitle.flash;
			comb.setMajority(6);
			return comb;
		}
		if(ifStraight6()){
			comb.name=CombinationTitle.straight;
			comb.setMajority(5);
			return comb;
		}
		if(ifSet6()){
			comb.name=CombinationTitle.set;
			comb.setMajority(4);
			return comb;
		}
		if(ifDoubleCouple6()){
			comb.name=CombinationTitle.doublecouple;
			comb.setMajority(3);
			return comb;
		}
		if(ifCouple6()){
			comb.name=CombinationTitle.couple;
			comb.setMajority(2);
			return comb;
		}
		comb.name=CombinationTitle.none;
		comb.setMajority(0);
		return comb;
	}
	if(handcards.size()==5){
		if(ifRoyalFlash5()){
			comb.name=CombinationTitle.royalflash;
			comb.setMajority(10);
			return comb;
		}
		if(ifStraightFlash5()){
			comb.name=CombinationTitle.straightflash;
			comb.setMajority(9);
			return comb;
		}
		if(ifFoak5()){
			comb.name=CombinationTitle.foak;
			comb.setMajority(8);
			return comb;
		}
		if(ifFullhouse5()){
			comb.name=CombinationTitle.fullhouse;
			comb.setMajority(7);
			return comb;
		}
		if(ifFlash5()){
			comb.name=CombinationTitle.flash;
			comb.setMajority(6);
			return comb;
		}
		if(ifStraight5()){
			comb.name=CombinationTitle.straight;
			comb.setMajority(5);
			return comb;
		}
		if(ifSet5()){
			comb.name=CombinationTitle.set;
			comb.setMajority(4);
			return comb;
		}
		if(ifDoubleCouple5()){
			comb.name=CombinationTitle.doublecouple;
			comb.setMajority(3);
			return comb;
		}
		if(ifCouple5()){
			comb.name=CombinationTitle.couple;
			comb.setMajority(2);
			return comb;
		}
		comb.name=CombinationTitle.none;
		comb.setMajority(0);
		return comb;
	}
	if(handcards.size()==2){
		if(ifCouple2()){
			comb.name=CombinationTitle.couple;
			comb.setMajority(2);
			return comb;
		}
		else
		comb.name=CombinationTitle.none;
		comb.setMajority(0);
		return comb;
	}
	return null;
	}
	
}


public class Poker {

	public static void main(String[] args) {
		/*Deck deck=new Deck();
		deck.shuffle();
	    Player[] allplayers=new Player[3];
	    for(int i=0; i<allplayers.length; i++){
	    	allplayers[i]=new AIPlayer(1000);
	    }
	    for(int i=0; i<2; i++){
	    	Game game=new Game();
	    	int num=0;
	    	for(int j=0; j<allplayers.length; j++){
	    		if (allplayers[j].getWallet()>0) num++;
	    	}
	    	ArrayList<Player> players=new ArrayList<Player>(num);
	    	for(int j=0; j<allplayers.length; j++){
	    		if(allplayers[j].getWallet()>0) players.add(allplayers[j]);
	    		Hand hand=new Hand(deck.GetCard(),deck.GetCard());
				players.get(j).setHand(hand);
	    	}
	    	game.makeBlinds(players);
	    	for(int j=0; j<players.size(); j++){
	    		WhichCombination wc1=new WhichCombination(players.get(j).getHand().card1,players.get(j).getHand().card2);
				Combination comb=new Combination();
				players.get(j).getHand().combination=wc1.whichCombination(comb);
				System.out.println("player"+j+" "+wc1.handcards+" "+players.get(j).getHand().combination+" bet"+players.get(j).bet+" wallet"+players.get(j).getWallet() );
			}
	    	//game.makeBets(players,2);
	    	System.out.println("totalbet"+game.totalbet);
	    	FTR ftr1=new FTR(deck.GetCard(),deck.GetCard(),deck.GetCard());
	    	for(int j=0; j<players.size(); j++){
	    		WhichCombination wc1=new WhichCombination(players.get(j).getHand().card1,players.get(j).getHand().card2,ftr1.getftr());
				Combination comb=new Combination();
				players.get(j).getHand().combination=wc1.whichCombination(comb);
				System.out.println("player"+j+" "+wc1.handcards+" "+players.get(j).getHand().combination+" bet"+players.get(j).bet+" wallet"+players.get(j).getWallet() );;
			}
	    	for(int j=0; j<players.size(); j++){
	    		players.get(j).bet=0;
	    	}
	    	game.lastbet=0;
	    	//game.makeBets(players, 5);
	    	System.out.println("totalbet"+game.totalbet);
	    	ftr1.ftradd(deck.GetCard());
	    	for(int j=0; j<players.size(); j++){
	    		WhichCombination wc1=new WhichCombination(players.get(j).getHand().card1,players.get(j).getHand().card2,ftr1.getftr());
				Combination comb=new Combination();
				players.get(j).getHand().combination=wc1.whichCombination(comb);
				System.out.println("player"+j+" "+wc1.handcards+" "+players.get(j).getHand().combination+" bet"+players.get(j).bet+" wallet"+players.get(j).getWallet() );;
			}
	    	for(int j=0; j<players.size(); j++){
	    		players.get(j).bet=0;
	    	}
	    	game.lastbet=0;
	    	//game.makeBets(players, 6);
	    	System.out.println("totalbet"+game.totalbet);
	    	ftr1.ftradd(deck.GetCard());
	    	for(int j=0; j<players.size(); j++){
	    		WhichCombination wc1=new WhichCombination(players.get(j).getHand().card1,players.get(j).getHand().card2,ftr1.getftr());
				Combination comb=new Combination();
				players.get(j).getHand().combination=wc1.whichCombination(comb);
				System.out.println("player"+j+" "+wc1.handcards+" "+players.get(j).getHand().combination+" bet"+players.get(j).bet+" wallet"+players.get(j).getWallet() );;
			}
	    	for(int j=0; j<players.size(); j++){
	    		players.get(j).bet=0;
	    	}
	    	game.lastbet=0;
	    	//game.makeBets(players, 7);
	    	System.out.println("totalbet"+game.totalbet);
	    	ArrayList<Combination> finalcomb=new ArrayList<Combination>(players.size());
			for(int j=0; j<players.size(); j++ ){
				finalcomb.add(j,players.get(j).getHand().combination);
			}
			Collections.sort(finalcomb);
			int max=finalcomb.get(0).getMajority()+finalcomb.get(0).getWeight();
			for(int j=0; j<players.size(); j++){
				if(players.get(j).getHand().combination.getMajority()+players.get(j).getHand().combination.getWeight()==max){
					players.get(j).Status="win";
				}
				else players.get(j).Status="lose";
			}
			int countwin=0;
			for(int j=0; j<players.size(); j++){
			   if (players.get(j).Status=="win") countwin++; 	
			}
			game.totalbet=Math.round(game.totalbet/countwin);
			for(int j=0; j<players.size(); j++){
				   if (players.get(j).Status=="win") players.get(j).setWallet(players.get(j).getWallet()+game.totalbet); 
				   System.out.println("player"+j+" status:"+players.get(j).Status+"; wallet"+players.get(j).getWallet());
			}
	    }*/

	}
}
