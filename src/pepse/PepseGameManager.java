package pepse;

import danogl.GameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Sky;

public class PepseGameManager extends GameManager {
    int skyLayer = Layer.BACKGROUND;
    static Vector2 windowSize = new Vector2(700, 500);
    private Vector2 windowDimensions;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;

    PepseGameManager(){
        super("", windowSize);
    }


    @Override
        public void initializeGame(danogl.gui.ImageReader imageReader, danogl.gui.SoundReader
            soundReader,
                         danogl.gui.UserInputListener inputListener,
                        danogl.gui.WindowController windowController){
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        Sky.create(gameObjects(), windowDimensions, skyLayer);
    }


    public static void main(String[] args){
        new PepseGameManager().run();
    }
}
