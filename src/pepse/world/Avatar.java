package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import pepse.util.configurations.AvatarConfiguration;
import pepse.util.configurations.TerrainConfiguration;

import java.awt.event.KeyEvent;

import java.io.File;
import java.util.Arrays;


public class Avatar extends GameObject {
    //######## private fields ########
    private final AnimationRenderable standingAnimation;
    private final AnimationRenderable rightAnimation;
    private final AnimationRenderable upAnimation;
    private final AnimationRenderable leftAnimation;
    private final UserInputListener inputListener;
    private float energyLevel = 100;
    private AvatarConfiguration.movementDirections directionOfMovement =
            AvatarConfiguration.movementDirections.STRAIGHT;
    private AvatarConfiguration.Mode mode;

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
        Vector2 avatarPlacement = AvatarConfiguration.initialAvatarLocation;

        return new Avatar(avatarPlacement, inputListener, imageReader);
    }

    /**
     * returns a list of strings representing paths to avatar renderables according to the path given
     * @param path path for images folder
     * @return String[] renderables paths
     */
    private static String[] getAvatarAnimationsConfigs(String path){
        File dir = new File(path);

        File[] directoryListing = dir.listFiles();

        Arrays.sort(directoryListing);

        String[] DirImages = new String[directoryListing.length];
        int i = 0;
        for (File child : directoryListing) {
            DirImages[i] = child.getAbsolutePath();
            i += 1;
        }
        return DirImages;
    }

    //######## public methods ########

    /**
     * Constructor for an avatar
     * @param pos position to set avatar in
     * @param inputListener to register user input for avatar movements
     * @param imageReader image reader to read the images of the avatar
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {

        super(pos, Vector2.ONES.mult(50f),
                new AnimationRenderable(getAvatarAnimationsConfigs(
                        AvatarConfiguration.AVATAR_IMAGE_FOLDER_PATH +
                                AvatarConfiguration.AVATAR_IMAGE_STANDING_FOLDER),
                imageReader, true, AvatarConfiguration.TIME_BETWEEN_CLIPS));

        this.setTag(AvatarConfiguration.AVATAR_TAG);
        this.inputListener = inputListener;
        this.leftAnimation =
                new AnimationRenderable(getAvatarAnimationsConfigs(
                        AvatarConfiguration.AVATAR_IMAGE_FOLDER_PATH +
                AvatarConfiguration.AVATAR_IMAGE_LEFT_FOLDER),
                imageReader, true, AvatarConfiguration.TIME_BETWEEN_CLIPS);

        this.rightAnimation = new AnimationRenderable(
                getAvatarAnimationsConfigs(AvatarConfiguration.AVATAR_IMAGE_FOLDER_PATH +
                        AvatarConfiguration.AVATAR_IMAGE_RIGHT_FOLDER),
                imageReader, true, AvatarConfiguration.TIME_BETWEEN_CLIPS);

        this.upAnimation = new AnimationRenderable(getAvatarAnimationsConfigs(
                AvatarConfiguration.AVATAR_IMAGE_FOLDER_PATH +
                AvatarConfiguration.AVATAR_IMAGE_UP_FOLDER),
                imageReader, true, AvatarConfiguration.TIME_BETWEEN_CLIPS);

        this.standingAnimation = new AnimationRenderable(getAvatarAnimationsConfigs(
                AvatarConfiguration.AVATAR_IMAGE_FOLDER_PATH +
                        AvatarConfiguration.AVATAR_IMAGE_STANDING_FOLDER),
                imageReader, true, AvatarConfiguration.TIME_BETWEEN_CLIPS);
        setPhysics();
        this.mode = AvatarConfiguration.Mode.REST;
    }

    /**
     * on collision with something make avatar velocity zero on some axis
     * so it does not dive into trees and blocks
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.mode = AvatarConfiguration.Mode.REST;

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

        if (this.mode.equals(AvatarConfiguration.Mode.REST)) {
            if (this.energyLevel < AvatarConfiguration.initialAvatarEnergyLevel) {
                this.energyLevel += AvatarConfiguration.ENERGY_FACTOR;
            }
        } else if (this.mode.equals(AvatarConfiguration.Mode.FLYING)) {
            if (this.energyLevel > 0) {
                this.energyLevel -= AvatarConfiguration.ENERGY_FACTOR;
            }
        }

        respondToPressedKey();
    }

    public float getEnergyLevel() {
        return this.energyLevel;
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
        //if jump
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            transform().setVelocityY(AvatarConfiguration.VELOCITY_Y);
            this.renderer().setRenderable(upAnimation);
            this.mode = AvatarConfiguration.Mode.NOT_FLYING;

        }
        //fly
        else if (inputListener.isKeyPressed(KeyEvent.VK_SHIFT) &&
                inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
             fly();
        }

        else if (this.getVelocity() == Vector2.ZERO) {
            this.mode = AvatarConfiguration.Mode.REST;
        }

        else{
            if (this.mode.equals(AvatarConfiguration.Mode.REST)) {
                return;
            }
            this.mode = AvatarConfiguration.Mode.NOT_FLYING;
        }
    }

    /**
     * checks if right or left keys were pressed and handles accordingly
     */
    private void turnRightLeftOrStraight() {
        int xVel = 0;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= AvatarConfiguration.VELOCITY_X;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += AvatarConfiguration.VELOCITY_X;
        }

        if (xVel <0 && this.directionOfMovement != AvatarConfiguration.movementDirections.LEFT){
            this.directionOfMovement = AvatarConfiguration.movementDirections.LEFT;
            this.renderer().setRenderable(this.leftAnimation);
        }
        else if (xVel >0 && this.directionOfMovement != AvatarConfiguration.movementDirections.RIGHT){
            this.directionOfMovement = AvatarConfiguration.movementDirections.RIGHT;
            this.renderer().setRenderable(this.rightAnimation);
        }

        else if(xVel == 0 && this.directionOfMovement != AvatarConfiguration.movementDirections.STRAIGHT) {
            this.directionOfMovement = AvatarConfiguration.movementDirections.STRAIGHT;
            this.renderer().setRenderable(this.standingAnimation);
        }

        transform().setVelocityX(xVel);
    }

    /**
     * try to start flying
     * if we can't because energy level is zero, set to not flying mode
     */
    private void fly() {
        if (this.energyLevel > 0) {
            this.transform().setVelocityY(AvatarConfiguration.VELOCITY_Y);
            if (this.mode != AvatarConfiguration.Mode.FLYING) {
                this.mode = AvatarConfiguration.Mode.FLYING;
                this.renderer().setRenderable(this.standingAnimation);
            }
        }
        else{
            if (this.mode.equals(AvatarConfiguration.Mode.REST)) {
                return;
            }
            this.mode = AvatarConfiguration.Mode.NOT_FLYING;
        }
    }

}
