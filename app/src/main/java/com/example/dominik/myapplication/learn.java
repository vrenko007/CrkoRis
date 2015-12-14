package com.example.dominik.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class learn extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
    }


    public void OnClickButtonLearnChinese(View view){ //ob pritisku na gumb learn odpri kitajske crke
        PaintView paint_view = new PaintView(this);
        setContentView(paint_view);
    }

}
