package model;

import java.io.Serializable;

public class Jugada implements Serializable {
    private static final long serialVersionUID = 1L;
    private String player;
    private char symbol;
    private int row;
    private int col;

    public Jugada(String nom, int row, int col) {
        this.player = nom;
        this.row = row;
        this.col = col;
    }
    public Jugada(String nom, int row, int col, char symbol) {
        this.symbol = symbol;
        this.player = nom;
        this.row = row;
        this.col = col;
    }
    public Jugada(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }



    public Jugada() {}

    public String getPlayer() {
        return player;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setRow(int i) {
        this.row = i;
    }

    public void setCol(int i) {
        this.col = i;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "Jugada{" +
                "player='" + player + '\'' +
                ", symbol=" + symbol +
                ", row=" + row +
                ", col=" + col +
                '}';
    }

    public void setSymbol(char y) {
        this.symbol = y;
    }
}
