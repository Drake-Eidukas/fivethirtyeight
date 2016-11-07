package io.eidukas.fivethirtyeight.Models;


import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Outermost POJO from API. Included convenience method to convert json to arraylist of wrapped objects
 * for use with listview.
 */
public class ProbabilityObject {
    private StateOdd[] states;

    public StateOdd[] getStates(){
        return states;
    }

    public ProbabilityObject(StateOdd[] states){
        this.states = states;
    }

    public static ProbabilityObject fromJson(String jsonString){
        Gson gson = new Gson();
        return new ProbabilityObject(gson.fromJson(jsonString, StateOdd[].class));
    }

    /**
     * Returns an arraylist of the wrapper items used in the listview.
     * @param mode Model currently selected in settings.
     * @return Returns an arraylist of ProbabilityItem, the wrapper of this class.
     */
    public ArrayList<ProbabilityItem> toItemList(Models mode){
        ArrayList<ProbabilityItem> returnList = new ArrayList<>();
        for (StateOdd odd : states){
            ArrayList<PartyItem> items = new ArrayList<>();
            for(String key: odd.getLatest().keySet()){
                items.add(new PartyItem(odd.getLatest().get(key)));
            }
            returnList.add(new ProbabilityItem(odd.getState(), odd.getSentences(), mode, items));
        }
        return returnList;
    }
}

