package game;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import static javafx.scene.paint.Color.WHITE;

public class Ball {

    // BRIEF CLASS DESCRIPTION
    // Represents the physical model of a (3D) billiard ball

    private double acceleration, diameter;
    private Vector position, velocity, initialPosition;
    private Sphere sphere = new Sphere(12.5);
    private String image;
    private int ballType;
    private int ballNumber;
    private boolean dropped;

    // CONSTRUCTOR METHOD
    public Ball(double positionX, double positionY, String image, int ballType, int ballNumber) {
        position = new Vector(positionX, positionY);
        velocity = new Vector(0, 0);
        this.image = image;
        this.ballType = ballType;
        this.ballNumber = ballNumber;
        dropped = false;
        acceleration = .99;
        diameter = 25;
        initialPosition = new Vector(positionX, positionY);
    }
    
    public Node DrawBall() {

        sphere.setLayoutX(position.getX()); // offset from initial x to final x
        sphere.setLayoutY(position.getY()); // offset from initial y to final y
        sphere.setRotationAxis(Rotate.Y_AXIS); // fissa l'asse di rotazione (lato corto = y)
        sphere.setRotate(270); // angolo necessario a mostrare numero?
        Image img = new Image(getClass().getResourceAsStream(image));

        // parvenza tridimensionale
        PhongMaterial material = new PhongMaterial();
        material.setSpecularColor(WHITE);
        material.setDiffuseMap(img);
        material.setSpecularPower(30);
        sphere.setMaterial(material);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setSpread(30);
        dropShadow.setOffsetX(10);
        dropShadow.setOffsetY(10);
        sphere.setEffect(dropShadow);

        return sphere;

    }

    public void spin() {
        Rotate rx = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate ry = new Rotate (0, 0, 0, 0, Rotate.Y_AXIS);
        rx.setAngle(Math.toDegrees(velocity.getY() / 10)); // 10?
        ry.setAngle(Math.toDegrees(velocity.getX() / 10)); // 10?
        sphere.getTransforms().addAll(rx, ry);
    }

    public boolean collides(Ball b) {
        return position.sub(b.position).getSize() <= (diameter/2 + b.diameter/2);
    }

    public void transferEnergy(Ball b) {

        Vector nvb = position.sub(b.position); // distanza tra i centri delle sfere
        nvb.normalize(); // normalizziamo il vettore (modulo 1, ma direzione e verso da A a B)
        nvb.multiply(velocity.scalar(nvb)); // prodotto scalare tra il vettore velocità di A e la normale verso B, poi moltiplico il risultato (intensità velocità A) per la normale (direzione e verso)

        Vector angleVectorb = velocity.sub(nvb);

        Vector nva = b.position.sub(position);
        nva.normalize();
        nva.multiply(b.velocity.scalar(nva));

        Vector angleVectora = b.velocity.sub(nva);

        velocity = nva.add(angleVectorb);
        b.velocity = nvb.add(angleVectora);
        
    }

    public void wallCollision() {

        double x = position.getX();
        double y = position.getY();
        double r = diameter/2;

        // CASE A
        if (x - r <= 147 && (y >= 196 && y <= 557)) {
            velocity.setX (Math.abs (velocity.getX ()));
        }
        else if (x - r <= 147 && (y >= 557 && y <= 570)) {
            //velocity.setY (Math.abs (velocity.getY ()));
            velocity.setY (velocity.getSize ());
            velocity.setX (0);
        }
        else if (x - r <= 147 && (y >= 178 && y <= 196)) {
            //velocity.setY (-Math.abs (velocity.getY ()));
            velocity.setY (-velocity.getSize ());
            velocity.setX (0);
        }

        // CASE B
        if (x + r >= 952 && (y >= 193 && y <= 551)) {
            velocity.setX (-Math.abs (velocity.getX ()));
        }
        else if (x + r >= 952 && (y >= 180 && y <= 192)) {
            //velocity.setY (-Math.abs (velocity.getY ()));
            velocity.setY (-velocity.getSize ());
            velocity.setX (0);

        }
        else if (x + r >= 952 && (y >= 551 && y <= 570)) {
            //velocity.setY (Math.abs (velocity.getY ()));
            velocity.setY (velocity.getSize ());
            velocity.setX (0);
        }

        // CASE C
        if (y + r >= 578 && (x >= 174 && x <= 515)) {
            velocity.setY (-Math.abs (velocity.getY ()));
        }
        else if (y + r >= 578 && (x >= 153 && x <= 174)) {
            //velocity.setX (-Math.abs (velocity.getX ()));
            velocity.setX (-velocity.getSize ());
            velocity.setY (0);
        }
        else if (y + r >= 578 && (x >= 515 && x <= 526)) {
            velocity.setX (Math.abs (velocity.getX ()));
        }

        // CASE D
        if (y - r <= 172 && (x >= 170 && x <= 515)) {
            velocity.setY (Math.abs (velocity.getY ()));
        }
        else if (y - r <= 172 && (x >= 153 && x <= 170)) {
            //velocity.setX (-Math.abs (velocity.getX ()));
            velocity.setX (-velocity.getSize ());
            velocity.setY (0);

        }
        else if (y - r <= 172 && (x >= 515 && x <= 529)) {
            velocity.setX (Math.abs (velocity.getX ()));

        }

        // CASE E
        if (y - r <= 172 && (x >= 580 && x <= 924)) {
            velocity.setY (Math.abs (velocity.getY ()));
        }
        else if (y - r <= 172 && (x >= 568 && x <= 580)) {
            velocity.setX (-Math.abs (velocity.getX ()));

        }
        else if (y - r <= 172 && (x >= 924 && x <= 945)) {
            //velocity.setX (Math.abs (velocity.getX ()));
            velocity.setX (velocity.getSize ());
            velocity.setY (0);

        }
        // CASE F
        if (y + r >= 574 && (x >= 584 && x <= 928)) {
            velocity.setY (-Math.abs (velocity.getY ()));
        }
        else if (y + r >= 574 && (x >= 568 && x <= 584)) {
            velocity.setX (-Math.abs (velocity.getX ()));
        }
        else if (y + r >= 574 && (x >= 928 && x <= 944)) {
            //velocity.setX (Math.abs (velocity.getX ()));
            velocity.setX (velocity.getSize ());
            velocity.setY (0);
        }

    }

    public void applyTableFriction() {
        velocity.setX (velocity.getX () * acceleration);
        velocity.setY (velocity.getY () * acceleration);
    }

    // GET/SET METHODS

    public Vector getInitialPosition() {
        return initialPosition;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(double x, double y) {
        this.velocity.setX(x);
        this.velocity.setY(y);
    }

    public Sphere getSphere() {
        return sphere;
    }

    public int getBallType() {
        return ballType;
    }

    public int getBallNumber() {
        return ballNumber;
    }

    public boolean isDropped() {
        return dropped;
    }
    
    public void setDropped(boolean dropped) {
        this.dropped = dropped;
    }

}
