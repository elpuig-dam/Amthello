package model;

import java.io.Serializable;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    public final int MAX_ROWS = 8;
    public final int MAX_COLS = 8;
    private char[][] tauler;
    private int numPlayers;
    private boolean torn;
    private int numCasellesX;
    private int numCasellesO;

    public Game() {
        initTauler();
    }

    //TODO Netejar comentaris i prints sobrants
    //TODO Refactoritzar el codi
    //TODO Fer un test per comprovar si a un jugador no li queda cap jugada vàlida passa el torn
    //TODO Detectar final de la partida i guanyador

    public void initTauler() {
        //inicialitzar tauler
        tauler = new char[MAX_ROWS][MAX_COLS];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tauler[i][j] = '-';
            }
        }
        //caselles centrals
        tauler[3][3] = 'o';
        tauler[3][4] = 'x';
        tauler[4][3] = 'x';
        tauler[4][4] = 'o';

        //init torn
        torn = Math.random() > 0.5;
        numCasellesX = 2;
        numCasellesO = 2;
    }

    public String printGame() {
        StringBuilder sb = new StringBuilder();
        // Imprimeix els índexs de les columnes
        sb.append("   ");
        for (int i = 0; i < MAX_COLS; i++) {
            sb.append(i).append("  ");
        }
        sb.append("\n");

        for (int i = 0; i < MAX_ROWS; i++) {
            // Imprimeix l'índex de la fila
            sb.append(i).append("  ");
            for (int j = 0; j < MAX_COLS; j++) {
                sb.append(tauler[i][j]).append("  ");
            }
            sb.append("\n");
        }
        sb.append("Caselles ocupades per x: ").append(numCasellesX).append("\n");
        sb.append("Caselles ocupades per o: ").append(numCasellesO).append("\n");
        sb.append("torn: ").append(torn ? "x" : "o").append("\n");
        return sb.toString();

    }

    public char[][] getTauler() {
        return tauler;
    }

    public boolean setJugada(Jugada j) {
        System.out.println("comprovant jugada...");
        boolean tirada = false;
        char c = j.getSymbol();
        if(esValida(j)) {
            tauler[j.getRow()][j.getCol()] = c;
            System.out.println("jugada correcte");

            if (c == 'x') numCasellesX++;
            else numCasellesO++;

            // Per cada direcció, canvia les fitxes de l'oponent
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx != 0 || dy != 0) {
                        int canviades = canviaFitxes(j, dx, dy);
                        if (c == 'x') {
                            numCasellesX += canviades;
                            numCasellesO -= canviades;
                        } else {
                            numCasellesO += canviades;
                            numCasellesX -= canviades;
                        }
                    }
                }
            }
            printGame();
            tirada = true;
            nextTorn();
        }
        else tirada = false;

        // Comprova si el jugador actual té alguna jugada vàlida
        if (!hiHaJugadaValida(c)) {
            // Si no té cap jugada vàlida, passa el torn
            nextTorn();
        }

        return tirada;
    }

    private int canviaFitxes(Jugada j, int dx, int dy) {
        int x = j.getRow() + dx;
        int y = j.getCol() + dy;
        char oponent = (j.getSymbol() == 'x') ? 'o' : 'x';
        int canviades = 0;

        // Comprova si la casella adjacent és de l'oponent
        if (x >= 0 && x < MAX_ROWS && y >= 0 && y < MAX_COLS && tauler[x][y] == oponent) {
            // Continua en aquesta direcció fins que trobis una fitxa pròpia
            x += dx;
            y += dy;
            while (x >= 0 && x < MAX_ROWS && y >= 0 && y < MAX_COLS) {
                if (tauler[x][y] == j.getSymbol()) {
                    // Canvia les fitxes de l'oponent
                    int nx = j.getRow() + dx;
                    int ny = j.getCol() + dy;
                    while (nx != x || ny != y) {
                        tauler[nx][ny] = j.getSymbol();
                        nx += dx;
                        ny += dy;
                        canviades++;
                    }
                    break;
                } else if (tauler[x][y] == '-') {
                    break;
                }
                x += dx;
                y += dy;
            }
        }
        return canviades;
    }

    public int getNumCasellesX() {
        return numCasellesX;
    }

    public int getNumCasellesO() {
        return numCasellesO;
    }

    public boolean esValida(Jugada j) {
        // Comprova si la casella està buida
        if (tauler[j.getRow()][j.getCol()] != '-') {
            return false;
        }

        // Comprova totes les direccions per a veure si la jugada és vàlida
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) {
                    if (comprovaDireccio(j, dx, dy)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean hiHaJugadaValida(char symbol) {
        Jugada j = new Jugada();
        j.setSymbol(symbol);
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int k = 0; k < MAX_COLS; k++) {
                j.setRow(i);
                j.setCol(k);
                if (esValida(j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean comprovaDireccio(Jugada j, int dx, int dy) {
        int x = j.getRow() + dx;
        int y = j.getCol() + dy;
        char oponent = (j.getSymbol() == 'x') ? 'o' : 'x';

        // Comprova si la casella adjacent és de l'oponent
        if (x >= 0 && x < MAX_ROWS && y >= 0 && y < MAX_COLS && tauler[x][y] == oponent) {
            // Continua en aquesta direcció fins que trobis una fitxa pròpia
            x += dx;
            y += dy;
            while (x >= 0 && x < MAX_ROWS && y >= 0 && y < MAX_COLS) {
                if (tauler[x][y] == j.getSymbol()) {
                    return true;
                } else if (tauler[x][y] == '-') {
                    break;
                }
                x += dx;
                y += dy;
            }
        }
        return false;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }


    public int getNumPLayers() {
        return numPlayers;
    }

    public boolean isTorn() {
        return torn;
    }

    public void nextTorn() {
        torn = !torn;
    }

    @Override
    public String toString() {
        return printGame();
    }
}
