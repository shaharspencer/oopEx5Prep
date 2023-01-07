package pepse.world;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.PepseGameManager;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * this class is in charge of creating the infinite world
 * and controlling the creation and deletion of it as the avatar moves
 *
 */
public class InfiniteWorld{


    private final int BUFFER;
    private final PepseGameManager peps;
    private final BiConsumer<Integer, Integer> terrainCreator;
    private final BiConsumer<Integer, Boolean> blockDeletor;
    private final BiConsumer<Integer, Integer> treeCreator;
    private final BiConsumer<Integer, Boolean> treeDeletor;
    private final Supplier<Vector2> avatarLocation;
    private final Vector2 windowDimensions;

    private Vector2 currAvatarLocation;
    private int lastBlockToRight;
    private int lastBlockToLeft;


    /**
     * initializes an infinite world object
     * @param terrainCreator: given a minX and maxX range, creates terrain in that range
     * @param treeCreator: given a minX and maxX range, creates trees in that range
     * @param avatarLocation returns a vector2 of the avatar's current location
     * @param windowDimensions the dimensions of the screen
     */
    public InfiniteWorld(PepseGameManager peps, BiConsumer<Integer, Integer> terrainCreator,
                         BiConsumer<Integer, Boolean> blockDeletor,
                         BiConsumer<Integer, Integer> treeCreator,
                         BiConsumer<Integer, Boolean> treeDeletor,
                         Supplier<Vector2> avatarLocation, Vector2 windowDimensions){

        this.peps = peps;

        this.terrainCreator = terrainCreator;
        this.blockDeletor = blockDeletor;
        this.treeCreator = treeCreator;
        this.treeDeletor = treeDeletor;
        this.avatarLocation = avatarLocation;
        this.windowDimensions = windowDimensions;
        BUFFER = roundToNearestToBlockSize ((float) (0.75 * windowDimensions.x()));
        initializeAvatarLocationAndObjects();
    }


    /**
     * given an int round to nearest multiple of block size (30)
     * @param num number to round
     * @return rounded number (rounded down)
     */

    int roundToNearestToBlockSize(float num){
        return (int) (Math.floor((num/ Block.SIZE)) * Block.SIZE);
    }

    /**
     * initializes world according to avatar location
     */
    private void initializeAvatarLocationAndObjects() {

        this.currAvatarLocation = avatarLocation.get();
        this.lastBlockToLeft = roundToNearestToBlockSize(-BUFFER);
        this.lastBlockToRight = roundToNearestToBlockSize(windowDimensions.x()+BUFFER);
        createObjectsinInRange(lastBlockToLeft, lastBlockToRight);

    }

    /**
     * this function is in charge of adding and removing game objects
     * according to the avatar's location
     */
    public void updateByAvatarLocation(){
        int avatarNewLocation = (int) avatarLocation.get().x();
        if (avatarNewLocation > lastBlockToRight - BUFFER)
        {
            updateToRight();
            System.out.println(peps.terrain.blockColumns.size());
        }

        else if (avatarNewLocation < lastBlockToLeft + BUFFER){
            updateToLeft();
            System.out.println(peps.terrain.blockColumns.size());
        }

    }

    /**
     * when the avatar moved left enough, we want to create the world to the left
     * and delete world to the right
     */
    private void updateToLeft() {

        int bufferRightCreate = lastBlockToLeft - Block.SIZE;
        int bufferLeftCreate = bufferRightCreate - BUFFER;

        createObjectsinInRange(bufferLeftCreate, bufferRightCreate);
        this.lastBlockToLeft = bufferLeftCreate;


        int bufferRightDelete = lastBlockToRight -BUFFER;
        deleteObjectsUpdateLeft(bufferRightDelete);
        if (lastBlockToRight > bufferRightDelete){
            this.lastBlockToRight = bufferRightDelete-Block.SIZE;
        }

        this.currAvatarLocation = this.avatarLocation.get();
        System.out.println(this.lastBlockToLeft ==
                this.peps.terrain.blockColumns.getFirst().get(0).getTopLeftCorner().x());
        System.out.println(this.lastBlockToRight ==
                this.peps.terrain.blockColumns.getLast().get(0).getTopLeftCorner().x());
    }
    /**
     * deletes all objects from this point to the right
     * @param bufferRightDelete point to delete from
     */
    private void deleteObjectsUpdateLeft(int bufferRightDelete) {
        blockDeletor.accept(bufferRightDelete, true);
        treeDeletor.accept(bufferRightDelete, true);
    }


    /**
     * when the avatar moved right enough, we want to create the world to the right
     * and delete world to the left
     */
    private void updateToRight() {

        int bufferLeftCreate = lastBlockToRight+ Block.SIZE;
        int bufferRightCreate = bufferLeftCreate + BUFFER - Block.SIZE;

        createObjectsinInRange(bufferLeftCreate, bufferRightCreate);
        this.lastBlockToRight = bufferRightCreate + Block.SIZE;


        int bufferRightDelete = lastBlockToLeft + BUFFER;
        deleteObjectsUpdateRight(bufferRightDelete);
        if (lastBlockToLeft < bufferRightDelete){
            this.lastBlockToLeft = bufferRightDelete + Block.SIZE;
        }

        this.currAvatarLocation = this.avatarLocation.get();

        System.out.println(this.lastBlockToLeft ==
                this.peps.terrain.blockColumns.getFirst().get(0).getTopLeftCorner().x());
        System.out.println(this.lastBlockToRight ==
                this.peps.terrain.blockColumns.getLast().get(0).getTopLeftCorner().x());
    }

    /**
     * deletes all objects from this point to the left
     * @param bufferRightDelete point to delete from
     */

    private void deleteObjectsUpdateRight(int bufferRightDelete) {
        blockDeletor.accept(bufferRightDelete, false);
        treeDeletor.accept(bufferRightDelete, false);
    }


    /**
     * this function creates all objects in the range given
     */

    public void createObjectsinInRange(int minX, int maxX){
        terrainCreator.accept(minX, maxX);
        treeCreator.accept(minX, maxX);
    }

}
