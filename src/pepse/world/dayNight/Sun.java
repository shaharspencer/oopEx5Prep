package pepse.world.dayNight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.util.configurations.SkyAndDayNightConfiguration;

import java.awt.*;
import java.util.function.Consumer;

import static danogl.components.Transition.TransitionType.TRANSITION_LOOP;

/**
 * Represents the sun - moves across the sky in an elliptical path.
 */
public class Sun {

    //######## public methods ########

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
        Vector2 windowCenter = new Vector2(windowDimensions.x() /2, windowDimensions.y() /2);
        GameObject sun = new GameObject(Vector2.ZERO, SkyAndDayNightConfiguration.SUN_DIMENSIONS, sunRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SkyAndDayNightConfiguration.SUN_TAG);
        gameObjects.addGameObject(sun, layer);
        createSunTransition(sun, cycleLength, windowCenter);
        return sun;
    }

    //######## private methods ########

    /**
     * Creates the sun transition responsible for moving the sun in an oval orbit on the screen
     * @param sun gameObject of the sun
     * @param cycleLength length of day in the game, in seconds
     * @param windowCenter location of the center of the game window
     */
    private static void createSunTransition(GameObject sun, float cycleLength, Vector2 windowCenter){

        Consumer<Float> m = (Float angle)-> {
                sun.setCenter(new Vector2(
                                windowCenter.x() - (float) (Math.cos(angle) * windowCenter.x()),
               windowCenter.y ()+ (float) Math.sin(angle)* windowCenter.y()));};
        new Transition<Float>(sun,
                m, (float) ((float) Math.PI * 1.5), (float) (Math.PI * 3.5),
                Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength, TRANSITION_LOOP,
               null);
    }
}
