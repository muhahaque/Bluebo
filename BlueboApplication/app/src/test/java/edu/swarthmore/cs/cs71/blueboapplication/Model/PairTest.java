package edu.swarthmore.cs.cs71.blueboapplication.Model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PairTest {
    @Test
    public void getFirst() throws Exception {
        String first = "hello";
        String second = "world!";
        Pair<String, String> pair = new Pair<>(first, second);
        assertEquals(first, pair.getFirst());
    }

    @Test
    public void getSecond() throws Exception {
        String first = "hello";
        String second = "world!";
        Pair<String, String> pair = new Pair<>(first, second);
        assertEquals(second, pair.getSecond());
    }

}