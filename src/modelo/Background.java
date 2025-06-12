package modelo;

import java.awt.Image;
import java.awt.Graphics;
import interfaces.IDibujable;

public class Background extends Dibujo implements IDibujable {

    /**
     * Constructor para la clase Background.
     * Inicializa el fondo en la posición (0,0) con las dimensiones del tablero.
     *
     * @param imagen La imagen de fondo a dibujar.
     */
    public Background(Image imagen) {
        // Llama al constructor de la clase padre (Dibujo).
        // El fondo siempre se ubica en (0,0) y tiene las dimensiones fijas del tablero (360x640).
        super(0, 0, 360, 640, imagen);
    }

    /**
     * Dibuja la imagen de fondo en el contexto gráfico.
     * Este método sobrescribe el método abstracto de la clase Dibujo.
     * @param g Objeto Graphics sobre el que se dibujará el fondo.
     */
    @Override
    public void dibujar(Graphics g) {
        // Utiliza las propiedades heredadas de Dibujo para dibujar la imagen.
        g.drawImage(imagen, posEjeX, posEjeY, anchura, altura, null);
    }
}
