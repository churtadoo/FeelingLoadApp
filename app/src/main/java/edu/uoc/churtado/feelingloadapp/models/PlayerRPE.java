package edu.uoc.churtado.feelingloadapp.models;

public class PlayerRPE {
    private String PlayerEmail;
    private int RPE;

    public PlayerRPE(String playerEmail, int rpe){
        this.PlayerEmail = playerEmail;
        this.RPE = rpe;
    }

    public String getPlayerEmail(){
        return this.PlayerEmail;
    }

    public int getRPE(){
        return this.RPE;
    }

    public boolean HasRegisteredRPE(){
        return RPE > 0;
    }
}
