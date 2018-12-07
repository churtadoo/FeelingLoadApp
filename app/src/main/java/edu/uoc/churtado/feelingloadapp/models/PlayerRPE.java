package edu.uoc.churtado.feelingloadapp.models;

public class PlayerRPE {
    private String playerEmail;
    private int rpe;

    public PlayerRPE(){

    }

    public PlayerRPE(String playerEmail, int rpe){
        this.playerEmail = playerEmail;
        this.rpe = rpe;
    }

    public String getPlayerEmail(){
        return this.playerEmail;
    }

    public int getRPE(){
        return this.rpe;
    }

    public boolean HasRegisteredRPE(){
        return rpe > 0;
    }

    public void registerRpe(int rpe){
        this.rpe = rpe;
    }
}
