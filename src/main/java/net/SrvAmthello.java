package net;

import model.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SrvAmthello {
    private final int port;
    private final Logger logger;
    private Game game;
    private int numPlayers;

    private SrvAmthello(int port ) {
        numPlayers = 0;
        this.port = port;
        logger = Logger.getLogger(SrvAmthello.class.toString());
        game = new Game();
        System.out.println(game);
    }

    private void listen() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while(true) { //esperar connexió del client i llançar thread
                clientSocket = serverSocket.accept();
                //Llançar Thread per establir la comunicació
                //sumem 1 al numero de jugadors
                numPlayers++;
                logger.info("Qtat de jugadors: " + numPlayers);
                game.setNumPlayers(numPlayers);
                //TODO crear un game per cada dos
                ThreadSevidorAmthello filServerAmthello = new ThreadSevidorAmthello(clientSocket, game);
                filServerAmthello.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(SrvAmthello.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws IOException {
        SrvAmthello srv = new SrvAmthello(5555);

        Thread thTcp = new Thread(() -> srv.listen());
        thTcp.start();

    }
}
