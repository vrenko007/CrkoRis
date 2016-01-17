package fri.crkoris.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import fri.crkoris.R;
import fri.crkoris.models.CharacterModel;
import fri.crkoris.views.PaintView;

public class DrawActivity extends Activity {

    public static int score;
    CharacterModel mCharacter;
    String language;
    private PaintView myView;
    private int mListPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        boolean challenge = intent.getExtras().getBoolean("challenge");
        mCharacter = intent.getParcelableExtra("character");
        language = intent.getStringExtra("language");
        if(!challenge){
            setContentView(R.layout.activity_draw);
            myView = (PaintView) findViewById(R.id.paint_view_id);
            mListPosition = intent.getIntExtra("position", -1);
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
                    if(sc.equals("0")){
                        ((TextView) findViewById(R.id.TV_drawscore)).setText("Score: 0\nGame over\nTotal score: "+score);
                    }
                    else{
                        ((TextView) findViewById(R.id.TV_drawscore)).setText("You scored : " + sc);
                    }
                }
                else{
                    final Toast toast = Toast.makeText(getApplicationContext(), String.valueOf((millisUntilFinished - 2000) / 1000), Toast.LENGTH_SHORT);
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
                if(showScore().equals("0")){
                    Stop();
                }
                finish();

            }

        }.start();
    }

    public String showScore(){
        return String.valueOf(calculateScore());

    }

    private void Stop(){
        ChallengeActivity.stop = true;
        Intent intent = new Intent(this, ChallengeActivity.class);
        startActivity(intent);
    }
    public void StopButton(View view) {
        Stop();
    }

    private int calculateScore() {
        float accuracy = (100 * myView.mHits / (float) myView.mTries);
        float fill_factor = (float) (!language.equals("japanese") ? 1.5 : 1.7);
        if (mCharacter.getWellKnown() == 2) fill_factor = 2;
        float filled = myView.finalizeBitmap() * 100 * fill_factor;
        if (filled > 100) filled = 100;
        return (int) (accuracy * filled * 0.1);
    }

    public void finished(View v) {
        setContentView(R.layout.activity_results);
        TextView text = (TextView) findViewById(R.id.results_text);
        TextView score_text = (TextView) findViewById(R.id.score_text);
        text.setText(R.string.accuracy_results);
        int score = calculateScore();
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
            int known = results[mListPosition].getWellKnown();
            if (score > results[mListPosition].getScore())
                results[mListPosition].setScore(score);
            if (score > 980 && known < 2)
                results[mListPosition].setWellKnown(++known);
            else if (score < 980 && known > 0)
                results[mListPosition].setWellKnown(--known);
            editor.putString(language + "_lrn", gson.toJson(results));
            editor.apply();
            return true;
        }
        return false;
    }

}
