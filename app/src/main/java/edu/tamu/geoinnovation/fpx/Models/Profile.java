package edu.tamu.geoinnovation.fpx.Models;

public class Profile {

    public String name;
    public String count;


    public Profile() {
        // Default constructor required for calls to DataSnapshot.getValue(Profile.class)
    }

    public Profile(String name, String count) {
        this.name = name;
        this.count = count;
    }

}