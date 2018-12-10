package edu.uoc.churtado.feelingloadapp.models;

public class PlayerRPE {
    private String playerEmail;
    private int rpe;

    public PlayerRPE(){

    }

    PlayerRPE(String playerEmail, int rpe){
        this.playerEmail = playerEmail;
        this.rpe = rpe;
    }

    public String getPlayerEmail(){
        return this.playerEmail;
    }

    public int getRPE(){
        return this.rpe;
    }

    boolean HasRegisteredRPE(){
        return rpe > 0;
    }

    void registerRpe(int rpe){
        this.rpe = rpe;
    }
}
