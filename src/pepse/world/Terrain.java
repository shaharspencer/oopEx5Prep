package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;
import pepse.util.TerrainConfiguration;
import pepse.util.TreeConfiguration;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Responsible for the creation and management of terrain.
 */
public class Terrain {
    private static final double X_FACTOR = 0.01;
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private static Vector2 windowDimensions;
    private final int seed;

    public LinkedList<ArrayList<Block>> blockColumns = new LinkedList<>();


    private final NoiseGenerator noiseGenerator;
    private static int BASIC_HEIGHT = Block.SIZE * 12;

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    private final BlockFactory blockFactory;

    /**
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
        this.noiseGenerator = new NoiseGenerator(seed);
        this.blockFactory = new BlockFactory();
    }

    /**
     * This method return the ground height at a given location.
     * Parameters:
     * @param x - A number.
     * @return The ground height at the given location.
     */
    public float groundHeightAt(float x){
//
//        noiseGenerator.setSeed(Objects.hash(x, seed));
        noiseGenerator.setSeed(Objects.hash(x, seed));
        double noise = noiseGenerator.noise(X_FACTOR*x);

        double distFromFLoor = Math.abs(BASIC_HEIGHT * noise);

        if (distFromFLoor < Block.SIZE){
            distFromFLoor = Block.SIZE;
        }

        int distFromFloor_dividableBySize = (int) (Math.floor((distFromFLoor +
                (float) PepseGameManager.VECTOR_ZERO.y())/ Block.SIZE)
                * Block.SIZE);
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
        }
    }

    /**
     * This method creates terrain in a given range of x-values.
     * Parameters:
     * @param minX - The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX){

        int lowerBound = minX;
        int upperBound = maxX;

        ArrayList<ArrayList<Block>> rangeBlocks = new ArrayList<>();
        for (int x = lowerBound; x <= upperBound + Block.SIZE; x += Block.SIZE){
            int y = (int) groundHeightAt(x);
            Vector2 topLeftCorner = new Vector2(x ,y);
            rangeBlocks.add(createInYRange(topLeftCorner));
        }
        addToBlockColumns(rangeBlocks, minX, maxX);
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


    /** given a top left corner of a block
     * create a pile of blocks until the floor
     * if we are creating top 3 blocks, put them in layer in which the clash with the avatar
     * else put in background
     * @param topLeftCorner the top left corner of the top block
     */

    public ArrayList<Block> createInYRange(Vector2 topLeftCorner){

        ArrayList<Block> columnList = new ArrayList<>();
        float yCoord = topLeftCorner.y();

        for (int i = (int) (windowDimensions.y() - Block.SIZE); i >= yCoord ; i -= Block.SIZE){
            Block block = blockFactory.generateBlock(
                    new Vector2(
                            topLeftCorner.x(),
                            i));
            // if this is one of three top blocks
            if (i< yCoord + TerrainConfiguration.TOP_BLOCK_FACTOR * Block.SIZE ){
                gameObjects.addGameObject(block, TerrainConfiguration.getTopBlockLayer());
                block.setTag(TerrainConfiguration.TOP_BLOCK_TAG);
            }
            else{
                gameObjects.addGameObject(block, TerrainConfiguration.getDefaultBlocksLayer());
                block.setTag(TerrainConfiguration.LOW_BLOCK_TAG);
            }

            columnList.add(block);
        }

        return columnList;
    }
}