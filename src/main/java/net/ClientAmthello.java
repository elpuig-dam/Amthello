package net;

import model.Game;
import model.Jugada;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientAmthello extends Thread{
    /* CLient TCP que ha endevinar un número pensat per SrvTcpAdivina_Obj.java */

    private String nom;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Scanner scin;
    private boolean continueConnected;
    private Game game = new Game();
    private Jugada jugada;
    private char symbol = '-';

    private ClientAmthello(String hostname, int port) {
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        continueConnected = true;
        scin = new Scanner(System.in);
    }

    public void run() {
        String msg = null;
        System.out.println(game);
        while(continueConnected){
            //Llegir info del servidor (estat del tauler)
            game = getGameRequest();
            //preparar i enviar la jugada
            jugada = getJugadaRequest();
            try {
                out.writeObject(jugada);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        close(socket);

    }
    private Jugada getJugadaRequest() {
        Jugada j;
        if (game.isTorn() && symbol == 'x' || !game.isTorn() && symbol == 'o') {
            //Demanar jugada
            System.out.printf("Fes la teva jugada: %n");
            System.out.print("Fila: ");
            int f = scin.nextInt();
            System.out.print("Columna: ");
            int c = scin.nextInt();
            j = new Jugada(nom, f, c, symbol);
            System.out.println(j);
        } else {
            //System.out.println("No és el teu torn");
            j = new Jugada();
            j.setSymbol('-');
        }
        return j;
    }

    private Game getGameRequest() {
        Game newGame = null;
        try {
            newGame = (Game) in.readObject();
            if(newGame.getNumPLayers() == 1 && symbol == '-') {
                System.out.println("Esperant a l'altre jugador...");
                symbol = 'x';
            } else if(symbol == '-') symbol = 'o';
            if(game != null) {
                if(newGame.isTorn() != game.isTorn()) {
                    System.out.println(newGame);
                }
            }

            game = newGame;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newGame;
    }

    private void close(Socket socket){
        //si falla el tancament no podem fer gaire cosa, només enregistrar
        //el problema
        try {
            //tancament de tots els recursos
            if(socket!=null && !socket.isClosed()){
                if(!socket.isInputShutdown()){
                    socket.shutdownInput();
                }
                if(!socket.isOutputShutdown()){
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            //enregistrem l'error amb un objecte Logger
            Logger.getLogger(ClientAmthello.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public static void main(String[] args) {
        String jugador, ipSrv;

        //Demanem la ip del servidor i nom del jugador
        System.out.println("Ip del servidor?");
        Scanner sip = new Scanner(System.in);
        ipSrv = sip.next();
        System.out.println("Nom jugador:");
        jugador = sip.next();

        ClientAmthello clientTcp = new ClientAmthello(ipSrv,5555);
        clientTcp.nom = jugador;
        clientTcp.start();
    }
}