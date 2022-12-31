package pepse.world.dayNight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the halo of sun.
 */
public class SunHalo {

    private static final String HALO_TAG = "sun halo";
    private static final float HALO_SIZE_FACTOR = 1.5f;

    /**
     * This function creates a halo around a given object that represents the sun. The halo will be tied to the given sun, and will always move with it.
     * @param gameObjects  The collection of all participating game objects.
     * @param layer The number of the layer to which the created halo should be added.
     * @param sun A game object representing the sun (it will be followed by the created game object).
     * @param color The color of the halo
     * @return A new game object representing the sun's halo.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    GameObject sun, Color color) {
        Renderable haloRenderable = new OvalRenderable(color);
        GameObject halo = new GameObject(sun.getCenter(), sun.getDimensions().mult(HALO_SIZE_FACTOR),
                haloRenderable);
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        halo.addComponent(x -> halo.setCenter(sun.getCenter()));

        gameObjects.addGameObject(halo, layer);
        halo.setTag(HALO_TAG);
        return halo;
    }

}
