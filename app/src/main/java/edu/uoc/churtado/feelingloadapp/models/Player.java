package edu.uoc.churtado.feelingloadapp.models;

import java.util.ArrayList;

public class Player extends User {
    private String urlPhoto;
    private String coachEmail;
    private ArrayList<PlayerTraining> trainings;

    public Player(){
        this.setType(UserType.Player);
        this.trainings = new ArrayList<PlayerTraining>();
    }

    public Player(String urlPhoto, String coachEmail){
        this.urlPhoto = urlPhoto;
        this.coachEmail = coachEmail;
        this.trainings = new ArrayList<PlayerTraining>();
    }

    public String getUrlPhoto(){
        return this.urlPhoto;
    }

    public String getCoachEmail(){
        return this.coachEmail;
    }

    public ArrayList<PlayerTraining> getTrainings(){
        return this.trainings;
    }

    public String getDisplayName(){
        return this.getName() + " " + this.getSurname();
    }

    public void registerRpe(int playerTrainingPosition, int rpe){
        this.trainings.get(playerTrainingPosition).registerRPE(rpe);
    }

    public void setCoachEmail(String coachEmail){ this.coachEmail = coachEmail; }
}
