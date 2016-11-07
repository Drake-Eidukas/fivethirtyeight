package io.eidukas.fivethirtyeight.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class PartyItem implements Parcelable {
    private PartyModel model;
    private Models mode;

    public PartyItem(PartyModel model){
        this(model, Models.PLUS);
    }

    public PartyItem(PartyModel model, Models mode){
        setModel(model);
        setMode(mode);
    }

    public PartyModel getModel() {
        return model;
    }

    public void setModel(PartyModel model) {
        this.model = model;
    }

    public String getParty() {
        return model.getParty();
    }

    public double getWinShare(){
        switch(mode){
            case NOW:
                return model.getModels().get("now").getForecast();
            case POLLS:
                return model.getModels().get("polls").getForecast();
            case PLUS:
                return model.getModels().get("plus").getForecast();
        }
        return 0;
    }

    public String getCandidate(){
        return model.getCandidate();
    }

    public void update(PartyItem other){
        setMode(other.getMode());
        setModel(other.getModel());
    }

    public void setMode(Models mode){
        this.mode = mode;
    }

    public Models getMode(){
        return mode;
    }

    /**
     * Following methods from http://www.parcelabler.com/
     */
    protected PartyItem(Parcel in) {
        model = (PartyModel) in.readValue(PartyModel.class.getClassLoader());
        mode = (Models) in.readValue(Models.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(model);
        dest.writeValue(mode);
    }

    public static final Parcelable.Creator<PartyItem> CREATOR = new Parcelable.Creator<PartyItem>() {
        @Override
        public PartyItem createFromParcel(Parcel in) {
            return new PartyItem(in);
        }

        @Override
        public PartyItem[] newArray(int size) {
            return new PartyItem[size];
        }
    };
}
