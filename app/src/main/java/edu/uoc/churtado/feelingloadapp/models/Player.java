package edu.uoc.churtado.feelingloadapp.models;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String Email;
    private String Name;
    private String Surname;
    private String UrlPhoto;
    private String CoachEmail;
    private List<PlayerTraining> Trainings;

    public Player(String email, String name, String surname, String urlPhoto, String coachEmail){
        this.Email = email;
        this.Name = name;
        this.Surname = surname;
        this.UrlPhoto = urlPhoto;
        this.CoachEmail = coachEmail;
        this.Trainings = new ArrayList<PlayerTraining>();
    }

    public String getEmail(){
        return this.Email;
    }

    public String getName(){
        return this.Name;
    }

    public String getSurname(){
        return this.Surname;
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
