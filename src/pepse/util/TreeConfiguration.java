package pepse.util;

import danogl.collisions.Layer;

import java.awt.*;

public class TreeConfiguration {
    /* leaf definitions */
    public static String LEAF_TAG = "leaf";
    //todo: check the layers
    public static int LEAF_LAYER =  Layer.BACKGROUND + 201;

    public static int TREE_LAYER = Layer.BACKGROUND + 2;

    public static int[] LEAF_DIM = new int[]{13, 15, 17};

    Color temp = new Color(91, 157, 74);
    public static Color[] LEAF_COLORS = new Color[]{new Color(43, 105, 19),
            new Color(33, 75, 28),
            new Color(91, 157, 74)};

    public static int LEAF_FALL_VELOCITY = 50;

    public static int MAX_LEAF_FALL_TIME = 10;

    public static int MAX_LEAF_DEATH_TIME = 16;
    // how long it takes the leaf to fade out

    public static int LEAF_FADEOUT_TIME = 5;

    public static int LEAF_MAX_X_VELOCITY = 10;

    public static float LEAF_X_TRANSITION_TIME = 1f;

}