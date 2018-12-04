package edu.uoc.churtado.feelingloadapp.models;

import java.util.ArrayList;
import java.util.Date;

public class Coach extends User {
    private ArrayList<Player> players;
    private ArrayList<Training> trainings;

    public Coach(){
        this.setType(UserType.Coach);
        players = new ArrayList<Player>();
        trainings = new ArrayList<Training>();
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    public ArrayList<Training> getTrainings(){
        Training training1 = new Training(new Date());
        Training training2 = new Training(new Date());
        this.trainings.add(training1);
        this.trainings.add(training2);
        return this.trainings;
    }

    public void addPlayer(Player player){
        this.players.add(player);
        Date currentDate = new Date();
        for(int i = 0; i < this.trainings.size(); ++i){
            if(this.trainings.get(i).getDate().after(currentDate)){
                this.trainings.get(i).addPlayerRPE(player.getEmail());
            }
        }
    }

    public void updatePlayer(int position, Player player) {
        this.players.set(position, player);
    }
}
