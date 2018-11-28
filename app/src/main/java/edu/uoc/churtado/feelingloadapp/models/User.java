package edu.uoc.churtado.feelingloadapp.models;

public class User {
    private String Email;
    private UserType Type;

    private static final String CoachType = "coach";

    public User(String email, UserType type){
        this.Email = email;
        this.Type = type;
    }

    public String getEmail(){
        return this.Email;
    }

    public UserType getType(){
        return this.Type;
    }
}
