package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import pepse.util.configurations.AvatarConfiguration;
import pepse.util.configurations.TerrainConfiguration;

import java.awt.event.KeyEvent;

import java.io.File;
import java.util.function.Function;

public class Avatar extends GameObject {
    //######## private fields ########
    private static final double TIME_BETWEEN_CLIPS = 1;
    private static final float ENERGY_FACTOR = 0.5F;
    private static GameObjectCollection gameObjects;
    private final AnimationRenderable standingAnimation;
    private final AnimationRenderable rightAnimation;
    private final AnimationRenderable upAnimation;
    private final AnimationRenderable leftAnimation;
    private Mode mode;
    private UserInputListener inputListener;
    private Function<Float, Float> terrainCallback;
    private float energyLevel = 100;
    private NumericLifeCounter energyCounter;

    private enum Mode {FLYING, REST, NOT_FLYING};

    //######## public methods ########

    /**
     * Constructor for an avatar
     * @param pos position to set avatar in
     * @param inputListener to register user input for avatar movements
     * @param imageReader image reader to read the images of the avatar
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {

        super(pos, Vector2.ONES.mult(50f), new AnimationRenderable(getAvatarConfigsStanding(),
                imageReader, true, TIME_BETWEEN_CLIPS));


        this.inputListener = inputListener;
        this.rightAnimation = new AnimationRenderable(
                getAvatarConfigsRight(),
                imageReader, true, TIME_BETWEEN_CLIPS);
        this.upAnimation = new AnimationRenderable(getAvatarConfigsUp(),
                imageReader, true, TIME_BETWEEN_CLIPS);

        this.leftAnimation = new AnimationRenderable(getAvatarConfigsLeft(),
                imageReader, true, TIME_BETWEEN_CLIPS);

        this.standingAnimation = new AnimationRenderable(getAvatarConfigsStanding(),
                imageReader, true, TIME_BETWEEN_CLIPS);
        setPhysics();
        this.mode = Mode.REST;
    }

    /**
     * on collision with something make avatar velocity zero on some axis
     * so it does not dive into trees and blocks
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.mode = Mode.REST;

        // if we crashed into something lower than us, set y velocity to be 0
        if (other.getCenter().y() > this.getCenter().y()) {
            setVelocity(new Vector2(this.getVelocity().x(), 0));
        }
        // if we definetly crashed into a block, set velocity = 0
        if (other.getTag().equals(TerrainConfiguration.TOP_BLOCK_TAG)) {
            setVelocity(new Vector2(0, 0));

        }
        // else set x velocity to be 0

        else {
            setVelocity(new Vector2(0, this.getVelocity().y()));
        }

    }


    /**
     * Updates the Avatar
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (this.getVelocity().y() > AvatarConfiguration.MAX_Y_VELOCITY) {
            this.setVelocity(Vector2.of(this.getVelocity().x(), AvatarConfiguration.MAX_Y_VELOCITY));
        } else if (this.getVelocity().y() < AvatarConfiguration.MIN_Y_VELOCITY) {
            this.setVelocity(Vector2.of(this.getVelocity().x(), AvatarConfiguration.MIN_Y_VELOCITY));
        }

        if (this.mode.equals(Mode.REST)) {
            if (this.energyLevel < AvatarConfiguration.initialAvatarEnergyLevel) {
                this.energyLevel += ENERGY_FACTOR;
            }
        } else if (this.mode.equals(Mode.FLYING)) {
            if (this.energyLevel > 0) {
                this.energyLevel -= ENERGY_FACTOR;
                transform().setAccelerationY(AvatarConfiguration.GRAVITY);
            }
        }

        respondToPressedKey();
    }


    public void setTerrainCallback(Function<Float, Float> yCoordinateCallback) {
        this.terrainCallback = yCoordinateCallback;
    }


    public float getEnergyLevel() {
        return this.energyLevel;
    }

    //######## static methods ########

    /**
     * This function creates an avatar that can travel the world and is followed by the camera.
     * The can stand, walk, jump and fly, and never reaches the end of the world.
     * Parameters:
     * gameObjects - The collection of all participating game objects.
     * layer - The number of the layer to which the created avatar should be added.
     * topLeftCorner - The location of the top-left corner of the created avatar.
     * inputListener - Used for reading input from the user.
     * imageReader - Used for reading images from disk or from within a jar.
     * Returns:
     * A newly created representing the avatar.
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
        Avatar.gameObjects = gameObjects;
        Vector2 avatarPlacement = AvatarConfiguration.initialAvatarLocation;

        return new Avatar(avatarPlacement, inputListener, imageReader);
    }



    /**
     * returns a list of strings representing paths to avatar renderables for standing
     *
     * @return String[] renderables
     */
    public static String[] getAvatarConfigsStanding() {

        File dir =
                new File(AvatarConfiguration.AVATAR_IMAGE_FOLDER_PATH + AvatarConfiguration.AVATAR_IMAGE_STANDING_FOLDER);

        File[] directoryListing = dir.listFiles();

        assert directoryListing != null;
        String[] DirImages = new String[directoryListing.length];
        int i = 0;
        for (File child : directoryListing) {
            DirImages[i] = child.getAbsolutePath();
            i += 1;
        }
        return DirImages;
    }


