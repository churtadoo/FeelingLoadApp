package edu.uoc.churtado.feelingloadapp.models;

public class PlayerRPE {
    private String playeremail;
    private int rpe;

    public PlayerRPE(String playerEmail, int rpe){
        this.playeremail = playerEmail;
        this.rpe = rpe;
    }

    public String getPlayerEmail(){
        return this.playeremail;
    }

    public int getRPE(){
        return this.rpe;
    }

    public boolean HasRegisteredRPE(){
        return rpe > 0;
    }
}
