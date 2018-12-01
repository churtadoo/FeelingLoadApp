package edu.uoc.churtado.feelingloadapp.models;

import java.util.ArrayList;
import java.util.List;

public class Player extends User {
    private String urlPhoto;
    private String coachEmail;
    private ArrayList<PlayerTraining> trainings;

    public Player(){
        trainings = new ArrayList<PlayerTraining>();
    }

    public Player(String urlPhoto, String coachEmail){
        this.urlPhoto = urlPhoto;
        this.coachEmail = coachEmail;
        this.trainings = new ArrayList<PlayerTraining>();
    }

    public String UrlPhoto(){
        return this.urlPhoto;
    }

    public String getCoachEmail(){
        return this.coachEmail;
    }

    public ArrayList<PlayerTraining> getTrainings(){
        return this.trainings;
    }
}
