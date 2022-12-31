package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Random;
import java.util.function.Function;

/**
 * Responsible for the creation and management of trees.
 */
public class Tree {
    private static final String TREE_TAG = "tree";
    //TODO: CHANGE TREE MAX WIDTH AND HIGHT
    private static final int MAX_TREE_STUMP_WIDTH = 30;

    //todo: remove this when implementing infinit world
    private static final int WORLD_END_COORDINATE = 500;
    private static final int MAX_TREE_STUMP_HEIGHT = 300;
    private static final int MAX_TREETOP_SIZE = 80;
    private static final int MAX_TREE_DISTANCE_FACTOR = 4;
    private static final int MIN_TREE_WIDTH = 10;
    private static final int DEFAULT_STUMP_WIDTH = Block.SIZE;

    private static final int DEFAULT_STUMP_HEIGHT = 220;

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
             startPositionX = startPositionX +
                     (rand.nextInt(MAX_TREE_DISTANCE_FACTOR)* DEFAULT_STUMP_WIDTH+ DEFAULT_STUMP_WIDTH)) {

            int stumpHeight = rand.nextInt(MAX_TREE_STUMP_HEIGHT);
            if (stumpHeight<= DEFAULT_STUMP_WIDTH*2){
                stumpHeight = DEFAULT_STUMP_HEIGHT;
            }
            //todo: try to fix this with lab support: in some cases the tree is one pixel above the floor
            float startPositionY = yCoordinateCallback.apply((float) startPositionX);
            Vector2 location = new Vector2(startPositionX, startPositionY-stumpHeight);

            Vector2 stumpSize = new Vector2(DEFAULT_STUMP_WIDTH, stumpHeight);
            Vector2 treetopSize = new Vector2(rand.nextInt(MAX_TREETOP_SIZE), rand.nextInt(MAX_TREETOP_SIZE));
            SingleTree aTree = createSingleTree(location, stumpSize, treetopSize);
            gameObjects.addGameObject(aTree, treeLayer);
        }
    }

    /**
     * Creates a SingleTree object, with leafs, at location and according to dimensions
     * @param topLeftCorner location to start tree from
     * @param stumpDimensions stump dimensions (width, height)
     * @param treetopDimensions treetop possible dimensions (width, height)
     * @return
     */
    public SingleTree createSingleTree(Vector2 topLeftCorner, Vector2 stumpDimensions,
                                       Vector2 treetopDimensions) {
        SingleTree tree = new SingleTree(topLeftCorner, stumpDimensions);
        //todo: make sure this float isn't bothering anything
        Vector2 fixTreetopLocationBy = new Vector2(treetopDimensions.x()*(-0.5f),treetopDimensions.y());
        Vector2 treetopLocation = topLeftCorner.add(fixTreetopLocationBy);
        
        /*
        leafs get: location, game objects and renderable and add themselves to gameobject.
        they have setColor method, and they move in the wind.
         */

        //SingleTree.sproutLeafs();

        return tree;
    }
}
