package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.dayNight.Night;

public class PepseGameManager extends GameManager {
    private static final int GROUND_LAYER = Layer.STATIC_OBJECTS;
    private static final int SEED = 0;
    // duration of a single day in the game in seconds
    private static final int DAY_CYCLE_LENGTH = 60;
    int skyLayer = Layer.BACKGROUND;
    static Vector2 windowSize = new Vector2(700, 500);
    private Vector2 windowDimensions;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;

    PepseGameManager() {
        super("", windowSize);
    }


    public void createGameObjects(){

        Sky.create(gameObjects(), windowDimensions, skyLayer);
        Terrain terrain = new Terrain(gameObjects(), GROUND_LAYER, windowDimensions, SEED);
        terrain.createInRange(0, (int) windowDimensions.y());


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

        createGameObjects();
        GameObject nightObject = Night.create(gameObjects(), Layer.FOREGROUND, windowDimensions,
                DAY_CYCLE_LENGTH);
    }


    public static void main(String[] args){
        new PepseGameManager().run();
    }
}
