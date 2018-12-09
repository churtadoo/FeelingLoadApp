package edu.uoc.churtado.feelingloadapp.models;

public class User {
    private String email;
    private String type;
    private String name;
    private String surname;

    public User(){

    }

    public User(String email, UserType type, String name, String surname){
        this.email = email;
        this.type = getType(type);
        this.name = name;
        this.surname = surname;
    }

    public String getEmail(){
        return this.email;
    }

    public UserType getType(){
        return getType(this.type);
    }

    public String getName() { return this.name; }

    public String getSurname() { return this.surname; }

    private UserType getType(String userType) {
        if(userType.equals("Coach")) return UserType.Coach;
        else return UserType.Player;
    }

    private String getType(UserType userType) {
        if(userType == UserType.Coach) return "Coach";
        else return "Player";
    }

    public void setType(UserType userType){
        this.type = getType(userType);
    }

    public void setName(String name){
        this.name = name;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public void setEmail(String email) { this.email = email; }
}
