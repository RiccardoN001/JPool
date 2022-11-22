package game.model;

// BRIEF CLASS DESCRIPTION
// Represents 2D vectors starting from the origin of the xy plane (Pane)

public class Vector {
    
    private double x, y;

    // CONSTRUCTOR METHOD
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // VECTOR OPERATIONS

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector sub(Vector v) {
        return new Vector(x - v.x, y - v.y);
    }

    public void multiply(double k) {
        x *= k;
        y *= k;
    }

    public double scalar(Vector v) {
        return x*v.x + y*v.y;
    }

    public void normalize() {
        double size = getSize();
        x /= size;
        y /= size;
    }

    public Vector perpendicularLeft() {
        return new Vector(y, -x);
    }

    public Vector perpendicularRight() {
        return new Vector(-y, x);
    }

    public double determinant(Vector a, Vector b) {
        return ((x - a.getX()) * (b.getY() - a.getY()) - (y - a.getY()) * (b.getX() - a.getX()));
    }

    public double getSize() {
        return Math.sqrt(x*x + y*y);
    }

    public Boolean isNull() {
        return ((x == 0) && (y == 0));
    }

    // GET/SET METHODS

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
