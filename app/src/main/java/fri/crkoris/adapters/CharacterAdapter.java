package fri.crkoris.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import fri.crkoris.models.CharacterModel;

/**
 * Created by Dominik on 30/12/2015.
 */
public class CharacterAdapter extends ArrayAdapter<CharacterModel> implements Filterable {

    private CharacterFilter mFilter;
    private CharacterModel[] mData;
    private CharacterModel[] mFilteredData;
    private LayoutInflater mInflater;
    private Context ctx;

    public CharacterAdapter(Context context, int resource, CharacterModel[] objects) {
        super(context, resource, objects);
        this.mData = objects;
        this.mFilteredData = objects;
        this.mFilter = new CharacterFilter();
        this.ctx = context;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public CharacterModel getItem(int position) {
        if (position < 0 || position > mFilteredData.length) return null;
        return mFilteredData[position];
    }

    public void updateData(CharacterModel[] values) {
        for (int i = 0; i < values.length; i++)
            if (values[i].getAccuracy() != mData[i].getAccuracy())
                mData[i].setAccuracy(values[i].getAccuracy());
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mInflater = LayoutInflater.from(ctx);
        convertView = mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);
        if (position >= mFilteredData.length)
            return convertView;
        textView1.setText(mFilteredData[position].getName());
        if (mFilteredData[position].getAccuracy() != -1)
            textView2.setText(String.format("Top accuracy was: %d%%", mFilteredData[position].getAccuracy()));
        else
            textView2.setText("Not yet completed");
        return convertView;
    }

    private class CharacterFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final ArrayList<CharacterModel> temp = new ArrayList<CharacterModel>();

            for (CharacterModel c : mData)
                if (c.getNormalized().toLowerCase().startsWith(filterString))
                    temp.add(c);

            results.values = temp.toArray();
            results.count = temp.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mFilteredData = Arrays.copyOf((Object[]) filterResults.values, filterResults.count, CharacterModel[].class);
            notifyDataSetChanged();
        }
    }
}
