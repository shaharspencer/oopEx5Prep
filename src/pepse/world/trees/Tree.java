package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

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

    //todo: remove this when implementing infinit world
    private static final int WORLD_END_COORDINATE = 500;
    private static final int MAX_TREE_STUMP_HEIGHT = 100;
    private static final int MAX_TREETOP_SIZE = 80;
    private static final int MAX_TREE_DISTANCE = 200;
    private static final int MIN_TREE_WIDTH = 10;
    private static final int DEFAULT_STUMP_WIDTH = 25;

    private static final int DEFAULT_STUMP_HEIGHT = 220;

    private final int treeLayer;
    private final int seed;
    private final Random rand;
    private GameObjectCollection gameObjects;
    private Function<Float, Double> yCoordinateCallback;

    public Tree(GameObjectCollection gameObjects, Function<Float, Double> yCoordinateCallback, int seed,
                int treeLayer) {
        this.gameObjects = gameObjects;
        this.yCoordinateCallback = yCoordinateCallback;
        this.seed = seed;
        this.treeLayer = treeLayer;
        this.rand = new Random(seed);
    }

    /**
     * Creates trees in the provided range:
     * chooses randomly the distance between trees and the width & height of each tree's stump.
     *
     * @param minX beginning of range
     * @param maxX end of range
     */
    public void createInRange(int minX, int maxX) {
        //int nextTreeStumpWidth = rand.nextInt(MAX_TREE_STUMP_WIDTH);
        int nextTreeStumpWidth = DEFAULT_STUMP_WIDTH;
        for (int startPositionX = minX;
             startPositionX <= maxX - MAX_TREE_STUMP_WIDTH;
             startPositionX = startPositionX + rand.nextInt(MAX_TREE_DISTANCE) + nextTreeStumpWidth) {
            if (nextTreeStumpWidth < MIN_TREE_WIDTH) {
                nextTreeStumpWidth = rand.nextInt(MAX_TREE_STUMP_WIDTH);
                continue;
            }
            float startPositionY = yCoordinateCallback.apply((float) startPositionX).floatValue();
            Vector2 location = new Vector2(startPositionX, startPositionY);
            //todo: make sure higher than wide by a set factor else default width
            Vector2 stumpSize = new Vector2(nextTreeStumpWidth, rand.nextInt(MAX_TREE_STUMP_HEIGHT));
            Vector2 treetopSize = new Vector2(rand.nextInt(MAX_TREETOP_SIZE), rand.nextInt(MAX_TREETOP_SIZE));
            SingleTree aTree = createSingleTree(location, stumpSize, treetopSize);
            gameObjects.addGameObject(aTree, treeLayer);
            nextTreeStumpWidth = rand.nextInt(MAX_TREE_STUMP_WIDTH);
        }
    }


    public SingleTree createSingleTree(Vector2 topLeftCorner, Vector2 stumpDimensions,
                                       Vector2 treetopDimensions) {
        SingleTree tree = new SingleTree(topLeftCorner, stumpDimensions);
        /*
        leafs get: location, game objects and renderable and add themselves to gameobject.
        they have setColor method, and they move in the wind.
         */

        //SingleTree.sproutLeafs();

        return tree;
    }
}
