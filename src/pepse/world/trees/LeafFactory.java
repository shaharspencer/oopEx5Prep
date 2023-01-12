package pepse.world.trees;

import java.awt.*;
import java.util.Random;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.configurations.TreeConfiguration;

/**
 * Factory for leaves (returns instance of Leaf that has been added to the game).
 */
public class LeafFactory {
    //######## private fields ########
    private final GameObjectCollection gameObjects;
    private final int seed;

    //######## public methods ########

    /**
     * Constructor of LeafFactory
     * @param gameObjects collection of gameObjects to add the created leaves to
     */
    public LeafFactory(GameObjectCollection gameObjects, int seed){
        this.gameObjects = gameObjects;
        this.seed = seed;
    }

    /**
     * Creates a leaf at location and returns it.
     * The returned leaf is added to gameObjects during its creation.
     * @param topLeftCorner the location of the top left corner of the desired leaf
     * @param rand Random object of the tree that requested a leaf
     * @return the created Leaf instance
     */
    public Leaf createLeaf(Vector2 topLeftCorner, Random rand){
        // choose leaf dimension
        int leafDimensionIndx = rand.nextInt(TreeConfiguration.LEAF_DIM.length);
        int leafDimension = TreeConfiguration.LEAF_DIM[leafDimensionIndx];

        int colorIndx = rand.nextInt(TreeConfiguration.LEAF_COLORS.length);
        Color leafColor = TreeConfiguration.LEAF_COLORS[colorIndx];
        return new Leaf(topLeftCorner, gameObjects, new RectangleRenderable(leafColor),
                leafDimension, seed, leafColor);
    }
}
