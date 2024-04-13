package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testPrintGame() {
        Game game = new Game();
        System.out.println(game);
    }

    @Test
    public void testSetJugadaNoValida() {
        Game game = new Game();
        Jugada jugada = new Jugada("jug1", 3, 3, 'x');

        // Comprova que la jugada és vàlida
        assertFalse(game.esValida(jugada));
    }

    @Test
    public void testSetJugadaValida() {
        Game game = new Game();
        Jugada jugada = new Jugada("jug1", 3, 2, 'x');

        // Comprova que la jugada és vàlida
        assertTrue(game.esValida(jugada));
    }
    
}
