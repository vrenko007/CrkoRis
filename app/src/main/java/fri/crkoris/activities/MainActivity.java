package fri.crkoris.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import fri.crkoris.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void OnClickButtonLearn(View view) {
        Intent intent = new Intent(this, LanguageActivity.class);
        startActivity(intent);
    }

    public void OnClickButtonTest(View view) {
        Intent intent = new Intent(this, ChallengeActivity.class);
        startActivity(intent);
    }

    public void OnClickButtonStatistics(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }
}
