package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.util.configurations.*;
import pepse.world.*;
import pepse.world.dayNight.Night;
import pepse.world.dayNight.Sun;
import pepse.world.dayNight.SunHalo;
import pepse.world.trees.Tree;

/**
 * Initializes and manages the game
 */
public class PepseGameManager extends GameManager {

    //######## private fields ########
    private Terrain terrain;
    private Vector2 windowDimensions;
    private ImageReader imageReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private GameObject sun;
    private Avatar avatar;
    private InfiniteWorldManager infiniteWorldManagerCreator;
    private Tree treesManager;
    private NumericLifeCounter energyCounter;
    private SeasonManager seasonManager;
    private SkyManager skyManager;


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
        this.energyCounter.setEnergyLevel(avatar.getEnergyLevel());

        if(this.seasonManager.getDidSeasonChange()){
            this.treesManager.setSeason(this.seasonManager.getSeason());
            this.skyManager.setSeason(this.seasonManager.getSeason());
            this.terrain.setSeason(this.seasonManager.getSeason());
            this.seasonManager.turnOffDidSeasonChange();
        }

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
        this.inputListener = inputListener;
        this.windowController = windowController;
        windowController.setTargetFramerate(GameManagerConfiguration.TARGET_FRAMERATE);
        setWhichLayersShouldCollide();
        createGameObjects();
    }

    //######## private methods ########


    /**
     * Sets which layers should collide with each other in the game
     */
    private void setWhichLayersShouldCollide() {
        //for avatar:
        GameObject tempPixel = new GameObject(Vector2.ZERO, Vector2.ZERO, null);
        GameObject anotherTempPixel = new GameObject(Vector2.ZERO, Vector2.ZERO, null);

        int[] avatarCollisionLayers = {TerrainConfiguration.TOP_BLOCKS_LAYER, TreeConfiguration.TREE_LAYER};
        setLayersCollision(AvatarConfiguration.AVATAR_LAYER, avatarCollisionLayers, tempPixel,
                anotherTempPixel);

        //for leafs:
        int[] leafsCollisionLayers = {TerrainConfiguration.TOP_BLOCKS_LAYER};
        setLayersCollision(TreeConfiguration.LEAF_LAYER, leafsCollisionLayers, tempPixel, anotherTempPixel);
    }

    private void setLayersCollision(int firstLayer, int[] secondLayer, GameObject nullObject,
                                    GameObject anotherNullObject) {
        gameObjects().addGameObject(nullObject, firstLayer);
        for (int layer : secondLayer) {
            gameObjects().addGameObject(anotherNullObject, layer);
            gameObjects().layers().shouldLayersCollide(firstLayer, layer, true);
            gameObjects().removeGameObject(anotherNullObject, layer);
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
        createSeasons();

        createEnergyCounter();

        avatar.setTerrainCallback(terrain::groundHeightAt);
    }

    private void createSeasons() {
        this.seasonManager = new SeasonManager();
        seasonManager.create(gameObjects(), SkyAndDayNightConfiguration.SKY_LAYER,
                GameManagerConfiguration.DAY_CYCLE_LENGTH*1f);
        //TODO: MULTIPLY BY HOW MANY DAYS ARE A SEASON
    }

    /**
     * initializes an energy counter object
     */

    private void createEnergyCounter() {
        this.energyCounter = new NumericLifeCounter(
                Vector2.ONES.mult(10)
                , new Vector2(30, 30),
                avatar.getEnergyLevel());
        gameObjects().addGameObject(energyCounter);

        this.energyCounter.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

    }



    /**
     * creates avatar object and sets the camera on it
     */
    private void createAvatar() {
        this.avatar = Avatar.create(gameObjects(), UIConfiguration.AVATAR_LAYER,
                Vector2.ZERO, inputListener, imageReader);
        gameObjects().addGameObject(avatar, AvatarConfiguration.AVATAR_LAYER);


        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    /**
     * initializes an infinite world object and the world according to avatars location
     */
    private void createInfiniteWorld() {
        this.terrain = new Terrain(gameObjects(), TerrainConfiguration.GROUND_LAYER, windowDimensions,
                GameManagerConfiguration.SEED);

        this.treesManager = new Tree(gameObjects(), terrain::groundHeightAt, GameManagerConfiguration.SEED,
                imageReader);

        this.infiniteWorldManagerCreator = new InfiniteWorldManager(terrain::createInRange,
                terrain::deleteInRange,
                treesManager::createInRange,
                treesManager::deleteInRange,
                avatar::getCenter,
                this.windowDimensions);

        infiniteWorldManagerCreator.updateByAvatarLocation();
    }

    private void createSky() {
        skyManager = new SkyManager(gameObjects(), windowDimensions, SkyAndDayNightConfiguration.SKY_LAYER,
                imageReader);
        skyManager.createSky();
        //Sky.create(gameObjects(), windowDimensions, GameManagerConfiguration.SKY_LAYER);
    }

    private void createNight() {
        Night.create(gameObjects(), Layer.FOREGROUND, windowDimensions,
                GameManagerConfiguration.DAY_CYCLE_LENGTH);
    }

    private void createSun() {
        this.sun = Sun.create(gameObjects(), SkyAndDayNightConfiguration.SUN_LAYER, windowDimensions,
                GameManagerConfiguration.DAY_CYCLE_LENGTH);
        SunHalo.create(gameObjects(), SkyAndDayNightConfiguration.HALO_LAYER, sun,
                SkyAndDayNightConfiguration.HALO_COLOR);
    }

    public static void main(String[] args) {
        new PepseGameManager(GameManagerConfiguration.WINDOW_TITLE,
                GameManagerConfiguration.WINDOW_SIZE).run();
    }
}
