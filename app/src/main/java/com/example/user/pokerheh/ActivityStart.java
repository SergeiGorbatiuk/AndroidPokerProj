package com.example.user.pokerheh;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * Created by User on 07.04.2016.
 */
public class ActivityStart extends AppCompatActivity{
    int AInum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final Button Start=(Button)findViewById(R.id.button);
        final Button Rules=(Button)findViewById(R.id.rules);
        final EditText WalletAmount=(EditText)findViewById(R.id.editText);
        final RadioButton r1=(RadioButton)findViewById(R.id.radioButton);
        final RadioButton r2=(RadioButton)findViewById(R.id.radioButton2);
        final RadioButton r3=(RadioButton)findViewById(R.id.radioButton3);
        Start.setVisibility(View.INVISIBLE);

        AInum=1;
        r1.setChecked(true);

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r2.setChecked(false);
                r3.setChecked(false);
                AInum = 1;
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setChecked(false);
                r3.setChecked(false);
                AInum=2;
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setChecked(false);
                r2.setChecked(false);
                AInum=3;
            }
        });

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ActivityStart.this, MainActivity.class);
                int amount=Integer.parseInt(WalletAmount.getText().toString());
                i.putExtra("amount", amount);
                i.putExtra("num", AInum);
                startActivity(i);
            }
        });

        Rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityStart.this);
                builder.setTitle("Правила")
                        .setMessage(R.string.rules)
                        .setIcon(R.drawable.icon)
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        WalletAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1=s.toString();
                if (s1.length() != 0) {
                    int i = Integer.parseInt(s1);
                    if(i>10) Start.setVisibility(View.VISIBLE);
                    else Start.setVisibility(View.INVISIBLE);
                }
                else Start.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
