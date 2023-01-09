package pepse.world;

import danogl.util.Vector2;
import pepse.util.configurations.BlockConfiguration;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * This class is in charge of creating the infinite world, controlling the creation and deletion of the
 * game objects in it as the avatar moves.
 */
public class InfiniteWorldManager {
    //######## private fields ########

    //the size of the world that exists at any time during the game is windowSixe + 2*Buffer
    private final int EXISTING_WORLD_SIZE_BUFFER;
    //used to define the buffer size
    private static final double BUFFER_FACTOR = 0.75;
    private final BiConsumer<Integer, Integer> terrainCreator;
    private final BiConsumer<Integer, Boolean> blockDeletor;
    private final BiConsumer<Integer, Integer> treeCreator;
    private final BiConsumer<Integer, Boolean> treeDeletor;
    //callback to avatar.getCenter method
    private final Supplier<Vector2> avatarLocation;
    private final Vector2 windowDimensions;
    private int lastBlockToRight;
    private int lastBlockToLeft;

    //######## public methods ########

    /**
     * initializes an infinite world object
     *
     * @param terrainCreator:  given a minX and maxX range, creates terrain in that range
     * @param treeCreator:     given a minX and maxX range, creates trees in that range
     * @param avatarLocation   returns a vector2 of the avatar's current location
     * @param windowDimensions the dimensions of the screen
     */
    public InfiniteWorldManager(BiConsumer<Integer, Integer> terrainCreator,
                                BiConsumer<Integer, Boolean> blockDeletor,
                                BiConsumer<Integer, Integer> treeCreator,
                                BiConsumer<Integer, Boolean> treeDeletor,
                                Supplier<Vector2> avatarLocation, Vector2 windowDimensions) {
        this.terrainCreator = terrainCreator;
        this.blockDeletor = blockDeletor;
        this.treeCreator = treeCreator;
        this.treeDeletor = treeDeletor;
        this.avatarLocation = avatarLocation;
        this.windowDimensions = windowDimensions;

        EXISTING_WORLD_SIZE_BUFFER = roundToNearestToBlockSize((float) BUFFER_FACTOR *
                windowDimensions.x());
        initializeAvatarLocationAndObjects();
    }

    /**
     * this function is in charge of adding and removing game objects
     * according to the avatar's location
     */
    public void updateByAvatarLocation() {
        int avatarNewLocation = (int) avatarLocation.get().x();
        // extend world on right side and delete left

        if (avatarNewLocation > lastBlockToRight - EXISTING_WORLD_SIZE_BUFFER) {
            updateDueToMoveRight();
        }
        // extend world on left side and delete right
        else if (avatarNewLocation < lastBlockToLeft + EXISTING_WORLD_SIZE_BUFFER) {
            updateDueToMoveLeft();
        }
    }

    //######## private methods ########

    /**
     * given an int round to the nearest multiple of block size (30)
     *
     * @param num number to round
     * @return rounded number (rounded down)
     */
    private int roundToNearestToBlockSize(float num) {
        return (int) (Math.floor((num / BlockConfiguration.SIZE)) * BlockConfiguration.SIZE);
    }

    /**
     * initializes world according to avatar location
     * defines the current right-most and left-most ground blocks
     */
    private void initializeAvatarLocationAndObjects() {
        this.lastBlockToLeft = roundToNearestToBlockSize(-EXISTING_WORLD_SIZE_BUFFER);
        this.lastBlockToRight = roundToNearestToBlockSize(windowDimensions.x() + EXISTING_WORLD_SIZE_BUFFER);
        createObjectsInInRange(lastBlockToLeft, lastBlockToRight);
    }

    /**
     * when the avatar moved left enough, we want to create the world to the left
     * and delete world to the right
     */
    private void updateDueToMoveLeft() {
        //create additional world according to the avatar movement:
        int createEndPosition = lastBlockToLeft - BlockConfiguration.SIZE;
        int createStartPosition = createEndPosition - EXISTING_WORLD_SIZE_BUFFER;

        createObjectsInInRange(createStartPosition, createEndPosition);
        this.lastBlockToLeft = createStartPosition;

        //delete world objects according to the avatar movement:
        int deleteStartPosition = lastBlockToRight - EXISTING_WORLD_SIZE_BUFFER;
        deleteObjectsOnRightSide(deleteStartPosition);
        if (lastBlockToRight > deleteStartPosition) {
            this.lastBlockToRight = deleteStartPosition - BlockConfiguration.SIZE;
        }
    }

    /**
     * deletes all objects from this point to the right
     *
     * @param startPosition point to delete from
     */
    private void deleteObjectsOnRightSide(int startPosition) {
        blockDeletor.accept(startPosition, true);
        treeDeletor.accept(startPosition, true);
    }


    /**
     * when the avatar moved right enough, we want to create the world to the right
     * and delete world to the left
     */
    private void updateDueToMoveRight() {
        //create additional world according to the avatar movement:
        int createStartPosition = lastBlockToRight + BlockConfiguration.SIZE;
        int createEndPosition = createStartPosition + EXISTING_WORLD_SIZE_BUFFER - BlockConfiguration.SIZE;

        createObjectsInInRange(createStartPosition, createEndPosition);
        this.lastBlockToRight = createEndPosition + BlockConfiguration.SIZE;

        //delete world objects according to the avatar movement:
        int deleteStartPosition = lastBlockToLeft + EXISTING_WORLD_SIZE_BUFFER;
        deleteObjectsOnLeftSide(deleteStartPosition);
        if (lastBlockToLeft < deleteStartPosition) {
            this.lastBlockToLeft = deleteStartPosition + BlockConfiguration.SIZE;
        }
    }

    /**
     * deletes all objects from this point to the left
     *
     * @param startPosition point to delete from
     */
    private void deleteObjectsOnLeftSide(int startPosition) {
        blockDeletor.accept(startPosition, false);
        treeDeletor.accept(startPosition, false);
    }


    /**
     * this function creates all objects in the range given
     */
    private void createObjectsInInRange(int minX, int maxX) {
        terrainCreator.accept(minX, maxX);
        treeCreator.accept(minX, maxX);
    }

}
