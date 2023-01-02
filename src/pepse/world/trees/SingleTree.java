package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;


/**
 * Factory responsible for creating a single Tree object
 */
public class SingleTree extends GameObject {
    private static final String TREE_TAG = "tree";
    private static final Color STUMP_COLOR = new Color(94, 60, 39);
    private static final Renderable STUMP_RENDERABLE = new RectangleRenderable(STUMP_COLOR);
    private static final Color LEAF_COLOR = new Color(43, 105, 19);

    private final GameObjectCollection gameObjects;
    private int leafLayer;
    private Random rand;

    //private Leaf[] leafs;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     */
    public SingleTree(Vector2 topLeftCorner, Vector2 dimensions, GameObjectCollection gameObjects,
                      int leafLayer,
                      Vector2 treetopLocation, Vector2 treetopDimensions,
                      Random rand) {
        super(topLeftCorner, dimensions, STUMP_RENDERABLE);
        this.rand = rand;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(TREE_TAG);
        this.gameObjects = gameObjects;
        this.leafLayer = leafLayer;
        Vector2 treetopCenter = treetopLocation.add(treetopDimensions.mult(0.5f));
        sproutLeafs(treetopCenter, (int) dimensions.x());
    }

    public void sproutLeafs(Vector2 treetopCenter, int treetopRadius) {
        //todo: change factors to static
        for (int i = 0;
             i <= treetopRadius - TempLeaf.LEAF_SIZE;
             i+=TempLeaf.LEAF_SIZE*0.25) {
            Vector2 currRadius = treetopCenter.add(Vector2.of(i, i));
            //for each possible angle to place the leaf
            //todo: find the right angle change
            for (float angle = 0; angle <= 360 - TempLeaf.LEAF_SIZE; angle+=TempLeaf.LEAF_SIZE*0.3) {
                if(rand.nextInt(10)<=4){
                    Vector2 leafLocation = currRadius.rotated(angle).mult(i*0.01f).add(treetopCenter);
                    sproutALeaf(leafLocation);
                }
            }
        }
    }

    public void sproutALeaf(Vector2 leafLocation) {
        RectangleRenderable leafRenderable = new RectangleRenderable(LEAF_COLOR);
        TempLeaf newLeaf = new TempLeaf(leafLocation, leafRenderable);
        gameObjects.addGameObject(newLeaf, leafLayer);
    }
}
