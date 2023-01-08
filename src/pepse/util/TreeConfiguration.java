package pepse.util;

import danogl.collisions.Layer;
import pepse.world.Block;

import java.awt.*;

public class TreeConfiguration {
    /* tree stump definitions */
    public static final int MAX_TREE_STUMP_WIDTH = 30;
    public static final int MAX_TREE_STUMP_HEIGHT = 300;
    public static final int DEFAULT_STUMP_WIDTH = Block.SIZE;
    public static final int DEFAULT_STUMP_HEIGHT = 250;
    public static final int TREE_SPROUT_PROBABILITY = 4;
    public static final int TREE_SPROUT_PROBABILITY_RANGE = 20;
    public static final int MIN_DIST_BETWEEN_TREES_FACTOR = 2;
    //TODO: CHANGE TREE MAX WIDTH AND HIGHT?



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
    public static int MAX_LEAF_FALL_TIME = 1000;

    // we choose a randomnumber in this range for the leaf to stay dead
    public static int MAX_LEAF_DEATH_TIME = 3;

    // how long it takes the leaf the fadeout when it starts dying
    public static int LEAF_FADEOUT_TIME = 20;

    // the velocity on x dimension while the leaf falls
    public static float LEAF_MAX_X_VELOCITY = 10;

    // the transition time for the leaf going back and forth on x dimension while it falls
    public static float LEAF_X_TRANSITION_TIME = 2;

    // the leaf size (will be a square)
    public static final int LEAF_SIZE = 15;

    // how long the leaf waits before it start blowing in the wind
    public static final int WIND_WAIT_TIME = 5;

}
