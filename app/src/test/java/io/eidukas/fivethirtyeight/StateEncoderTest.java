package io.eidukas.fivethirtyeight;

import org.junit.Test;

import static org.junit.Assert.*;

public class StateEncoderTest {
    @Test
    public void testStateLookup(){
        assertEquals("United States", StateEncoder.STATE_MAP.get("US"));
        assertEquals("Alaska", StateEncoder.STATE_MAP.get("AK"));
        assertEquals("Hawaii", StateEncoder.STATE_MAP.get("HI"));
    }

}