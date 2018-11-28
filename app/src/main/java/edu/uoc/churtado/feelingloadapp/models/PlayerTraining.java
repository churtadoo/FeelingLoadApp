package edu.uoc.churtado.feelingloadapp.models;

import java.util.Date;

public class PlayerTraining {
    Date Date;
    int RPE;

    public PlayerTraining(Date date){
        this.Date = date;
    }

    public Date getDate(){
        return this.Date;
    }

    public int getRPE(){
        return this.RPE;
    }

    public boolean HasRegisteredRPE(){
        return RPE > 0;
    }
}
