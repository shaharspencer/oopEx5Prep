package pepse.util;

import danogl.collisions.Layer;

import java.awt.*;

public class TerrainConfiguration {

    public static String TOP_BLOCK_TAG = "top_block";

    public static int BLOCK_LAYER = Layer.STATIC_OBJECTS;


    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static Color other = new Color(200, 130, 100);
    public static Color otherOther = new Color(210, 123, 74);
    private static Color another = new Color(250, 130, 50);
//    public Color lightBrown = Color.getColor("light brown");

    public static Color[] blockColors = {BASE_GROUND_COLOR, other, another, otherOther};

}
