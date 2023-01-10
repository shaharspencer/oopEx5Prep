package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.configurations.DayNightConfiguration;
import pepse.util.configurations.GameManagerConfiguration;
import pepse.util.configurations.TreeConfiguration;

import java.util.HashSet;

import static danogl.components.Transition.TransitionType.TRANSITION_BACK_AND_FORTH;
import static danogl.components.Transition.TransitionType.TRANSITION_LOOP;

public class SkyManager {
    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;
    private int skyLayer;
    private final ImageRenderable cloudImage;

    private GameObject currentSky;

    private GameObject currentClouds;
    private HashSet<GameObject> currentSnowflakes = new HashSet<>();
    private Renderable snowflakeImage;

    public SkyManager(GameObjectCollection gameObjects, Vector2 windowDimensions, int skyLayer,
                      ImageReader imageReader){

        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.skyLayer = skyLayer;
        //TODO: MOVE TO ANOTHER CONFIGURATION FILE
        this.cloudImage = imageReader.readImage(DayNightConfiguration.CLOUD_IMAGE,
                true);
    }
    public void setSeason(int season) {

        switch (season) {
            //summer
            case (0):
                break;
            //"fall"
            case (1):
                addClouds();
                break;
            //winter
            case (2):
                //addSnowflakes();
                break;
            //spring
            case (3):
                removeClouds();
                //removeSnowflakes();
                break;

        }
    }

    private void removeSnowflakes() {
        for(GameObject snowflake: currentSnowflakes){
            gameObjects.removeGameObject(snowflake, skyLayer);
        }
        this.currentSnowflakes = new HashSet<>();
    }

    private void addSnowflakes() {
        for(float i=windowDimensions.x()*-0.5f; i<windowDimensions.x()*0.5f; i+= Block.SIZE*10){
            Vector2 location = Vector2.of(i, 0);
            GameObject snowflake = createSnowflake(location);
            currentSnowflakes.add(snowflake);
            gameObjects.addGameObject(snowflake, skyLayer);
        }
    }

    private GameObject createSnowflake(Vector2 startLocation){
        GameObject snowflake = new GameObject(startLocation, DayNightConfiguration.SNOW_SIZE, snowflakeImage);
        //todo: change to be of snow and not leaf
        snowflake.setVelocity(Vector2.DOWN);
        new Transition<Vector2>(snowflake,
                (location) -> snowflake.setCenter(snowflake.getCenter().add(Vector2.of(0,(float) location.y()))),
                startLocation,
                Vector2.of(startLocation.x(), windowDimensions.y()),
                Transition.CUBIC_INTERPOLATOR_VECTOR,
                TreeConfiguration.LEAF_WIND_MOVEMENT_TRANSITION_TIME,
                TRANSITION_LOOP, () -> snowflake.setCenter(startLocation));

        new Transition<>(snowflake,
                (angle) -> snowflake.renderer().setRenderableAngle(angle),
                //todo: change to be of snow and not leaf
                TreeConfiguration.LEAF_SIDE_OF_WIND_MOVEMENT *
                        TreeConfiguration.LEAF_SIDE_MOVEMENT_FACTOR,
                -1 * TreeConfiguration.LEAF_SIDE_OF_WIND_MOVEMENT *
                        TreeConfiguration.LEAF_SIDE_MOVEMENT_FACTOR,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                TreeConfiguration.LEAF_WIND_MOVEMENT_TRANSITION_TIME,
                TRANSITION_BACK_AND_FORTH,
                null);
        return snowflake;
    }
    private void removeClouds() {
        gameObjects.removeGameObject(this.currentClouds, skyLayer);
        this.currentClouds = null;
    }

    private void addClouds() {
        GameObject cloud = new GameObject(Vector2.ZERO, windowDimensions, cloudImage);
        cloud.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        currentClouds = cloud;
        gameObjects.addGameObject(cloud, skyLayer);
    }

    public void createSky() {
        currentSky = Sky.create(gameObjects, windowDimensions, GameManagerConfiguration.SKY_LAYER);
    }
}
