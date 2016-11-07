package io.eidukas.fivethirtyeight.Models;

import java.util.Map;

public class PartyModel {
    private String party;
    private String candidate;
    private Map<String,InnerModel> models;


    public String getParty(){
        return party;
    }
    public String getCandidate(){
        return candidate;
    }
    public Map<String, InnerModel> getModels(){
        return models;
    }
}

