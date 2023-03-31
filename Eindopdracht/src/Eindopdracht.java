
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
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
import org.dyn4j.dynamics.joint.PinJoint;
import org.dyn4j.dynamics.joint.PrismaticJoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Eindopdracht extends Application {

    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private boolean debugSelected = false;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Body dino;
    private Body dinoDuck;
    private PrismaticJoint dinoJoint;


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

    }

    public void init() {
        world = new World();
        world.setGravity(new Vector2(0, -9.8));

        Body floor = new Body();
        floor.addFixture(Geometry.createRectangle(20, 2));
        floor.getTransform().setTranslation(0, -4.5);
        floor.setMass(MassType.INFINITE);
        world.addBody(floor);

        dino = new Body();
        dino.addFixture(Geometry.createRectangle(.75, 1.5));
        dino.getTransform().setTranslation(-8, -2);
        dino.setMass(MassType.NORMAL);
        world.addBody(dino);

        dinoDuck = new Body();
        dinoDuck.addFixture(Geometry.createRectangle(1.5,.75));
        dinoDuck.getTransform().setTranslation(-8,-2);
        dinoDuck.setMass(MassType.NORMAL);


        dinoJoint = new PrismaticJoint(dino, floor, new Vector2(0,0), new Vector2(0,0));
        dinoJoint.setCollisionAllowed(true);
        world.addJoint(dinoJoint);
    }

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
    }

    private void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseButton.PRIMARY){
            System.out.println("hallo");
            dino.applyForce(new Vector2(0,450));
        } else if (e.getButton() == MouseButton.SECONDARY){
//            world.removeBody(dino);
            dino = dinoDuck;
            world.addBody(dino);

        }
    }

    public void update(double deltaTime) {
//        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        world.update(deltaTime);


    }

    public static void main(String[] args) {
        launch(Eindopdracht.class);
    }

}