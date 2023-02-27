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

public class MovingCharacter extends Application {
    private ResizableCanvas canvas;
    private BufferedImage[] tiles;
    private BufferedImage currentSprite;
    int spriteIndex = 42;
    int xPos = 0;
    int yPos = 200;
    double lastMoveTimer = 0;
    int moveSpeed = 5;
    int flipScaleX = 3;
    boolean isJumping = false;
    boolean isFlipped = false;

    @Override
    public void start(Stage stage) throws Exception
    {

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        init();

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Moving Character");
        stage.show();
        draw(g2d);

        canvas.setOnMouseClicked(event -> {
            isJumping = true;
            spriteIndex = 17;

        });
    }

    public void init(){
        try {
            BufferedImage image = ImageIO.read(getClass().getResource("images/sprite.png"));
            tiles = new BufferedImage[65];

            //knip de afbeelding op in 65 stukjes van 32x32 pixels.
            for(int i = 0; i < 65; i++)
                tiles[i] = image.getSubimage(64 * (i%8), 64 * (i/8), 64, 64);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.BLACK);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        AffineTransform character = new AffineTransform();
        character.translate(xPos, yPos);
        character.scale(flipScaleX, 3);

        graphics.drawImage(currentSprite, character, null);
    }


    public void update(double deltaTime)
    {
        lastMoveTimer += deltaTime;

        if (spriteIndex == 50)
        {
            spriteIndex = 42;
        }
        currentSprite = tiles[spriteIndex];
//        System.out.println(lastMoveTimer);
        if (lastMoveTimer > 0.06)
        {
            spriteIndex++;
            System.out.println(spriteIndex);
            lastMoveTimer = 0;
        }

        if (xPos >= canvas.getWidth() - (currentSprite.getWidth() * flipScaleX))
        {
            isFlipped = true;
        }

        if (xPos < currentSprite.getWidth()){
            isFlipped = false;
        }

        if (isFlipped)
        {
            xPos -= moveSpeed;
            flipScaleX = -3;
        }
        else
        {
            xPos += moveSpeed;
            flipScaleX = 3;
        }


        if (isJumping)
        {
            this.moveSpeed = 0;
            if (spriteIndex == 25)
            {
                isJumping = false;
                spriteIndex = 42;
                this.moveSpeed = 5;
            }
        }


    }

    public static void main(String[] args)
    {
        launch(MovingCharacter.class);
    }

}