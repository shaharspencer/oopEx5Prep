package pepse.util;

import danogl.collisions.Layer;

import java.awt.*;

public class TreeConfiguration {

    /* leaf definitions */
    public static String LEAF_TAG = "leaf";
    //todo: check the layers

    // the leaf layer - should not collide with anything
    public static int LEAF_LAYER =  Layer.BACKGROUND + 201;

    // the layer at which the tree is
    public static int TREE_LAYER = Layer.DEFAULT;

    // the possible leaf dimensions
    public static int[] LEAF_DIM = new int[]{13, 15, 17};


    // possible leaf colors
    public static Color[] LEAF_COLORS = new Color[]{new Color(43, 105, 19),
            new Color(33, 75, 28),
            new Color(91, 157, 74)};


    // velocity for the leaf when it falls
    public static int LEAF_FALL_VELOCITY = 50;


    // how long the leaf will take before it starts falling - choose a random int in this range
    public static int MAX_LEAF_FALL_TIME = 60;

    // we choose a randomnumber in this range for the leaf to stay dead
    public static int MAX_LEAF_DEATH_TIME = 16;

    // how long it takes the leaf the fadeout when it starts dying
    public static int LEAF_FADEOUT_TIME = 5;

    // the velocity on x dimension while the leaf falls
    public static int LEAF_MAX_X_VELOCITY = 2;

    // the transition time for the leaf going back and forth on x dimension while it falls
    public static float LEAF_X_TRANSITION_TIME = 1f;

    // the leaf size (will be a square)
    public static final int LEAF_SIZE = 15;

}
