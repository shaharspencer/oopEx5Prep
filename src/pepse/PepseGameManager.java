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
import pepse.util.*;
import pepse.world.InfiniteWorldManager;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.dayNight.Night;
import pepse.world.dayNight.Sun;
import pepse.world.dayNight.SunHalo;
import pepse.world.trees.Tree;
import pepse.world.Avatar;

import java.awt.*;

/**
 * Initializes and manages the game
 */
public class PepseGameManager extends GameManager {
    //######## public fields ########
    //todo: understand what this means and why is it static when defined by an instance
    //vector zero is used by Terrain when calculating the ground height
    public static Vector2 VECTOR_ZERO;
    public Terrain terrain;

    //######## private fields ########
    private Vector2 windowDimensions;
    private ImageReader imageReader;
    //TODO: either add sound or delete this field
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private GameObject sun;
    private Avatar avatar;
    private InfiniteWorldManager infiniteWorldManagerCreator;
    private Tree treesManager;

    //######## public methods ########

    /**
     * Constructor of PepseGameManager object
     */
    public PepseGameManager(String windowTitle, Vector2 windowSize) {
        super(windowTitle, windowSize);
    }

    /**
     * updates world by avatars location via infiniteWorld object
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        infiniteWorldManagerCreator.updateByAvatarLocation();
    }

    /**
     * Initializes the game
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed, which returns whether
     *                         a given key is currently pressed by the user or not. See its
     *                         documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
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
        windowController.setTargetFramerate(GameManagerConfiguration.TARGET_FRAMERATE);
        VECTOR_ZERO = windowController.getWindowDimensions().mult(0.5f);
        setWhichLayersShouldCollide();
        createGameObjects();
    }

    //######## private methods ########


    /**
     * Sets which layers should collide with each other in the game
     */
    private void setWhichLayersShouldCollide() {
        //todo: maybe change so that the Avatar Layer is in the GamaManagerConfiguration
        //for avatar:
        GameObject tempPixel = new GameObject(Vector2.ZERO, Vector2.ZERO, null);
        int[] avatarCollisionLayers = {TerrainConfiguration.getTopBlockLayer(), Layer.DEFAULT};
        setLayersCollision(AvatarConfiguration.AVATAR_LAYER, avatarCollisionLayers, tempPixel);

        //for leafs:
        int[] leafsCollisionLayers = {TerrainConfiguration.getTopBlockLayer()};
        setLayersCollision(TreeConfiguration.LEAF_LAYER, leafsCollisionLayers, tempPixel);
    }

    private void setLayersCollision(int firstLayer, int[] secondLayer, GameObject nullObject) {
        gameObjects().addGameObject(nullObject, firstLayer);
        for (int layer : secondLayer) {
            gameObjects().layers().shouldLayersCollide(firstLayer, layer, true);
        }
        gameObjects().removeGameObject(nullObject, firstLayer);

    }

    /**
     * This method creates all game objects
     */
    private void createGameObjects() {
        createAvatar();
        createInfiniteWorld();
        createSky();
        createNight();
        createSun();
    }

    /**
     * creates avatar object and sets the camera on it
     */
    private void createAvatar() {
        this.avatar = Avatar.create(gameObjects(), UIConfiguration.AVATAR_LAYER,
                Vector2.ZERO, inputListener, imageReader);
        //todo: maybe change so that the Avatar Layer is in the GamaManagerConfiguration
        gameObjects().addGameObject(avatar, AvatarConfiguration.AVATAR_LAYER);

        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    /**
     * initializes an infinite world object and the world according to avatars location
     */
    private void createInfiniteWorld() {
        //todo: maybe overload the constructor of Terrain and provide it with VECTOR_ZERO
        this.terrain = new Terrain(gameObjects(), GameManagerConfiguration.GROUND_LAYER, windowDimensions,
                GameManagerConfiguration.SEED);

        this.treesManager = new Tree(gameObjects(), terrain::groundHeightAt, GameManagerConfiguration.SEED,
                TreeConfiguration.TREE_LAYER);

        this.infiniteWorldManagerCreator = new InfiniteWorldManager(terrain::createInRange,
                terrain::deleteInRange,
                treesManager::createInRange,
                treesManager::deleteInRange,
                avatar::getCenter,
                this.windowDimensions);

        infiniteWorldManagerCreator.updateByAvatarLocation();
    }

    private void createSky() {
        Sky.create(gameObjects(), windowDimensions, GameManagerConfiguration.SKY_LAYER);
    }

    private void createNight() {
        Night.create(gameObjects(), Layer.FOREGROUND, windowDimensions,
                GameManagerConfiguration.DAY_CYCLE_LENGTH);
    }

    private void createSun() {
        this.sun = Sun.create(gameObjects(), GameManagerConfiguration.SUN_LAYER, windowDimensions,
                GameManagerConfiguration.DAY_CYCLE_LENGTH);
        SunHalo.create(gameObjects(), GameManagerConfiguration.HALO_LAYER, sun,
                GameManagerConfiguration.HALO_COLOR);
    }

    //todo: maybe leaf is supposed to extend block

    public static void main(String[] args) {
        new PepseGameManager(GameManagerConfiguration.WINDOW_TITLE, GameManagerConfiguration.WINDOW_SIZE).run();
    }
}
