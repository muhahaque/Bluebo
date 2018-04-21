package edu.swarthmore.cs.cs71.blueboapplication.Model;

import java.util.Date;

public class User {

    private Date dateJoined;
    private String username;
    private String Uid;

    public User(String username) {
        this.username = username;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return Uid;
    }
}
