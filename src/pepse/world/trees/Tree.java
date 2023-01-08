package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.util.TerrainConfiguration;
import pepse.util.TreeConfiguration;
import pepse.world.Block;

import java.awt.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Responsible for the creation and management of trees.
 */
public class Tree {
    //######## static fields ########
    private static final int MAX_TREE_DISTANCE_FACTOR = 4;


    //######## private fields ########

    private final int seed;
    private final GameObjectCollection gameObjects;
    private final Function<Float, Float> yCoordinateCallback;
    private final int treeLayer;
    private LinkedList<SingleTree> existingTrees = new LinkedList<>();

    //######## public Methods ########
    //todo: add documentation
    /**
     *
     * @param gameObjects
     * @param yCoordinateCallback
     * @param seed
     * @param treeLayer
     */
    public Tree(GameObjectCollection gameObjects, Function<Float, Float> yCoordinateCallback, int seed,
                int treeLayer) {
        this.gameObjects = gameObjects;
        this.yCoordinateCallback = yCoordinateCallback;
        this.seed = seed;
        this.treeLayer = treeLayer;
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
        ArrayList<SingleTree> treesInRange = new ArrayList<>();
        //for every possible tree position (for every floor block)
        for(int startPositionX = minX; startPositionX <= maxX - TreeConfiguration.MAX_TREE_STUMP_WIDTH;
        startPositionX += TreeConfiguration.MIN_DIST_BETWEEN_TREES_FACTOR * Block.SIZE){
            Random randX = new Random(Objects.hash(startPositionX, seed));
            if(randX.nextInt(TreeConfiguration.TREE_SPROUT_PROBABILITY_RANGE) <
                    TreeConfiguration.TREE_SPROUT_PROBABILITY){
                SingleTree aTree = sproutATree(randX, startPositionX);
                treesInRange.add(aTree);
            }
        }

        if (existingTrees.isEmpty()){
            existingTrees.addAll(treesInRange);
            return;
        }
        // if we want to add at beggining of list
        if (existingTrees.get(0).getTopLeftCorner().x() > maxX){
            existingTrees.addAll(0, existingTrees);
            return;
        }
        // if we want to add to end of list
        else if (existingTrees.getLast().getTopLeftCorner().x() < minX){
            existingTrees.addAll(existingTrees.size(), treesInRange);
            return;
        }

    }

    private SingleTree sproutATree(Random treeRandom, int startPositionX) {
        int stumpHeight = treeRandom.nextInt(TreeConfiguration.MAX_TREE_STUMP_HEIGHT);
        if (stumpHeight <= TreeConfiguration.DEFAULT_STUMP_WIDTH * 3) {
            stumpHeight = TreeConfiguration.DEFAULT_STUMP_HEIGHT;
        }
        float startPositionY = yCoordinateCallback.apply((float) startPositionX);

        Vector2 location = new Vector2(startPositionX, startPositionY - stumpHeight);

        Vector2 stumpSize = new Vector2(TreeConfiguration.DEFAULT_STUMP_WIDTH, stumpHeight);
        int treetopRadius = (int) (Math.ceil(stumpHeight/3f));

        SingleTree aTree = createSingleTree(location, stumpSize, treetopRadius, treeRandom);
        gameObjects.addGameObject(aTree, treeLayer);
        return aTree;
    }


    public void deleteInRange(int startingSpot, Boolean removeToRight) {

        // remove all objects to the left of the starting point from the game
        if (!removeToRight){

            LinkedList<SingleTree> newList = new LinkedList<>();
            for (SingleTree tree: existingTrees){

                if (tree.getTopLeftCorner().x() <= startingSpot){
                    removeObjectsInColumnFromGame(tree);
                }
                else{
                    newList.add(tree);
                }

            }
            // remove all objects to the left of the starting point from the linked list
            this.existingTrees = newList;
        }

        else{
            LinkedList<SingleTree> newList = new LinkedList<>();
            for (SingleTree tree: existingTrees){

                if (tree.getTopLeftCorner().x() >= startingSpot){
                    removeObjectsInColumnFromGame(tree);
                }
                else{
                    newList.add(tree);
                }
            }
            // remove all objects to the left of the starting point from the linked list
            this.existingTrees = newList;
        }

    }

    private void removeObjectsInColumnFromGame(SingleTree tree) {
        tree.removeLeafs();
        gameObjects.removeGameObject(tree, treeLayer);
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
                                        int treetopRadius, Random randomPerX) {
        Vector2 treetopCenter =
                new Vector2(topLeftCorner.x()+stumpDimensions.x()*0.5f-TreeConfiguration.LEAF_SIZE*0.5f,
                topLeftCorner.y());
        SingleTree tree = new SingleTree(topLeftCorner, stumpDimensions, gameObjects,
                treeLayer, treetopCenter, treetopRadius, randomPerX);
        return tree;
    }
}
