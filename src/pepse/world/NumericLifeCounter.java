package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;

import java.awt.*;

public class NumericLifeCounter extends GameObject {

    private final TextRenderable textRenderable;
    private float energyLevel;


    public NumericLifeCounter(danogl.util.Vector2 topLeftCorner,
                              danogl.util.Vector2 dimensions, float energyLevel
                             ){
        super(topLeftCorner, dimensions, null);
        this.energyLevel = energyLevel;
        this.textRenderable = new TextRenderable(Float.toString(energyLevel));
        renderer().setRenderable(textRenderable);
        textRenderable.setColor(Color.RED);

    }


    public void setEnergyLevel(float energyLevel){

        this.energyLevel = energyLevel;
    }

    /**
     * This method is overwritten from GameObject.
     * It sets the string value of the text object to the number of current lives left.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        String energyLevelString = Integer.toString((int) this.energyLevel);
        textRenderable.setString(energyLevelString);
    }
}