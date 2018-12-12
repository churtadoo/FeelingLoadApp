package edu.uoc.churtado.feelingloadapp;

import org.junit.Test;

import edu.uoc.churtado.feelingloadapp.models.PlayerRPE;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlayerRPEUnitTest {
    @Test
    public void registerRpe_isCorrect(){
        PlayerRPE playerRPE = new PlayerRPE();
        playerRPE.registerRpe(7);

        assertEquals(7, playerRPE.getRPE());
    }

    @Test
    public void hasRegisteredRpe_isCorrect(){
        PlayerRPE playerRPE = new PlayerRPE();
        playerRPE.registerRpe(7);

        assertTrue(playerRPE.HasRegisteredRPE());

        PlayerRPE playerNoRPE = new PlayerRPE();
        assertFalse(playerNoRPE.HasRegisteredRPE());
    }
}
