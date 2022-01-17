package simulation.matrix;

import simulation.vector.Vector3D;

public class Matrix3D {

    public Vector3D u;
    public Vector3D v;
    public Vector3D w;

    public Matrix3D(Vector3D u, Vector3D v, Vector3D w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    public Matrix3D m3Timesm3(Matrix3D m1, Matrix3D m2){
        Vector3D u = new Vector3D(((m1.u.x * m2.u.x) + (m1.v.x * m2.u.y) + (m1.w.x * m2.u.z)),
            ((m1.u.y * m2.u.x) + (m1.v.y * m2.u.y) + (m1.w.y * m2.u.z)),
                ((m1.u.z * m2.u.x) + (m1.v.z * m2.u.y) + (m1.w.z * m2.u.z)));
        Vector3D v = new Vector3D(((m1.u.x * m2.v.x) + (m1.v.x * m2.v.y) + (m1.w.x * m2.v.z)),
                ((m1.u.y * m2.v.x) + (m1.v.y * m2.v.y) + (m1.w.y * m2.v.z)),
                ((m1.u.z * m2.v.x) + (m1.v.z * m2.v.y) + (m1.w.z * m2.v.z)));
        Vector3D w = new Vector3D(((m1.u.x * m2.w.x) + (m1.v.x * m2.w.y) + (m1.w.x * m2.w.z)),
                ((m1.u.y * m2.w.x) + (m1.v.y * m2.w.y) + (m1.w.y * m2.w.z)),
                ((m1.u.z * m2.w.x) + (m1.v.z * m2.w.y) + (m1.w.z * m2.w.z)));

        return new Matrix3D(u, v, w);
    }
    public Matrix2D m3Timesm2(Matrix3D m3d, Matrix2D m2d) {
        return null;
    }
    public Vector3D matrix3dTimesVector(Matrix3D m, Vector3D v) {
        double x = (m.u.x * v.x) + (m.v.x * v.y) + (m.w.x * v.z);
        double y = (m.u.y * v.x) + (m.v.y * v.y) + (m.w.y * v.z);
        double z = (m.u.z * v.x) + (m.v.z * v.y) + (m.w.z * v.z);
        v.x = x;
        v.y = y;
        v.z = z;

        return v;
    }
}
