package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the sky.
 */

public class Sky {
    // the color of the sky
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    private static int skyLayer;

    /**
     * This function creates a light blue rectangle which is always at the back of the window.
     * @param gameObjects The collection of all participating game objects.
     * @param windowDimensions - The number of the layer to which the created game object should be added.
     * @param skyLayer - The number of the layer to which the created sky should be added.
     * @return  A new game object representing the sky.
     */
    public static danogl.GameObject create(danogl.collisions.GameObjectCollection gameObjects,
                                            danogl.util.Vector2 windowDimensions, int skyLayer){
        Sky.skyLayer = skyLayer;
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        // the sky should move with the camera
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sky, skyLayer);
        // since the sky inherits directly from gameobject, set a tag to recognize it
        sky.setTag("sky");
        return sky;
    }

}
