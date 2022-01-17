package simulation.globus;

import simulation.constants.Constants;
import simulation.matrix.Matrix2D;
import simulation.vector.Vector3D;

import javax.swing.*;
import java.awt.*;

public class PointModelGlobe extends JPanel {

    protected int pointDiameter;
    protected Matrix2D projectionMatrix;
    protected double moveX;

    public PointModelGlobe(int pointDiameter) {
        this.pointDiameter = pointDiameter;
        projectionMatrix = new Matrix2D(Constants.projectionS1, Constants.projectionAlpha);
        this.moveX = 0;
    }

    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int lastX = 0;
        int lastY = 0;

        //Nordhalbkugel
        for(double i = 0; i < 90; i += Constants.pointGap) {
            for(double j = 0; j < 360; j += Constants.pointGap) {
                g.setColor(Color.GREEN);
                g.fillOval((int) geodeticPoint(i, (j+ moveX), true, false).x, (int) geodeticPoint(i, (j+ moveX), true, false).y, pointDiameter, pointDiameter);
            }
        }
        //Südhalbkugel
        for(double i = 0; i > -90; i -= Constants.pointGap) {
            for(double j = 0; j < 360; j += Constants.pointGap) {
                g.setColor(Color.GREEN);
                g.fillOval((int) geodeticPoint(i, (j+ moveX), true, false).x, (int) geodeticPoint(i, (j+ moveX), true , false).y, pointDiameter, pointDiameter);
            }
        }
        moveX += Constants.rotateSpeed;
        if(moveX > 360) {
            moveX = 0;
        }
    }

    public Vector3D geodeticPoint(double theta, double phi, boolean projectToScreen, boolean airport) {
        theta = Math.toRadians(theta);
        phi = Math.toRadians(phi);

        double x = Constants.globeRadius * Math.cos(theta) * Math.cos(phi);
        double y = Constants.globeRadius * Math.cos(theta) * Math.sin(phi);
        double z = Constants.globeRadius * Math.sin(theta);

        Vector3D result = new Vector3D(x, y, z);

        //Wenn obere Zeile aktiv -> 3D, wenn zweite Zeile dann 2D:
        if(projectToScreen) {
            result = projectionMatrix.matrix2dTimesVector(this.projectionMatrix, result);
            //result.moveToCenter();
        }
        if(airport) {
            result.x -= Constants.airportPointDiameter/2;
            result.y -= Constants.airportPointDiameter/2;
        }

        return result;
    }

    public void resetProjectionMatrix() {
        projectionMatrix = new Matrix2D(Constants.projectionS1, Constants.projectionAlpha);
    }
}
