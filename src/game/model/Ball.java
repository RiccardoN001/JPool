package game.model;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class Ball {

    // BRIEF CLASS DESCRIPTION (FINISHED)
    // Represents the physical model of a (3D) billiard ball

    private Vector initialPosition, position, velocity;
    private double acceleration, diameter;
    private Sphere sphere = new Sphere(12.5);
    private String imagePath;
    private int ballType;
    private int ballNumber;
    private boolean dropped;

    // CONSTRUCTOR METHOD
    public Ball(double positionX, double positionY, String imagePath, int ballType, int ballNumber) {
        position = new Vector(positionX, positionY);
        velocity = new Vector(0, 0);
        this.imagePath = imagePath;
        this.ballType = ballType;
        this.ballNumber = ballNumber;
        dropped = false;
        acceleration = .99;
        diameter = 25;
        initialPosition = new Vector(positionX, positionY);
    }
    
    public Node DrawBall() {

        sphere.setLayoutX(position.getX());
        sphere.setLayoutY(position.getY());
        sphere.setRotationAxis(Rotate.Y_AXIS);
        sphere.setRotate(270); // angolo necessario a mostrare numero?

        // 3D
        PhongMaterial material = new PhongMaterial();
        material.setSpecularColor(Color.WHITE);
        Image img = new Image("file:" + this.imagePath);
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

    public void ballCollision(Ball b) {

        // ball A calls the method, ball B is given as an input

        Vector normalToB = position.sub(b.position);
        normalToB.normalize();
        normalToB.multiply(velocity.scalar(normalToB));

        Vector bCollisionVector = velocity.sub(normalToB);

        Vector normalToA = b.position.sub(position);
        normalToA.normalize();
        normalToA.multiply(b.velocity.scalar(normalToA));

        Vector aCollisionVector = b.velocity.sub(normalToA);
        

        velocity = normalToA.add(bCollisionVector);
        b.velocity = normalToB.add(aCollisionVector);
        
    }

    public void bankCollision() {

        double x = position.getX();
        double y = position.getY();
        double r = diameter/2;

        // LEFT BANK (A)
        // REGION A1
        if (x - r <= Constants.A_MARGIN && (y >= Constants.A_UP_CORNER_START && y <= Constants.A_UP_CORNER_END)) {
            velocity.setY(-velocity.getSize());
            velocity.setX(0);
        }
        // REGION A2
        else if (x - r <= Constants.A_MARGIN && (y >= Constants.A_UP_CORNER_END && y <= Constants.A_DOWN_CORNER_START)) {
            velocity.setX(Math.abs(velocity.getX()));
        }
        // REGION A3
        else if (x - r <= Constants.A_MARGIN && (y >= Constants.A_DOWN_CORNER_START && y <= Constants.A_DOWN_CORNER_END)) {
            velocity.setY(velocity.getSize());
            velocity.setX(0);
        }

        // RIGHT BANK (B)
        // REGION B1
        if (x + r >= Constants.B_MARGIN && (y >= Constants.B_UP_CORNER_START && y <= Constants.B_UP_CORNER_END)) {
            velocity.setY(-velocity.getSize());
            velocity.setX(0);

        }
        // REGION B2
        else if (x + r >= Constants.B_MARGIN && (y >= Constants.B_UP_CORNER_END && y <= Constants.B_DOWN_CORNER_START)) {
            velocity.setX(-Math.abs(velocity.getX()));
        }
        
        // REGION B3
        else if (x + r >= Constants.B_MARGIN && (y >= Constants.B_DOWN_CORNER_START && y <= Constants.B_DOWN_CORNER_END)) {
            velocity.setY(velocity.getSize());
            velocity.setX(0);
        }

        // LEFT DOWN BANK (E)
        // REGION E1
        if (y + r >= Constants.EF_MARGIN && (x >= Constants.E_LEFT_CORNER_START && x <= Constants.E_LEFT_CORNER_END)) {
            velocity.setX(-velocity.getSize());
            velocity.setY(0);
        }
        // REGION E2
        else if (y + r >= Constants.EF_MARGIN && (x >= Constants.E_LEFT_CORNER_END && x <= Constants.E_RIGHT_CORNER_START)) {
            velocity.setY(-Math.abs(velocity.getY()));
        }
        // REGION E3
        else if (y + r >= Constants.EF_MARGIN && (x >= Constants.E_RIGHT_CORNER_START && x <= Constants.E_RIGHT_CORNER_START)) {
            velocity.setX(Math.abs(velocity.getX()));
        }

        // RIGHT DOWN BANK (F)
        // REGION F1
        if (y + r >= Constants.EF_MARGIN && (x >= Constants.F_LEFT_CORNER_START && x <= Constants.F_LEFT_CORNER_END)) {
            velocity.setX(-Math.abs(velocity.getX()));
        }
        // REGION F2
        else if (y + r >= Constants.EF_MARGIN && (x >= Constants.F_LEFT_CORNER_END && x <= Constants.F_RIGHT_CORNER_START)) {
            velocity.setY(-Math.abs(velocity.getY()));
        }
        // REGION F3
        else if (y + r >= Constants.EF_MARGIN && (x >= Constants.F_RIGHT_CORNER_START && x <= Constants.F_RIGHT_CORNER_END)) {
            velocity.setX(velocity.getSize());
            velocity.setY(0);
        }

        // LEFT UP BANK (C)
        // REGION C1
        if (y - r <= Constants.CD_MARGIN && (x >= Constants.C_LEFT_CORNER_START && x <= Constants.C_LEFT_CORNER_END)) {
            velocity.setX(-velocity.getSize());
            velocity.setY(0);
        }
        // REGION C2
        else if (y - r <= Constants.CD_MARGIN && (x >= Constants.C_LEFT_CORNER_END && x <= Constants.C_RIGHT_CORNER_START)) {
            velocity.setY(Math.abs(velocity.getY()));
        }
        // REGION C3
        else if (y - r <= Constants.CD_MARGIN && (x >= Constants.C_RIGHT_CORNER_START && x <= Constants.C_RIGHT_CORNER_END)) {
            velocity.setX(Math.abs(velocity.getX()));
        }

        // RIGHT UP BANK (D)
        // REGION D1
        if (y - r <= Constants.CD_MARGIN && (x >= Constants.D_LEFT_CORNER_START && x <= Constants.D_LEFT_CORNER_END)) {
            velocity.setX(-Math.abs(velocity.getX()));
        }
        // REGION D2
        else if (y - r <= Constants.CD_MARGIN && (x >= Constants.D_LEFT_CORNER_END && x <= Constants.D_RIGHT_CORNER_START)) {
            velocity.setY(Math.abs(velocity.getY()));
        }
        // REGION D3
        else if (y - r <= Constants.CD_MARGIN && (x >= Constants.D_RIGHT_CORNER_START && x <= Constants.D_RIGHT_CORNER_END)) {
            velocity.setX(velocity.getSize());
            velocity.setY(0);
        }

    }

    public void tableFriction() {
        velocity.setX (velocity.getX() * acceleration);
        velocity.setY (velocity.getY() * acceleration);
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

    public double getDiameter() {
        return diameter;
    }

}
