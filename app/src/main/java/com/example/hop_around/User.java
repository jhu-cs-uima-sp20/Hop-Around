package com.example.hop_around;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String email;
    private String password;
    private String displayName;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String password, String displayName) {
        this.displayName = displayName;
        this.email = email;
        this.password = password;
    }

    public String getDisplayName(){
        return this.displayName;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPassword(){
        return this.password;
    }

    public boolean setDisplayName(String displayName){
        this.displayName = displayName;
        return true;
    }

    public boolean setEmail(String email){
        this.email = email;
        return true;
    }

    public boolean setPassword(String password){
        this.password = password;
        return true;
    }
}
