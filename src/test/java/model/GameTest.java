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
        Jugada jugada = new Jugada('x', 3, 3);

        // Comprova que la jugada és vàlida
        assertFalse(game.esValida(jugada));
    }

    @Test
    public void testSetJugadaValida() {
        Game game = new Game();
        Jugada jugada = new Jugada('x', 3, 2);

        // Comprova que la jugada és vàlida
        assertTrue(game.esValida(jugada));
    }
    
}
