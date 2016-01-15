package fri.crkoris.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import fri.crkoris.R;

public class LanguageActivity extends Activity {

    public static String[] langs = {"english","slovene","japanese"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
    }

    public void pickEnglish(View v) {
        Intent intent = new Intent(LanguageActivity.this, LearningActivity.class);
        intent.putExtra("language", "english");
        startActivity(intent);
    }

    public void pickSlovenian(View v) {
        Intent intent = new Intent(LanguageActivity.this, LearningActivity.class);
        intent.putExtra("language", "slovene");
        startActivity(intent);
    }

    public void pickJapanese(View v) {
        Intent intent = new Intent(LanguageActivity.this, LearningActivity.class);
        intent.putExtra("language", "japanese");
        startActivity(intent);
    }
}
