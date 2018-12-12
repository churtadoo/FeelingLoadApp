package edu.uoc.churtado.feelingloadapp;

import org.junit.Test;

import edu.uoc.churtado.feelingloadapp.models.PlayerRPE;
import edu.uoc.churtado.feelingloadapp.models.PlayerTraining;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlayerTrainingUnitTest {
    @Test
    public void registerRpe_isCorrect(){
        PlayerTraining playerTraining = new PlayerTraining();
        playerTraining.registerRPE(7);

        assertEquals(7, playerTraining.getRPE());
    }

    @Test
    public void hasRegisteredRpe_isCorrect(){
        PlayerTraining playerTraining = new PlayerTraining();
        playerTraining.registerRPE(7);

        assertTrue(playerTraining.HasRegisteredRPE());

        PlayerTraining playerTrainingNoRpe = new PlayerTraining();
        assertFalse(playerTrainingNoRpe.HasRegisteredRPE());
    }
}
