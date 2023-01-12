package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;

import java.awt.*;

/**
 * Numeric energy meter to be presented on the screen
 */
public class NumericEnergyMeter extends GameObject {

    private final TextRenderable textRenderable;
    private float energyLevel;

    /**
     * Constructor for enerty meter object
     *
     * @param topLeftCorner location to place the meter at
     * @param dimensions    size of meter
     * @param energyLevel   energy level to present when creating the meter
     */
    public NumericEnergyMeter(danogl.util.Vector2 topLeftCorner,
                              danogl.util.Vector2 dimensions, float energyLevel
    ) {
        super(topLeftCorner, dimensions, null);
        this.energyLevel = energyLevel;
        this.textRenderable = new TextRenderable(Float.toString(energyLevel));
        renderer().setRenderable(textRenderable);
        textRenderable.setColor(Color.RED);

    }

    /**
     * This method is overwritten from GameObject.
     * It sets the string value of the text object to the number of current lives left.
     *
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


    /**
     * changes the energy level to the new energy level provided
     *
     * @param energyLevel new energy level
     */
    public void setEnergyLevel(float energyLevel) {

        this.energyLevel = energyLevel;
    }

}