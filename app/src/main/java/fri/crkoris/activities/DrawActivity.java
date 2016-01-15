package fri.crkoris.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Timer;

import fri.crkoris.R;
import fri.crkoris.models.CharacterModel;
import fri.crkoris.views.PaintView;

public class DrawActivity extends Activity {

    CharacterModel mCharacter;
    String language;
    private PaintView myView;
    private int mListPosition;
    public static int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        boolean challenge = intent.getExtras().getBoolean("challenge");
        mCharacter = intent.getParcelableExtra("character");
        if(!challenge){
            setContentView(R.layout.activity_draw);
            myView = (PaintView) findViewById(R.id.paint_view_id);
            mListPosition = intent.getIntExtra("position", -1);
            language = intent.getStringExtra("language");
            myView.setCharacter(mCharacter.getName(), mCharacter.getWellKnown());
        } else{
            setContentView(R.layout.activity_drawchallenge);
            myView = (PaintView) findViewById(R.id.paint_view_id2);
            myView.setCharacter(mCharacter.getName(), mCharacter.getWellKnown());
            ChallengeMode();
        }
    }

    private void ChallengeMode(){
        new CountDownTimer(6000, 1000) {
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished<2000){
                    String sc = showScore();
                    score += Integer.parseInt(sc);
                    ((TextView)findViewById(R.id.TV_drawscore)).setText("You scored : " + sc);
                }
                else{
                    final Toast toast = Toast.makeText(getApplicationContext(), String.valueOf((millisUntilFinished-2000) / 1000), Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 1000);
                }
            }
            public void onFinish() {
                finish();

            }

        }.start();
    }

    public String showScore(){
        float accuracy = (100 * myView.mHits / (float) myView.mTries);
        float filled = (float) (myView.finalizeBitmap() * 100 * 2.5);
        int score = (int) (accuracy * filled * 0.1);
        return String.valueOf(score);

    }

    public void StopButton(View view) {
        ChallengeActivity.stop = true;
        Intent intent = new Intent(this, ChallengeActivity.class);
        startActivity(intent);
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
