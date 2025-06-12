package modelo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import interfaces.IDibujable;

public class Fruta extends Dibujo implements IDibujable {
    private String tipo;       // "doblePuntaje" o "ralentizar"
    private boolean visible;

    public Fruta(int x, int y, int ancho, int alto, Image imagen, String tipo) {
        super(x, y, ancho, alto, imagen);
        this.tipo = tipo;
        this.visible = true; // Por defecto, la fruta es visible al crearse
        System.out.println("Fruta creada: Tipo=" + tipo + ", X=" + x + ", Y=" + y + ", Visible=" + visible); // Debugging
    }

    @Override
    public void dibujar(Graphics g) {
        if (visible) {
            // Este print te indicará si el método dibujar de una fruta específica está siendo llamado
            System.out.println("DEBUG: Dibujando fruta en x=" + posEjeX + ", y=" + posEjeY + ", Tipo=" + tipo);
            g.drawImage(imagen, posEjeX, posEjeY, anchura, altura, null);
        } else {
            System.out.println("DEBUG: Fruta no visible, no se dibuja. X=" + posEjeX + ", Y=" + posEjeY + ", Tipo=" + tipo);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(posEjeX, posEjeY, anchura, altura);
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        System.out.println("Fruta en X=" + posEjeX + ", Y=" + posEjeY + " cambió visibilidad a: " + visible); // Debugging
    }

    // Método opcional para reutilizar fruta si querés evitar recrear objetos
    public void reset(int x, int y, String nuevoTipo, Image nuevaImagen) {
        this.posEjeX = x;
        this.posEjeY = y;
        this.tipo = nuevoTipo;
        this.imagen = nuevaImagen;
        this.visible = true;
        System.out.println("Fruta reseteada: Tipo=" + nuevoTipo + ", X=" + x + ", Y=" + y); // Debugging
    }
}
