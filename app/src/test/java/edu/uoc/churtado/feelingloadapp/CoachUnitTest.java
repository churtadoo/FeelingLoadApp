package edu.uoc.churtado.feelingloadapp;

import org.junit.Test;

import java.util.Date;

import edu.uoc.churtado.feelingloadapp.models.Coach;
import edu.uoc.churtado.feelingloadapp.models.Player;
import edu.uoc.churtado.feelingloadapp.models.Training;

import static org.junit.Assert.*;

public class CoachUnitTest {
    @Test
    public void addPlayer_isCorrect() {
        Coach coach = new Coach();
        Player player = new Player();
        player.setEmail("test@test.com");
        player.setName("Test");
        player.setSurname("Surname");
        coach.addPlayer(player);
        assertEquals(1, coach.getPlayers().size());
        assertEquals("test@test.com", coach.getPlayers().get(0).getEmail());
        assertEquals("Test", coach.getPlayers().get(0).getName());
        assertEquals("Surname", coach.getPlayers().get(0).getSurname());
    }

    @Test
    public void updatePlayer_isCorrect() {
        Coach coach = new Coach();
        Player player = new Player();
        player.setEmail("test@test.com");
        player.setName("Test");
        player.setSurname("Surname");
        coach.addPlayer(player);

        Player playerUpdated = new Player();
        playerUpdated.setName("NameUpdated");
        playerUpdated.setSurname("SurnameUpdated");
        coach.updatePlayer(0, playerUpdated);

        assertEquals("NameUpdated", coach.getPlayers().get(0).getName());
        assertEquals("SurnameUpdated", coach.getPlayers().get(0).getSurname());
    }

    @Test
    public void addTraining_isCorrect() {
        Coach coach = new Coach();

        Player player = new Player();
        player.setEmail("test@test.com");
        player.setName("Test");
        player.setSurname("Surname");
        coach.addPlayer(player);

        Date date = new Date();
        coach.addTraining(date);

        assertEquals(1, coach.getTrainings().size());
        assertEquals(date, coach.getTrainings().get(0).getDate());
        assertEquals(1, coach.getTrainings().get(0).getRPEs().size());
        assertEquals("test@test.com", coach.getTrainings().get(0).getRPEs().get(0).getPlayerEmail());
    }

    @Test
    public void registerRpe_isCorrect() {
        Coach coach = new Coach();

        Player player = new Player();
        player.setEmail("test@test.com");
        player.setName("Test");
        player.setSurname("Surname");
        coach.addPlayer(player);

        Date date = new Date();
        coach.addTraining(date);

        coach.registerRpe(date, 7, "test@test.com");

        assertEquals(7, coach.getTrainings().get(0).getRPEs().get(0).getRPE());
    }

    @Test
    public void hasPlayer_isCorrect() {
        Coach coach = new Coach();
        Player player = new Player();
        player.setEmail("test@test.com");
        player.setName("Test");
        player.setSurname("Surname");
        coach.addPlayer(player);
        assertTrue(coach.hasPlayer("test@test.com"));
        assertFalse(coach.hasPlayer("new@new.com"));
    }
}