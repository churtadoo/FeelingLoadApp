package edu.uoc.churtado.feelingloadapp.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Training implements Comparable<Training>{
    private Date date;
    private List<PlayerRPE> rpes;

    public Training(){
        this.rpes = new ArrayList<>();
    }

    Training(Date date){
        this.date = date;
        this.rpes = new ArrayList<>();
    }

    public Date getDate(){
        return this.date;
    }

    public List<PlayerRPE> getRPEs(){
        return this.rpes;
    }

    void addPlayerRPE(String playerEmail){
        PlayerRPE playerRPE = new PlayerRPE(playerEmail, 0);
        this.rpes.add(playerRPE);
    }

    void registerRpe(String playerEmail, int rpe){
        for(int i = 0; i < this.rpes.size(); ++i){
            PlayerRPE playerRpe = this.rpes.get(i);
            if(playerRpe.getPlayerEmail().equals(playerEmail)){
                playerRpe.registerRpe(rpe);
                this.rpes.set(i, playerRpe);
            }
        }
    }

    public boolean allPlayersWithRPERegistered(){
        for(int i = 0; i < this.rpes.size(); ++i){
            if(!this.rpes.get(i).HasRegisteredRPE()) return false;
        }
        return true;
    }

    public void setDate(Date date) { this.date = date; }

    @Override
    public int compareTo(@NonNull Training training) {
        if (getDate() == null || training.getDate() == null) {
            return 0;
        }
        return getDate().compareTo(training.getDate());
    }
}
