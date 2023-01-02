package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;
import java.util.function.Function;

/**
 * Responsible for the creation and management of trees.
 */
public class Tree {
    private static final String TREE_TAG = "tree";
    //TODO: CHANGE TREE MAX WIDTH AND HIGHT
    private static final int MAX_TREE_STUMP_WIDTH = 30;
    private static final int MAX_TREE_STUMP_HEIGHT = 300;
    private static final int MAX_TREE_DISTANCE_FACTOR = 4;
    private static final int DEFAULT_STUMP_WIDTH = Block.SIZE;

    private static final int DEFAULT_STUMP_HEIGHT = 250;
    private static final Color LEAF_COLOR = new Color(29, 105, 19);
    private final int treeLayer;
    private final int seed;
    private final Random rand;
    private GameObjectCollection gameObjects;
    private Function<Float, Float> yCoordinateCallback;

    public Tree(GameObjectCollection gameObjects, Function<Float, Float> yCoordinateCallback, int seed,
                int treeLayer) {
        this.gameObjects = gameObjects;
        this.yCoordinateCallback = yCoordinateCallback;
        this.seed = seed;
        this.treeLayer = treeLayer;
        this.rand = new Random();
        //rand.setSeed(seed);
    }
//todo: update documentation

    /**
     * Creates trees in the provided range:
     * chooses randomly the distance between trees and the width & height of each tree's stump.
     *
     * @param minX beginning of range
     * @param maxX end of range
     */
    public void createInRange(int minX, int maxX) {

        for (int startPositionX = minX;
             startPositionX <= maxX - MAX_TREE_STUMP_WIDTH;
             startPositionX += rand.nextInt(MAX_TREE_DISTANCE_FACTOR) *
                     DEFAULT_STUMP_WIDTH + DEFAULT_STUMP_WIDTH) {

            int stumpHeight = rand.nextInt(MAX_TREE_STUMP_HEIGHT);
            if (stumpHeight <= DEFAULT_STUMP_WIDTH * 3) {
                stumpHeight = DEFAULT_STUMP_HEIGHT;
            }
            //todo: try to fix this with lab support: in some cases the tree is one pixel above the floor:
            // answer - round down should fix it
            float startPositionY = yCoordinateCallback.apply((float) startPositionX);
            Vector2 location = new Vector2(startPositionX, startPositionY - stumpHeight);

            Vector2 stumpSize = new Vector2(DEFAULT_STUMP_WIDTH, stumpHeight);
            int treetopRadius = (int) (stumpHeight*0.3);
            SingleTree aTree = createSingleTree(location, stumpSize, treetopRadius);
            gameObjects.addGameObject(aTree);
        }
    }

    /**
     * Creates a SingleTree object, with leafs, at location and according to dimensions
     *
     * @param topLeftCorner   location to start tree from
     * @param stumpDimensions stump dimensions (width, height)
     * @param treetopRadius   treetop possible radius
     * @return A SingleTree
     */
    private SingleTree createSingleTree(Vector2 topLeftCorner, Vector2 stumpDimensions,
                                       int treetopRadius) {
        Vector2 treetopDimensions = new Vector2(treetopRadius, treetopRadius);
        Vector2 treetopLocation =
                topLeftCorner.add(treetopDimensions.mult(-0.5f).add(new Vector2(
                        stumpDimensions.x()*0.5f,0)));
        SingleTree tree = new SingleTree(topLeftCorner, stumpDimensions, gameObjects, treeLayer,
                treetopLocation, treetopDimensions, rand);
        return tree;
    }
}
