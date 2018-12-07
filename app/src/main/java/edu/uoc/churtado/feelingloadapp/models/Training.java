package edu.uoc.churtado.feelingloadapp.models;

import java.util.ArrayList;
import java.util.Date;

public class Training {
    private Date date;
    private ArrayList<PlayerRPE> rpes;

    public Training(){
        this.rpes = new ArrayList<PlayerRPE>();
    }

    public Training(Date date){
        this.date = date;
        this.rpes = new ArrayList<PlayerRPE>();
    }

    public Date getDate(){
        return this.date;
    }

    public ArrayList<PlayerRPE> getRPEs(){
        return this.rpes;
    }

    public void addPlayerRPE(String playerEmail){
        PlayerRPE playerRPE = new PlayerRPE(playerEmail, 0);
        this.rpes.add(playerRPE);
    }

    public boolean allPlayersWithRPERegistered(){
        for(int i = 0; i < this.rpes.size(); ++i){
            if(!this.rpes.get(i).HasRegisteredRPE()) return false;
        }
        return true;
    }

    public void setDate(Date date) { this.date = date; }
}
