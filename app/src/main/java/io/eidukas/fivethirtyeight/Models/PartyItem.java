package io.eidukas.fivethirtyeight.Models;


public class PartyItem {
    private PartyModel model;
    private Models mode;

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


}
