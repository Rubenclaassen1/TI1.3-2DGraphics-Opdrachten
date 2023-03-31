
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.PinJoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class AngryBirds extends Application {

    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private boolean debugSelected = false;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private PinJoint catapultJoint;


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

    }

    public void init() {

        world = new World();
        world.setGravity(new Vector2(0, -9.8/4));

        Body floor = new Body();
        floor.addFixture(Geometry.createRectangle(20,2));
        floor.getTransform().setTranslation(0,-3.5);
        floor.setMass(MassType.INFINITE);
        world.addBody(floor);
        gameObjects.add(new GameObject("/images/Java2D.png",floor, new Vector2(0,33),2));

        for (int x = 0; x <2 ; x++)
        {
            for (int y = 0; y < 2; y++)
            {
                Body box = new Body();
                box.addFixture(Geometry.createRectangle(.75,.75));
                box.setMass(MassType.NORMAL);
                box.getTransform().setTranslation(5 + x*2,-2 + y*.75);
                world.addBody(box);
                gameObjects.add(new GameObject("/images/Box.png", box, new Vector2(0,0), 1));
            }
        }

        Body beam = new Body();
        beam.addFixture(Geometry.createRectangle(2.75, .25));
        beam.setMass(MassType.NORMAL);
        beam.getTransform().setTranslation(6,-.5);
        world.addBody(beam);
        gameObjects.add(new GameObject("/images/Beam.png", beam, new Vector2(0,0),1));

        for (int i = 0; i < 2; i++)
        {
            Body topbox = new Body();
            topbox.addFixture(Geometry.createRectangle(.75,.75));
            topbox.setMass(MassType.NORMAL);
            topbox.getTransform().setTranslation(6,-.25+i*.75);
            world.addBody(topbox);
            gameObjects.add(new GameObject("/images/Box.png", topbox, new Vector2(0,0), 1));
        }



        Body catapult = new Body();
        catapult.getTransform().setTranslation(-8, -2);
        catapult.setMass(MassType.INFINITE);
        world.addBody(catapult);
        gameObjects.add(new GameObject("/images/Catapult.png", catapult, new Vector2(0,0), 1));

        Body bird = new Body();
        bird.addFixture(Geometry.createCircle(.2));
        bird.getTransform().setTranslation(catapult.getTransform().getTranslation());
        bird.setMass(MassType.NORMAL);
        bird.getFixture(0).setRestitution(0.2);
        world.addBody(bird);
        gameObjects.add(new GameObject("/images/bird.png", bird, new Vector2(0,0), 1));

        catapultJoint = new PinJoint(bird, catapult.getTransform().getTranslation(),10,0,500);
        catapultJoint.setTarget(catapult.getTransform().getTranslation());
        catapultJoint.setCollisionAllowed(false);
        world.addJoint(catapultJoint);
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

    public void update(double deltaTime) {
        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        world.update(deltaTime);

    }

    public static void main(String[] args) {
        launch(AngryBirds.class);
    }

}
