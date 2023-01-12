package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Vector2;
import pepse.util.configurations.SkyAndDayNightConfiguration;


public class SkyManager {
    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;
    private final int skyLayer;
    private final ImageRenderable cloudImage;
    private GameObject currentClouds;

    public SkyManager(GameObjectCollection gameObjects, Vector2 windowDimensions, int skyLayer,
                      ImageReader imageReader) {

        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.skyLayer = skyLayer;
        this.cloudImage = imageReader.readImage(SkyAndDayNightConfiguration.CLOUD_IMAGE,
                true);
    }

    /**
     * Prompts changes to sky when the season changes
     *
     * @param season new season
     */
    public void setSeason(int season) {

        switch (season) {
            //fall
            case (1):
                addClouds();
                break;
            //spring
            case (3):
                removeClouds();
                break;
            default:
                break;
        }
    }

    /**
     * removes clouds from sky
     */
    private void removeClouds() {
        gameObjects.removeGameObject(this.currentClouds, skyLayer);
        this.currentClouds = null;
    }

    /**
     * adds clouds to sky
     */
    private void addClouds() {
        GameObject cloud = new GameObject(Vector2.ZERO, windowDimensions, cloudImage);
        cloud.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        currentClouds = cloud;
        gameObjects.addGameObject(cloud, skyLayer);
    }

    /**
     * creates the basic blue sky
     */
    public void createSky() {
        Sky.create(gameObjects, windowDimensions, SkyAndDayNightConfiguration.SKY_LAYER);
    }
}
