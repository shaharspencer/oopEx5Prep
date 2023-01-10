package pepse.util.configurations;

import danogl.collisions.Layer;
import danogl.util.Vector2;

import java.awt.*;

public class SkyAndDayNightConfiguration {
    public static int SKY_LAYER = Layer.BACKGROUND;
    public static final int LAYERS_DIFF = 1;
    public static int SUN_LAYER = SKY_LAYER + LAYERS_DIFF;
    public static int HALO_LAYER = SUN_LAYER + LAYERS_DIFF;
    public static final String NIGHT_TAG = "night";
    public static final String CLOUD_IMAGE = "./src/assets/clouds.png";
    public static final Vector2 SNOW_SIZE = Vector2.of(30,30);
    public static float NOON_OPACITY = 0f;
    public static float MIDNIGHT_OPACITY = 0.5f;
    public static String SUN_TAG = "sun";
    public static Vector2 SUN_DIMENSIONS = new Vector2(100, 100);
    public static final Color HALO_COLOR = new Color(255, 255, 0, 20);

    public static final String HALO_TAG = "sun halo";
    public static final float HALO_SIZE_FACTOR = 1.5f;

    public static final String SKY_TAG = "sky";
    // the color of the sky
    public static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");;

}
