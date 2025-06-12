package modelo;

import javax.swing.*;

public class Juego extends JFrame {

    public Juego(String nombreJugador) {
        int boardWidth = 360;
        int boardHeight = 640;

        setTitle("Flappy Bird - Jugando como " + nombreJugador);
        setSize(boardWidth, boardHeight);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        GamePanel flappyBird = new GamePanel(nombreJugador);
        add(flappyBird);
        pack();
        flappyBird.requestFocus();
        setVisible(true);
    }
}

