package fri.crkoris.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import fri.crkoris.R;
import fri.crkoris.models.CharacterModel;

public class ChallengeActivity extends Activity implements OnItemSelectedListener {

    public static String language;
    private static int score;
    public static int temp_score = 0;
    private static CharacterModel[] cm;
    private String[] letters;
    private String[] letters2;
    public static boolean stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerChallenge);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("english");
        categories.add("japanese");
        categories.add("slovene");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }


    public void StartChallenge(View view) {
        int d = letters.length;
        cm = new CharacterModel[d];
        for(int i=0;i<d;i++){
            cm[i] = new CharacterModel(letters[i],letters2[i],i,-1,0);
        }
        Set<CharacterModel> set = new HashSet<CharacterModel>(Arrays.asList(cm));
        Iterator iter = set.iterator();
        DrawActivity.score = 0;
        stop = false;
        while (iter.hasNext() && !stop) {
            CharacterModel crka = (CharacterModel) iter.next();
            Intent intent = new Intent(this, DrawActivity.class);
            intent.putExtra("character", crka);
            intent.putExtra("challenge",true);
            startActivity(intent);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        temp_score = DrawActivity.score;
        if(score<temp_score){
            score = temp_score;
            SharedPreferences.Editor editor;
            SharedPreferences savedData = PreferenceManager.getDefaultSharedPreferences(this);
            editor = savedData.edit();
            editor.putString(language + "_challenge", String.valueOf(score));
            editor.apply();
            ((TextView)findViewById(R.id.TV_highscore)).setText("High score : " + score);
            Toast toast = Toast.makeText(getApplicationContext(), "You set a new High score!!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void loadScore(String lang){

        SharedPreferences savedData = PreferenceManager.getDefaultSharedPreferences(this);
        String results_string = savedData.getString(lang + "_challenge", "");
        if (!results_string.equals("")) {
            score = Integer.parseInt(results_string);
        }
        else
            score = 0;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        language = parent.getItemAtPosition(position).toString();
        loadScore(language);
        ((TextView)findViewById(R.id.TV_highscore)).setText("High score : "+score);
        if (language.equals("english")) {
            letters = LearningActivity.firstTimeSetUpEn;
            letters2 = letters;
        }
        else if(language.equals("japanese")){
            letters = LearningActivity.firstTimeSetUpJapKatakana;
            letters2 = LearningActivity.firstTimeSetUpJapKNormal;
        }
        else{
            letters = LearningActivity.firstTimeSetUpSlo;
            letters2 = letters;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
