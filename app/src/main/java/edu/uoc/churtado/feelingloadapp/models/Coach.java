package edu.uoc.churtado.feelingloadapp.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Coach extends User {
    private ArrayList<Player> players;
    private ArrayList<Training> trainings;

    public Coach(){
        this.setType(UserType.Coach);
        players = new ArrayList<>();
        trainings = new ArrayList<>();
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    public ArrayList<Training> getTrainings(){
        Collections.sort(this.trainings);
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

    public void addTraining(Date date) {
        Training training = new Training(date);
        for(int i = 0; i < this.players.size(); ++i){
            training.addPlayerRPE(this.players.get(i).getEmail());
        }
        this.trainings.add(training);

    }

    public void registerRpe(Date trainingDate, int rpe, String playerEmail){
        for(int i = 0; i < this.trainings.size(); ++i){
            if(this.trainings.get(i).getDate().equals(trainingDate)){
                this.trainings.get(i).registerRpe(playerEmail, rpe);
            }
        }
    }

    public boolean hasPlayer(String playerEmail){
        for(int i = 0; i < this.players.size(); ++i){
            if(this.players.get(i).getEmail().equals(playerEmail)){
                return true;
            }
        }
        return false;
    }
}
