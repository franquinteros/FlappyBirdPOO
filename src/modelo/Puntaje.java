package modelo;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import interfaces.IDibujable;



public class Puntaje implements IDibujable{ //puntaje no hereda de Dibujo porque no comparte atributos. Sólo implementa el método de la interfaz

    //No dibuja una imagen, sino texto. el método dibujar() cambia.

    //Puntaje necesita saber si el juego ha terminado para poder cambiar la forma en que se dibuja el texto (de "12" a "Game Over: 12").
    private boolean gameOver = false;
    private int puntajeActual = 0;



    private int posEjeX;
    private int posEjeY;



    public Puntaje(int posEjeX, int posEjeY) {
        this.posEjeX = posEjeX;
        this.posEjeY = posEjeY;

        //gameOver y puntaleActual ya se inicializan antes
    }

    @Override
    public void dibujar(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 32));

        String texto;
        if (gameOver) {
            texto = "Game Over: " + puntajeActual;
        } else {//(*)
            texto = String.valueOf(puntajeActual);
        }
        g.drawString(texto, posEjeX, posEjeY); //drawString(String str, int x, int y)
    }

    ///a su vez, pensaba que GamePanel tenga una variable score y luego se la pase como parametro a puntaje mediante el metodo actualizar():
    ///score += 0.5; // Se suma 0.5 por cada tubería (superior e inferior)
    public void actualizar(int puntaje, boolean gameOver) {
        this.puntajeActual = puntaje;
        this.gameOver = gameOver;}

}
