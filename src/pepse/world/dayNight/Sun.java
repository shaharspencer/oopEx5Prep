package pepse.world.dayNight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static danogl.components.Transition.TransitionType.TRANSITION_LOOP;
import static danogl.components.Transition.TransitionType.TRANSITION_ONCE;

public class Sun {

    private static String suntag = "sun";
    private static Vector2 sunDimensions = new Vector2(100, 100);
    private static int sunCoord = 20;

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

        Vector2 windowCenter = new Vector2(windowDimensions.x() /2, windowDimensions.y() /2);
        GameObject sun = new GameObject(topLeftCorner, sunDimensions, sunRenderable);
        sun.setCenter(windowCenter);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(suntag);
        gameObjects.addGameObject(sun, layer);
        createSunTransition(sun, cycleLength, windowCenter);
        return sun;
    }

    private static void createSunTransition(GameObject sun, float cycleLength, Vector2 windowCenter){

        Consumer<Float> m = (Float angle)-> {
                sun.setCenter(new Vector2(
                                windowCenter.x() + (float) (Math.cos(angle) * windowCenter.x()),
               windowCenter.y ()+ (float) Math.sin(angle)* windowCenter.y()));};
        new Transition<Float>(sun,
                m, (float) ((float) Math.PI * 1.5), (float) (Math.PI * 3.5),
                Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength, TRANSITION_LOOP,
               null);
        //todo: make the sun turn not in a perfect circle (oval)
        //todo: Vector2 has a method "rotated" that rotates the vector accirding to a given angle. it saves
        // the calculation of the consumer.
        // todo: Vector2 has method multY and multX. we can use them to make it oval and not a circle.

    }


}
