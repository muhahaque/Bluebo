package edu.swarthmore.cs.cs71.blueboapplication.Model;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void setDateJoined() throws Exception {
        User testUser = new User("tuser1");
        Date dateJoined = new Date();
        dateJoined.setTime(System.currentTimeMillis());
        testUser.setDateJoined(dateJoined);
        assertEquals(dateJoined, testUser.getDateJoined());
    }

    @Test
    public void setUsername() throws Exception {
        User testUser = new User("tuser1");
        testUser.setUsername("tuser2");
        assertEquals("tuser2", testUser.getUsername());
    }

    @Test
    public void setUid() throws Exception {
        User testUser = new User("tuser1");
        testUser.setUid("abcdefg");
        assertEquals("abcdefg", testUser.getUid());
    }

    @Test
    public void getDateJoined() throws Exception {
        User testUser = new User("tuser1");
        Date dateJoined = new Date();
        dateJoined.setTime(System.currentTimeMillis());
        testUser.setDateJoined(dateJoined);
        assertEquals(dateJoined, testUser.getDateJoined());
    }

    @Test
    public void getUsername() throws Exception {
        User testUser = new User("tuser1");
        assertEquals("tuser1", testUser.getUsername());
    }

    @Test
    public void getUid() throws Exception {
        User testUser = new User("tuser1");
        testUser.setUid("abcdefg");
        assertEquals("abcdefg", testUser.getUid());

    }

}