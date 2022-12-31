package pepse.world.dayNight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {

    private static String suntag = "sun";
    private static Vector2 sunDimensions = new Vector2(100, 100);
    /**
     * This function creates a yellow circle that moves in the sky
     * in an elliptical path (in camera coordinates).
     * @param gameObjects - The collection of all participating game objects.
     * @param layer - The number of the layer to which the created sun should be added.
     * @param windowDimensions - The dimensions of the windows.
     * @param cycleLength - The amount of seconds it should take the created game object to complete a full cycle.
     * @return Sun gameObject
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
        OvalRenderable sunRenderable = new OvalRenderable(Color.YELLOW);
        Vector2 topLeftCorner = new Vector2( 20,
               20);
        GameObject sun = new GameObject(topLeftCorner, sunDimensions, sunRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(suntag);
        gameObjects.addGameObject(sun, layer);
        return sun;
    }

    private static void createSunTransition(GameObject sun){

    }

}
