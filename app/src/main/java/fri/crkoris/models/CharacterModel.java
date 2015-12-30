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
    int accuracy;

    public CharacterModel(String name, String ascii, int position, int accuracy) {
        this.name = name;
        this.normalized = ascii;
        this.position = position;
        this.accuracy = accuracy;
    }

    public CharacterModel(Parcel in) {
        name = in.readString();
        normalized = in.readString();
        position = in.readInt();
        accuracy = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(name);
        parcel.writeString(normalized);
        parcel.writeInt(position);
        parcel.writeInt(accuracy);
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

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }
}
