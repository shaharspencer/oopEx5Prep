package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.util.configurations.AvatarConfiguration;
import pepse.util.configurations.BlockConfiguration;
import pepse.util.configurations.NoiseGenerator;
import pepse.util.configurations.TerrainConfiguration;

import java.util.*;

/**
 * Responsible for the creation and management of terrain.
 */
public class Terrain {
    //######## static fields ########
    private static final double X_NOISE_FACTOR = 0.01;
    private static final int BASIC_HEIGHT = BlockConfiguration.SIZE * 12;
    private final Vector2 vectorZero;

    //######## public fields ########
    public LinkedList<ArrayList<Block>> blockColumns = new LinkedList<>();

    //######## private fields ########
    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;
    private final int groundLayer;
    private final int seed;
    private final NoiseGenerator noiseGenerator = new NoiseGenerator();
    private final BlockFactory blockFactory;

    //######## public methods ########

    /**
     * Constructor of Terrain
     * @param gameObjects - The collection of all participating game objects.
     * @param groundLayer - The number of the layer to which the created ground objects should be added.
     * @param windowDimensions - The dimensions of the windows.
     * @param seed - A seed for a random number generator.
     */
    public Terrain(danogl.collisions.GameObjectCollection gameObjects, int groundLayer,
                   danogl.util.Vector2 windowDimensions, int seed){
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.seed = seed;
        this.blockFactory = new BlockFactory();
        //todo: maybe not use the avatar configuration?
        Vector2 avatarLocation = AvatarConfiguration.initialAvatarLocation;
        this.vectorZero = windowDimensions.mult(0.5f).add(avatarLocation.mult(-1f));

    }

    /**
     * This method return the ground height at a given location.
     * Parameters:
     * @param x - the location (any float)
     * @return The ground height at the given location x.
     */
    public float groundHeightAt(float x){
        noiseGenerator.setSeed(Objects.hash(x, seed));
        double noise = noiseGenerator.noise(X_NOISE_FACTOR *x);

        double distFromFloor = Math.abs(BASIC_HEIGHT * noise);

        if (distFromFloor < BlockConfiguration.SIZE){
            distFromFloor = BlockConfiguration.SIZE;
        }

        int distFromFloor_dividableBySize = (int) (Math.floor((distFromFloor +
                vectorZero.y())/ BlockConfiguration.SIZE)
                * BlockConfiguration.SIZE);
        return windowDimensions.y() - distFromFloor_dividableBySize;
    }

    /**
     * This method deletes terrain in a given range of x-values.
     * @param startingSpot - where we start removing from
     * @param removeToRight - whether to remove to right of starting point or to the left
     */
    public void deleteInRange(int startingSpot, Boolean removeToRight) {

        if (!removeToRight){
            removeStartingRight(startingSpot);
        }

        else{
            removeStartingLeft(startingSpot);
        }

    }

    /**
     * This method creates terrain in a given range of x-values.
     * Creation is performed by columns, using createBlocksColumn.
     * Parameters:
     * @param minX - The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX){

        int lowerBound = minX;
        int upperBound = maxX;
        //todo: check if HashSet is better here instead of the internal ArrayList, and check if LinkedList
        // or linked hash set is better then the outer ArrayList.
        ArrayList<ArrayList<Block>> rangeBlocks = new ArrayList<>();
        for (int x = lowerBound; x <= upperBound + BlockConfiguration.SIZE; x += BlockConfiguration.SIZE){
            int y = (int) groundHeightAt(x);
            Vector2 topLeftCorner = new Vector2(x ,y);
            rangeBlocks.add(createBlocksColumn(topLeftCorner));
        }
        addToBlockColumns(rangeBlocks, minX, maxX);
    }

    //######## private methods ########

    /**
     * remove all objects to right of strarting point
     * @param startingSpot minX
     */
    private void removeStartingLeft(int startingSpot) {
        LinkedList<ArrayList<Block>> newList= new LinkedList<>();
        for (ArrayList<Block> blockColumn: blockColumns){

            if (blockColumn.get(0).getTopLeftCorner().x() >= startingSpot){
                removeObjectsInColumnFromGame(blockColumn);
            }
            else{
                newList.add(blockColumn);
            }
        }
        this.blockColumns = newList;
    }

