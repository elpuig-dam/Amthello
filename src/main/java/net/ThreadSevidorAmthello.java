package net;

import model.Game;
import model.Jugada;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ThreadSevidorAmthello extends Thread {
    private Game game;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private int idPlayer;
    private final Logger logger;

    public ThreadSevidorAmthello(Socket clientSocket, Game game) {
        logger = Logger.getLogger(ThreadSevidorAmthello.class.toString());
        this.game = game;
        try {
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        idPlayer = game.getNumPLayers();

    }

    public int getIdPlayer() {
        return idPlayer;
    }

    @Override
    public void run() {
        Jugada j = null;

        while(true) {
            //Enviem tauler al jugador
            try {
                oos.writeObject(game);
                oos.reset();
                oos.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //Llegim la jugada
            try {
                j = (Jugada) ois.readObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
            if(j.getPlayer() != null) {
                System.out.println("jugada: " + j.getPlayer() + "->" + j.getRow() + "," + j.getCol());
            }
            //if(!game.setJugada(j)) System.out.println("Jugada incorrecta " + j);
            if(j.getSymbol() != '-') {
                game.setJugada(j);
            }
        }


        //Enviem últim estat del tauler abans de acabar amb la comunicació i acabem
        /*try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(game);
            oos.flush();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
}
