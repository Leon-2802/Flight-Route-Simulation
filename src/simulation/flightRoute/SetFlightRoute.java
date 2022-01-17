package simulation.flightRoute;

import simulation.constants.Constants;
import simulation.globus.WireframeGlobe;
import simulation.matrix.Matrix2D;
import simulation.matrix.Matrix3D;
import simulation.vector.Vector3D;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class SetFlightRoute extends Airports {

    private WireframeGlobe wireframeGlobe;
    protected Matrix2D projectionMatrix;
    private int randomTheta, randomPhi, randomTheta2, randomPhi2;
    private String aiportName1, airportName2;
    private double delta;
    private double deltaInDeg;
    private Matrix3D rot;
    private double distance;
    private boolean routeCalculated;
    private double move;
    private double tPlane;
    double lastT;
    private double ripleMove;

    public SetFlightRoute (WireframeGlobe wireframeGlobe) {
        super(wireframeGlobe);

        this.wireframeGlobe = wireframeGlobe;
        projectionMatrix = new Matrix2D(Constants.projectionS1, Constants.projectionAlpha);
        routeCalculated = false;

        move = 0;

        setRandomAirports(move);
    }
    private void setRandomAirports(double move) {
        int index1 = ThreadLocalRandom.current().nextInt(0, airportTheta.length);
        randomTheta = airportTheta[index1];
        randomPhi = airportPhi[index1];

        int index2 = ThreadLocalRandom.current().nextInt(0, airportTheta.length);
        if(index2 == index1)
            setRandomAirports(move);
        randomTheta2 = airportTheta[index2];
        randomPhi2 = airportPhi[index2];

        aiportName1 = airportNames[index1];
        airportName2 = airportNames[index2];

        tPlane = 0;
        lastT = 0;
        ripleMove = 0;
        calculations(move);
        System.out.println("Angle between the two points on the globe: " + deltaInDeg);
        System.out.println("Distance between the two points on the globe: " + distance);
        System.out.println("Globe-radius: 250");

        System.out.println("Theta of Point1: " + randomTheta);
        System.out.println("Phi of Point1: " + randomPhi);
        System.out.println("Theta of Point2: " + randomTheta2);
        System.out.println("Phi of Point2: " + randomPhi2);
    }

    @Override public void paintComponent(Graphics g) {

        move += Constants.rotateSpeed;
        if(move > 360) {
            move = 0;
        }
        if(routeCalculated == false)
            return;

        //Flughafennamen anzeigen:
        g.setColor(Color.GREEN);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        g.drawString("From: " + aiportName1, 100, 130);
        g.drawString("To: " + airportName2, 100, 160);

        //bewegender Punkt und Fluglinie:
        if(tPlane <= deltaInDeg/Constants.flightspeed) {
            movingPoint(g, tPlane);
            tPlane++;
        }
        else {
            drawRoute(g,deltaInDeg);
        }

        //Ripple-Effekt
        if(tPlane >= deltaInDeg/Constants.flightspeed && ripleMove <= Constants.rippleDiameter /Constants.rippleSpeed) {
            rippleEffect(g, pointOnRoute(deltaInDeg, true), ripleMove * Constants.rippleSpeed);
            ripleMove++;
        }
        if(ripleMove >= Constants.rippleDiameter/Constants.rippleSpeed) {
            routeCalculated = false;
            setRandomAirports(move);
            return;
        }

    }

    private Vector3D pointOnRoute(double t, boolean projectToScreen) {
        calculations(move);

        Vector3D pointCoordinates = new Vector3D(
                Constants.globeRadius * Math.cos(Math.toRadians(t)),
                Constants.globeRadius * Math.sin(Math.toRadians(t)),
                0
        );

        Vector3D result = rot.matrix3dTimesVector(this.rot, pointCoordinates);
        if(projectToScreen)
            result = projectionMatrix.matrix2dTimesVector(this.projectionMatrix, result);

        return result;
    }

    private void calculations(double move) {
        Vector3D p = pointModelGlobe.geodeticPoint(randomTheta, randomPhi + move, false, false);
        double pLength = p.vectorLength(p);
        Vector3D q = pointModelGlobe.geodeticPoint(randomTheta2, randomPhi2 + move, false, false);

        delta = p.vectorAngle(q);
        deltaInDeg = Math.toDegrees(delta);
        distance = Constants.globeRadius * delta;

        rot = calculateRotationMatrix(p,q,pLength);

        routeCalculated = true;
    }

    private Matrix3D calculateRotationMatrix(Vector3D p, Vector3D q, double pLength) {
        Vector3D pDach = p.toUnitVector(pLength);
        Vector3D nDach = p.crossproduct(q).toUnitVector(p.crossproduct(q).vectorLength(p.crossproduct(q)));
        Vector3D uDach = nDach.crossproduct(pDach).toUnitVector(nDach.crossproduct(pDach).vectorLength(nDach.crossproduct(pDach)));

        Matrix3D rot = new Matrix3D(
                pDach,
                uDach,
                nDach
        );

        return rot;
    }

    private void movingPoint(Graphics g, double t) {
        Vector3D v = pointOnRoute(Constants.flightspeed*t, true);
        g.setColor(checkColor(pointOnRoute(Constants.flightspeed*t, false), false));
        g.fillOval((int) (v.x - Constants.airportPointDiameter/2), (int) (v.y - Constants.airportPointDiameter/2),
                Constants.airportPointDiameter, Constants.airportPointDiameter);

        drawRoute(g, Constants.flightspeed*t);
    }
    private void drawRoute(Graphics g, double t) {
        for(double tLine = 0; tLine <= t; tLine += Constants.pointGap) {
            wireframeGlobe.connectGeodeticPoints(g, checkColor(pointOnRoute(lastT, false), true),
                    pointOnRoute(lastT, true), pointOnRoute(tLine, true));
            lastT = tLine;
        }
        lastT = 0;
    }

    private void rippleEffect(Graphics g, Vector3D center, double diameter) {
        g.setColor(Color.WHITE);
        g.drawOval((int) (center.x - diameter/2), (int) (center.y - diameter/2), (int) diameter, (int) diameter);
    }

    public void resetProjectionMatrix() {
        projectionMatrix = new Matrix2D(Constants.projectionS1, Constants.projectionAlpha);
    }

    private Color checkColor(Vector3D v, boolean line) {
        double check = Math.cos(Constants.phiP)*Math.cos(Constants.thetaP)*v.x + Math.sin(Constants.phiP)*Math.cos(Constants.thetaP)*v.y + Math.sin(Constants.thetaP)*v.z;
        if(line) {
            if(check < 0)
                return Color.gray;
            else
                return Color.WHITE;
        } else {
            if(check < 0)
                return new Color(51, 175, 184);
            else
                return Color.CYAN;
        }
    }
}
