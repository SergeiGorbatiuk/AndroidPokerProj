package com.example.user.pokerheh;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.renderscript.Short4;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Game game = new Game();
    ArrayList<Player> players;
    int solutions[];
    Deck deck;
    FTR ftr;
    Player[] allplayers;
    Timer timer;
    goTimer timer1;
    startTimer timer2;
    BlindTimer timer4;
    State state;
    String str, str1;
    TextView p1txtcards, p2txtcards, p3txtcards, p4txtcards, p1comb, p2comb, p3comb, p4comb, p1bet, p2bet, p3bet, p4bet, Flop1, Flop2, Flop3, Turn, River, Lastbet, Summbet;
    Button Check, Call, Raise, Fold, Help;
    EditText RaiseAmount;
    boolean bshow;
    Bitmap bm, border;
    Map<String, Bitmap> hm;
    ImageView p1card1, p1card2, p2card1, p2card2, p3card1, p3card2, p4card1, p4card2, flop1, flop2, flop3, turn, river;
    LinearLayout main, player2, player3, player4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final getBorder getBorder = new getBorder(this);
        player2=(LinearLayout)findViewById(R.id.player2);
        player3=(LinearLayout)findViewById(R.id.player3);
        player4=(LinearLayout)findViewById(R.id.player4);


        main=(LinearLayout)findViewById(R.id.main);

        p1card1=(ImageView)findViewById(R.id.p1card1);
        p1card2=(ImageView)findViewById(R.id.p1card2);
        p1txtcards= (TextView)findViewById(R.id.p1txtcards);
        p1comb=(TextView)findViewById(R.id.p1comb);
        p1bet=(TextView)findViewById(R.id.p1bet);
        p2card1=(ImageView)findViewById(R.id.p2card1);
        p2card2=(ImageView)findViewById(R.id.p2card2);
        p2txtcards= (TextView)findViewById(R.id.p2txtcards);
        p2bet=(TextView)findViewById(R.id.p2bet);
        p2comb=(TextView)findViewById(R.id.p2comb);
        p3card1=(ImageView)findViewById(R.id.p3card1);
        p3card2=(ImageView)findViewById(R.id.p3card2);
        p3txtcards= (TextView)findViewById(R.id.p3txtcards);
        p3comb=(TextView)findViewById(R.id.p3comb);
        p3bet=(TextView)findViewById(R.id.p3bet);
        p4card1=(ImageView)findViewById(R.id.p4card1);
        p4card2=(ImageView)findViewById(R.id.p4card2);
        p4txtcards= (TextView)findViewById(R.id.p4txtcards);
        p4comb=(TextView)findViewById(R.id.p4comb);
        p4bet=(TextView)findViewById(R.id.p4bet);
        Call=(Button)findViewById(R.id.call);
        Check=(Button)findViewById(R.id.check);
        Raise=(Button)findViewById(R.id.raise);
        Fold=(Button)findViewById(R.id.fold);
        RaiseAmount=(EditText)findViewById(R.id.RaiseAmount);
        Flop1=(TextView)findViewById(R.id.flop1txt);
        Flop2=(TextView)findViewById(R.id.flop2txt);
        Flop3=(TextView)findViewById(R.id.flop3txt);
        Turn=(TextView)findViewById(R.id.turntxt);
        River=(TextView)findViewById(R.id.rivertxt);
        Lastbet=(TextView)findViewById(R.id.lastbet);
        Summbet=(TextView)findViewById(R.id.summbet);
        flop1=(ImageView)findViewById(R.id.flop1);
        flop2=(ImageView)findViewById(R.id.flop2);
        flop3=(ImageView)findViewById(R.id.flop3);
        turn=(ImageView)findViewById(R.id.turn);
        river=(ImageView)findViewById(R.id.river);
        Help=(Button)findViewById(R.id.help);

        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(i);
            }
        });

        RaiseAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1 = s.toString();
                if ((s1.length() != 0)) {
                    int i = Integer.parseInt(s1);
                    if (i > game.lastbet && i <= players.get(players.size() - 1).getWallet())
                        Raise.setVisibility(View.VISIBLE);
                }
                else Raise.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bshow = false;
                solutions[players.size() - 1] = players.get(players.size() - 1).HumanmakeBet(game.lastbet, 2, 0);
                game.summbet += game.totalbet;
                game.totalbet = 0;
                game.lastbet = 0;
                timer1 = new goTimer(1000, 1000);
                timer1.start();
                state = new State(ftr, solutions, players, game);
                state.setState();
            }
        });

        Call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bshow = false;
                game.totalbet -= players.get(players.size() - 1).bet;
                solutions[players.size() - 1] = players.get(players.size() - 1).HumanmakeBet(game.lastbet, 1, 0);
                game.totalbet += players.get(players.size() - 1).bet;
                state = new State(ftr, solutions, players, game);
                state.setState();
                if (game.goFurther(players, solutions) == 0) {
                    timer = new Timer(1000, 1000, 0);
                    timer.start();
                } else {
                    timer1 = new goTimer(1000, 1000);
                    timer1.start();
                }
            }
        });

        Raise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bshow = false;
                int raiseby = Integer.parseInt(RaiseAmount.getText().toString());
                game.totalbet -= players.get(players.size() - 1).bet;
                solutions[players.size() - 1] = players.get(players.size() - 1).HumanmakeBet(game.lastbet, 3, raiseby);
                game.totalbet += players.get(players.size() - 1).bet;
                game.lastbet = players.get(players.size() - 1).bet;
                RaiseAmount.setText("");
                state = new State(ftr, solutions, players, game);
                state.setState();
                timer = new Timer(1000, 1000, 0);
                timer.start();
            }
        });
        Fold.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bshow = false;
                solutions[players.size() - 1] = players.get(players.size() - 1).HumanmakeBet(game.lastbet, 0, 0);
                state = new State(ftr, solutions, players, game);
                state.setState();
                if (game.goFurther(players, solutions) == 0) {
                    timer = new Timer(1000, 1000, 0);
                    timer.start();
                } else {
                    timer1 = new goTimer(1000, 1000);
                    timer1.start();
                }
            }
        });

        bm = BitmapFactory.decodeResource(getResources(), R.drawable.cards2);
        hm=new HashMap<String, Bitmap>();
        for (Suit suit : Suit.values())
            for (Value value : Value.values()){
                Card card=new Card(suit, value);
                int i=card.value.weight-2;
                int sui=card.suit.sui;
                hm.put(card.toString(), Bitmap.createScaledBitmap(Bitmap.createBitmap(bm,i*197,sui*285, 197, 285),138, 200, false));
            }
        hm.put("cardBack", Bitmap.createScaledBitmap(Bitmap.createBitmap(bm, 197, 4 * 285, 197, 285), 138, 200, false));
        int Pnum=getIntent().getIntExtra("num",1)+1;
        switch (Pnum){
            case 2:{
                p3bet.setVisibility(View.INVISIBLE);
                p3txtcards.setVisibility(View.INVISIBLE);
                p3comb.setVisibility(View.INVISIBLE);
                p4bet.setVisibility(View.INVISIBLE);
                p4txtcards.setVisibility(View.INVISIBLE);
                p4comb.setVisibility(View.INVISIBLE);
                player2.setBackgroundResource(R.drawable.border2);
                break;
            }
            case 3:{
                p4bet.setVisibility(View.INVISIBLE);
                p4txtcards.setVisibility(View.INVISIBLE);
                p4comb.setVisibility(View.INVISIBLE);
                player3.setBackgroundResource(R.drawable.border2);
                break;
            }
            case 4:{
                player4.setBackgroundResource(R.drawable.border2);
            }
        }
        deck = new Deck();
        deck.shuffle();
        allplayers = new Player[Pnum];
        for (int i = 0; i < allplayers.length - 1; i++) {
            allplayers[i] = new AIPlayer(getIntent().getIntExtra("amount", 1000));
        }
        allplayers[allplayers.length - 1] = new HumanPlayer(getIntent().getIntExtra("amount", 1000));
        players = new ArrayList<Player>(Pnum);
        for (int i = 0; i < allplayers.length; i++) {
            players.add(allplayers[i]);
        }
        for (int j = 0; j < allplayers.length; j++) {
            Hand hand = new Hand(deck.GetCard(), deck.GetCard());
            players.get(j).setHand(hand);
        }
        solutions = new int[Pnum];
        for (int i = 0; i < players.size(); i++) {
            players.get(i).Status = 'l';
            solutions[i] = -1;
        }
        players.get(players.size()-1).cshow=true;
        ftr=new FTR();
        bshow=false;
        timer4=new BlindTimer(1000,1000);
        timer4.start();
    }


    class BlindTimer extends CountDownTimer{
        public BlindTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            switch (players.size()){
                case 2:{
                    game.makeBlinds(players, 0, 1);

                    state = new State(ftr, solutions, players, game);
                    state.setState();

                    if(game.goFurther(players,solutions)==0){
                        timer=new Timer(1000, 1000, 0);
                        timer.start();
                    }
                    else{
                        timer1=new goTimer(1000, 1000);
                        timer1.start();
                    }
                    break;
                }
                case 3:{
                    if(players.get(0).getWallet()>0&&players.get(1).getWallet()>0){
                        game.makeBlinds(players, 0 ,1);

                        bshow=true;
                        state = new State(ftr, solutions, players, game);
                        state.setState();
                    }
                    else {
                        if(players.get(0).getWallet()==0){
                            game.makeBlinds(players, 1, 2);
                            state = new State(ftr, solutions, players, game);
                            state.setState();
                            timer=new Timer(1000, 1000, 1);
                            timer.start();
                        }
                        else {
                            game.makeBlinds(players, 0, 2);
                            state = new State(ftr, solutions, players, game);
                            state.setState();
                            timer=new Timer(1000, 1000, 0);
                            timer.start();
                        }
                    }
                    break;
                }
                case 4:{
                    if(players.get(0).getWallet()>0){
                        if(players.get(1).getWallet()>0){
                            game.makeBlinds(players, 0 ,1);

                            state = new State(ftr, solutions, players, game);
                            state.setState();
                            timer=new Timer(1000, 1000, 2);
                            timer.start();
                        }
                        else{
                            if(players.get(2).getWallet()>0){
                                game.makeBlinds(players, 0,2);
                                bshow=true;
                                state = new State(ftr, solutions, players, game);
                                state.setState();
                            }
                            else {
                                game.makeBlinds(players, 0,3);
                                timer=new Timer(1000, 1000, 0);
                                timer.start();
                            }
                        }
                    }
                    else {
                        if(players.get(1).getWallet()>0){
                            if(players.get(2).getWallet()>0){
                                game.makeBlinds(players, 1,2);
                                bshow=true;
                                state = new State(ftr, solutions, players, game);
                                state.setState();
                            }
                            else{
                                game.makeBlinds(players, 1,3);
                                timer=new Timer(1000, 1000, 1);
                                timer.start();
                            }
                        }
                        else{
                            game.makeBlinds(players, 2,3);
                            timer=new Timer(1000, 1000, 2);
                            timer.start();
                        }
                    }
                }
            }
        }
    }

    class Timer extends CountDownTimer {
        int i;

        public Timer(long millisInFuture, long countDownInterval,int i) {
            super(millisInFuture, countDownInterval);
            this.i = i;
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if (!players.get(i).fold&&players.get(i).getWallet()!=0) {
                game.totalbet -= players.get(i).bet;
                solutions[i] = players.get(i).AImakeBet(game.lastbet, ftr.ftrsize() + 2);
                game.totalbet += players.get(i).bet;
                if (players.get(i).bet > game.lastbet) game.lastbet = players.get(i).bet;
                state = new State(ftr, solutions, players, game);
                state.setState();
            }
            if(game.goFurther(players, solutions)==0){     ////все плохо
                if(i<players.size()-2){ /////не все боты поставили
                    timer = new Timer(1000, 1000,i+1);
                    timer.start();
                }
                else {/////все боты поставили
                    if(!players.get(players.size() - 1).fold&&players.get(players.size()-1).getWallet()!=0){//игрок не сбросил, показать кнопки
                        bshow = true;
                        state = new State(ftr, solutions, players, game);
                        state.setState();
                    }
                    else{////игрок сбросил
                        timer = new Timer(1000, 1000, 0);
                        timer.start();
                    }
                }
            }
            else{
                timer1=new goTimer(1000, 1000);
                timer1.start();
            }
        }
    }

    class goTimer extends CountDownTimer {

        public goTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if (game.goFurther(players, solutions) == 2) {
                switch (ftr.ftrsize()) {
                    case 0: {
                        ftr.ftradd(deck.GetCard());
                        ftr.ftradd(deck.GetCard());
                        ftr.ftradd(deck.GetCard());
                        ftr.ftradd(deck.GetCard());
                        ftr.ftradd(deck.GetCard());
                        game.summbet += game.totalbet;
                        game.totalbet = 0;
                        game.lastbet = 0;
                        game.endgame(players, ftr);
                        break;
                    }
                    case 3: {
                        ftr.ftradd(deck.GetCard());
                        ftr.ftradd(deck.GetCard());
                        game.summbet += game.totalbet;
                        game.totalbet = 0;
                        game.lastbet = 0;
                        game.endgame(players, ftr);
                        break;
                    }
                    case 4: {
                        ftr.ftradd(deck.GetCard());
                        game.summbet += game.totalbet;
                        game.totalbet = 0;
                        game.lastbet = 0;
                        game.endgame(players, ftr);
                        break;
                    }
                    case 5: {
                        game.summbet += game.totalbet;
                        game.totalbet = 0;
                        game.lastbet = 0;
                        game.endgame(players, ftr);
                    }
                }
                state = new State(ftr, solutions, players, game);
                state.setState();
                timer2 = new startTimer(2000, 2000);
                timer2.start();
            }
            else {
                switch (ftr.ftrsize()) {
                    case 0: {
                        ftr.ftradd(deck.GetCard());
                        ftr.ftradd(deck.GetCard());
                        ftr.ftradd(deck.GetCard());
                        break;
                    }
                    case 3: {
                        ftr.ftradd(deck.GetCard());
                        break;
                    }
                    case 4: {
                        ftr.ftradd(deck.GetCard());
                        break;
                    }
                    case 5: {
                        game.summbet += game.totalbet;
                        game.totalbet = 0;
                        game.lastbet = 0;
                        game.endgame(players, ftr);
                    }
                }
                game.summbet += game.totalbet;
                game.totalbet = 0;
                game.lastbet = 0;
                for (int i = 0; i < players.size(); i++) {
                    players.get(i).bet = 0;
                    if ((solutions[i] != 0)&&(solutions[i]!=-2)){
                        solutions[i] = -1;
                    }

                }
                state = new State(ftr, solutions, players, game);
                state.setState();
                if ((ftr.ftrsize() <= 5) && (!game.end)) {
                    timer = new Timer(1000, 1000, 0);
                    timer.start();
                }
                if (game.end) {
                    timer2 = new startTimer(2000, 2000);
                    timer2.start();
                }
            }
        }
    }

    class startTimer extends CountDownTimer {

        public startTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if (players.get(players.size() - 1).getWallet() == 0) {
                String str="Damn son u lost(((";
                Intent i=new Intent(MainActivity.this, Game_over.class);
                i.putExtra("str",str);
                startActivity(i);

            }
            else {
                boolean win=true;
                for (int i=0; i<players.size()-1; i++){
                    if(players.get(i).getWallet()>0) win=false;
                }
                if(win){
                    String str="Gg mate u have won";
                    Intent i=new Intent(MainActivity.this, Game_over.class);
                    i.putExtra("str",str);
                    startActivity(i);
                }
                else{
                    game.summbet = 0;
                    game.totalbet = 0;
                    game.lastbet = 0;
                    deck.clear();
                    deck = new Deck();
                    deck.shuffle();
                    ftr.clear();
                    for (int i = 0; i < players.size(); i++) {
                        players.get(i).bet = 0;
                        if(players.get(i).getWallet()>0){
                            players.get(i).fold = false;
                            Hand hand= new Hand(deck.GetCard(), deck.GetCard());
                            players.get(i).setHand(hand);
                            solutions[i] = -1;
                            players.get(i).Status='l';
                            players.get(i).cshow=false;
                        }
                        else{
                            players.get(i).fold=true;
                            players.get(i).setHand(null);
                            solutions[i] = -2;
                            players.get(i).cshow=false;
                        }

                    }
                    game.end = false;
                    state = new State(ftr, solutions, players, game);
                    state.setState();
                    bshow=false;
                    timer4=new BlindTimer(1000,1000);
                    timer4.start();
                }
            }
        }
    }

    class State {
        FTR ftr;
        int[] solutions;
        WhichCombination wc;
        ArrayList<Player> players;
        Game game;

        public State(FTR ftr, int[] solutions, ArrayList<Player> players, Game game) {
            this.ftr = ftr;
            this.solutions = solutions;
            this.players = players;
            this.game = game;
        }

        public void setState() {
            if(game.lastbet>0&&players.get(players.size()-1).bet<game.lastbet&&bshow){
                Lastbet.setVisibility(View.VISIBLE);
                Lastbet.setText("To call: " + (game.lastbet-players.get(players.size()-1).bet));
            }
            else{
                Lastbet.setVisibility(View.INVISIBLE);
            }
            if(game.summbet>0){
                Summbet.setVisibility(View.VISIBLE);
                Summbet.setText("Totalbet: " + game.summbet);
            }
            else{
                Summbet.setVisibility(View.INVISIBLE);
            }
            switch (ftr.ftrsize()) {
                case 0: {
                    Flop1.setVisibility(View.INVISIBLE);
                    Flop2.setVisibility(View.INVISIBLE);
                    Flop3.setVisibility(View.INVISIBLE);
                    Turn.setVisibility(View.INVISIBLE);
                    River.setVisibility(View.INVISIBLE);
                    flop1.setVisibility(View.INVISIBLE);
                    flop2.setVisibility(View.INVISIBLE);
                    flop3.setVisibility(View.INVISIBLE);
                    turn.setVisibility(View.INVISIBLE);
                    river.setVisibility(View.INVISIBLE);
                    break;
                }
                case 3: {
                    Flop1.setVisibility(View.VISIBLE);
                    Flop2.setVisibility(View.VISIBLE);
                    Flop3.setVisibility(View.VISIBLE);
                    Flop1.setText(ftr.getcard(0).toString());
                    Flop2.setText(ftr.getcard(1).toString());
                    Flop3.setText(ftr.getcard(2).toString());
                    flop1.setImageBitmap(hm.get(ftr.getcard(0).toString()));
                    flop2.setImageBitmap(hm.get(ftr.getcard(1).toString()));
                    flop3.setImageBitmap(hm.get(ftr.getcard(2).toString()));
                    flop1.setVisibility(View.VISIBLE);
                    flop2.setVisibility(View.VISIBLE);
                    flop3.setVisibility(View.VISIBLE);
                    break;
                }
                case 4: {
                    Turn.setVisibility(View.VISIBLE);
                    Turn.setText(ftr.getcard(3).toString());
                    turn.setImageBitmap(hm.get(ftr.getcard(3).toString()));
                    turn.setVisibility(View.VISIBLE);
                    break;
                }
                case 5: {
                    if(game.end){
                        River.setVisibility(View.VISIBLE);
                        Flop1.setText(ftr.getcard(0).toString());
                        Flop2.setText(ftr.getcard(1).toString());
                        Flop3.setText(ftr.getcard(2).toString());
                        Turn.setText(ftr.getcard(3).toString());
                        River.setText(ftr.getcard(4).toString());
                        flop1.setImageBitmap(hm.get(ftr.getcard(0).toString()));
                        flop2.setImageBitmap(hm.get(ftr.getcard(1).toString()));
                        flop3.setImageBitmap(hm.get(ftr.getcard(2).toString()));
                        turn.setImageBitmap(hm.get(ftr.getcard(3).toString()));
                        river.setImageBitmap(hm.get(ftr.getcard(4).toString()));
                        flop1.setVisibility(View.VISIBLE);
                        flop2.setVisibility(View.VISIBLE);
                        flop3.setVisibility(View.VISIBLE);
                        turn.setVisibility(View.VISIBLE);
                        river.setVisibility(View.VISIBLE);
                    }
                    else{
                        River.setText(ftr.getcard(4).toString());
                        river.setImageBitmap(hm.get(ftr.getcard(4).toString()));
                        river.setVisibility(View.VISIBLE);
                    }
                }
            }
            if (!bshow||game.end) {
                Check.setVisibility(View.INVISIBLE);
                Call.setVisibility(View.INVISIBLE);
                Raise.setVisibility(View.INVISIBLE);
                Fold.setVisibility(View.INVISIBLE);
                RaiseAmount.setVisibility(View.INVISIBLE);
            } else {
                if (players.get(players.size() - 1).bet == game.lastbet) {
                    Check.setVisibility(View.VISIBLE);
                    Call.setVisibility(View.INVISIBLE);
                    Raise.setVisibility(View.INVISIBLE);
                    Fold.setVisibility(View.VISIBLE);
                    RaiseAmount.setVisibility(View.VISIBLE);
                } else {
                    Check.setVisibility(View.INVISIBLE);
                    Call.setVisibility(View.VISIBLE);
                    Raise.setVisibility(View.INVISIBLE);
                    Fold.setVisibility(View.VISIBLE);
                    RaiseAmount.setVisibility(View.VISIBLE);
                }
            }
            ////////////////////////////////////////////////////////////////
            for (int j = 0; j < players.size(); j++) {
                if(players.get(j).getHand()!=null){
                    wc = new WhichCombination(players.get(j).getHand().card1, players.get(j).getHand().card2, ftr.getftr());
                    Combination comb = new Combination();
                    players.get(j).getHand().combination = wc.whichCombination(comb);
                }
            }
            switch (players.size()){
                case 2:{
                    switch (solutions[1]){
                        case -2:
                        case -1:str=""; break;
                        case 0:str="FOLD"; break;
                        case 1:str="CALL";break;
                        case 2:str="CHECK";break;
                        case 3:str="RAISE";
                    }
                    if(game.end){
                        if(players.get(1).Status=='w') str1="WIN";
                        else str1="LOSE";
                    }
                    else str1="";
                    p2txtcards.setText(players.get(1).getHand().toString());
                    p2bet.setText(str + "(" + players.get(1).bet + ") wallet(" + players.get(1).getWallet()+")"+str1);
                    p2comb.setText(players.get(1).hand.combination.toString());
                    p2card1.setImageBitmap(hm.get(players.get(1).getHand().card1.toString()));
                    p2card2.setImageBitmap(hm.get(players.get(1).getHand().card2.toString()));

                    switch (solutions[0]){
                        case -1:str=""; break;
                        case 0:str="FOLD"; break;
                        case 1:str="CALL";break;
                        case 2:str="CHECK";break;
                        case 3:str="RAISE";

                    }
                    if(players.get(0).cshow&&!players.get(0).fold){
                        p1card1.setImageBitmap(hm.get(players.get(0).getHand().card1.toString()));
                        p1card2.setImageBitmap(hm.get(players.get(0).getHand().card2.toString()));
                        p1txtcards.setVisibility(View.VISIBLE);
                        p1comb.setVisibility(View.VISIBLE);
                        p1txtcards.setText(players.get(0).getHand().toString());
                        p1comb.setText(players.get(0).hand.combination.toString());
                        String str1;
                        if(players.get(0).Status=='w') str1="WIN";
                        else str1="LOSE";
                        p1bet.setText(str+"("+players.get(0).bet+") wallet("+players.get(0).getWallet()+")"+str1);
                    }
                    else{
                        p1card1.setImageBitmap(hm.get("cardBack"));
                        p1card2.setImageBitmap(hm.get("cardBack"));
                        p1txtcards.setVisibility(View.INVISIBLE);
                        p1comb.setVisibility(View.INVISIBLE);
                        p1bet.setText(str + "(" + players.get(0).bet + ") wallet(" + players.get(0).getWallet()+")");
                    }

                    break;

                }
                case 3:{
                    switch (solutions[2]){
                        case -2:
                        case -1:str=""; break;
                        case 0:str="FOLD"; break;
                        case 1:str="CALL";break;
                        case 2:str="CHECK";break;
                        case 3:str="RAISE";
                    }
                    if(game.end){
                        if(players.get(2).Status=='w') str1="WIN";
                        else str1="LOSE";
                    }
                    else str1="";
                    p3txtcards.setText(players.get(2).getHand().toString());
                    p3bet.setText(str + "(" + players.get(2).bet + ") wallet(" + players.get(2).getWallet()+")"+str1);
                    p3comb.setText(players.get(2).hand.combination.toString());
                    p3card1.setImageBitmap(hm.get(players.get(2).getHand().card1.toString()));
                    p3card2.setImageBitmap(hm.get(players.get(2).getHand().card2.toString()));

                    switch (solutions[0]){
                        case -2:
                        case -1:str=""; break;
                        case 0:str="FOLD"; break;
                        case 1:str="CALL";break;
                        case 2:str="CHECK";break;
                        case 3:str="RAISE";
                    }
                    if(players.get(0).cshow&&!players.get(0).fold&&players.get(0).getHand()!=null){
                        p1card1.setImageBitmap(hm.get(players.get(0).getHand().card1.toString()));
                        p1card2.setImageBitmap(hm.get(players.get(0).getHand().card2.toString()));
                        p1txtcards.setVisibility(View.VISIBLE);
                        p1comb.setVisibility(View.VISIBLE);
                        p1txtcards.setText(players.get(0).getHand().toString());
                        if(players.get(0).Status=='w') str1="WIN";
                        else str1="LOSE";
                        p1bet.setText(str + "(" + players.get(0).bet + ") wallet(" + players.get(0).getWallet()+")"+str1);
                        p1comb.setText(players.get(0).hand.combination.toString());
                    }
                    else {
                        if(players.get(0).getHand()!=null) {
                            p1card1.setImageBitmap(hm.get("cardBack"));
                            p1card2.setImageBitmap(hm.get("cardBack"));
                            p1txtcards.setVisibility(View.INVISIBLE);
                            p1comb.setVisibility(View.INVISIBLE);
                            p1bet.setText(str + "(" + players.get(0).bet + ") wallet(" + players.get(0).getWallet()+")");
                        }
                        else {
                            p1card1.setVisibility(View.INVISIBLE);
                            p1card2.setVisibility(View.INVISIBLE);
                            p1txtcards.setVisibility(View.INVISIBLE);
                            p1bet.setVisibility(View.INVISIBLE);
                            p1comb.setVisibility(View.INVISIBLE);
                        }
                    }

                    switch (solutions[1]){
                        case -2:
                        case -1:str=""; break;
                        case 0:str="FOLD"; break;
                        case 1:str="CALL";break;
                        case 2:str="CHECK";break;
                        case 3:str="RAISE";
                    }
                    if(players.get(1).cshow&&!players.get(1).fold&&players.get(1).getHand()!=null){
                        p2card1.setImageBitmap(hm.get(players.get(1).getHand().card1.toString()));
                        p2card2.setImageBitmap(hm.get(players.get(1).getHand().card2.toString()));
                        p2txtcards.setVisibility(View.VISIBLE);
                        p2comb.setVisibility(View.VISIBLE);
                        p2txtcards.setText(players.get(1).getHand().toString());
                        if(players.get(1).Status=='w') str1="WIN";
                        else str1="LOSE";
                        p2bet.setText(str + "(" + players.get(1).bet + ") wallet(" + players.get(1).getWallet()+")"+str1);
                        p2comb.setText(players.get(1).hand.combination.toString());
                    }
                    else {
                        if(players.get(1).getHand()!=null) {
                            p2card1.setImageBitmap(hm.get("cardBack"));
                            p2card2.setImageBitmap(hm.get("cardBack"));
                            p2txtcards.setVisibility(View.INVISIBLE);
                            p2comb.setVisibility(View.INVISIBLE);
                            p2bet.setText(str + "(" + players.get(1).bet + ") wallet(" + players.get(1).getWallet()+")");
                        }
                        else {
                            p2card1.setVisibility(View.INVISIBLE);
                            p2card2.setVisibility(View.INVISIBLE);
                            p2txtcards.setVisibility(View.INVISIBLE);
                            p2bet.setVisibility(View.INVISIBLE);
                            p2comb.setVisibility(View.INVISIBLE);
                        }
                    }
                    break;
                }
                case 4:{
                    switch (solutions[3]){
                        case -2:
                        case -1:str=""; break;
                        case 0:str="FOLD"; break;
                        case 1:str="CALL";break;
                        case 2:str="CHECK";break;
                        case 3:str="RAISE";
                    }
                    if(game.end){
                        if(players.get(3).Status=='w') str1="WIN";
                        else str1="LOSE";
                    }
                    else str1="";
                    p4txtcards.setText(players.get(3).getHand().toString());
                    p4bet.setText(str + "(" + players.get(3).bet + ") wallet(" + players.get(3).getWallet()+")"+str1);
                    p4comb.setText(players.get(3).hand.combination.toString());
                    p4card1.setImageBitmap(hm.get(players.get(3).getHand().card1.toString()));
                    p4card2.setImageBitmap(hm.get(players.get(3).getHand().card2.toString()));

                    switch (solutions[0]){
                        case -2:
                        case -1:str=""; break;
                        case 0:str="FOLD"; break;
                        case 1:str="CALL";break;
                        case 2:str="CHECK";break;
                        case 3:str="RAISE";
                    }
                    if(players.get(0).cshow&&!players.get(0).fold&&players.get(0).getHand()!=null){
                        p1card1.setImageBitmap(hm.get(players.get(0).getHand().card1.toString()));
                        p1card2.setImageBitmap(hm.get(players.get(0).getHand().card2.toString()));
                        p1txtcards.setVisibility(View.VISIBLE);
                        p1comb.setVisibility(View.VISIBLE);
                        p1txtcards.setText(players.get(0).getHand().toString());
                        if(players.get(0).Status=='w') str1="WIN";
                        else str1="LOSE";
                        p1bet.setText(str + "(" + players.get(0).bet + ") wallet(" + players.get(0).getWallet()+")"+str1);
                        p1comb.setText(players.get(0).hand.combination.toString());
                    }
                    else {
                        if(players.get(0).getHand()!=null) {
                            p1card1.setImageBitmap(hm.get("cardBack"));
                            p1card2.setImageBitmap(hm.get("cardBack"));
                            p1txtcards.setVisibility(View.INVISIBLE);
                            p1comb.setVisibility(View.INVISIBLE);
                            p1bet.setText(str + "(" + players.get(0).bet + ") wallet(" + players.get(0).getWallet()+")");
                        }
                        else {
                            p1card1.setVisibility(View.INVISIBLE);
                            p1card2.setVisibility(View.INVISIBLE);
                            p1txtcards.setVisibility(View.INVISIBLE);
                            p1bet.setVisibility(View.INVISIBLE);
                            p1comb.setVisibility(View.INVISIBLE);
                        }
                    }

                    switch (solutions[1]){
                        case -2:
                        case -1:str=""; break;
                        case 0:str="FOLD"; break;
                        case 1:str="CALL";break;
                        case 2:str="CHECK";break;
                        case 3:str="RAISE";
                    }
                    if(players.get(1).cshow&&!players.get(1).fold&&players.get(1).getHand()!=null){
                        p2card1.setImageBitmap(hm.get(players.get(1).getHand().card1.toString()));
                        p2card2.setImageBitmap(hm.get(players.get(1).getHand().card2.toString()));
                        p2txtcards.setVisibility(View.VISIBLE);
                        p2comb.setVisibility(View.VISIBLE);
                        p2txtcards.setText(players.get(1).getHand().toString());
                        if(players.get(1).Status=='w') str1="WIN";
                        else str1="LOSE";
                        p2bet.setText(str + "(" + players.get(1).bet + ") wallet(" + players.get(1).getWallet()+")"+str1);
                        p2comb.setText(players.get(1).hand.combination.toString());
                    }
                    else {
                        if(players.get(1).getHand()!=null) {
                            p2card1.setImageBitmap(hm.get("cardBack"));
                            p2card2.setImageBitmap(hm.get("cardBack"));
                            p2txtcards.setVisibility(View.INVISIBLE);
                            p2comb.setVisibility(View.INVISIBLE);
                            p2bet.setText(str + "(" + players.get(1).bet + ") wallet(" + players.get(1).getWallet()+")");
                        }
                        else {
                            p2card1.setVisibility(View.INVISIBLE);
                            p2card2.setVisibility(View.INVISIBLE);
                            p2txtcards.setVisibility(View.INVISIBLE);
                            p2bet.setVisibility(View.INVISIBLE);
                            p2comb.setVisibility(View.INVISIBLE);
                        }
                    }

                    switch (solutions[2]){
                        case -2:
                        case -1:str=""; break;
                        case 0:str="FOLD"; break;
                        case 1:str="CALL";break;
                        case 2:str="CHECK";break;
                        case 3:str="RAISE";
                    }
                    if(players.get(2).cshow&&!players.get(2).fold&&players.get(2).getHand()!=null){
                        p3card1.setImageBitmap(hm.get(players.get(2).getHand().card1.toString()));
                        p3card2.setImageBitmap(hm.get(players.get(2).getHand().card2.toString()));
                        p3txtcards.setVisibility(View.VISIBLE);
                        p3comb.setVisibility(View.VISIBLE);
                        p3txtcards.setText(players.get(2).getHand().toString());
                        if(players.get(2).Status=='w') str1="WIN";
                        else str1="LOSE";
                        p3bet.setText(str + "(" + players.get(2).bet + ") wallet(" + players.get(2).getWallet()+")"+str1);
                        p3comb.setText(players.get(2).hand.combination.toString());
                    }
                    else {
                        if(players.get(2).getHand()!=null) {
                            p3card1.setImageBitmap(hm.get("cardBack"));
                            p3card2.setImageBitmap(hm.get("cardBack"));
                            p3txtcards.setVisibility(View.INVISIBLE);
                            p3comb.setVisibility(View.INVISIBLE);
                            p3bet.setText(str + "(" + players.get(2).bet + ") wallet(" + players.get(2).getWallet()+")");
                        }
                        else {
                            p3card1.setVisibility(View.INVISIBLE);
                            p3card2.setVisibility(View.INVISIBLE);
                            p3txtcards.setVisibility(View.INVISIBLE);
                            p3bet.setVisibility(View.INVISIBLE);
                            p3comb.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        }
    }
}

