package modelo;

import java.awt.Graphics;
import java.awt.Image;

/// Esta clase abstracta sirve como base para todos los elementos dibujables en el juego.
/// Centraliza la gestión de posición, tamaño e imagen, simplificando el GamePanel.
public abstract class Dibujo {

    protected int posEjeX;
    protected int posEjeY;
    protected int anchura;
    protected int altura;
    protected Image imagen; // Imagen a dibujar para este elemento

    /**
     * Constructor principal para inicializar un objeto dibujable.
     * Es fundamental para asegurar que todos los elementos tengan una posición,
     * tamaño y una imagen válida desde su creación.
     *
     * @param x       Posición inicial en el eje X.
     * @param y       Posición inicial en el eje Y.
     * @param ancho   Anchura del elemento.
     * @param alto    Altura del elemento.
     * @param imagen  Objeto Image a dibujar.
     */
    public Dibujo(int x, int y, int ancho, int alto, Image imagen) {
        this.posEjeX = x;
        this.posEjeY = y;
        this.anchura = ancho;
        this.altura = alto;
        this.imagen = imagen;
        // Mensaje de depuración para confirmar la correcta inicialización
        System.out.println("DEBUG: Constructor de Dibujo -> X=" + this.posEjeX + ", Y=" + this.posEjeY + ", Ancho=" + this.anchura + ", Alto=" + this.altura + " (Imagen: " + (imagen != null ? "Cargada" : "NULA") + ")");
    }

    /**
     * Método abstracto para dibujar el elemento en la pantalla.
     * Cada subclase (Pajaro, Fruta, Tuberia) debe implementar su propia lógica de dibujo.
     * @param g Objeto Graphics sobre el que se dibujará.
     */
    public abstract void dibujar(Graphics g);

    // --- Getters y Setters para las propiedades ---

    public int getPosEjeX() {
        return posEjeX;
    }

    public void setPosEjeX(int posEjeX) {
        this.posEjeX = posEjeX;
    }

    public int getPosEjeY() {
        return posEjeY;
    }

    public void setPosEjeY(int posEjeY) {
        this.posEjeY = posEjeY;
    }

    public int getAnchura() {
        return anchura;
    }

    public void setAnchura(int anchura) {
        this.anchura = anchura;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }
}

