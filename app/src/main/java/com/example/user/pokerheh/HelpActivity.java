package com.example.user.pokerheh;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 12.04.2016.
 */
public class HelpActivity extends AppCompatActivity {
    Bitmap bm;
    Map<String, Bitmap> hm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        final ImageView rf1=(ImageView)findViewById(R.id.rf1);
        final ImageView rf2=(ImageView)findViewById(R.id.rf2);
        final ImageView rf3=(ImageView)findViewById(R.id.rf3);
        final ImageView  rf4=(ImageView)findViewById(R.id.rf4);
        final ImageView rf5=(ImageView)findViewById(R.id.rf5);
        final ImageView sf1 =(ImageView)findViewById(R.id.sf1);
        final ImageView sf2=(ImageView)findViewById(R.id.sf2);
        final ImageView sf3=(ImageView)findViewById(R.id.sf3);
        final ImageView sf4=(ImageView)findViewById(R.id.sf4);
        final ImageView sf5=(ImageView)findViewById(R.id.sf5);
        final ImageView foak1=(ImageView)findViewById(R.id.foak1);
        final ImageView foak2=(ImageView)findViewById(R.id.foak2);
        final ImageView foak3=(ImageView)findViewById(R.id.foak3);
        final ImageView foak4=(ImageView)findViewById(R.id.foak4);
        final ImageView fh1=(ImageView)findViewById(R.id.fh1);
        final ImageView fh2=(ImageView)findViewById(R.id.fh2);
        final ImageView fh3=(ImageView)findViewById(R.id.fh3);
        final ImageView fh4=(ImageView)findViewById(R.id.fh4);
        final ImageView fh5=(ImageView)findViewById(R.id.fh5);
        final ImageView f1=(ImageView)findViewById(R.id.f1);
        final ImageView f2=(ImageView)findViewById(R.id.f2);
        final ImageView f3=(ImageView)findViewById(R.id.f3);
        final ImageView f4=(ImageView)findViewById(R.id.f4);
        final ImageView f5=(ImageView)findViewById(R.id.f5);
        final ImageView s1 =(ImageView)findViewById(R.id.s1);
        final ImageView s2=(ImageView)findViewById(R.id.s2);
        final ImageView s3=(ImageView)findViewById(R.id.s3);
        final ImageView s4=(ImageView)findViewById(R.id.s4);
        final ImageView s5=(ImageView)findViewById(R.id.s5);
        final ImageView set1=(ImageView)findViewById(R.id.set1);
        final ImageView set2=(ImageView)findViewById(R.id.set2);
        final ImageView set3=(ImageView)findViewById(R.id.set3);
        final ImageView dc1=(ImageView)findViewById(R.id.dc1);
        final ImageView dc2=(ImageView)findViewById(R.id.dc2);
        final ImageView dc3=(ImageView)findViewById(R.id.dc3);
        final ImageView dc4=(ImageView)findViewById(R.id.dc4);
        final ImageView c1=(ImageView)findViewById(R.id.c1);
        final ImageView c2=(ImageView)findViewById(R.id.c2);

        bm = BitmapFactory.decodeResource(getResources(), R.drawable.cards2);
        hm = new HashMap<String, Bitmap>();
        for (Suit suit : Suit.values())
        {
            for (Value value : Value.values()) {
                Card card = new Card(suit, value);
                int i = card.value.weight - 2;
                int sui = card.suit.sui;
                hm.put(card.toString(), Bitmap.createScaledBitmap(Bitmap.createBitmap(bm,i*197,sui*285, 197, 285),138, 200, false));
            }
        }
        rf1.setImageBitmap(hm.get("Spades | ten"));
        rf2.setImageBitmap(hm.get("Spades | jack"));
        rf3.setImageBitmap(hm.get("Spades | queen"));
        rf4.setImageBitmap(hm.get("Spades | king"));
        rf5.setImageBitmap(hm.get("Spades | ace"));

        sf1.setImageBitmap(hm.get("Spades | three"));
        sf2.setImageBitmap(hm.get("Spades | four"));
        sf3.setImageBitmap(hm.get("Spades | five"));
        sf4.setImageBitmap(hm.get("Spades | six"));
        sf5.setImageBitmap(hm.get("Spades | seven"));

        foak1.setImageBitmap(hm.get("Spades | queen"));
        foak2.setImageBitmap(hm.get("Diamonds | queen"));
        foak3.setImageBitmap(hm.get("Clubs | queen"));
        foak4.setImageBitmap(hm.get("Hearts | queen"));

        fh1.setImageBitmap(hm.get("Clubs | five"));
        fh2.setImageBitmap(hm.get("Hearts | five"));
        fh3.setImageBitmap(hm.get("Diamonds | king"));
        fh4.setImageBitmap(hm.get("Spades | king"));
        fh5.setImageBitmap(hm.get("Clubs | king"));

        f1.setImageBitmap(hm.get("Diamonds | two"));
        f2.setImageBitmap(hm.get("Diamonds | three"));
        f3.setImageBitmap(hm.get("Diamonds | ten"));
        f4.setImageBitmap(hm.get("Diamonds | queen"));
        f5.setImageBitmap(hm.get("Diamonds | ace"));

        s1.setImageBitmap(hm.get("Spades | seven"));
        s2.setImageBitmap(hm.get("Hearts | eight"));
        s3.setImageBitmap(hm.get("Diamonds | nine"));
        s4.setImageBitmap(hm.get("Hearts | ten"));
        s5.setImageBitmap(hm.get("Clubs | jack"));

        set1.setImageBitmap(hm.get("Hearts | six"));
        set2.setImageBitmap(hm.get("Clubs | six"));
        set3.setImageBitmap(hm.get("Spades | six"));

        dc1.setImageBitmap(hm.get("Diamonds | three"));
        dc2.setImageBitmap(hm.get("Spades | three"));
        dc3.setImageBitmap(hm.get("Hearts | jack"));
        dc4.setImageBitmap(hm.get("Spades | jack"));

        c1.setImageBitmap(hm.get("Clubs | eight"));
        c2.setImageBitmap(hm.get("Diamonds | eight"));
    }
}
