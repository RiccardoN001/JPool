package game.model;

public class Vector {

    // BRIEF CLASS DESCRIPTION (FINISHED)
    // Represents 2D vectors starting from the origin of the xy plane
    
    private double x, y; // vector x,y components

    // CONSTRUCTOR METHOD
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // VECTOR OPERATIONS

    // vector sum
    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    // vector difference
    public Vector sub(Vector v) {
        return new Vector(x - v.x, y - v.y);
    }

    // vector scalar multiplication
    public void multiply(double k) {
        x *= k;
        y *= k;
    }

    // scalar product
    public double scalar(Vector v) {
        return x*v.x + y*v.y;
    }

    // vector module
    public double getSize() {
        return Math.sqrt(x*x + y*y);
    }

    // vector normalization
    public void normalize() {
        double size = getSize();
        x /= size;
        y /= size;
    }

    // null vector
    public Boolean isNull() {
        return ((x == 0) && (y == 0));
    }

    // GET/SET METHODS

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }



   /*  public static void sphereCollisionVelocities(Vector c1, Vector v1, Vector c2, Vector v2) {

        Vector pDiff1 = c1.sub(c2);
        Vector vDiff1 = v1.sub(v2);
        pDiff1.normalize();
        pDiff1.multiply(vDiff1.scalar(pDiff1));

        v1 = v1.sub(pDiff1);

        Vector pDiff2 = c2.sub(c1);
        Vector vDiff2 = v2.sub(v1);
        pDiff2.normalize();
        pDiff2.multiply(vDiff2.scalar(pDiff2));

        v2 = v2.sub(pDiff2);

    }*/


}
