package com.example.user.pokerheh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by User on 08.04.2016.
 */
public class Game_over extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        TextView t=(TextView)findViewById(R.id.textView);
        Button b=(Button)findViewById(R.id.button2);
        t.setText(getIntent().getStringExtra("str"));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Game_over.this, ActivityStart.class);
                startActivity(i);
            }
        });
    }
}
