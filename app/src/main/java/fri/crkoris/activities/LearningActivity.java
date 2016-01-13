package fri.crkoris.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fri.crkoris.R;
import fri.crkoris.adapters.CharacterAdapter;
import fri.crkoris.models.CharacterModel;

public class LearningActivity extends Activity {

    static final String[] firstTimeSetUpEn = new String[]{
            "A", "a", "B", "b", "C", "c", "D", "d", "E", "e", "F", "f", "G", "g", "H", "h",
            "I", "i", "J", "j", "K", "k", "L", "l", "M", "m", "N", "n", "O", "o", "P", "p",
            "Q", "q", "R", "r", "S", "s", "T", "t", "U", "u", "V", "v", "W", "w", "X", "x",
            "Y", "y", "Z", "z"
    };
    static final String[] firstTimeSetUpSlo = new String[]{
            "A", "a", "B", "b", "C", "c", "Č", "č", "D", "d", "E", "e", "F", "f", "G", "g", "H", "h",
            "I", "i", "J", "j", "K", "k", "L", "l", "M", "m", "N", "n", "O", "o", "P", "p",
            "R", "r", "S", "s", "Š", "š", "T", "t", "U", "u", "V", "v",
            "Z", "z", "Ž", "ž"
    };
    static final String[] firstTimeSetUpJapKatakana = new String[]{
            "ア", "イ", "ウ", "エ", "オ", "カ", "キ", "ク", "ケ", "コ", "サ", "シ", "ス", "セ", "ソ", "タ", "チ", "ツ",
            "テ", "ト", "ナ", "ニ", "ヌ", "ネ", "ノ", "ハ", "ヒ", "フ", "ヘ", "ホ", "マ", "ミ", "ム", "メ", "モ", "ヤ",
            "ユ", "ヨ", "ラ", "リ", "ル", "レ", "ロ", "ガ", "ギ", "グ", "ゲ", "ゴ", "ザ", "ジ", "ズ", "ゼ", "ゾ", "ダ",
            "ヂ", "ヅ", "デ", "ド", "バ", "ビ", "ブ", "ベ", "ボ", "パ", "ピ", "プ", "ペ", "ポ", "ン"
    };
    static final String[] firstTimeSetUpJapKNormal = new String[]{
            "a", "i", "u", "e", "o", "ka", "ki", "ku", "ke", "ko", "sa", "shi", "su", "se", "so", "ta", "chi", "tsu",
            "te", "to", "na", "ni", "nu", "ne", "no", "ha", "hi", "fu", "he", "ho", "ma", "mi", "mu", "me", "mo",
            "ya", "yu", "yo", "ra", "ri", "ru", "re", "ro", "ga", "gi", "gu", "ge", "go", "za", "dzi", "zu", "ze",
            "zo", "da", "dzi", "dzu", "de", "do", "ba", "bi", "bu", "be", "bo", "pa", "pi", "pu", "pe", "po", "n"
    };
    ListView mListView;
    EditText mFilterView;
    CharacterAdapter mAdapter;
    String language;
    boolean firstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstTime = true;
        Intent intent = getIntent();
        language = intent.getStringExtra("language");
        setContentView(R.layout.activity_learn);
        mListView = (ListView) findViewById(R.id.learn_list);
        mFilterView = (EditText) findViewById(R.id.learn_filter);
        CharacterModel[] values = retrieveSavedData();
        setUpAdapter(values);
        setUpListViewOnClick();
        setUpFilterView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!firstTime) {
            CharacterModel[] values = retrieveSavedData();
            mAdapter.updateData(values);
        }
        firstTime = false;

    }

    private void setUpFilterView() {
        mFilterView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setUpListViewOnClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CharacterModel itemValue = (CharacterModel) parent.getAdapter().getItem(position);
                Intent intent = new Intent(LearningActivity.this, DrawActivity.class);
                intent.putExtra("character", itemValue);
                intent.putExtra("language", language);
                intent.putExtra("position", itemValue.getPosition());
                startActivity(intent);
            }
        });
    }

    private void setUpAdapter(CharacterModel[] values) {
        mAdapter = new CharacterAdapter(this, android.R.layout.simple_list_item_2, values);
        mListView.setAdapter(mAdapter);
    }

    private CharacterModel[] retrieveSavedData() {
        SharedPreferences savedData = PreferenceManager.getDefaultSharedPreferences(this);
        String results_string = savedData.getString(language + "_lrn", "");
        Gson gson = new Gson();
        CharacterModel[] mCharacters;
        if (!results_string.equals("")) {
            mCharacters = gson.fromJson(results_string, CharacterModel[].class);
        } else {
            if (language.equals("english")) {
                mCharacters = new CharacterModel[firstTimeSetUpEn.length];
                for (int i = 0; i < mCharacters.length; i++)
                    mCharacters[i] = new CharacterModel(firstTimeSetUpEn[i], firstTimeSetUpEn[i], i, -1, 0);
            } else if (language.equals("slovene")) {
                mCharacters = new CharacterModel[firstTimeSetUpSlo.length];
                for (int i = 0; i < mCharacters.length; i++)
                    mCharacters[i] = new CharacterModel(firstTimeSetUpSlo[i], firstTimeSetUpSlo[i], i, -1, 0);
            } else if (language.equals("japanese")) {
                mCharacters = new CharacterModel[firstTimeSetUpJapKatakana.length];
                for (int i = 0; i < mCharacters.length; i++)
                    mCharacters[i] = new CharacterModel(firstTimeSetUpJapKatakana[i], firstTimeSetUpJapKNormal[i], i, -1, 0);
            } else
                return new CharacterModel[1];

            SharedPreferences.Editor editor;
            editor = savedData.edit();
            editor.putString(language + "_lrn", gson.toJson(mCharacters));
            editor.apply();
        }
        return mCharacters;
    }

}
