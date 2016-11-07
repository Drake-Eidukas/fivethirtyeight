package io.eidukas.fivethirtyeight.Models;

import java.util.Map;

/**
 * Just another container in the pojo decomposition.
 */
public class StateOdd{
    private String state;
    private ModelContainer sentences;
    private Map<String, PartyModel> latest;

    public String getState(){
        return state;
    }

    public ModelContainer getSentences(){
        return sentences;
    }

    public Map<String, PartyModel> getLatest(){
        return latest;
    }
}