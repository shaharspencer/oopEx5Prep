package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import pepse.util.AvatarConfiguration;

import java.awt.*;
import java.awt.event.KeyEvent;

import java.io.File;

import static pepse.PepseGameManager.SEED;

public class Avatar extends GameObject {

    private static final String AVATAR_IMAGE_FOLDER_PATH = "./src/pepse/util/AvatarImages/retro_man/";
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final Color AVATAR_COLOR = Color.DARK_GRAY;
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
//     super(pos, Vector2.ONES.mult(50), new OvalRenderable(AVATAR_COLOR));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.rightAnimation = new AnimationRenderable(getAvatarConfigsRight(),
                imageReader, true, 0.75);
        this.upAnimation = new AnimationRenderable(getAvatarConfigsUp(),
                imageReader, true, 0.75);

        this.leftAnimation = new AnimationRenderable(getAvatarConfigsLeft(),
                imageReader, true, 0.75);
        this.terrain = new Terrain(null, 0, Vector2.ZERO, SEED);
    }

    /**
     * This function creates an avatar that can travel the world and is followed by the camera. The can stand, walk, jump and fly, and never reaches the end of the world.
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

    public static String[] getAvatarConfigsRight() {

        File dir = new File(AVATAR_IMAGE_FOLDER_PATH+AVATAR_IMAGE_RIGHT_FOLDER);
        /*
                        "C:\\Users\\User\\IdeaProjects\\oopEx5Prep\\" +
                        "src\\pepse\\util\\AvatarImages\\retro_man\\right");
         */

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

    public static String[] getAvatarConfigsUp() {

        File dir = new File(AVATAR_IMAGE_FOLDER_PATH + AVATAR_IMAGE_UP_FOLDER);
        /*
        C:\\Users\\User\\IdeaProjects\\oopEx5Prep\\src\\pepse\\util\\AvatarImages\\retro_man\\up");

         */
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

    public static String[] getAvatarConfigsLeft() {

        File dir = new File(AVATAR_IMAGE_FOLDER_PATH + AVATAR_IMAGE_LEFT_FOLDER);
        /*

                "C:\\Users\\User\\IdeaProjects\\oopEx5Prep\\" +
                        "src\\pepse\\util\\AvatarImages\\retro_man\\left");
         */
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

    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= VELOCITY_X;
            transform().setVelocityX(xVel);
            this.renderer().setRenderable(leftAnimation);

        }

        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += VELOCITY_X;
            transform().setVelocityX(xVel);
            this.renderer().setRenderable(rightAnimation);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_DOWN)) {
            physics().preventIntersectionsFromDirection(null);
            new ScheduledTask(this, .5f, false,
                    () -> physics().preventIntersectionsFromDirection(Vector2.ZERO));
            return;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            transform().setVelocityY(VELOCITY_Y);
            this.renderer().setRenderable(upAnimation);
        }


        // set the avatar's minimal y height at groundHeightAt(x) + half of the avatar's body
        float minYPlace = terrain.groundHeightAt(this.getCenter().x()) - (float) this.getDimensions().y() /2 + 0.01f;
        if (this.getCenter().y() > minYPlace){
            this.setCenter(new Vector2(this.getCenter().x(), minYPlace));
        }
    }


}
