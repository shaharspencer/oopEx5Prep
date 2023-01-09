package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.util.configurations.BlockConfiguration;
import pepse.util.configurations.TreeConfiguration;
import pepse.world.Block;

import java.util.*;
import java.util.function.Function;

/**
 * Responsible for the creation and management of trees.
 */
public class Tree {

    //######## private fields ########
    private final int seed;
    private final GameObjectCollection gameObjects;
    private final Function<Float, Float> yCoordinateCallback;
    private LinkedList<SingleTree> existingTrees = new LinkedList<>();
    private final LeafFactory leafFactory;

    //######## public Methods ########

    /**
     * Constructor of Tree object that manages all the trees (SingleTree) in the game.
     *
     * @param gameObjects         collection of the gameObjects in the game
     * @param yCoordinateCallback callback to a method that calculates y coordinate of the floor provided x
     *                            coordinate.
     * @param seed                used for setting the seed of the Random object of each tree (alongside
     *                            its x coordinate)
     */
    public Tree(GameObjectCollection gameObjects, Function<Float, Float> yCoordinateCallback, int seed) {
        this.gameObjects = gameObjects;
        this.yCoordinateCallback = yCoordinateCallback;
        this.seed = seed;
        leafFactory = new LeafFactory(gameObjects, seed);
    }


    /**
     * This method creates trees in a given range of x-values.
     *
     * @param minX beginning of range
     * @param maxX end of range
     */
    public void createInRange(int minX, int maxX) {
        ArrayList<SingleTree> treesInRange = new ArrayList<>();
        //for every possible tree position (for every floor block)
        for (int startPositionX = minX; startPositionX <= maxX - TreeConfiguration.MAX_TREE_STUMP_WIDTH;
             startPositionX += TreeConfiguration.MIN_DIST_BETWEEN_TREES_FACTOR * BlockConfiguration.SIZE) {
            Random randX = new Random(Objects.hash(startPositionX, seed));
            if (randX.nextInt(TreeConfiguration.TREE_SPROUT_PROBABILITY_RANGE) <
                    TreeConfiguration.TREE_SPROUT_PROBABILITY) {
                SingleTree aTree = sproutATree(randX, startPositionX);
                treesInRange.add(aTree);
            }
        }

        if (existingTrees.isEmpty()) {
            existingTrees.addAll(treesInRange);
        }
        // if we want to add at beginning of list
        else if (existingTrees.get(0).getTopLeftCorner().x() > maxX) {
            existingTrees.addAll(0, existingTrees);
        }
        // if we want to add to end of list
        else if (existingTrees.getLast().getTopLeftCorner().x() < minX) {
            existingTrees.addAll(existingTrees.size(), treesInRange);
        }

    }

    /**
     * Removes all the trees from the game starting from startingPoint going in a specified direction and
     * updates the field containing the current existing trees in accordance.
     *
     * @param startingSpot  x coordinate in the game to start removing trees from
     * @param removeToRight boolean, if true then removing from startingPoint going towards the right
     *                      (increasing x values), if false then starting from startingPoint and going to
     *                      the left (decreasing x values).
     */
    public void deleteInRange(int startingSpot, Boolean removeToRight) {
        //new existingTrees linkedList to hold the trees that were not deleted
        LinkedList<SingleTree> newList = new LinkedList<>();

        // remove all objects to the left of the starting point from the game
        if (!removeToRight) {
            for (SingleTree tree : existingTrees) {

                if (tree.getTopLeftCorner().x() <= startingSpot) {
                    removeObjectsInColumnFromGame(tree);
                } else {
                    newList.add(tree);
                }
            }
        }

        // remove all objects to the right of the starting point from the linked list
        else {
            for (SingleTree tree : existingTrees) {

                if (tree.getTopLeftCorner().x() >= startingSpot) {
                    removeObjectsInColumnFromGame(tree);
                } else {
                    newList.add(tree);
                }
            }
        }
        this.existingTrees = newList;
    }


    //######## private methods ########

    /**
     * Defines all the params required for sprouting a SingleTree in startingPosition, creates the tree and
     * adds it to the game.
     *
     * @param treeRandom     random object of this specific tree, which is only used for randomizing things for
     *                       this tree.
     * @param startPositionX x coordinate of the left edge of the tree's stump.
     * @return the SingleTree created
     */
    private SingleTree sproutATree(Random treeRandom, int startPositionX) {
        int stumpHeight = treeRandom.nextInt(TreeConfiguration.MAX_TREE_STUMP_HEIGHT);
        if (stumpHeight <= TreeConfiguration.DEFAULT_STUMP_WIDTH * 3) {
            stumpHeight = TreeConfiguration.DEFAULT_STUMP_HEIGHT;
        }
        float startPositionY = yCoordinateCallback.apply((float) startPositionX);

        Vector2 location = new Vector2(startPositionX, startPositionY - stumpHeight);

        Vector2 stumpSize = new Vector2(TreeConfiguration.DEFAULT_STUMP_WIDTH, stumpHeight);
        int treetopRadius = (int) (Math.ceil(stumpHeight / 3f));

        SingleTree aTree = createSingleTree(location, stumpSize, treetopRadius, treeRandom);
        gameObjects.addGameObject(aTree, TreeConfiguration.TREE_LAYER);
        return aTree;
    }
    //todo: did the screen coordinates change? where is (0,0)? we need to explain this in the readme.


    /**
     * Creates a SingleTree object, with leafs, at location and according to dimensions
     *
     * @param topLeftCorner   location to start tree from
     * @param stumpDimensions stump dimensions (width, height)
     * @param treetopRadius   treetop possible radius
     * @return the SingleTree created
     */
    private SingleTree createSingleTree(Vector2 topLeftCorner, Vector2 stumpDimensions,
                                        int treetopRadius, Random randomPerX) {
        Vector2 treetopCenter =
                new Vector2(topLeftCorner.x() + stumpDimensions.x() * 0.5f - TreeConfiguration.LEAF_SIZE * 0.5f,
                        topLeftCorner.y());
        SingleTree tree = new SingleTree(topLeftCorner, stumpDimensions, gameObjects,
                treetopCenter, treetopRadius, randomPerX, leafFactory);
        return tree;
    }

    /**
     * Removes a SingleTree from the game (the leafs and stump of it)
     *
     * @param tree SingleTree to remove from the game
     */
    private void removeObjectsInColumnFromGame(SingleTree tree) {
        tree.removeLeafs();
        gameObjects.removeGameObject(tree, TreeConfiguration.TREE_LAYER);
    }

}
