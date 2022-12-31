package pepse.world.dayNight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {

    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    GameObject sun, Color color) {
        Vector2 location = Vector2.ZERO;
        Renderable haloRenderable = new OvalRenderable(color);
        GameObject halo = new GameObject(location, sun.getDimensions().add(new Vector2(1,1)), haloRenderable);
        return null;
    }

}
