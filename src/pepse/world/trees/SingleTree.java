package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.GameObjectPhysics;
import danogl.util.Vector2;
import pepse.util.configurations.TreeConfiguration;

import java.util.HashSet;
import java.util.Random;


/**
 * Factory responsible for creating a single Tree object
 */
public class SingleTree extends GameObject {
    //######## private fields ########
    private final GameObjectCollection gameObjects;
    private final Random rand;
    private final LeafFactory leafFactory;
    private HashSet<Leaf> leaves;

    //######## public methods ########

    /**
     * Construct a new GameObject instance.
     *
     * @param stumpTopLeftCorner Position of the object, in window coordinates (pixels).
     *                           Note that (0,0) is the top-left corner of the window.
     * @param stumpDimensions    Width and height in window coordinates.
     */
    public SingleTree(Vector2 stumpTopLeftCorner, Vector2 stumpDimensions, GameObjectCollection gameObjects,
                      Vector2 treetopCenter, int treetopRadius,
                      Random rand, LeafFactory leafFactory) {
        super(stumpTopLeftCorner, stumpDimensions, TreeConfiguration.STUMP_RENDERABLE);
        this.rand = rand;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(TreeConfiguration.TREE_TAG);
        this.gameObjects = gameObjects;
        this.leaves = new HashSet<>();
        this.leafFactory = leafFactory;

        sproutLeaves(treetopCenter, treetopRadius);
    }

    /**
     * Removes all the tree's leafs from the game
     */
    public void removeLeafs() {
        for (Leaf leaf : leaves) {
            gameObjects.removeGameObject(leaf, TreeConfiguration.LEAF_LAYER);
        }
        this.leaves = new HashSet<>();
    }

    //######## private methods ########

    /**
     * Create new leafs around the treetop center in a circle with radius  matching treetopRadius.
     *
     * @param treetopCenter location of the treetop center
     * @param treetopRadius the radius of the treetop, the radius of the circle shaped area in which leafs
     *                      will be created.
     */
    private void sproutLeaves(Vector2 treetopCenter, int treetopRadius) {
        this.leaves = new HashSet<>();
        for (int currentRadiusSize = 0;
             currentRadiusSize <= treetopRadius - TreeConfiguration.LEAF_SIZE;
             currentRadiusSize += TreeConfiguration.LEAF_SIZE * TreeConfiguration.LEAF_OVERLAP_FACTOR) {

            for (float angle = 0; angle <= 360 - TreeConfiguration.LEAF_SIZE;
                 angle += TreeConfiguration.LEAF_SIZE * TreeConfiguration.LEAF_ANGLE_CHANGE_FACTOR) {

                if (rand.nextInt(TreeConfiguration.LEAF_SPROUT_PROBABILITY_RANGE) <=
                        TreeConfiguration.LEAF_SPROUT_PROBABILITY) {
                    Vector2 leafLocation =
                            Vector2.of(currentRadiusSize,currentRadiusSize).rotated(angle).add(treetopCenter);
                    Leaf leaf = sproutALeaf(leafLocation);
                    this.leaves.add(leaf);
                }
            }
        }

    }

    /**
     * Creates a single Leaf instance at leafLocation
     * @param leafLocation coordinates to create leaf at (top left corner of the desired leaf)
     * @return the created Leaf
     */
    private Leaf sproutALeaf(Vector2 leafLocation) {
        Leaf leaf = leafFactory.createLeaf(leafLocation, rand);
        return leaf;
    }
}
