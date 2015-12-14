package com.example.dominik.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {
    private static Button b_learn;

    //@Override
   // protected void onCreate(Bundle savedInstanceState) {
    //    super.onCreate(savedInstanceState);
     //   PaintView paint_view = new PaintView(this);
        //setContentView(paint_view);
    //}

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void OnClickButtonLearn(View view) { //ob pritisku na gumb LearningActivity odpri LearningActivity activity
        Intent intent = new Intent(this, LearningActivity.class);
        startActivity(intent);


    }

    public void OnClickButtonSettings(View view) { //ob pritisku na gumb LearningActivity odpri LearningActivity activity
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);


    }

    public void OnClickButtonTest(View view) { //ob pritisku na gumb LearningActivity odpri LearningActivity activity
        Intent intent = new Intent(this, ChallengeActivity.class);
        startActivity(intent);


    }

    public void OnClickButtonStatistics(View view) { //ob pritisku na gumb LearningActivity odpri LearningActivity activity
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
