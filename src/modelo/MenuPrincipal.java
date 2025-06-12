package modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Flappy Bird - Menú");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setBackground(new Color(50, 50, 50));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titulo = new JLabel("🐦 Flappy Bird", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(titulo, gbc);

        // Botón Jugar
        JButton btnJugar = new JButton("🎮 Jugar");
        btnJugar.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 1;
        panel.add(btnJugar, gbc);

        // Botón Ver Puntajes
        JButton btnPuntajes = new JButton("🏆 Ver Puntajes");
        btnPuntajes.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 2;
        panel.add(btnPuntajes, gbc);

        // Botón Salir
        JButton btnSalir = new JButton("❌ Salir");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 3;
        panel.add(btnSalir, gbc);

        add(panel);

        // Acciones
        btnJugar.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Ingrese su nombre:", "Nombre del jugador", JOptionPane.PLAIN_MESSAGE);
            if (nombre != null && !nombre.trim().isEmpty()) {
                dispose(); // cerrar menú
                new Juego(nombre.trim()); // iniciar juego con nombre
            } else {
                JOptionPane.showMessageDialog(this, "Debe ingresar un nombre para jugar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnPuntajes.addActionListener(e -> mostrarPuntajes());

        btnSalir.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void mostrarPuntajes() {
        java.util.List<String> lineas = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("puntajes.txt"))) {
            while (scanner.hasNextLine()) {
                lineas.add(scanner.nextLine());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo leer el archivo de puntajes.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        lineas.sort((a, b) -> {
            try {
                int scoreA = Integer.parseInt(a.split("-")[1].trim());
                int scoreB = Integer.parseInt(b.split("-")[1].trim());
                return Integer.compare(scoreB, scoreA);
            } catch (Exception e) {
                return 0;
            }
        });

        StringBuilder mensaje = new StringBuilder("🏆 Puntajes guardados:\n\n");
        for (String linea : lineas) {
            mensaje.append(linea).append("\n");
        }

        JOptionPane.showMessageDialog(this, mensaje.toString(), "Puntajes", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuPrincipal menu = new MenuPrincipal();
            menu.setVisible(true);
        });
    }

}
