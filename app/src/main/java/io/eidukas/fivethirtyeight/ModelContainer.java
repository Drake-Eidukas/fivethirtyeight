package io.eidukas.fivethirtyeight;

/**
 * POJO class for holding the probability data for all 3 models in the json response.
 */
public class ModelContainer {
    private Model plus;
    private Model now;
    private Model polls;

    public Model getNow() {
        return now;
    }

    public Model getPlus() {
        return plus;
    }

    public Model getPolls() {
        return polls;
    }

}