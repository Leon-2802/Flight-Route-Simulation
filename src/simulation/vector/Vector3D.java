package simulation.vector;

import simulation.constants.Constants;

public class Vector3D {

    public double x, y, z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void moveToCenter() {
        this.x += Constants.WIDTH/2;
        this.y += Constants.HEIGHT/2;
    }
    public void moveBack() {
        this.x -= Constants.WIDTH/2;
        this.y -= Constants.HEIGHT/2;
    }

    public Vector3D addVectors(Vector3D v2) {
        return new Vector3D(
                this.x + v2.x,
                this.y + v2.y,
                this.z + v2.z
        );
    }
    public Vector3D multiplyVector(double amount) {
        return new Vector3D(
                this.x * amount,
                this.y * amount,
                this.z * amount
        );
    }
    public double vectorAngle(Vector3D v2) {
        double zaehler = scalarProduct(this, v2);
        double nenner = vectorLength(this) * vectorLength(v2);
        double angle = Math.acos(zaehler/nenner);
        return angle;
    }
    public Vector3D toUnitVector(double length) {
        return new Vector3D(
                this.x/length,
                this.y/length,
                this.z/length
        );
    }

    public Vector3D crossproduct(Vector3D v2) {
        double x = (this.y * v2.z) - (this.z * v2.y);
        double y = (this.z * v2.x) - (this.x * v2.z);
        double z = (this.x * v2.y) - (this.y * v2.x);

        return new Vector3D(x, y, z);
    }
    private double scalarProduct(Vector3D v1, Vector3D v2) {
        double result = (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
        return result;
    }
    public double vectorLength(Vector3D v) {
        double result = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
        return result;
    }
}
