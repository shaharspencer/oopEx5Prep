package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.Random;

/**
 * Responsible for the creation and management of terrain.
 */
public class Terrain {
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private final int seed;

    private final int FACTOR = 10;

    private int BASIC_HEIGHT = Block.SIZE * FACTOR;

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private final NoiseGenerator noiseGenerator;
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
        this.blockFactory = new BlockFactory(new RectangleRenderable(
                ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
    }

    /**
     * This method return the ground height at a given location.
     * Parameters:
     * @param x - A number.
     * @return The ground height at the given location.
     */
    public double groundHeightAt(float x){

        double noise = noiseGenerator.noise(x);

        double ret = Math.abs(BASIC_HEIGHT *
                noise);


        if (ret < Block.SIZE){
            return windowDimensions.y() - Block.SIZE;
        }

        return windowDimensions.y() - ret;

    }

    /**
     * This method creates terrain in a given range of x-values.
     * Parameters:
     * @param minX - The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX){
        int lowerBound = minX;
//        int upperBound = maxX - Block.SIZE;
        int upperBound = maxX;
        for (int x = lowerBound; x <= upperBound; x += Block.SIZE){
            int y = (int) groundHeightAt(x);
            Vector2 topLeftCorner = new Vector2(x ,y);
//            Block block = blockFactory.generateBlock(topLeftCorner);
//            gameObjects.addGameObject(block);
            createInYRange(topLeftCorner);
        }
    }


    /** given a top left corner of a block
     * create a pile of blocks until the floor
     * @param
     */
    public void createInYRange(Vector2 topLeftCorner){
        float yCoord = topLeftCorner.y();

        for (int i = (int) (windowDimensions.y() - Block.SIZE); i >= yCoord - Block.SIZE; i -= Block.SIZE){
            Block block = blockFactory.generateBlock(
                    new Vector2(
                    topLeftCorner.x(),
                    i));
            gameObjects.addGameObject(block);
        }

    }
}
