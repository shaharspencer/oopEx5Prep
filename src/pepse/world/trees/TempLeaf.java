package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class TempLeaf extends GameObject {
    public static final int LEAF_SIZE = 15;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public TempLeaf(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, new Vector2(LEAF_SIZE, LEAF_SIZE), renderable);
        this.setCenter(topLeftCorner);
    }
}
