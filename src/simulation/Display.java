package simulation;

import simulation.constants.Constants;
import simulation.controls.ControlPanel;
import simulation.flightRoute.Airports;
import simulation.flightRoute.SetFlightRoute;
import simulation.globus.PointModelGlobe;
import simulation.globus.WireframeGlobe;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Display extends Canvas implements Runnable {

    private Thread thread;
    private JFrame frame; //Bildschirmanzeige
    private static String title = "Flight Route Simulation";
    private static boolean running = false;
    private PointModelGlobe pointModelGlobe;
    private WireframeGlobe wireframeGlobe;
    private Airports airports;
    private SetFlightRoute setFlightRoute;

    public Display() {
        //Set up simulation frame:
        this.frame = new JFrame(title);
        Dimension size = new Dimension(Constants.WIDTH, Constants.HEIGHT);
        this.setPreferredSize(size);

        //Instantiate essential classes:
        pointModelGlobe = new PointModelGlobe(5);
        wireframeGlobe = new WireframeGlobe(true, 15);
        airports = new Airports(this.wireframeGlobe);
        setFlightRoute = new SetFlightRoute(this.wireframeGlobe);

        //Control Panel:
        new ControlPanel(pointModelGlobe, wireframeGlobe, setFlightRoute);
    }

    public static void main(String[] args) {
        Display display = new Display();
        display.frame.add(display);
        display.frame.pack();
        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.setLocationRelativeTo(null);
        display.frame.setResizable(false);
        display.frame.setVisible(true);

        display.Start();
    }

    public synchronized void Start() {
        running = true;
        this.thread = new Thread(this, "Display");
        this.thread.start();
    }

    public synchronized void Stop() {
        running = false;
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60;
        double delta = 0;
        int frames = 0;

        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                render();
                frames++;
                delta--;
            }

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                this.frame.setTitle(title + " | " + frames + " fps");
                frames = 0;
            }
        }

        Stop();
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);

        //Hier Alle Grafik-Klassen einfügen:
        //pointModelGlobe.paintComponent(g);
        wireframeGlobe.paintComponent(g);
        airports.paintComponent(g);
        setFlightRoute.paintComponent(g);

        g.dispose();
        bs.show();
    }

    private void update() {
    }
}