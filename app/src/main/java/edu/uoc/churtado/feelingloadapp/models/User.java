package edu.uoc.churtado.feelingloadapp.models;

public class User {
    private String email;
    private String type;

    private static final String CoachType = "Coach";

    public User(){

    }

    public User(String email, UserType type){
        this.email = email;
        this.type = getType(type);
    }

    public String getEmail(){
        return this.email;
    }

    public UserType getType(){
        return getType(this.type);
    }

    private UserType getType(String userType) {
        if(userType.equals("Coach")) return UserType.Coach;
        else return UserType.Player;
    }

    private String getType(UserType userType) {
        if(userType == UserType.Coach) return "Coach";
        else return "Player";
    }
}
