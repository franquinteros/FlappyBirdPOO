package modelo;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.*;
import java.io.File;
import java.util.Random;
import javax.swing.*;
//Game Panel
public class GamePanel extends JPanel implements ActionListener, KeyListener {
    public int boardWidth = 360;
    public int boardHeight = 640;

    private Pajaro pajaro;
    private ArrayList<Tuberia> tuberias;
    private ArrayList<Fruta> frutas; // Lista para almacenar las frutas
    private long tiempoUltimaFruta = 0;
    private final long intervaloFruta = 8000; // cada 8 segundos aprox

    private boolean doblePuntajeActivo = false;
    private boolean ralentizarActivo = false;
    private long tiempoBonusActivo = 0;
    private final long duracionBonus = 5000;

    private Puntaje puntaje;
    private Background bkg;

    private int velocidadTuberiasX = -2;
    private int velocidadPajaroY = 0;
    private int gravedadPajaro = 1;

    private Timer gameLoop;
    private Timer posTuberiasTimer;

    boolean gameOver = false;
    boolean iniciado = false;
    boolean pausado = false;
    double score = 0;

    private String nombreJugador = "";
    private Random random = new Random();

    // Carga de imágenes
    Image backgroundImg = new ImageIcon(getClass().getResource("flappybirdbg.png")).getImage();
    Image birdImg = new ImageIcon(getClass().getResource("flappybird.png")).getImage();
    Image topPipeImg = new ImageIcon(getClass().getResource("toppipe.png")).getImage();
    Image bottomPipeImg = new ImageIcon(getClass().getResource("bottompipe.png")).getImage();
    Image manzanaRojaImg = new ImageIcon(getClass().getResource("manzana_roja.png")).getImage();
    Image manzanaVerdeImg = new ImageIcon(getClass().getResource("manzana_verde.png")).getImage();

    public GamePanel(String nombreJugador) {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);
        reproducirMusicaFondo();

        this.nombreJugador = nombreJugador;
        this.pajaro = new Pajaro(boardWidth / 8, boardHeight / 2, birdImg);
        this.frutas = new ArrayList<>(); // Inicialización de la lista de frutas
        this.tuberias = new ArrayList<>();
        this.puntaje = new Puntaje(10, 35);
        this.bkg = new Background(backgroundImg);

