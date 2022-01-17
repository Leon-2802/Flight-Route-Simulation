package simulation.matrix;

import simulation.vector.Vector2D;
import simulation.vector.Vector3D;

public class Matrix2D {

    public Vector2D u;
    public Vector2D v;
    public Vector2D w;

    public Matrix2D(Vector2D u, Vector2D v, Vector2D w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }
    public Matrix2D(double s1, double alpha) {
        this.u = new Vector2D(-s1 * Math.sin(alpha), -s1 * Math.cos(alpha));
        this.v = new Vector2D(1, 0);
        this.w = new Vector2D(0, -1);
    }

    public Vector3D matrix2dTimesVector(Matrix2D m1, Vector3D v1) {
        double x = (m1.u.x * v1.x) + (m1.v.x * v1.y) + (m1.w.x * v1.z);
        double y = (m1.u.y * v1.x) + (m1.v.y * v1.y) + (m1.w.y * v1.z);
        v1.x = x;
        v1.y = y;

        v1.moveToCenter();

        return v1;
    }
    public Matrix2D m2Timesm3(Matrix2D m2, Matrix3D m3) {
        Vector2D u = new Vector2D(
                ((m2.u.x * m3.u.x) + (m2.v.x * m3.u.y) + (m2.w.x * m3.u.z)),
                ((m2.u.y * m3.u.x) + (m2.v.y * m3.u.y) + (m2.w.y * m3.u.z))
        );
        Vector2D v = new Vector2D(
                ((m2.u.x * m3.v.x) + (m2.v.x * m3.v.y) + (m2.w.x * m3.v.z)),
                ((m2.u.y * m3.v.x) + (m2.v.y * m3.v.y) + (m2.w.y * m3.v.z))
        );
        Vector2D w = new Vector2D(
                ((m2.u.x * m3.w.x) + (m2.v.x * m3.w.y) + (m2.w.x * m3.w.z)),
                ((m2.u.y * m3.w.x) + (m2.v.y * m3.w.y) + (m2.w.y * m3.w.z))
        );

        return new Matrix2D(u, v, w);
    }
}
