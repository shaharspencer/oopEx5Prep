package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.configurations.SkyAndDayNightConfiguration;

/**
 * Represents the sky.
 */
public class Sky {
    //######## public static Methods ########

    /**
     * This function creates a light blue rectangle which is always at the back of the window.
     * @param gameObjects The collection of all participating game objects.
     * @param windowDimensions - The number of the layer to which the created game object should be added.
     * @param skyLayer - The number of the layer to which the created sky should be added.
     * @return  A new game object representing the sky.
     */
    public static danogl.GameObject create(GameObjectCollection gameObjects,
                                           Vector2 windowDimensions, int skyLayer){
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(SkyAndDayNightConfiguration.BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sky, skyLayer);
        sky.setTag(SkyAndDayNightConfiguration.SKY_TAG);
        return sky;
    }

}
