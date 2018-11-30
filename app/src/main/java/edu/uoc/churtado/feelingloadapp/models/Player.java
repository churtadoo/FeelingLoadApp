package edu.uoc.churtado.feelingloadapp.models;

import java.util.ArrayList;
import java.util.List;

public class Player extends User {
    private String UrlPhoto;
    private String CoachEmail;
    private List<PlayerTraining> Trainings;

    public Player(){
        Trainings = new ArrayList<PlayerTraining>();
    }

    public Player(String urlPhoto, String coachEmail){
        this.UrlPhoto = urlPhoto;
        this.CoachEmail = coachEmail;
        this.Trainings = new ArrayList<PlayerTraining>();
    }

    public String UrlPhoto(){
        return this.UrlPhoto;
    }

    public String getCoachEmail(){
        return this.CoachEmail;
    }

    public List<PlayerTraining> getTrainings(){
        return this.Trainings;
    }
}
