package game;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import static javafx.scene.paint.Color.WHITE;

public class Ball {

    private double acceleration, diameter;
    private Vector position, velocity, initialPosition;
    private Sphere sphere = new Sphere(12.5);
    private String image;
    private int ballType; // 0,1,2,3
    private int ballNumber; // 0-15
    private boolean isDropped;

    public Ball(double positionX, double positionY, String image, int ballType, int ballNumber) {
        position = new Vector(positionX, positionY);
        velocity = new Vector(0, 0);
        this.image = image;
        this.ballType = ballType;
        this.ballNumber = ballNumber;
        isDropped = false;
        acceleration = .99;
        diameter = 25;
        initialPosition = new Vector(positionX, positionY);
    }

    public Node DrawBall() {
        // set radius
        sphere.setLayoutX(position.getX()); // offset from initial x to final x
        sphere.setLayoutY(position.getY()); // offset from initial y to final y
        sphere.setRotationAxis(Rotate.Y_AXIS); // fissa l'asse di rotazione (lato corto = y)
        sphere.setRotate(270); // angolo necessario a mostrate numero?
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
        nvb.multiply(velocity.scalarProd(nvb)); // prodotto scalare tra il vettore velocità di A e la normale verso B, poi moltiplico il risultato (intensità velocità A) per la normale (direzione e verso)

        Vector angleVectorb = velocity.sub(nvb);

        Vector nva = b.position.sub(position);
        nva.normalize();
        nva.multiply(b.velocity.scalarProd(nva));

        Vector angleVectora = b.velocity.sub(nva);

        velocity = angleVectorb.add(nva);
        b.velocity = angleVectora.add(nvb);
        
    }

}