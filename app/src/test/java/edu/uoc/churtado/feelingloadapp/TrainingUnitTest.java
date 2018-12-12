package edu.uoc.churtado.feelingloadapp;

import org.junit.Test;

import edu.uoc.churtado.feelingloadapp.models.Training;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TrainingUnitTest {
    @Test
    public void addPlayerRpe_isCorrect(){
        Training training = new Training();
        training.addPlayerRPE("test@test.com");

        assertEquals(1, training.getRPEs().size());
        assertEquals("test@test.com", training.getRPEs().get(0).getPlayerEmail());
    }

    @Test
    public void registerRpe_isCorrect(){
        Training training = new Training();
        training.addPlayerRPE("test@test.com");
        training.registerRpe("test@test.com", 7);

        assertEquals(7, training.getRPEs().get(0).getRPE());
    }

    @Test
    public void allPlayersWithRpeRegistered_isCorrect(){
        Training training = new Training();
        training.addPlayerRPE("test@test.com");
        training.registerRpe("test@test.com", 7);

        assertTrue(training.allPlayersWithRPERegistered());

        training.addPlayerRPE("second@second.com");
        assertFalse(training.allPlayersWithRPERegistered());
    }
}
