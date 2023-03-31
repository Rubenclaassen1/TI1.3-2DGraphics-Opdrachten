
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    private ArrayList<Integer> height = new ArrayList<>();


    private Body dino;
    private Body dinoDuck;
    private Body dinoStand;
    private PrismaticJoint dinoJoint;
    private Boolean jumpable = true;


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
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());


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

        canvas.setOnMouseClicked(e -> mouseClicked(e));
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> MouseReleased(e));

    }

    public void init() {

        try{
            BufferedImage birdie =ImageIO.read(getClass().getResource("bird.png"));
            enemies.add(birdie);
            height.add(800);

            BufferedImage cactus = ImageIO.read(getClass().getResource("cactus.png"));
            enemies.add(cactus);
            height.add(885);

            BufferedImage cacti = ImageIO.read(getClass().getResource("cacti.png"));
            enemies.add(cacti);
            height.add(885);


        }catch (IOException e){
            e.printStackTrace();
        }

        world = new World();
        world.setGravity(new Vector2(0, -9.8));

        Body floor = new Body();
        floor.addFixture(Geometry.createRectangle(20, 0.1));
        floor.getTransform().setTranslation(0, -4.5);
        floor.setMass(MassType.INFINITE);
        world.addBody(floor);

        dinoStand = new Body();
        dinoStand.addFixture(Geometry.createRectangle(.75, 1.5));
        dinoStand.getTransform().setTranslation(-8, -3.7);
        dinoStand.setMass(MassType.NORMAL);


        dinoDuck = new Body();
        dinoDuck.addFixture(Geometry.createRectangle(1.5,.75));
        dinoDuck.getTransform().setTranslation(-7.75,-4.08);
        dinoDuck.setMass(MassType.NORMAL);

        dino =dinoStand;
        world.addBody(dino);

        dinoJoint = new PrismaticJoint(dino, floor, new Vector2(0,0), new Vector2(0,0));
        dinoJoint.setCollisionAllowed(true);
        world.addJoint(dinoJoint);


        jumpable = true;
    }

    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private double spawntime = 0;

    private int speed = 400;
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

        for (Obstacle obstacle : obstacles) {
            obstacle.draw(graphics);
        }
    }

    public void update(double deltaTime) {
//        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        world.update(deltaTime);

        if (spawntime<=0){
            Random r = new Random();
            int x = r.nextInt(enemies.size());

            obstacles.add(new Obstacle(2000, height.get(x),enemies.get(x), speed));
            spawntime = 2 + Math.random();
            speed += 100;

        }
        spawntime -= deltaTime;
        System.out.println(spawntime);

        for (Obstacle obstacle : obstacles) {
            obstacle.update(deltaTime);

        }
//        System.out.println(dino.getTransform().getTranslationY());
//        Random random = new Random();
//        int i = random.nextInt(3);
//        System.out.println(i);

    }

    private void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseButton.PRIMARY && jumpable && getyPos(dino)<=-3.7) {
//            jump();
        }
    }

    private void mousePressed(MouseEvent e){
        if (e.getButton() == MouseButton.SECONDARY && getyPos(dino) <= -3.7){
            jumpable = false;
            world.removeBody(dino);
            dino = dinoDuck;
            world.addBody(dino);
        } else if (e.getButton() == MouseButton.PRIMARY && getyPos(dino) <= -3.7){
            jump();
            System.out.println("hoi");
        }
    }

    private void MouseReleased(MouseEvent e) {
        if(e.getButton() == MouseButton.SECONDARY){
            jumpable = true;
            world.removeBody(dino);
            dino = dinoStand;
            world.addBody(dino);
            world.addJoint(dinoJoint);
        }
    }

    private void jump(){
        dino.applyForce(new Vector2(0, 350));
    }

    private double getyPos(Body body){
        return body.getTransform().getTranslationY();
    }

    public static void main(String[] args) {
        launch(Eindopdracht.class);
    }

}