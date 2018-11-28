package edu.uoc.churtado.feelingloadapp.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Coach {
    private String Email;
    private String Name;
    private String Surname;
    private List<Player> Players;
    private List<Training> Trainings;

    public Coach(String email, String name, String surname){
        this.Email = email;
        this.Name = name;
        this.Surname = surname;
        Players = new ArrayList<Player>();
        Trainings = new ArrayList<Training>();
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
