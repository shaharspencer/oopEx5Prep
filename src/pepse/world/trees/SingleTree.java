package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.TreeConfiguration;

import java.awt.*;
import java.util.Random;
import java.util.Set;


/**
 * Factory responsible for creating a single Tree object
 */
public class SingleTree extends GameObject {
    private static final String TREE_TAG = "tree";
    private static final Color STUMP_COLOR = new Color(94, 60, 39);
    private static final Renderable STUMP_RENDERABLE = new RectangleRenderable(STUMP_COLOR);

    private final GameObjectCollection gameObjects;
    private final Random rand;

    private final Set<GameObject> treeObjects;


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
        Vector2 treetopCenter = treetopLocation.add(treetopDimensions.mult(0.5f));
        Set<Leaf> = sproutLeafs(treetopCenter, (int) dimensions.x());
        this.treeObjects =
    }

    public Set<Leaf> sproutLeafs(Vector2 treetopCenter, int treetopRadius) {
        //todo: change factors to static
        Set<Leaf> = new Set();
        for (int i = 0;
             i <= treetopRadius - TreeConfiguration.LEAF_SIZE;
             i+=TreeConfiguration.LEAF_SIZE*0.25) {
            Vector2 currRadius = treetopCenter.add(Vector2.of(i, i));
            //for each possible angle to place the leaf
            //todo: find the right angle change
            for (float angle = 0; angle <= 360 - TreeConfiguration.LEAF_SIZE;
                 angle+=TreeConfiguration.LEAF_SIZE*0.2) {
                if(rand.nextInt(10)<=4){
                    Vector2 leafLocation = currRadius.rotated(angle).mult(i*0.01f).add(treetopCenter);
                    sproutALeaf(leafLocation);
                }
            }
        }
    }

    public Leaf sproutALeaf(Vector2 leafLocation) {
        LeafFactory leafFactory = new LeafFactory(gameObjects);
        Leaf leaf = leafFactory.createLeaf(leafLocation);
        return leaf;
    }
}
