package edu.uoc.churtado.feelingloadapp;

import org.junit.Test;

import java.util.Date;

import edu.uoc.churtado.feelingloadapp.models.Coach;
import edu.uoc.churtado.feelingloadapp.models.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerUnitTest {
    @Test
    public void addTraining_isCorrect(){
        Player player = new Player();
        Date date = new Date();
        player.addTraining(date);

        assertEquals(1, player.getTrainings().size());
        assertEquals(date, player.getTrainings().get(0).getDate());
    }
    @Test
    public void registerRpe_isCorrect() {
        Player player = new Player();
        Date date = new Date();
        player.addTraining(date);

        player.registerRpe(0, 7);

        assertEquals(7, player.getTrainings().get(0).getRPE());
    }
}