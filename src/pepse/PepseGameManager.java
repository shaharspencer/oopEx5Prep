package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.util.AvatarConfiguration;
import pepse.util.TerrainConfiguration;
import pepse.util.UIConfiguration;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.dayNight.Night;
import pepse.world.dayNight.Sun;
import pepse.world.dayNight.SunHalo;
import pepse.world.trees.Tree;
import pepse.world.Avatar;

import java.awt.*;

public class PepseGameManager extends GameManager {
    private static final int GROUND_LAYER = Layer.STATIC_OBJECTS;
    public static final int SEED = 0;
    // duration of a single day in the game in seconds
    private static final int DAY_CYCLE_LENGTH = 60;


    public static Vector2 VECTOR_ZERO;

    private static int SKY_LAYER = Layer.BACKGROUND;
    private static final int LAYERS_DIFF = 1;
    //todo: maybe this is not the best way to define it, maybe they should be independent from one another
    private static int SUN_LAYER = SKY_LAYER + LAYERS_DIFF;
    private static int HALO_LAYER = SUN_LAYER + LAYERS_DIFF;

    private static int TREE_LAYER = SUN_LAYER + LAYERS_DIFF;

    private static int LEAF_LAYER = SUN_LAYER + LAYERS_DIFF;

    static Vector2 windowSize = new Vector2(700, 500);
    private Vector2 windowDimensions;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private GameObject sun;
    private Avatar avatar;

    PepseGameManager() {
        super("", windowSize);
    }


    public void createGameObjects() {


        createAvatar();



        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);
        Terrain terrain = new Terrain(gameObjects(), GROUND_LAYER, windowDimensions, SEED);
        terrain.createInRange(-500, (int) windowDimensions.x());

        this.sun = Sun.create(gameObjects(), SUN_LAYER, windowDimensions, DAY_CYCLE_LENGTH);
        Night.create(gameObjects(), Layer.FOREGROUND, windowDimensions, DAY_CYCLE_LENGTH);
        SunHalo.create(gameObjects(), HALO_LAYER, sun, new Color(255, 255, 0, 20));
        Tree treesManager = new Tree(gameObjects(), terrain::groundHeightAt, SEED, HALO_LAYER);
        treesManager.createInRange(-500, (int) windowDimensions.x());

        Night.create(gameObjects(), Layer.FOREGROUND, windowDimensions,
                DAY_CYCLE_LENGTH);


    }

    private void createAvatar() {
        this.avatar = Avatar.create(gameObjects(), UIConfiguration.AVATAR_LAYER, Vector2.ZERO, inputListener, imageReader);
        this.VECTOR_ZERO = windowController.getWindowDimensions().mult(0.5f);
        gameObjects().addGameObject(avatar, AvatarConfiguration.AVATAR_LAYER);
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));

        gameObjects().layers().shouldLayersCollide(AvatarConfiguration.AVATAR_LAYER,
                TerrainConfiguration.getTopBlockLayer(), true);
        gameObjects().layers().shouldLayersCollide(AvatarConfiguration.AVATAR_LAYER,
                Layer.DEFAULT, true);
    }


    @Override
    public void initializeGame(danogl.gui.ImageReader imageReader, danogl.gui.SoundReader
            soundReader,
                               danogl.gui.UserInputListener inputListener,
                               danogl.gui.WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();

        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        windowController.setTargetFramerate(70);



        createGameObjects();

    }

    public static void main(String[] args){
        new PepseGameManager().run();
    }
}
