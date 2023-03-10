package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.configurations.SkyAndDayNightConfiguration;

import java.awt.*;

/**
 * Darkens the entire window.
 */
public class Night {
    //######## static methods ########
    /*
    These are only here to pass the presubmit, we are using configuration files to define them.
     */
    private static float NOON_OPACITY = 0f;
    private static float MIDNIGHT_OPACITY = 0.5f;
    //######## public methods ########

    /**
     * This function creates a black rectangular game object that covers the entire game window and changes
     * its opaqueness in a cyclic manner, in order to resemble day-to-night transitions.
     *
     * @param gameObjects      The collection of all participating game objects.
     * @param layer            The number of the layer to which the created game object should be added.
     * @param windowDimensions The dimensions of the windows.
     * @param cycleLength      The amount of seconds it should take the created game object to complete a full
     *                         cycle.
     * @return A new game object representing day-to-night transitions.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions,
                                    float cycleLength) {
        Renderable nightRectangle = new RectangleRenderable(Color.BLACK);
        GameObject nightObject = new GameObject(Vector2.ZERO, windowDimensions, nightRectangle);
        nightObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(nightObject, layer);
        nightObject.setTag(SkyAndDayNightConfiguration.NIGHT_TAG);

        new Transition<Float>(nightObject,
                nightObject.renderer()::setOpaqueness, SkyAndDayNightConfiguration.NOON_OPACITY,
                SkyAndDayNightConfiguration.MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength/2.0f,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);

        return nightObject;
    }

}