        posTuberiasTimer = new Timer(2500, e -> placePipes());
        gameLoop = new Timer(1000 / 60, this);
    }

    public void placePipes() {
        // Uso correcto de Math.random() y constantes de Tuberia
        int randomPipeY = (int) (0 - Tuberia.ALTURA_TUBERIA / 4 - Math.random() * (Tuberia.ALTURA_TUBERIA / 2));
        int openingSpace = boardHeight / 4;

        Tuberia topPipe = new Tuberia(boardWidth, 0, topPipeImg);
        topPipe.setPosEjeY(randomPipeY);
        tuberias.add(topPipe);

        Tuberia bottomPipe = new Tuberia(boardWidth, 0, bottomPipeImg);
        bottomPipe.setPosEjeY(topPipe.getPosEjeY() + topPipe.getAlturaTuberia() + openingSpace);
        tuberias.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        bkg.dibujar(g);

        for (Tuberia t : tuberias) {
            t.dibujar(g);
        }

        pajaro.dibujar(g);
        puntaje.dibujar(g);

        //Dibuja las frutas
        for (Fruta f : frutas) {
            // NEW DEBUG: Check visibility of each fruit in the list
            System.out.println("DEBUG: En paintComponent, procesando fruta: visible=" + f.isVisible() + ", tipo=" + f.getTipo() + ", X=" + f.getPosEjeX() + ", Y=" + f.getPosEjeY());
            f.dibujar(g);
        }

        g.setColor(Color.WHITE); // Color para el texto del jugador
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Jugador: " + nombreJugador, 10, boardHeight - 10);

        if (!iniciado) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.YELLOW);
            FontMetrics metrics = g.getFontMetrics();
            String texto1 = "Presiona ESPACIO para empezar";
            String texto2 = "Presiona V para ver ranking";
            int x1 = (boardWidth - metrics.stringWidth(texto1)) / 2;
            int x2 = (boardWidth - metrics.stringWidth(texto2)) / 2;
            int y = boardHeight / 2;
            g.drawString(texto1, x1, y);
            g.drawString(texto2, x2, y + 30);
        }

        if (pausado) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.RED);
            g.drawString("PAUSADO", boardWidth / 3, boardHeight / 3);
        }
    }

    public void move() {
        velocidadPajaroY += gravedadPajaro;
        pajaro.setPosEjeY(pajaro.getPosEjeY() + velocidadPajaroY);
        pajaro.setPosEjeY(Math.max(pajaro.getPosEjeY(), 0)); // Evita que el pájaro suba por encima del borde superior

        for (Tuberia pipe : tuberias) {
            pipe.setPosEjeX(pipe.getPosEjeX() + velocidadTuberiasX);

            // Lógica de puntuación
            if (!pipe.isPassed() && pajaro.getPosEjeX() > pipe.getPosEjeX() + pipe.anchura) {
                score += doblePuntajeActivo ? 1.0 : 0.5; // Si doble puntaje activo, suma 1.0, sino 0.5
                pipe.setPassed(true);
                reproducirSonidoPunto();
            }

            // Colisión con tuberías
            if (collision(pajaro, pipe)) {
                gameOver = true;
            }
        }

        // Colisión con el suelo (fuera de la pantalla por abajo)
        if (pajaro.getPosEjeY() > boardHeight) {
            gameOver = true;
        }
    }

    // Método que no se utiliza, se puede eliminar si no es necesario
    boolean collisionCon(Fruta fruta) {
        return pajaro.getBounds().intersects(fruta.getBounds());
    }

    boolean collision(Pajaro p, Tuberia t) {
        return p.getPosEjeX() < t.getPosEjeX() + t.anchura &&
                p.getPosEjeX() + p.getAnchura() > t.getPosEjeX() &&
                p.getPosEjeY() < t.getPosEjeY() + t.getAltura() &&
                p.getPosEjeY() + p.getAltura() > t.getPosEjeY();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!pausado && iniciado) {
            move();
            actualizarFrutas(); // Llama al método para actualizar y añadir frutas
            detectarColisionFrutas();
            puntaje.actualizar((int) score, gameOver); // Actualiza la visualización del puntaje
            repaint(); // Redibuja el panel

            if (gameOver) {
                detenerMusicaFondo();
                reproducirSonidoGameOver();
                posTuberiasTimer.stop();
                gameLoop.stop();
                guardarPuntaje();
                mostrarMenuGameOver();
            }
        }
    }

    private void actualizarFrutas() {
        long tiempoActual = System.currentTimeMillis();

        // Agregar fruta cada cierto tiempo
        if (tiempoActual - tiempoUltimaFruta > intervaloFruta) {
            System.out.println("DEBUG: Antes de añadir, frutas en lista: " + frutas.size()); // Debug para ver el tamaño antes de añadir
            int x = boardWidth;
            int y = random.nextInt(boardHeight - 100) + 50; // Posición Y aleatoria, evitando los bordes
            String tipo = random.nextBoolean() ? "doblePuntaje" : "ralentizar";
            Image img = tipo.equals("doblePuntaje") ? manzanaRojaImg : manzanaVerdeImg;
            Fruta nuevaFruta = new Fruta(x, y, 32, 32, img, tipo);
            frutas.add(nuevaFruta);
            tiempoUltimaFruta = tiempoActual; // Actualiza el tiempo de la última fruta añadida
            System.out.println("Fruta añadida. Total de frutas en lista: " + frutas.size());
            System.out.println("DEBUG: Nueva fruta añadida en X=" + nuevaFruta.getPosEjeX() + ", Y=" + nuevaFruta.getPosEjeY() + ", Visible=" + nuevaFruta.isVisible());
        }

        // Mover frutas hacia la izquierda
        for (Fruta fruta : frutas) {
            fruta.setPosEjeX(fruta.getPosEjeX() + velocidadTuberiasX); // Usar velocidadTuberiasX para que se muevan con el mismo flujo
        }

        // Eliminar frutas que salen de pantalla o las que ya no están visibles (tras colisión)
        boolean removedAny = frutas.removeIf(f -> {
            boolean conditionMet = !f.isVisible() || f.getPosEjeX() + f.getAnchura() < 0;
            if (conditionMet) {
                // Nuevo DEBUG: Imprime el estado de la fruta que se va a eliminar
                System.out.println("DEBUG: Fruta MARCADA para eliminación: Tipo=" + f.getTipo() + ", visible=" + f.isVisible() + ", X=" + f.getPosEjeX() + ", Anchura=" + f.getAnchura() + ", Resultado Condición: " + conditionMet);
            }
            return conditionMet;
        });
        if (removedAny) {
            System.out.println("DEBUG: Se han eliminado una o más frutas de la lista. Total actual: " + frutas.size());
        }

        // Desactivar bonus si pasó el tiempo
        if (doblePuntajeActivo || ralentizarActivo) {
            if (tiempoActual - tiempoBonusActivo > duracionBonus) {
                System.out.println("Bonus " + (doblePuntajeActivo ? "Doble Puntaje" : "Ralentizar") + " desactivado.");
                doblePuntajeActivo = false;
                ralentizarActivo = false;
                velocidadTuberiasX = -2; // Restaura la velocidad normal de las tuberías
            }
        }
    }

    private void detectarColisionFrutas() {
        for (int i = 0; i < frutas.size(); i++) {
            Fruta fruta = frutas.get(i);
            if (fruta.isVisible() && pajaro.getBounds().intersects(fruta.getBounds())) {
                aplicarBonus(fruta.getTipo());
                fruta.setVisible(false); // La fruta ya no es visible
                System.out.println("¡Colisión con fruta! Tipo: " + fruta.getTipo());
                reproducirSonido("bonus.wav");
            }
        }
    }

    private void aplicarBonus(String tipo) {
        tiempoBonusActivo = System.currentTimeMillis(); // Reinicia el contador de duración del bonus
        if (tipo.equals("doblePuntaje")) {
            doblePuntajeActivo = true;
            ralentizarActivo = false; // Asegura que solo un bonus esté activo
            System.out.println("Bonus de DOBLE PUNTAJE activado.");
        } else if (tipo.equals("ralentizar")) {
            ralentizarActivo = true;
            doblePuntajeActivo = false; // Asegura que solo un bonus esté activo
            velocidadTuberiasX = -1; // más lento
            System.out.println("Bonus de RALENTIZAR activado. Velocidad tuberías: " + velocidadTuberiasX);
        }
    }

    private void reproducirSonido(String archivo) {
        try {
            // Carga el archivo desde el classpath, dentro del paquete modelo
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(archivo));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir sonido: " + archivo);
            e.printStackTrace();
        }
    }

    private void reproducirSonidoGameOver() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("caida.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir el sonido de Game Over.");
            e.printStackTrace();
        }
    }
    private void reproducirSonidoPunto() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("puntos.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir el sonido de puntos.");
            e.printStackTrace();
        }
    }


    private Clip musicaFondo;

    private void reproducirMusicaFondo() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("musicafondo.wav"));
            musicaFondo = AudioSystem.getClip();
            musicaFondo.open(audioIn);
            musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
            musicaFondo.start();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir la música de fondo.");
            e.printStackTrace();
        }
    }
    private void detenerMusicaFondo() {
        if (musicaFondo != null && musicaFondo.isRunning()) {
            musicaFondo.stop();
            musicaFondo.close();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE) {
            if (!iniciado) {
                iniciado = true;
                gameLoop.start();
                posTuberiasTimer.start();
                tiempoUltimaFruta = System.currentTimeMillis(); // Inicia el contador de frutas al iniciar el juego
            }
            if (!gameOver && !pausado) {
                velocidadPajaroY = -9;
            } else if (gameOver) {
                reiniciarJuego();
            }
        }
        if (key == KeyEvent.VK_P && iniciado && !gameOver) {
            pausado = !pausado;
            System.out.println("Juego " + (pausado ? "PAUSADO" : "REANUDADO"));
        }
        if (key == KeyEvent.VK_V) {
            mostrarPuntajes();
        }
    }

    private void reiniciarJuego() {
        pajaro.setPosEjeY(boardHeight / 2);
        velocidadPajaroY = 0;
        tuberias.clear();
        frutas.clear(); // Limpia las frutas al reiniciar el juego
        gameOver = false;
        pausado = false;
        score = 0;
        iniciado = false;
        doblePuntajeActivo = false; // Reinicia los bonus
        ralentizarActivo = false;
        velocidadTuberiasX = -2; // Restaura velocidad
        tiempoUltimaFruta = 0; // Reinicia el tiempo para la primera fruta
        repaint();
        System.out.println("Juego Reiniciado.");
    }

    private void guardarPuntaje() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("puntajes.txt", true))) {
            writer.write(nombreJugador + " - " + (int) score + "\n");
            System.out.println("Puntaje guardado: " + nombreJugador + " - " + (int) score);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar puntaje", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void mostrarMenuGameOver() {
        int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Quieres volver a jugar?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Sí", "No"},
                "Sí"
        );
        if (opcion == JOptionPane.YES_OPTION) {
            reiniciarJuego();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }



    private void mostrarPuntajes() {
        java.util.List<String> lineas = new ArrayList<>();
        try (java.util.Scanner scanner = new java.util.Scanner(new java.io.File("puntajes.txt"))) {
            while (scanner.hasNextLine()) {
                lineas.add(scanner.nextLine());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo leer el archivo de puntajes.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        // Ordenar por puntaje descendente
        lineas.sort((a, b) -> {
            try {
                int scoreA = Integer.parseInt(a.split("-")[1].trim());
                int scoreB = Integer.parseInt(b.split("-")[1].trim());
                return Integer.compare(scoreB, scoreA); // de mayor a menor
            } catch (Exception e) {
                System.err.println("Error al parsear puntaje en línea: " + a + " o " + b + " - " + e.getMessage());
                return 0; // Mantener el orden relativo si hay un error de parseo
            }
        });

        StringBuilder mensaje = new StringBuilder("🏆 Puntajes guardados:\n\n");
        for (String linea : lineas) {
            mensaje.append(linea).append("\n");
        }
        JOptionPane.showMessageDialog(this, mensaje.toString(), "Puntajes", JOptionPane.INFORMATION_MESSAGE);
    }
}
