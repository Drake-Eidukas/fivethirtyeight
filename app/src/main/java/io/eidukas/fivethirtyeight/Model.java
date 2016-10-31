package io.eidukas.fivethirtyeight;

/**
 * Inner-most POJO object from the api, holds the name of the most likely candidate to win and their probability to win.
 */
public class Model {
    private String leader;
    private double probability;

    public double getProbability() {
        return probability;
    }

    public String getLeader() {
        return leader;
    }

    public Model(double probability, String leader){
        this.leader = leader;
        this.probability = probability;
    }

}
