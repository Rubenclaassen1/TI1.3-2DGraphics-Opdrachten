import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class FadingImage extends Application {
    private ResizableCanvas canvas;
    private BufferedImage background;
    private BufferedImage background2;
    private float fade = 0.00f;
    private float step = 0.001f;
    
    @Override
    public void start(Stage stage) throws Exception {


        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());


        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
		if(last == -1)
                    last = now;
		update((now - last) / 1000000000.0);
		last = now;
		draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Fading image");
        stage.show();
        draw(g2d);
    }
    
    
    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int)canvas.getWidth(), (int)canvas.getHeight());

        AffineTransform tx = new AffineTransform();
        tx.translate(0,0);
        tx.scale(1.5f,1.5f);

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        graphics.drawImage(background2,tx,null);

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.fade));
        graphics.drawImage(background,tx,null);

    }
    

    public void update(double deltaTime) {
        fade+= step;
        System.out.println(fade);

        if (fade >= 1.00f){
            fade = 1;
            step = -step;
        }else if (fade <= 0.00f){
            fade = 0;
            step = -step;
        }

    }

    public static void main(String[] args) {
        launch(FadingImage.class);
    }

    public void init(){
        try{
            background = ImageIO.read(getClass().getResource("background.jpg"));
            background2 = ImageIO.read(getClass().getResource("background2.jpeg"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
