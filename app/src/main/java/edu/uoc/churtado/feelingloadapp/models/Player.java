package edu.uoc.churtado.feelingloadapp.models;

import java.util.ArrayList;
import java.util.Date;

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

    public void setUrlPhoto(String urlPhoto) { this.urlPhoto = urlPhoto; }

    public void addTraining(Date date){
        this.trainings.add(new PlayerTraining(date));
    }
}
