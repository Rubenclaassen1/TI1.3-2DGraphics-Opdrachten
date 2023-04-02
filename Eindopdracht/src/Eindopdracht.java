
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.PrismaticJoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;

public class Eindopdracht extends Application {

    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private boolean debugSelected = false;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private ArrayList<BufferedImage> enemies = new ArrayList<>();
    private ArrayList<Double> height = new ArrayList<>();
    private ArrayList<Double> width = new ArrayList<>();
    private ArrayList<Double> yHeight = new ArrayList<>();
    private GameObject dinoObject;
    private GameObject duckedDinoObject;
    private GameObject floorObject;



    private Body dino;
    private Body dinoDuck;
    private Body dinoStand;
    private Body floor;
    private PrismaticJoint dinoJoint;
    private Boolean jumpable = true;

    private BufferedImage[] dinoImage = new BufferedImage[2];
    private BufferedImage[] duckedDinoImage = new BufferedImage[2];
    private BufferedImage floorImage;
    private BufferedImage meteorite;
    private BufferedImage cactus;
    private BufferedImage cacti;
    FXGraphics2D g2d;


    @Override
    public void start(Stage stage) throws Exception {

        BorderPane mainPane = new BorderPane();

        // Add debug button
        javafx.scene.control.CheckBox showDebug = new CheckBox("Show debug");
        showDebug.setOnAction(e -> {
            debugSelected = showDebug.isSelected();
        });
        mainPane.setTop(showDebug);

        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        g2d = new FXGraphics2D(canvas.getGraphicsContext2D());


        camera = new Camera(canvas, g -> draw(g), g2d);
        mousePicker = new MousePicker(canvas);


        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane, 1920, 1080));
        stage.setTitle("Angry Birds");
        stage.show();

        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> MouseReleased(e));

    }

    public void init() {

        try {
            BufferedImage dinoImage = ImageIO.read(getClass().getResource("dino.png"));
            for (int i = 0; i < 2; i++) {
                this.dinoImage[i] = dinoImage.getSubimage(i*75,0,75,150);
            }
            BufferedImage duckedDino = ImageIO.read(getClass().getResource("duckedDino.png"));
            for (int i = 0; i < 2; i++) {
                this.duckedDinoImage[i] = duckedDino.getSubimage(i*150,0,150,75);
            }

            floorImage = ImageIO.read(getClass().getResource("floor.png"));

            meteorite = ImageIO.read(getClass().getResource("meteorite.png"));
            enemies.add(meteorite);
            yHeight.add(-3.1);
            height.add(.5);
            width.add(.6);


            cactus = ImageIO.read(getClass().getResource("cactus.png"));
            enemies.add(cactus);
            yHeight.add(-4.0);
            height.add(1.0);
            width.add(.5);

            cacti = ImageIO.read(getClass().getResource("cacti.png"));
            enemies.add(cacti);
            yHeight.add(-4.0);
            height.add(1.0);
            width.add(1.0);


        } catch (IOException e) {
            e.printStackTrace();
        }



        world = new World();
        world.setGravity(new Vector2(0, -9.8));

        floor = new Body();
        floor.addFixture(Geometry.createRectangle(20, 0.1));
        floor.getTransform().setTranslation(0, -4.5);
        floor.setMass(MassType.INFINITE);
        floorObject = new GameObject(floorImage, floor, new Vector2(0,0),1);
        world.addBody(floor);

        dinoStand = new Body();
        dinoStand.addFixture(Geometry.createRectangle(.75, 1.5));
        dinoStand.getTransform().setTranslation(-8, -3.7);
        dinoStand.setMass(MassType.NORMAL);
        dinoObject = new GameObject(dinoImage[0], dinoStand, new Vector2(0,0),1);

        dinoDuck = new Body();
        dinoDuck.addFixture(Geometry.createRectangle(1.5, .75));
        dinoDuck.getTransform().setTranslation(-7.75, -4.08);
        dinoDuck.setMass(MassType.NORMAL);
        duckedDinoObject = new GameObject(duckedDinoImage[0], dinoDuck, new Vector2(0,0),1);

        dino = dinoStand;
        world.addBody(dino);

        dinoJoint = new PrismaticJoint(dino, floor, new Vector2(0, 0), new Vector2(0, 0));
        dinoJoint.setCollisionAllowed(true);
        world.addJoint(dinoJoint);

        jumpable = true;
    }

    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    GameOver gameOver1;
    private double spawntime = 0;
    private double spawnrate = 2;
    private boolean paused = true;
    private boolean duckable = true;
    boolean gameOver = false;
    private double speed = 0.05;
    private int score;
    int moveCounter = 0;
    int imageCounter = 0;

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        AffineTransform originalTransform = graphics.getTransform();

        graphics.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        graphics.scale(1, -1);


        for (GameObject go : gameObjects) {
            go.draw(graphics);
        }

        if (debugSelected) {
            graphics.setColor(Color.blue);
            DebugDraw.draw(graphics, world, 100);
        }

        graphics.setTransform(originalTransform);

        showScore(g2d, 10,30, score);


        if(gameOver){
            gameOver1 = new GameOver(score, canvas);
            gameOver1.draw(g2d);
        }
    }

    private void showScore(Graphics2D graphics, int x, int y, int score){
        AffineTransform tx = new AffineTransform();
        tx.translate(x,y);

        Font font = new Font ("Arial", Font.PLAIN, 30);
        Shape shape = font.createGlyphVector(graphics.getFontRenderContext(), String.valueOf(score)).getOutline();
        graphics.draw(tx.createTransformedShape(shape));
        graphics.fill(tx.createTransformedShape(shape));
    }

    private AffineTransform getDinoTransform() {
        AffineTransform tx = new AffineTransform();
        tx.translate(dino.getTransform().getTranslationX(), dino.getTransform().getTranslationY());
//        System.out.println(dino.getTransform().getTranslationX());
        return tx;
    }

    public void update(double deltaTime) {
        if (paused) {
            return;
        }


        world.update(deltaTime);

        if (spawntime <= 0) {
            Random r = new Random();
            int x = r.nextInt(enemies.size());


            obstacles.add(new Obstacle(width.get(x), height.get(x),yHeight.get(x), speed, world, enemies.get(x)));

            if (spawnrate >.4){
                spawnrate = spawnrate * 0.9;
                System.out.println("hoi");
            }
            spawntime = spawnrate + Math.random();
            System.out.println(spawnrate);
            if (speed <1){
                speed += 0.005;
            }

        }
        if (moveCounter % 100 == 50){
            dinoObject.setImage(dinoImage[imageCounter%dinoImage.length]);
            duckedDinoObject.setImage(duckedDinoImage[imageCounter%duckedDinoImage.length]);
            imageCounter++;
        }
        if (moveCounter % 10 == 9){
            score++;
        }

        moveCounter++;
        spawntime -= deltaTime;
        double width = dino.getFixture(0).getShape().createAABB().getWidth();
        double height = dino.getFixture(0).getShape().createAABB().getHeight();

        Shape playerShape = getDinoTransform().createTransformedShape(new Rectangle2D.Double(0, 0, width, height));

        for (Obstacle obstacle : obstacles) {
            GameObject enemieObject = new GameObject(obstacle.getImage(),obstacle.getBody(), new Vector2(0,0),1);
            gameObjects.add(enemieObject);
            obstacle.update(deltaTime);
            if (obstacle.colliding(playerShape)) {
                gameOver();
            }
            if (obstacle.getX() < -11) {
                world.removeBody(obstacle.getBody());
                obstacles.remove(obstacle);
            }

        }

    }


    private void mousePressed(MouseEvent e) {
        if (paused) {
            builtworld();
            gameOver = false;
        }
        if (e.getButton() == MouseButton.SECONDARY && getyPos(dino) <= -3.7 && !paused && duckable) {
            jumpable = false;
            world.removeBody(dino);
            world.removeJoint(dinoJoint);
            gameObjects.remove(dinoObject);
            gameObjects.add(duckedDinoObject);
            dino = dinoDuck;
            world.addBody(dino);

        } else if (e.getButton() == MouseButton.PRIMARY && getyPos(dino) <= -3.7 && jumpable && !paused) {
            duckable = false;
            jump();


        }
    }

    private void MouseReleased(MouseEvent e) {
        if (paused){
            paused = false;

        }
        if (e.getButton() == MouseButton.SECONDARY && !paused) {
            jumpable = true;
            world.removeBody(dino);
            dino = dinoStand;
            world.addBody(dino);
            world.addJoint(dinoJoint);
            gameObjects.remove(duckedDinoObject);
            gameObjects.add(dinoObject);
        }
        if (e.getButton() == MouseButton.PRIMARY && !paused){
            duckable = true;
        }
    }

    private void jump() {
        dino.applyForce(new Vector2(0, 350));
    }

    private double getyPos(Body body) {
        return body.getTransform().getTranslationY();
    }

    public static void main(String[] args) {
        launch(Eindopdracht.class);
    }

    private void gameOver() {
        paused = true;
        gameOver = true;
        obstacles.clear();
        gameObjects.clear();
        spawnrate = 2;
        score = 0;

    }

    private void builtworld() {
        gameObjects.add(dinoObject);
        gameObjects.add(floorObject);


    }

}