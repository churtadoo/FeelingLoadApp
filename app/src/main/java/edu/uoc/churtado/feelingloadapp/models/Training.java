package edu.uoc.churtado.feelingloadapp.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Training {
    private Date date;
    private List<PlayerRPE> playerRPEs;

    public Training(Date date){
        this.date = date;
        this.playerRPEs = new ArrayList<PlayerRPE>();
    }

    public Date getDate(){
        return this.date;
    }

    public List<PlayerRPE> getRPEs(){
        return this.playerRPEs;
    }

    public void addPlayerRPE(String playerEmail){
        PlayerRPE playerRPE = new PlayerRPE(playerEmail, 0);
        this.playerRPEs.add(playerRPE);
    }

    public boolean allPlayersWithRPERegistered(){
        for(int i = 0; i < this.playerRPEs.size(); ++i){
            if(!this.playerRPEs.get(i).HasRegisteredRPE()) return false;
        }
        return true;
    }
}
