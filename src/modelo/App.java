package modelo;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Usamos SwingUtilities para asegurar que la interfaz gráfica se maneje en el hilo correcto
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MenuPrincipal(); // Mostrar menú principal
            }
        });
    }
}


