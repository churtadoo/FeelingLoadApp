package edu.uoc.churtado.feelingloadapp.models;

import android.support.annotation.NonNull;

import java.util.Date;

public class PlayerTraining implements Comparable<PlayerTraining>{
    private Date date;
    private int rpe;

    public PlayerTraining() {}
    PlayerTraining(Date date){
        this.date = date;
    }

    public Date getDate(){
        return this.date;
    }

    public int getRPE(){
        return this.rpe;
    }

    public boolean HasRegisteredRPE(){
        return rpe > 0;
    }

    void registerRPE(int rpe) {
        this.rpe = rpe;
    }

    @Override
    public int compareTo(@NonNull PlayerTraining training) {
        if (getDate() == null || training.getDate() == null) {
            return 0;
        }
        return getDate().compareTo(training.getDate());
    }
}
