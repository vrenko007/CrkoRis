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

import fri.crkoris.R;
import fri.crkoris.adapters.CharacterAdapter;
import fri.crkoris.models.CharacterModel;

public class LearningActivity extends Activity {

    static final String[] firstTimeSetUp = new String[]{
            "A", "a", "B", "b", "C", "c", "D", "d", "E", "e", "F", "f", "G", "g", "H", "h",
            "I", "i", "J", "j", "K", "k", "L", "l", "M", "m", "N", "n", "O", "o", "P", "p",
            "Q", "q", "R", "r", "S", "s", "T", "t", "U", "u", "V", "v", "W", "w", "X", "x",
            "Y", "y", "Z", "z"
    };
    ListView mListView;
    EditText mFilterView;
    CharacterAdapter mAdapter;
    boolean firstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstTime = true;
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
        String results_string = savedData.getString("learning_results", "");
        Gson gson = new Gson();
        CharacterModel[] mEnglish;
        if (!results_string.equals("")) {
            mEnglish = gson.fromJson(results_string, CharacterModel[].class);
        } else {
            mEnglish = new CharacterModel[firstTimeSetUp.length];
            for (int i = 0; i < mEnglish.length; i++)
                mEnglish[i] = new CharacterModel(firstTimeSetUp[i], firstTimeSetUp[i], i, -1, 0);

            SharedPreferences.Editor editor;
            editor = savedData.edit();
            editor.putString("learning_results", gson.toJson(mEnglish));
            editor.commit();
        }
        return mEnglish;
    }

}
