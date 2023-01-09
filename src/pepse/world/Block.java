package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.configurations.BlockConfiguration;

/**
 * Represents a single block (larger objects can be created from blocks).
 * In our game all the Block instances are ground blocks.
 */
public class Block extends GameObject {
    //######## public methods ########
    public static int SIZE = 30;

    //######## public methods ########

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
    }
}
