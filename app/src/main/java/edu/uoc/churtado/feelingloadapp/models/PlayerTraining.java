package edu.uoc.churtado.feelingloadapp.models;

import java.util.Date;

public class PlayerTraining {
    Date Date;
    int RPE;

    public boolean HasRegisteredRPE(){
        return RPE > 0;
    }
}
