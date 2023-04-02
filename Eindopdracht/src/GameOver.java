import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameOver {
    private ResizableCanvas canvas;
    private int score;
    private String gameOver;

    public GameOver(int score, ResizableCanvas canvas) {
        this.score = score;
        this.canvas = canvas;
    }

    public void draw(Graphics2D graphics) {

        AffineTransform tx = new AffineTransform();
        tx.translate(850, 400);

        AffineTransform score = new AffineTransform();
        score.translate(950, 700);
        Font font = new Font("Arial", Font.BOLD, 100);
        graphics.setColor(Color.BLACK);
        Shape gameOver = font.createGlyphVector(graphics.getFontRenderContext(), "Game Over").getOutline();
        graphics.draw(tx.createTransformedShape(gameOver));
        graphics.fill(tx.createTransformedShape(gameOver));
        font = new Font("Arial", Font.BOLD, 50);
        Shape shape = font.createGlyphVector(graphics.getFontRenderContext(), String.valueOf(this.score)).getOutline();
        graphics.draw(score.createTransformedShape(shape));
        graphics.fill(score.createTransformedShape(shape));

    }

}
