package pepse.util;

import danogl.collisions.Layer;

import java.awt.*;

public class TreeConfiguration {
    /* leaf definitions */
    public static String LEAF_TAG = "leaf";
    public static int LEAF_LAYER =  Layer.BACKGROUND + 3;

    public static int[] LEAF_DIM = new int[]{13, 15, 17};

    public static Color[] LEAF_COLORS = new Color[]{Color.GREEN};

    public static int LEAF_FALL_VELOCITY = 50;

    public static int MAX_LEAF_FALL_TIME = 10;

    public static int MAX_LEAF_DEATH_TIME = 16;

    public static int LEAF_FADEOUT_TIME = 5;

    public static int LEAF_MAX_X_VELOCITY = 10;

    public static int LEAF_X_TRANSITION_TIME = 2;

}