    /**
     * returns a list of strings representing paths to avatar renderables for going right
     *
     * @return String[] renderables
     */
    public static String[] getAvatarConfigsRight() {

        File dir =
                new File(AvatarConfiguration.AVATAR_IMAGE_FOLDER_PATH +
                        AvatarConfiguration.AVATAR_IMAGE_RIGHT_FOLDER);

        File[] directoryListing = dir.listFiles();

        assert directoryListing != null;
        String[] DirImages = new String[directoryListing.length];
        int i = 0;
        for (File child : directoryListing) {
            DirImages[i] = child.getAbsolutePath();
            i += 1;
        }
        return DirImages;
    }

    /**
     * returns a list of strings representing paths to avatar renderables for jumping
     *
     * @return String[] renderables
     */

    public static String[] getAvatarConfigsUp() {

        File dir =
                new File(AvatarConfiguration.AVATAR_IMAGE_FOLDER_PATH +
                        AvatarConfiguration.AVATAR_IMAGE_UP_FOLDER);
        File[] directoryListing = dir.listFiles();

        assert directoryListing != null;
        String[] DirImages = new String[directoryListing.length];
        int i = 0;
        for (File child : directoryListing) {
            DirImages[i] = child.getAbsolutePath();
            i += 1;
        }
        return DirImages;
    }

    /**
     * returns a list of strings representing paths to avatar renderables for going left
     *
     * @return String[] renderables
     */

    public static String[] getAvatarConfigsLeft() {

        File dir = new File(AvatarConfiguration.AVATAR_IMAGE_FOLDER_PATH +
                AvatarConfiguration.AVATAR_IMAGE_LEFT_FOLDER);

        File[] directoryListing = dir.listFiles();

        assert directoryListing != null;
        String[] DirImages = new String[directoryListing.length];
        int i = 0;
        for (File child : directoryListing) {
            DirImages[i] = child.getAbsolutePath();
            i += 1;
        }
        return DirImages;
    }


    //######## private methods ########

    /**
     * sets avatar physics
     */
    private void setPhysics() {
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(AvatarConfiguration.GRAVITY);
    }

    /**
     * responds to pressed key using an appropriate function
     */
    private void respondToPressedKey() {
        turnRightLeftOrStraight();

        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                inputListener.isKeyPressed(KeyEvent.VK_DOWN)) {
            this.mode = Mode.NOT_FLYING;

            upAnddownArePressed();
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            upKeyIsPressed();
            this.mode = Mode.NOT_FLYING;
        } else if (this.getVelocity() == Vector2.ZERO) {
            noKeyIsPressedAndAvatarIsStanding();
            this.mode = Mode.REST;
        }

        flyingControllor();
    }

    private void turnRightLeftOrStraight() {
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            leftKeyIsPressed();
        } else if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            rightkeyIsPressed();
        } else {
            this.renderer().setRenderable(this.upAnimation);
        }
    }

    /**
     * this functions controlls flting of avatar.
     * if shift and space are pressed, calls fly()
     * else sets to not flying mode
     */

    private void flyingControllor() {
        if (inputListener.isKeyPressed(KeyEvent.VK_SHIFT) &&
                inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            fly();
        } else {
            setNotFlying();
        }
    }

    /**
     * this function sets all appropriate fields to not flying mode
     */
    private void setNotFlying() {
        transform().setAccelerationY(AvatarConfiguration.GRAVITY);
        if (this.mode.equals(Mode.REST)) {
            return;
        }
        this.mode = Mode.NOT_FLYING;
    }

    /**
     * try to start flying
     * if we can't because energy level is zero, set to not flying mode
     */
    private void fly() {
        if (this.energyLevel <= 0) {
            setNotFlying();
        } else {
            transform().setAccelerationY(0);
            this.mode = Mode.FLYING;
            this.renderer().setRenderable(this.upAnimation);
        }
    }

    private void noKeyIsPressedAndAvatarIsStanding() {
        this.renderer().setRenderable(standingAnimation);
        this.setDimensions((Vector2.ONES.mult(50f)));
        this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
    }


    /**
     * when space key is pressed sends the avatar flying up and changes renderable to flying renderable
     */

    private void upKeyIsPressed() {
        transform().setVelocityY(AvatarConfiguration.VELOCITY_Y);
        transform().setVelocityX(0);
        this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        this.renderer().setRenderable(upAnimation);
        this.setDimensions((Vector2.ONES.mult(50f)));
    }

    /**
     * deals with case when both up and down are pressed and controlls the movement
     */

    private void upAnddownArePressed() {
        physics().preventIntersectionsFromDirection(null);
        new ScheduledTask(this, .5f, false,
                () -> physics().preventIntersectionsFromDirection(Vector2.ZERO));
    }

    /**
     * when right key is pressed sets a positive velocity and changes the renderable
     */

    private void rightkeyIsPressed() {
        int xVel = 0;
        xVel += AvatarConfiguration.VELOCITY_X;
        transform().setVelocityX(xVel);
        this.renderer().setRenderable(rightAnimation);
        this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        this.setDimensions((Vector2.ONES.mult(50f)));
    }

    /**
     * when right key is pressed sets a negative velocity and changes the renderable
     */

    private void leftKeyIsPressed() {
        int xVel = 0;
        xVel -= AvatarConfiguration.VELOCITY_X;
        transform().setVelocityX(xVel);
        this.renderer().setRenderable(leftAnimation);
        this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        this.setDimensions((Vector2.ONES.mult(50f)));
    }

}
