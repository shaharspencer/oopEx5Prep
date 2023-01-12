package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.ImageRenderable;
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
    private final ImageRenderable flowerImage;
    private final LeafFactory leafFactory;
    private final Vector2 treetopCenter;
    private final int treetopRadius;
    private final HashSet<Leaf> leaves = new HashSet<>();
    private final HashSet<GameObject> flowers = new HashSet<>();

    //######## public methods ########

    /**
     * Construct a new GameObject instance.
     *
     * @param stumpTopLeftCorner Position of the object, in window coordinates (pixels).
     *                           Note that (0,0) is the top-left corner of the window.
     * @param stumpDimensions    Width and height in window coordinates.
     * @param flowerImage
     */
    public SingleTree(Vector2 stumpTopLeftCorner, Vector2 stumpDimensions, GameObjectCollection gameObjects,
                      Vector2 treetopCenter, int treetopRadius,
                      Random rand, LeafFactory leafFactory, ImageRenderable flowerImage) {
        super(stumpTopLeftCorner, stumpDimensions, TreeConfiguration.STUMP_RENDERABLE);
        this.rand = rand;
        this.flowerImage = flowerImage;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(TreeConfiguration.TREE_TAG);
        this.gameObjects = gameObjects;
        this.leafFactory = leafFactory;
        this.treetopCenter = treetopCenter;
        this.treetopRadius = treetopRadius;

        sproutLeaves(treetopCenter, treetopRadius, false);
    }

    /**
     * Removes all the tree's leafs from the game
     */
    public void removeLeafs() {
        for (Leaf leaf : leaves) {
            gameObjects.removeGameObject(leaf, TreeConfiguration.LEAF_LAYER);
        }
        this.leaves.clear();

        removeFlowers();

    }

    /**
     * Changes leaves colors according to the season
     *
     * @param season new season
     */
    public void changeColor(int season) {
        for (Leaf leaf : leaves) {
            leaf.changeColor(season);
        }
    }

    /**
     * Adds flowers to the trees
     */
    public void addFlowers() {
        sproutLeaves(treetopCenter, treetopRadius, true);
    }

    /**
     * removes flowers from the trees
     */
    public void removeFlowers() {
        for (GameObject flower : flowers) {
            gameObjects.removeGameObject(flower, TreeConfiguration.LEAF_LAYER);
        }
        this.flowers.clear();
    }

    //######## private methods ########

    /**
     * Create new leafs around the treetop center in a circle with radius  matching treetopRadius.
     *
     * @param treetopCenter location of the treetop center
     * @param treetopRadius the radius of the treetop, the radius of the circle shaped area in which leafs
     *                      will be created.
     */
    private void sproutLeaves(Vector2 treetopCenter, int treetopRadius, boolean isFlower) {
        for (int currentRadiusSize = 0;
             currentRadiusSize <= treetopRadius - TreeConfiguration.LEAF_SIZE;
             currentRadiusSize += TreeConfiguration.LEAF_SIZE * TreeConfiguration.LEAF_OVERLAP_FACTOR) {

            for (float angle = 0; angle <= 360 -
                    TreeConfiguration.LEAF_SIZE * TreeConfiguration.LEAF_OVERLAP_FACTOR;
                 angle += TreeConfiguration.LEAF_SIZE * TreeConfiguration.LEAF_ANGLE_CHANGE_FACTOR) {
                Vector2 leafLocation =
                        Vector2.of(currentRadiusSize, currentRadiusSize).rotated(angle).add(treetopCenter);
                if (isFlower) {
                    if (rand.nextInt(TreeConfiguration.LEAF_SPROUT_PROBABILITY_RANGE * 10) <=
                            TreeConfiguration.LEAF_SPROUT_PROBABILITY) {
                        GameObject flower = new GameObject(leafLocation, TreeConfiguration.FLOWER_SIZE,
                                flowerImage);
                        gameObjects.addGameObject(flower, TreeConfiguration.LEAF_LAYER);
                        this.flowers.add(flower);
                    }
                } else {
                    if (rand.nextInt(TreeConfiguration.LEAF_SPROUT_PROBABILITY_RANGE) <=
                            TreeConfiguration.LEAF_SPROUT_PROBABILITY) {
                        Leaf leaf = sproutALeaf(leafLocation);
                        this.leaves.add(leaf);
                    }
                }
            }
        }

    }

    /**
     * Creates a single Leaf instance at leafLocation
     *
     * @param leafLocation coordinates to create leaf at (top left corner of the desired leaf)
     * @return the created Leaf
     */
    private Leaf sproutALeaf(Vector2 leafLocation) {
        return leafFactory.createLeaf(leafLocation, rand);
    }

}
