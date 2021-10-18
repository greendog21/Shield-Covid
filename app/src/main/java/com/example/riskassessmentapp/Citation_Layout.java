package com.example.riskassessmentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Citation_Layout extends AppCompatActivity {

    private Button returnBack;

    private void openDisplay(){
        Intent intent = new Intent(this, Display_Layout.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citation__layout);

        TextView textView = (TextView) findViewById(R.id.textView_linkReal);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        returnBack = (Button) findViewById(R.id.button_backToDisplay);

        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDisplay();
            }
        });





    }
}