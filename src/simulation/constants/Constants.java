package simulation.constants;

import simulation.vector.Vector3D;

public class Constants {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 800;
    public static final Vector3D center = new Vector3D(WIDTH/2, HEIGHT/2, 0);
    public static final int globeRadius = 250;
    public static final int airportPointDiameter = 8;
    public static final double pointGap = 6;
    public static final int lineGap = 10;
    public static double projectionS1 = 1/Math.sqrt(2.5);//Wert in Klammer durch Panel anpassbar
    public static int projectionAlpha = 135;
    public static double rotateSpeed = -0.3;//Auch im Panel durch Schieberegler anpassbar
    public static final double flightspeed = 0.15;
    public static final double rippleSpeed = 0.9;
    public static final double rippleDiameter = 70;
    public static double phiP = Math.atan(projectionS1*Math.sin(projectionAlpha));
    public static double thetaP = Math.atan((-projectionS1*Math.cos(projectionAlpha))*Math.cos(phiP));

    public static void resetPhiPThetaP() {
        phiP = Math.atan(projectionS1*Math.sin(projectionAlpha));
        thetaP = Math.atan((-projectionS1*Math.cos(projectionAlpha))*Math.cos(phiP));
    }
}
