package simulation.globus;

import simulation.constants.Constants;
import simulation.matrix.Matrix3D;
import simulation.matrix.Matrix2D;
import simulation.vector.Vector3D;

import java.awt.*;

public class WireframeGlobe extends PointModelGlobe {

    private boolean shade;
    private boolean umrissElipse;
    private Vector3D centerPoint;
    public WireframeGlobe(boolean shade, int pointDiameter) {
        super(pointDiameter);
        this.shade = shade;
        this.umrissElipse = false;
        this.centerPoint = new Vector3D(0,0,0);
        centerPoint.moveToCenter();
    }

    @Override
    public void paintComponent(Graphics g) {

        //drawUmrissellipse(g);

        int lastPhi = 0;
        int lastTheta = -90;

        //Breitenkreise:
        for(int i = 0; i <= 90; i+= Constants.lineGap) {
            for (int j = 6; j <= 360; j += Constants.pointGap) {
                Vector3D globeCoordinates = geodeticPoint(i, lastPhi + moveX, false, false);
                connectGeodeticPoints(g, checkColor(globeCoordinates), geodeticPoint(i ,lastPhi + moveX, true, false),
                        geodeticPoint(i,j + moveX, true, false));
                lastPhi = j;
            }
        }
        for(int i = 0; i >= -90; i-= Constants.lineGap) {
            for (int j = 6; j <= 360; j += Constants.pointGap) {
                Vector3D globeCoordinates = geodeticPoint(i, lastPhi + moveX, false, false);
                connectGeodeticPoints(g, checkColor(globeCoordinates), geodeticPoint(i, lastPhi + moveX, true, false),
                        geodeticPoint(i,j + moveX, true, false));
                lastPhi = j;
            }
        }
        //Längenkreise:
        for(double i = 0; i <= 360; i+= Constants.lineGap) {
            for(int j = -90; j <= 90; j += Constants.pointGap) {
                Vector3D globeCoordinates = geodeticPoint(lastTheta, i + moveX, false, false);
                connectGeodeticPoints(g, checkColor(globeCoordinates), geodeticPoint(lastTheta, i + moveX, true, false),
                        geodeticPoint(j,i + moveX, true, false));
                lastTheta = j;
            }
        }
        //Achsen:
        connectGeodeticPoints(g, Color.BLUE, centerPoint, geodeticPoint(0, 0+moveX, true, false));
        connectGeodeticPoints(g, Color.RED, centerPoint, geodeticPoint(0, 90+moveX, true, false));
        g.setColor(Color.BLUE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        g.drawString("Blue Axis = Z-Axis (Phi = 0°)", 100, 220);
        g.setColor(Color.RED);
        g.drawString("Red Axis = X-Axis (Phi = 90°)", 100, 250);

        moveX += Constants.rotateSpeed;
        if(moveX > 360) {
            moveX = 0;
        }
    }

    private Color checkColor(Vector3D v) {
        double check = Math.cos(Constants.phiP)*Math.cos(Constants.thetaP)*v.x + Math.sin(Constants.phiP)*Math.cos(Constants.thetaP)*v.y + Math.sin(Constants.thetaP)*v.z;
        if(check < 0) {
            if(shade)
                return new Color(47, 105, 43);
            else
                return Color.GREEN;
        }
        else
            return Color.GREEN;
    }

    public void connectGeodeticPoints(Graphics g, Color color, Vector3D p1, Vector3D p2) {
        g.setColor(color);
        g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
    }

    public void drawUmrissellipse(Graphics g) {
        double phiP = Math.atan(Constants.projectionS1*Math.sin(Math.toRadians(Constants.projectionAlpha)));
        double thetaP = Math.atan(-Constants.projectionS1*Math.cos(Math.toRadians(Constants.projectionAlpha))*Math.cos(phiP));
        System.out.println(phiP);
        System.out.println(thetaP);

        Matrix3D m2 = new Matrix3D(
                new Vector3D(Math.cos(phiP), Math.sin(phiP), 0),
                new Vector3D(-Math.sin(phiP), Math.cos(phiP), 0),
                new Vector3D(0,0,1)
        );
        Matrix3D m3 = new Matrix3D(
                new Vector3D(Math.cos(thetaP), 0,Math.sin(thetaP)),
                new Vector3D(0,1,0),
                new Vector3D(-Math.sin(thetaP), 0, Math.cos(thetaP))
        );

        Matrix2D step1 = projectionMatrix.m2Timesm3(projectionMatrix, m2);
        Matrix2D step2 = step1.m2Timesm3(step1, m3);

        Vector3D lastPoint = new Vector3D(0,0,0);

        for (double t = 0; t <= 360; t += Constants.pointGap) {
            Vector3D v = new Vector3D(Constants.globeRadius*0, Constants.globeRadius*Math.cos(Math.toRadians(t)), Constants.globeRadius*Math.sin(Math.toRadians(t)));
            Vector3D result = step2.matrix2dTimesVector(step2, v);

            if(t == 0)
                lastPoint = result;

            connectGeodeticPoints(g, Color.RED, lastPoint, result);
            lastPoint = result;
        }
    }
}
