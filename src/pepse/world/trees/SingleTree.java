package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Factory responsible for creating a single Tree object
 */
public class SingleTree extends GameObject {
    private static final String TREE_TAG = "tree";
    private static final Color STUMP_COLOR = new Color(94, 60, 39);
    private static final Renderable STUMP_RENDERABLE = new RectangleRenderable(STUMP_COLOR);

    private Leaf[] leafs;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     */
    public SingleTree(Vector2 topLeftCorner, Vector2 dimensions){
        super(topLeftCorner, dimensions, STUMP_RENDERABLE);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(TREE_TAG);
    }

    public static void sproutLeafs() {
        Vector2 leafLocation;
        //Leaf newLeaf = new Leaf();
        //newLeaf.setCenter(leafLocation);

    }
}
