package io.eidukas.fivethirtyeight;

import org.junit.Before;
import org.junit.Test;

import io.eidukas.fivethirtyeight.Models.Model;
import io.eidukas.fivethirtyeight.Models.ModelContainer;
import io.eidukas.fivethirtyeight.Models.Models;
import io.eidukas.fivethirtyeight.Models.ProbabilityItem;

import static org.junit.Assert.*;


public class ProbabilityItemTest {
    private ProbabilityItem originalItem;
    private ProbabilityItem updatedItem;
    private final double p1 = 0.2;
    private final double p2 = 0.3;
    private final double p3 = 0.4;
    private final double p4 = 0.5;
    private final double p5 = 0.6;
    private final double p6 = 0.7;
    private final String clinton = "Clinton";
    private final String trump = "Trump";
    private final String firstState = "AK";
    private final String secondSTate ="HI";
    @Before
    public void init(){
        originalItem = new ProbabilityItem(firstState, new ModelContainer(new Model(p1, trump), new Model(p2, trump), new Model(p3, clinton)), Models.PLUS);
        updatedItem = new ProbabilityItem(secondSTate, new ModelContainer(new Model(p4, clinton), new Model(p5, clinton), new Model(p6, trump)),Models.NOW);
    }

    @Test
    public void getState() throws Exception {
        assertEquals(originalItem.getState(), "Alaska");
        assertNotEquals(originalItem.getState(), "Hawaii");

        assertEquals(updatedItem.getState(), "Hawaii");
        assertNotEquals(updatedItem.getState(), "Alaska");
    }

    @Test
    public void getProbability() throws Exception {
        assertTrue(originalItem.getProbability() == p1);
        originalItem.setMode(Models.NOW);
        assertTrue(originalItem.getProbability() == p2);
        originalItem.setMode(Models.POLLS);
        assertTrue(originalItem.getProbability() == p3);
        originalItem.setMode(Models.PLUS);

    }

    @Test
    public void getCandidate() throws Exception {
        assertEquals(originalItem.getCandidate(),trump);
        originalItem.setMode(Models.NOW);
        assertEquals(originalItem.getCandidate(), trump);
        originalItem.setMode(Models.POLLS);
        assertEquals(originalItem.getCandidate(), clinton);
        originalItem.setMode(Models.PLUS);
    }

    @Test
    public void update() throws Exception {
        assertEquals(originalItem.getCandidate(),trump);
        assertTrue(originalItem.getProbability() == p1);
        originalItem.update(updatedItem);
        assertEquals(originalItem.getCandidate(),clinton);
        assertTrue(originalItem.getProbability() == p5);
    }

}