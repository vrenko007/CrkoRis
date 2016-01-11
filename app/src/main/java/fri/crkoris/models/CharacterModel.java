package fri.crkoris.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dominik on 30/12/2015.
 */
public class CharacterModel implements Parcelable {

    public static final Creator<CharacterModel> CREATOR = new Creator<CharacterModel>() {
        @Override
        public CharacterModel createFromParcel(Parcel in) {
            return new CharacterModel(in);
        }

        @Override
        public CharacterModel[] newArray(int size) {
            return new CharacterModel[size];
        }
    };
    String name;
    String normalized;
    int position;
    int score;
    int well_known;

    public CharacterModel(String name, String ascii, int position, int accuracy,int well_known) {
        this.name = name;
        this.normalized = ascii;
        this.position = position;
        this.score = accuracy;
        this.well_known = well_known;
    }

    protected CharacterModel(Parcel in) {
        name = in.readString();
        normalized = in.readString();
        position = in.readInt();
        score = in.readInt();
        well_known = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(normalized);
        dest.writeInt(position);
        dest.writeInt(score);
        dest.writeInt(well_known);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalized() {
        return normalized;
    }

    public void setNormalized(String normalized) {
        this.normalized = normalized;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWellKnown() {
        return well_known;
    }

    public void setWellKnown(int well_known) {
        this.well_known = well_known;
    }
}
