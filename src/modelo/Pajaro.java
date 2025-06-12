package modelo;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Graphics;
import interfaces.IDibujable;

public class Pajaro extends Dibujo implements IDibujable {
    // Dimensiones predeterminadas del pájaro
    private int anchuraPajaro = 34;
    private int alturaPajaro = 24;

    /**
     * Constructor para la clase Pajaro.
     * Inicializa el pájaro en una posición específica con una imagen.
     * Las dimensiones del pájaro se toman de anchuraPajaro y alturaPajaro.
     *
     * @param x   Posición inicial en el eje X del pájaro.
     * @param y   Posición inicial en el eje Y del pájaro.
     * @param img Imagen del pájaro.
     */
    public Pajaro(int x, int y, Image img) {
        // Llama al constructor de la clase padre (Dibujo) con todos los parámetros.
        // Las dimensiones se usan directamente desde las variables de instancia de Pajaro.
        super(x, y, 34, 24, img); // Usar valores directos o constantes si son fijas
    }

    /**
     * Dibuja el pájaro en el contexto gráfico.
     * Este método sobrescribe el método abstracto de la clase Dibujo.
     * @param g Objeto Graphics sobre el que se dibujará el pájaro.
     */
    @Override
    public void dibujar(Graphics g) {
        // Utiliza las propiedades heredadas de Dibujo para dibujar la imagen.
        g.drawImage(imagen, posEjeX, posEjeY, anchura, altura, null);
    }

    /**
     * Obtiene los límites del pájaro como un objeto Rectangle,
     * útil para la detección de colisiones.
     * @return Un objeto Rectangle que representa la posición y tamaño del pájaro.
     */
    public Rectangle getBounds() {
        // Crea un nuevo Rectangle usando las propiedades heredadas de Dibujo.
        return new Rectangle(posEjeX, posEjeY, anchura, altura);
    }

    // Getters y Setters para las dimensiones del pájaro (si es que pueden cambiar)
    public int getAnchuraPajaro() {
        return anchuraPajaro;
    }

    public void setAnchuraPajaro(int anchuraPajaro) {
        this.anchuraPajaro = anchuraPajaro;
    }

    public int getAlturaPajaro() {
        return alturaPajaro;
    }

    public void setAlturaPajaro(int alturaPajaro) {
        this.alturaPajaro = alturaPajaro;
    }
}
