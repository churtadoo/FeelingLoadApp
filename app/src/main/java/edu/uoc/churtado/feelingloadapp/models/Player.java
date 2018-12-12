package edu.uoc.churtado.feelingloadapp.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Player extends User {
    private String urlPhoto;
    private String coachEmail;
    private ArrayList<PlayerTraining> trainings;

    private static final String DefaultUrlPhoto = "https://firebasestorage.googleapis.com/v0/b/feelingloadapp.appspot.com/o/userImages%2Fuser.png?alt=media&token=84c95e11-170a-4d57-843d-20c1fe9600fa";

    public Player(){
        this.setType(UserType.Player);
        this.trainings = new ArrayList<>();
        this.urlPhoto = DefaultUrlPhoto;
    }

    public Player(String urlPhoto, String coachEmail){
        if(urlPhoto.isEmpty()) urlPhoto = DefaultUrlPhoto;
        this.urlPhoto = urlPhoto;
        this.coachEmail = coachEmail;
        this.trainings = new ArrayList<>();
    }

    public String getUrlPhoto(){
        return this.urlPhoto;
    }

    public String getCoachEmail(){
        return this.coachEmail;
    }

    public ArrayList<PlayerTraining> getTrainings(){
        Collections.sort(this.trainings);
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

    public void setTrainings(ArrayList<PlayerTraining> trainings) {
        this.trainings = trainings;
    }
}
