package game;

public class Vector {
    
    private double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

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

    public double getSize() {
        return Math.sqrt(x*x + y*y);
    }

    public void normalize() {
        double size = getSize();
        x /= size;
        y /= size;
    }

    public double scalarProd(Vector v) {
        return x*v.x + y*v.y;
    }

    public Boolean isNull() {
        return ((x == 0) && (y == 0));
    }

}
