package simulation.flightRoute;

import simulation.constants.Constants;
import simulation.globus.PointModelGlobe;
import simulation.matrix.Matrix2D;
import simulation.vector.Vector3D;

import javax.swing.*;
import java.awt.*;

public class Airports extends JPanel {

    protected int[] airportTheta = {50, 36, 25, 34, 31, 14, 51, 41, -23, -34};
    protected int[] airportPhi = {9, 140, 55, 276, 122, 101, 0, 286, 314, 151};
    protected String[] airportNames = {"Frankfurt a. Main", "Tokyo Haneda", "Dubai", "Atlanta", "Shanghai Pudong", "Bangkok", "London Heathrow", "JFK New York", "São Paulo", "Sidney"};
    protected PointModelGlobe pointModelGlobe;
    private int diameter;
    protected double moveX;

    public Airports(PointModelGlobe pointModelGlobe) {
        this.pointModelGlobe = pointModelGlobe;
        this.diameter = Constants.airportPointDiameter;
        this.moveX = 0;
    }

    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int i = 0; i < airportTheta.length; i++) {
            g.setColor(checkColor(pointModelGlobe.geodeticPoint(airportTheta[i], airportPhi[i]+moveX, false, true)));
            g.fillOval((int) pointModelGlobe.geodeticPoint(airportTheta[i], airportPhi[i]+moveX, true, true).x,
                    (int) pointModelGlobe.geodeticPoint(airportTheta[i], airportPhi[i]+moveX, true, true).y, diameter,diameter);
        }
        moveX += Constants.rotateSpeed;;
        if(moveX > 360) {
            moveX = 0;
        }
    }

    private Color checkColor(Vector3D v) {
        double check = Math.cos(Constants.phiP)*Math.cos(Constants.thetaP)*v.x + Math.sin(Constants.phiP)*Math.cos(Constants.thetaP)*v.y + Math.sin(Constants.thetaP)*v.z;
        if(check < 0)
            return Color.gray;
        else
            return Color.white;
    }
}
