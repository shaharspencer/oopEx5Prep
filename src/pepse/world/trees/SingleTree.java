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
import java.util.HashSet;
import java.util.Random;


/**
 * Factory responsible for creating a single Tree object
 */
public class SingleTree extends GameObject {
    private static final String TREE_TAG = "tree";
    private static final Color STUMP_COLOR = new Color(94, 60, 39);
    private static final Renderable STUMP_RENDERABLE = new RectangleRenderable(STUMP_COLOR);

    private final GameObjectCollection gameObjects;
    private int leafLayer;
    private Random rand;

    private HashSet<Leaf> leaves;
    //todo: make sure stump is also removed


    /**
     * Construct a new GameObject instance.
     *
     * @param stumpTopLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param stumpDimensions    Width and height in window coordinates.
     */
    public SingleTree(Vector2 stumpTopLeftCorner, Vector2 stumpDimensions, GameObjectCollection gameObjects,
                      int leafLayer,
                      Vector2 treetopCenter, int treetopRadius,
                      Random rand) {
        super(stumpTopLeftCorner, stumpDimensions, STUMP_RENDERABLE);
        this.rand = rand;

        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(TREE_TAG);
        this.gameObjects = gameObjects;
        this.leafLayer = leafLayer;
        this.leaves = new HashSet<>();

        sproutLeafs(treetopCenter, treetopRadius);
    }

    public void removeLeafs(){
        for (Leaf leaf: leaves){
            gameObjects.removeGameObject(leaf, TreeConfiguration.LEAF_LAYER);
        }
        this.leaves = new HashSet<>();
    }

    public void sproutLeafs(Vector2 treetopCenter, int treetopRadius) {
        //todo: change factors to static
        this.leaves = new HashSet<>();

        for (int currentRadiusSize = 0;
             currentRadiusSize <= treetopRadius - TreeConfiguration.LEAF_SIZE;
             currentRadiusSize+=TreeConfiguration.LEAF_SIZE*0.25) {
            //todo: find the right angle change
            for (float angle = 0; angle <= 360 - TreeConfiguration.LEAF_SIZE;
                 angle+=TreeConfiguration.LEAF_SIZE*0.3) {
                if(rand.nextInt(10)<=4){
                    Vector2 leafLocation =
                            Vector2.of(currentRadiusSize,currentRadiusSize).rotated(angle).add(treetopCenter);
                    Leaf leaf = sproutALeaf(leafLocation);
                    this.leaves.add(leaf);
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
