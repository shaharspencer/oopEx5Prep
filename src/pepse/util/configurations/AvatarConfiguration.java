package pepse.util.configurations;

import danogl.collisions.Layer;
import danogl.util.Vector2;

public class AvatarConfiguration {

    public static final int MAX_Y_VELOCITY = 300;
    public static final int MIN_Y_VELOCITY = -400;
    public static final String AVATAR_IMAGE_STANDING_FOLDER = "standing";
    public static int initialAvatarEnergyLevel = 100;
    public static Vector2 initialAvatarLocation = new Vector2(0, 0);
    public static int AVATAR_LAYER = Layer.UI;
    public static final String AVATAR_IMAGE_FOLDER_PATH = "./src/assets/retro_man/";
    public static final float VELOCITY_X = 250;
    public static final float VELOCITY_Y = -400;
    public static final float GRAVITY = 500;
    public static final String AVATAR_IMAGE_RIGHT_FOLDER = "right";
    public static final String AVATAR_IMAGE_UP_FOLDER = "up";
    public static final String AVATAR_IMAGE_LEFT_FOLDER = "left";
}
