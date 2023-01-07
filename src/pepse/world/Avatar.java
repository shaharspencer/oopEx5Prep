package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import pepse.util.AvatarConfiguration;
import pepse.util.TerrainConfiguration;

import java.awt.*;
import java.awt.event.KeyEvent;

import java.io.File;

import static pepse.PepseGameManager.SEED;

public class Avatar extends GameObject {
    private static final String AVATAR_IMAGE_FOLDER_PATH = "./src/pepse/util/assets/retro_man/";
    private static final float VELOCITY_X = 250;
    private static final float VELOCITY_Y = -500;
    private static final float GRAVITY = 500;

    private static final String AVATAR_IMAGE_RIGHT_FOLDER = "right";
    private static final String AVATAR_IMAGE_UP_FOLDER = "up";
    private static final String AVATAR_IMAGE_LEFT_FOLDER = "left";
    private final AnimationRenderable rightAnimation;
    private final AnimationRenderable upAnimation;
    private final AnimationRenderable leftAnimation;
    private final Terrain terrain;


    private Avatar avatar;
    private UserInputListener inputListener;


    Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {

        super(pos, Vector2.ONES.mult(50), new AnimationRenderable(getAvatarConfigsRight(),
                imageReader, true, 0.75));

        this.inputListener = inputListener;
        this.rightAnimation = new AnimationRenderable(getAvatarConfigsRight(),
                imageReader, true, 0.75);
        this.upAnimation = new AnimationRenderable(getAvatarConfigsUp(),
                imageReader, true, 0.75);

        this.leftAnimation = new AnimationRenderable(getAvatarConfigsLeft(),
                imageReader, true, 0.75);
        this.terrain = new Terrain(null, 0, Vector2.ZERO, SEED);

        setPhysics();

    }

    /**
     * sets avatar physics
     */
    private void setPhysics() {
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
    }

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
     * returns a list of strings representing paths to avatar renderables for going right
     * @return String[] renderables
     */
    public static String[] getAvatarConfigsRight() {

        File dir = new File(AVATAR_IMAGE_FOLDER_PATH+AVATAR_IMAGE_RIGHT_FOLDER);

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
     * returns a list of strings representing paths to avatar renderables for flying
     * @return String[] renderables
     */

    public static String[] getAvatarConfigsUp() {

        File dir = new File(AVATAR_IMAGE_FOLDER_PATH + AVATAR_IMAGE_UP_FOLDER);
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
     * @return String[] renderables
     */

    public static String[] getAvatarConfigsLeft() {

        File dir = new File(AVATAR_IMAGE_FOLDER_PATH + AVATAR_IMAGE_LEFT_FOLDER);

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
     * on collision with something make avatar velocity zero on some axis
     * so it does not dive into trees and blocks
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
        @Override
        public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // if we crashed into something lower than us, set y velocity to be 0
        if (other.getCenter().y() > this.getCenter().y()){
            setVelocity(new Vector2 (this.getVelocity().x(), 0));
        }
        // if we definetly crashed into a block, set velocity = 0
        if (other.getTag().equals(TerrainConfiguration.TOP_BLOCK_TAG)){
            setVelocity(new Vector2 (0, 0));

        }
        // else set x velocity to be 0

        else{
            setVelocity(new Vector2 (0, this.getVelocity().y()));
        }

        }

    public void update(float deltaTime) {
        super.update(deltaTime);

        respondToPressedKey();

        // set the avatar's minimal y height at groundHeightAt(x) + half of the avatar's body
        float minYPlace = terrain.groundHeightAt(this.getCenter().x()) -
                (float) this.getDimensions().y() /2 + 0.01f;
        if (this.getCenter().y() > minYPlace){
            this.setCenter(new Vector2(this.getCenter().x(), minYPlace));
        }
    }

    /**
     * responds to pressed key using an appropriate function
     */
    private void respondToPressedKey() {

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            leftKeyIsPressed();
        }

        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            rightkeyIsPressed();
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                inputListener.isKeyPressed(KeyEvent.VK_DOWN)) {

            upAnddownArePressed();
            return;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            upKeyIsPressed();
        }
    }

    /**
     * when space key is pressed sends the avatar flying up and changes renderable to flying renderable
     */

    private void upKeyIsPressed() {
        transform().setVelocityY(VELOCITY_Y);
        this.renderer().setRenderable(upAnimation);
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
        xVel += VELOCITY_X;
        transform().setVelocityX(xVel);
        this.renderer().setRenderable(rightAnimation);
    }

    /**
     * when right key is pressed sets a negative velocity and changes the renderable
     */

    private void leftKeyIsPressed() {
        int xVel = 0;
        xVel -= VELOCITY_X;
        transform().setVelocityX(xVel);
        this.renderer().setRenderable(leftAnimation);
    }


}
