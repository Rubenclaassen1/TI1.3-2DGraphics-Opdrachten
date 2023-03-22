import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class BlockDrag extends Application {
    ResizableCanvas canvas;
    private ArrayList<Renderable> blocks = new ArrayList<>();
    private Renderable selected;



    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Block Dragging");
        primaryStage.show();

        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());


        for (Renderable block : blocks) {
            Rectangle2D square = new Rectangle2D.Double(block.getPosition().getX(),block.getPosition().getY(), block.getWidth(),block.getHeight());
            graphics.setColor(block.getColor());
            graphics.fill(square);

            graphics.setColor(Color.black);
            graphics.draw(square);
        }
    }




    public static void main(String[] args)
    {
        launch(BlockDrag.class);
    }

    private void mousePressed(MouseEvent e)
    {
        double x = e.getX();
        double y = e.getY();
        System.out.println(x);
        System.out.println(y);

        if(e.getButton().equals(MouseButton.PRIMARY)){
            for (Renderable block : blocks) {
                if (-25 < (block.getPosition().getX()-x) && block.getPosition().getX() - x > 25 && block.getPosition().getY() - y < -25 && block.getPosition().getY() > 25){
                    this.selected = block;
                    System.out.println(selected.getPosition());
                    break;

                }
            }
        }
    }

    private void mouseReleased(MouseEvent e)
    {
        this.selected = null;
    }

    private void mouseDragged(MouseEvent e)
    {
        if (this.selected != null){
            blocks.remove(selected);

            Point2D position = new Point2D.Double(e.getX(),e.getY());
            selected.setPosition(position);

            Renderable square = new Renderable(selected.getShape(), position, selected.getColor());
            blocks.add(square);
            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        }
    }

    public void init(){
        int height = 50;
        int width = 50;
        for (int i = 0; i < 20; i++) {
            int x = (int) ((Math.random()*1600));
            int y = (int) ((Math.random()*900));

            Rectangle2D square = new Rectangle2D.Double(x,y,width,height);
            Point2D point2D = new Point2D.Double(x + (width/2f), y + (height/2f));
            Color color = (Color.getHSBColor((float) (Math.random() * 256), 0.7f, 1));

            Renderable block = new Renderable(square,point2D,color);
            blocks.add(block);
        }
    }
}
