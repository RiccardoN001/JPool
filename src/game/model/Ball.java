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

    private Sphere sphere = new Sphere(Constants.BALL_RADIUS);

    private Vector position, velocity;
    private int ballType;
    private int ballNumber;
    private boolean dropped;

    // CONSTRUCTOR METHOD
    public Ball(double positionX, double positionY, int ballNumber) {
        position = new Vector(positionX, positionY);
        velocity = new Vector(0, 0);
        this.ballNumber = ballNumber;
        if(ballNumber >= 1 && ballNumber <= 7) {
            this.ballType = 1;
        } else if(ballNumber >= 9 && ballNumber <= 15) {
            this.ballType = 2;
        } else if(ballNumber == 8) {
            this.ballType = 3;
        } else if(ballNumber == 0) {
            this.ballType = 0;
        }
        dropped = false;
    }
    
    public Node drawBall() {

        sphere.setLayoutX(position.getX());
        sphere.setLayoutY(position.getY());

        sphere.setRotationAxis(Rotate.Y_AXIS);
        sphere.setRotate(270);

        PhongMaterial material = new PhongMaterial();
        Image ballExt = new Image("file:src/game/resources/Balls/Ball" + this.ballNumber + "Ext.png");
        material.setDiffuseMap(ballExt);
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(30);
        sphere.setMaterial(material);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setSpread(30);
        dropShadow.setOffsetX(10);
        dropShadow.setOffsetY(10);
        sphere.setEffect(dropShadow);

        return sphere;
    }

    public boolean collides(Ball b) {
        return position.sub(b.position).getSize() <= 2 * Constants.BALL_RADIUS;
    }

    public void spin() {
        Rotate rx = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate ry = new Rotate (0, 0, 0, 0, Rotate.Y_AXIS);
        rx.setAngle(Math.toDegrees(velocity.getY() / 10)); // 10?
        ry.setAngle(Math.toDegrees(velocity.getX() / 10)); // 10?
        sphere.getTransforms().addAll(rx, ry);
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
        double r = Constants.BALL_RADIUS;

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
        velocity.setX (velocity.getX() * Constants.TABLE_FRICTION);
        velocity.setY (velocity.getY() * Constants.TABLE_FRICTION);
    }

    public static void triangle(Ball ball[]) {

        // FIXED POSITIONS

        ball[0] = new Ball(Constants.HEAD_SPOT_X, Constants.HEAD_SPOT_Y, 0);
        ball[1] = new Ball(Constants.FOOT_SPOT_X, Constants.FOOT_SPOT_Y, 1);
        ball[8] = new Ball(Constants.TRIANGLE_ROW3_X, Constants.TRIANGLE_COL5_Y, 8);

        // VARIABLE POSITIONS

        // (int)(Math.random()*(max-min+1)+min)

        // ROW 5 -> 2 EXTERNAL VERTICES
        int solidOrStriped1 = (int)(Math.random()*2); // 0 -> SOLID , 1 -> STRIPED (UP VERTEX REFERENCE)
        int randomSolid1 = (int)(Math.random()*6+2);
        int randomStriped1 = (int)(Math.random()*7+9);
        if(solidOrStriped1 == 0) {
            ball[randomSolid1] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL1_Y, randomSolid1);
            ball[randomStriped1] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL9_Y, randomStriped1);
        } else {
            ball[randomStriped1] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL1_Y, randomStriped1);
            ball[randomSolid1] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL9_Y, randomSolid1);
        }

        // ROW 2 -> 2 VERTICES
        int solidOrStriped2 = (int)(Math.random()*2); // 0 -> SOLID , 1 -> STRIPED (UP VERTEX REFERENCE)
        int randomSolid2;
        do {
            randomSolid2 = (int)(Math.random()*6+2);
        } while (randomSolid2 == randomSolid1);
        int randomStriped2;
        do {
            randomStriped2 = (int)(Math.random()*7+9);
        } while (randomStriped2 == randomStriped1);
        if(solidOrStriped2 == 0) {
            ball[randomSolid2] = new Ball(Constants.TRIANGLE_ROW2_X, Constants.TRIANGLE_COL4_Y, randomSolid2);
            ball[randomStriped2] = new Ball(Constants.TRIANGLE_ROW2_X, Constants.TRIANGLE_COL6_Y, randomStriped2);
        } else {
            ball[randomStriped2] = new Ball(Constants.TRIANGLE_ROW2_X, Constants.TRIANGLE_COL4_Y, randomStriped2);
            ball[randomSolid2] = new Ball(Constants.TRIANGLE_ROW2_X, Constants.TRIANGLE_COL6_Y, randomSolid2);
        }

        // ROW 3 -> 2 VERTICES
        int solidOrStriped3 = (int)(Math.random()*2); // 0 -> SOLID , 1 -> STRIPED (UP VERTEX REFERENCE)
        int randomSolid3;
        do {
            randomSolid3 = (int)(Math.random()*6+2);
        } while (randomSolid3 == randomSolid1 || randomSolid3 == randomSolid2);
        int randomStriped3;
        do {
            randomStriped3 = (int)(Math.random()*7+9);
        } while (randomStriped3 == randomStriped1 || randomStriped3 == randomStriped2);
        if(solidOrStriped3 == 0) {
            ball[randomSolid3] = new Ball(Constants.TRIANGLE_ROW3_X, Constants.TRIANGLE_COL3_Y, randomSolid3);
            ball[randomStriped3] = new Ball(Constants.TRIANGLE_ROW3_X, Constants.TRIANGLE_COL7_Y, randomStriped3);
        } else {
            ball[randomStriped3] = new Ball(Constants.TRIANGLE_ROW3_X, Constants.TRIANGLE_COL3_Y, randomStriped3);
            ball[randomSolid3] = new Ball(Constants.TRIANGLE_ROW3_X, Constants.TRIANGLE_COL7_Y, randomSolid3);
        }

        // ROW 4 -> 2 EXTERNAL VERTICES
        int solidOrStriped4 = (int)(Math.random()*2); // 0 -> SOLID , 1 -> STRIPED (UP VERTEX REFERENCE)
        int randomSolid4;
        do {
            randomSolid4 = (int)(Math.random()*6+2);
        } while (randomSolid4 == randomSolid1 || randomSolid4 == randomSolid2 || randomSolid4 == randomSolid3);
        int randomStriped4;
        do {
            randomStriped4 = (int)(Math.random()*7+9);
        } while (randomStriped4 == randomStriped1 || randomStriped4 == randomStriped2 || randomStriped4 == randomStriped3);
        if(solidOrStriped4 == 0) {
            ball[randomSolid4] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL2_Y, randomSolid4);
            ball[randomStriped4] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL8_Y, randomStriped4);
        } else {
            ball[randomStriped4] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL2_Y, randomStriped4);
            ball[randomSolid4] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL8_Y, randomSolid4);
        }

        // ROW 4 -> 2 INTERNAL VERTICES
        int solidOrStriped5 = (int)(Math.random()*2); // 0 -> SOLID , 1 -> STRIPED (UP VERTEX REFERENCE)
        int randomSolid5;
        do {
            randomSolid5 = (int)(Math.random()*6+2);
        } while (randomSolid5 == randomSolid1 || randomSolid5 == randomSolid2 || randomSolid5 == randomSolid3 || randomSolid5 == randomSolid4);
        int randomStriped5;
        do {
            randomStriped5 = (int)(Math.random()*7+9);
        } while (randomStriped5 == randomStriped1 || randomStriped5 == randomStriped2 || randomStriped5 == randomStriped3 || randomStriped5 == randomStriped4);
        if(solidOrStriped5 == 0) {
            ball[randomSolid5] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL4_Y, randomSolid5);
            ball[randomStriped5] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL6_Y, randomStriped5);
        } else {
            ball[randomStriped5] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL4_Y, randomStriped5);
            ball[randomSolid5] = new Ball(Constants.TRIANGLE_ROW4_X, Constants.TRIANGLE_COL6_Y, randomSolid5);
        }

        // ROW 5 -> 2 INTERNAL VERTICES
        int solidOrStriped6 = (int)(Math.random()*2); // 0 -> SOLID , 1 -> STRIPED (UP VERTEX REFERENCE)
        int randomSolid6;
        do {
            randomSolid6 = (int)(Math.random()*6+2);
        } while (randomSolid6 == randomSolid1 || randomSolid6 == randomSolid2 || randomSolid6 == randomSolid3 || randomSolid6 == randomSolid4 || randomSolid6 == randomSolid5);
        int randomStriped6;
        do {
            randomStriped6 = (int)(Math.random()*7+9);
        } while (randomStriped6 == randomStriped1 || randomStriped6 == randomStriped2 || randomStriped6 == randomStriped3 || randomStriped6 == randomStriped4 || randomStriped6 == randomStriped5);
        if(solidOrStriped6 == 0) {
            ball[randomSolid6] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL3_Y, randomSolid6);
            ball[randomStriped6] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL7_Y, randomStriped6);
        } else {
            ball[randomStriped6] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL3_Y, randomStriped6);
            ball[randomSolid6] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL7_Y, randomSolid6);
        }
        
        // ROW 5 -> CENTER
        int randomStriped7;
        do {
            randomStriped7 = (int)(Math.random()*7+9);
        } while (randomStriped7 == randomStriped1 || randomStriped7 == randomStriped2 || randomStriped7 == randomStriped3 || randomStriped7 == randomStriped4 || randomStriped7 == randomStriped5 || randomStriped7 == randomStriped6);
        ball[randomStriped7] = new Ball(Constants.TRIANGLE_ROW5_X, Constants.TRIANGLE_COL5_Y, randomStriped7);

    }

    // GET/SET METHODS

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
