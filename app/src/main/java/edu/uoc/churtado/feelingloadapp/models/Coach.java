package edu.uoc.churtado.feelingloadapp.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Coach extends User {
    private List<Player> Players;
    private List<Training> Trainings;

    public Coach(){
        Players = new ArrayList<Player>();
        Trainings = new ArrayList<Training>();
    }

    public List<Player> getPlayers(){
        return this.Players;
    }

    public List<Training> getTrainings(){
        return this.Trainings;
    }

    public void addPlayer(Player player){
        this.Players.add(player);
        Date currentDate = new Date();
        for(int i = 0; i < this.Trainings.size(); ++i){
            if(this.Trainings.get(i).getDate().after(currentDate)){
                this.Trainings.get(i).addPlayerRPE(player.getEmail());
            }
        }
    }
}
