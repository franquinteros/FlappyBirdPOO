package modelo;

import java.awt.Image;
import java.awt.Graphics;
import interfaces.IDibujable;

public class Tuberia extends Dibujo implements IDibujable {
    private boolean passed = false;

    // Constantes para las dimensiones de la tubería, fijas para todas las instancias
    public static final int ANCHURA_TUBERIA = 64;
    public static final int ALTURA_TUBERIA = 512;

    /**
     * Constructor para la clase Tuberia.
     * Inicializa una nueva tubería en una posición específica con una imagen.
     * La anchura y altura de la tubería se toman de las constantes ANCHURA_TUBERIA y ALTURA_TUBERIA.
     *
     * @param posEjeX Posición inicial en el eje X de la tubería.
     * @param posEjeY Posición inicial en el eje Y de la tubería.
     * @param imagen  Imagen de la tubería (superior o inferior).
     */
    public Tuberia(int posEjeX, int posEjeY, Image imagen) {
        // Llama al constructor de la clase padre (Dibujo) con todos los parámetros.
        // Las dimensiones de la tubería se "hardcodean" usando las constantes.
        super(posEjeX, posEjeY, ANCHURA_TUBERIA, ALTURA_TUBERIA, imagen);
    }

    /**
     * Devuelve la anchura estándar de una tubería.
     * @return La anchura de la tubería.
     */
    public int getAnchuraTuberia() {
        return ANCHURA_TUBERIA;
    }

    /**
     * Devuelve la altura estándar de una tubería.
     * @return La altura de la tubería.
     */
    public int getAlturaTuberia() {
        return ALTURA_TUBERIA;
    }

    /**
     * Dibuja la tubería en el contexto gráfico.
     * Este método sobrescribe el método abstracto de la clase Dibujo.
     * @param g Objeto Graphics sobre el que se dibujará la tubería.
     */
    @Override
    public void dibujar(Graphics g) {
        // Utiliza las propiedades heredadas de Dibujo para dibujar la imagen.
        g.drawImage(imagen, posEjeX, posEjeY, anchura, altura, null);
    }

    /**
     * Verifica si esta tubería ya ha sido "pasada" por el pájaro.
     * Se usa para el cálculo de puntaje.
     * @return true si la tubería ya fue pasada, false en caso contrario.
     */
    public boolean isPassed() {
        return passed;
    }

    /**
     * Establece el estado de "pasada" de la tubería.
     * @param passed true si la tubería fue pasada, false en caso contrario.
     */
    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}

