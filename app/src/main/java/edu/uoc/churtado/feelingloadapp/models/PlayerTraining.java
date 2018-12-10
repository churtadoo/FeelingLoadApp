package edu.uoc.churtado.feelingloadapp.models;

import java.util.Date;

public class PlayerTraining implements Comparable<PlayerTraining>{
    Date date;
    int rpe;

    public PlayerTraining() {}
    public PlayerTraining(Date date){
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

    public void registerRPE(int rpe) {
        this.rpe = rpe;
    }

    @Override
    public int compareTo(PlayerTraining training) {
        if (getDate() == null || training.getDate() == null) {
            return 0;
        }
        return getDate().compareTo(training.getDate());
    }
}
