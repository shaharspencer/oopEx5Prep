package pepse.util.configurations;

import danogl.collisions.Layer;
import danogl.util.Vector2;

import java.awt.*;

public class GameManagerConfiguration {
    public static final int SEED = 0;
    public static final String WINDOW_TITLE = "";
    public static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    public static Vector2 WINDOW_SIZE = new Vector2(700, 500);

    public static final int GROUND_LAYER = Layer.STATIC_OBJECTS;
    public static final int TARGET_FRAMERATE = 70;
    public static int SKY_LAYER = Layer.BACKGROUND;
    public static final int LAYERS_DIFF = 1;
    public static int SUN_LAYER = SKY_LAYER + LAYERS_DIFF;
    public static int HALO_LAYER = SUN_LAYER + LAYERS_DIFF;

    // duration of a single day in the game in seconds
    public static final int DAY_CYCLE_LENGTH = 10;

}
