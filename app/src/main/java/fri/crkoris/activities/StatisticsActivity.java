package fri.crkoris.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fri.crkoris.R;
import fri.crkoris.models.CharacterModel;

public class StatisticsActivity extends Activity implements OnItemSelectedListener {
    private String[] langs;
    private Map<String,String[]> langMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        langs = LanguageActivity.langs;
        langMap = getAllScores(getAll());
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("all Languages");
        categories.add("english");
        categories.add("japanese");
        categories.add("slovene");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    public Map<String,CharacterModel[]> getAll(){
        Map<String, CharacterModel[]> LangMap = new HashMap<String, CharacterModel[]>();
        String[] languages = langs;
        SharedPreferences savedData = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        CharacterModel[] mCharacters;
        for(String lang : languages){
            String results_string = savedData.getString(lang + "_lrn", "");
            if (!results_string.equals("")) {
                mCharacters = gson.fromJson(results_string, CharacterModel[].class);
                LangMap.put(lang, mCharacters);
            }
            else{
                LangMap.put(lang, new CharacterModel[0]);
            }

        }
        return LangMap;
    }

    private Map<String,String[]> getAllScores(Map<String,CharacterModel[]> map){
        Map<String,String[]> results = new HashMap<String, String[]>();
        int minTotal = 0;
        String minLetterTotal = "/";
        int maxTotal = 0;
        String maxLetterTotal = "/";
        int totalTotal = 0;
        int length = 0;
        for(Map.Entry<String,CharacterModel[]> entry : map.entrySet()){
            if(entry.getValue().length == 0){
                results.put(entry.getKey(), new String[] {"0","0","/","0","/","0"});
                continue;
            }
            length += entry.getValue().length;
            int min = 5;
            String minLetter = "";
            int max = -1;
            String maxLetter = "";
            int total = 0;
            for(CharacterModel cm : entry.getValue()){
                int score = cm.getScore() == -1 ? 0 : cm.getScore();
                if(score > max){
                    max = score;
                    maxLetter = cm.getName();
                }
                if(score < min){
                    min = score;
                    minLetter = cm.getName();
                }
                total += score;
                totalTotal+=score;
            }
            if(max > maxTotal){
                maxTotal = max;
                maxLetterTotal = maxLetter;
            }
            if(min <= minTotal){
                minTotal = min;
                minLetterTotal = minLetter;
            }
            int average = total / entry.getValue().length;

            results.put(entry.getKey(), new String[] {String.valueOf(total),String.valueOf(average),maxLetter,String.valueOf(max),minLetter,String.valueOf(min)});
        }

        int averageTotal = length == 0 ? 0 : totalTotal / length;
        results.put("all Languages",new String[] {String.valueOf(totalTotal),String.valueOf(averageTotal),maxLetterTotal,String.valueOf(maxTotal),minLetterTotal,String.valueOf(minTotal)});
        return results;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String language = parent.getItemAtPosition(position).toString();
        String[] results = langMap.get(language);

        ((TextView)findViewById(R.id.TV_totalscore)).setText("Your total score is : " + results[0]);
        ((TextView)findViewById(R.id.TV_avScore)).setText("Your average score is : " + results[1]);
        ((TextView)findViewById(R.id.TV_maxLetter)).setText("You scored highest on letter '"+results[2]+"' with score of "+results[3]);
        ((TextView)findViewById(R.id.TV_minLetter)).setText("You scored lowest on letter '"+results[4]+"' with score of "+results[5]);
        ((TextView)findViewById(R.id.TV_language)).setText(language.toUpperCase());



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
