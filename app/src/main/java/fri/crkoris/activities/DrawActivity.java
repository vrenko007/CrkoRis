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
        myView.setCharacter(mCharacter.getName());
    }

    public void finished(View v) {

        setContentView(R.layout.activity_results);
        TextView text = (TextView) findViewById(R.id.results_text);
        int accuracy = (int) (100 * myView.mHits / (float) myView.mTries);
        saveResult(accuracy);
        text.setText(String.format("%s%d%%", getString(R.string.accuracy_results), accuracy));
    }

    private boolean saveResult(int accuracy) {
        SharedPreferences savedData = PreferenceManager.getDefaultSharedPreferences(this);
        String results_string = savedData.getString("learning_results", "");
        final CharacterModel[] results;
        Gson gson = new Gson();
        if (!results_string.equals("")) {
            results = gson.fromJson(results_string, CharacterModel[].class);
            if (accuracy > results[mListPosition].getAccuracy()) {
                SharedPreferences.Editor editor;
                editor = savedData.edit();
                results[mListPosition].setAccuracy(accuracy);
                editor.putString("learning_results", gson.toJson(results));
                editor.commit();
            }
            return true;
        }
        return false;
    }

}
