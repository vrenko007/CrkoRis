package fri.crkoris.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import fri.crkoris.R;
import fri.crkoris.models.CharacterModel;
import fri.crkoris.views.PaintView;

public class DrawActivity extends Activity {

    CharacterModel mCharacter;
    String language;
    private PaintView myView;
    private int mListPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        setContentView(R.layout.activity_draw);

        myView = (PaintView) findViewById(R.id.paint_view_id);
        mCharacter = intent.getParcelableExtra("character");
        mListPosition = intent.getIntExtra("position", -1);
        language = intent.getStringExtra("language");
        myView.setCharacter(mCharacter.getName(), mCharacter.getWellKnown());
    }

    public void finished(View v) {
        setContentView(R.layout.activity_results);
        TextView text = (TextView) findViewById(R.id.results_text);
        TextView score_text = (TextView) findViewById(R.id.score_text);
        text.setText(R.string.accuracy_results);
        float accuracy = (100 * myView.mHits / (float) myView.mTries);
        float filled = (float) (myView.finalizeBitmap() * 100 * 2.5);
        int score = (int) (accuracy * filled * 0.1);
        saveResult(score);
        score_text.setText(Integer.toString(score));
    }

    private boolean saveResult(int score) {
        SharedPreferences savedData = PreferenceManager.getDefaultSharedPreferences(this);
        String results_string = savedData.getString(language + "_lrn", "");
        final CharacterModel[] results;
        Gson gson = new Gson();
        if (!results_string.equals("")) {
            results = gson.fromJson(results_string, CharacterModel[].class);
            SharedPreferences.Editor editor;
            editor = savedData.edit();
            int cutoff = 900;
            if (language.equals("jap")) cutoff = 750;
            int known = results[mListPosition].getWellKnown();
            if (score > results[mListPosition].getScore())
                results[mListPosition].setScore(score);
            if (score > cutoff && known < 10)
                results[mListPosition].setWellKnown(++known);
            else if (score < cutoff && known > 0)
                results[mListPosition].setWellKnown(--known);
            editor.putString(language + "_lrn", gson.toJson(results));
            editor.apply();
            return true;
        }
        return false;
    }

}
