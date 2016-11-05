package io.eidukas.fivethirtyeight.Models;

import io.eidukas.fivethirtyeight.StateEncoder;

/**
 * Wrapper object for the json response. This returns the probability and candidate for a specified model (Polls-plus, nowcast, and polls only).
 * This is what is used to make the data for the listview to work off of.
 */
public class ProbabilityItem {
    private String state;
    private ModelContainer models;
    private Models mode;

    public String getState() {
        return state;
    }

    /**
     * Fetches the state name from the abbreviation table, and handles numbers in the state name.
     * @param state Abbreviation of state name.
     */
    public void setState(String state) {
        if (Character.isDigit(state.charAt(state.length() - 1))){
            this.state = StateEncoder.STATE_MAP.get(state.substring(0,state.length() - 1)) + state.charAt(state.length() - 1);
        } else{
            this.state = StateEncoder.STATE_MAP.get(state);
        }
    }

    /**
     * @return the probability for the current model's decided winner.
     */
    public double getProbability() {
        switch(mode){
            case NOW:
                return models.getNow().getProbability();
            case POLLS:
                return models.getPolls().getProbability();
            case PLUS:
                return models.getPlus().getProbability();
        }
        return 0;
    }

    /**
     * @return the predicted candidate for the current model's decided winner.
     */
    public String getCandidate() {
        switch(mode){
            case NOW:
                return models.getNow().getLeader();
            case POLLS:
                return models.getPolls().getLeader();
            case PLUS:
                return models.getPlus().getLeader();
        }
        return "NA";
    }

    public void setModels(ModelContainer models){
        this.models = models;
    }

    public ModelContainer getModels(){
        return models;
    }

    public ProbabilityItem(String state, ModelContainer models, Models model){
        setState(state);
        setModels(models);
        setMode(model);
    }

    public void setMode(Models model){
        this.mode = model;
    }

    public Models getMode(){
        return mode;
    }

    /**
     * Change internal values to be the same as that of another.
     * @param other Object with desired values.
     */
    public void update(ProbabilityItem other){
        this.mode = other.getMode();
        this.models = other.getModels();
        this.state = other.getState();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || !(obj instanceof ProbabilityItem)){
            return false;
        }
        ProbabilityItem other = (ProbabilityItem) obj;
        return other.getState().equals(getState());
    }
}
