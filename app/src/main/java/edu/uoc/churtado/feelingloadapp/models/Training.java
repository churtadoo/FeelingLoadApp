package edu.uoc.churtado.feelingloadapp.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Training {
    private Date Date;
    private List<PlayerRPE> PlayerRPEs;

    public Training(Date date){
        this.Date = date;
        this.PlayerRPEs = new ArrayList<PlayerRPE>();
    }

    public Date getDate(){
        return this.Date;
    }

    public List<PlayerRPE> getRPEs(){
        return this.PlayerRPEs;
    }

    public void addPlayerRPE(String playerEmail){
        PlayerRPE playerRPE = new PlayerRPE(playerEmail, 0);
        this.PlayerRPEs.add(playerRPE);
    }

    public boolean allPlayersWithRPERegistered(){
        for(int i = 0; i < this.PlayerRPEs.size(); ++i){
            if(!this.PlayerRPEs.get(i).HasRegisteredRPE()) return false;
        }
        return true;
    }
}
