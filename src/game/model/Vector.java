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

}
