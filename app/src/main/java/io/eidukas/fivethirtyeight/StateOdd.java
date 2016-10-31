package io.eidukas.fivethirtyeight;

/**
 * Just another container in the pojo decomposition.
 */
public class StateOdd{
    private String state;
    private ModelContainer sentences;

    public String getState(){
        return state;
    }

    public ModelContainer getSentences(){
        return sentences;
    }
}