package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a single block (larger objects can be created from blocks).
 */
public class Block extends GameObject {
    // constant field for tag value to be set for class instances
    private static final String GROUND_TAG = "ground";
    // Size of a single block. Default size: 30.
    private static int SIZE = 30;

    /**
     * Construct a new Block instance.
     *
     * @param topLeftCorner  The location of the top-left corner of the created block, in window coordinates
     *                       (pixels).
     * @param renderable     A renderable to render as the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(GROUND_TAG);
    }
}