    /**
     * remove all objects to left of strarting point
     * @param startingSpot maxX
     */
    private void removeStartingRight(int startingSpot) {
        LinkedList<ArrayList<Block>> newList= new LinkedList<>();
        for (ArrayList<Block> blockColumn: blockColumns){

            if (blockColumn.get(0).getTopLeftCorner().x() <= startingSpot){
                removeObjectsInColumnFromGame(blockColumn);
            }

            else{
                newList.add(blockColumn);
            }
        }
        this.blockColumns = newList;
    }

    /**
     * removes all blocks in a blockColumn from game
     * @param blockColumn column to remove objects in
     */
    private void removeObjectsInColumnFromGame(ArrayList<Block> blockColumn) {
        for (Block block: blockColumn){
            gameObjects.removeGameObject(block, TerrainConfiguration.getTopBlockLayer());
            //todo: remember that the blocks are still in memory but when we add them back we create them
            // from scratch. same with trees.
        }
    }

    /**
     * adds to linked list of blocks according to proper placement
     * @param rangeBlocks list of block columns to add
     * @param minX minimal x range in which blocks were created
     * @param maxX maximal x range in which blocks were created
     */
    private void addToBlockColumns(ArrayList<ArrayList<Block>> rangeBlocks,
                                   int minX, int maxX) {
        if (blockColumns.isEmpty()){
            blockColumns.addAll(rangeBlocks);
        }
        // if we want to add at beggining of list
        else if (blockColumns.get(0).get(0).getTopLeftCorner().x() >= maxX){
            blockColumns.addAll(0, rangeBlocks);
        }
        // if we want to add to end of list
        else if (blockColumns.getLast().get(0).getTopLeftCorner().x() <= minX){
            blockColumns.addAll(blockColumns.size(), rangeBlocks);
        }
    }


    /**
     * Given a top left corner of a block create a pile of floor blocks up to the top left corner.
     * if we are creating top 3 blocks, put them in layer in which the clash with the avatar
     * else put in background
     * @param topLeftCorner the top left corner of the top block in the column.
     */
    private ArrayList<Block> createBlocksColumn(Vector2 topLeftCorner){

        ArrayList<Block> columnList = new ArrayList<>();
        for (int curBlockTopLeftYCoor = (int) (windowDimensions.y() - BlockConfiguration.SIZE);
             curBlockTopLeftYCoor >= topLeftCorner.y() ; curBlockTopLeftYCoor -= BlockConfiguration.SIZE){
            Block block = blockFactory.generateBlock(
                    new Vector2(topLeftCorner.x(), curBlockTopLeftYCoor));
            if (isCollisionBlock(curBlockTopLeftYCoor, topLeftCorner.y(), topLeftCorner.x())){
                gameObjects.addGameObject(block, TerrainConfiguration.TOP_BLOCKS_LAYER);
                block.setTag(TerrainConfiguration.TOP_BLOCK_TAG);
            }

            else{
                gameObjects.addGameObject(block, TerrainConfiguration.DEFAULT_BLOCKS_LAYER);
                block.setTag(TerrainConfiguration.LOW_BLOCK_TAG);
            }

            columnList.add(block);
        }

        return columnList;
    }

    /**
     * Checks if the block positioned at (x,y) needs to be in a layer that objects collide with or not.
     * @param y - y coordinate of the block to check about
     * @param columnMinY - min y coordinate of this blocks' column (the coordinate of the top most block in
     *                   the column)
     * @param x - x coordinate of the block, and of the entire column
     * @return
     */
    private boolean isCollisionBlock(int y, float columnMinY, float x) {
        boolean cond1 = y < columnMinY + TerrainConfiguration.TOP_BLOCK_FACTOR * BlockConfiguration.SIZE;
        boolean cond2 = (groundHeightAt(x - BlockConfiguration.SIZE) > columnMinY) &&
                (groundHeightAt(x - BlockConfiguration.SIZE) > y);
        boolean cond3 = (groundHeightAt(x + BlockConfiguration.SIZE) > columnMinY) &&
                (groundHeightAt(x + BlockConfiguration.SIZE) > y);
        return (cond1 || cond2 || cond3);
    }
}