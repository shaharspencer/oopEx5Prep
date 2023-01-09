package pepse.util;

import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
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
    public static final Color STUMP_COLOR = new Color(94, 60, 39);
    public static final Renderable STUMP_RENDERABLE = new RectangleRenderable(STUMP_COLOR);
    public static final String TREE_TAG = "tree_stump";
    public static final int LEAF_SIDE_OF_WIND_MOVEMENT = 1;


    /* leaf definitions */
    public static String LEAF_TAG = "leaf";
    public static final float LEAF_SIDE_MOVEMENT_FACTOR = 10f;
    public static final float LEAF_WIND_MOVEMENT_TRANSITION_TIME = 2.5f;
    public static final float LEAF_CHANGE_SIZE_INITIAL_SIZE_FACTOR = 1.5f;
    public static final float LEAF_CHANGE_SIZE_FINALE_SIZE_FACTOR = 1.05f;
    public static final int LEAF_CHANGE_SIZE_IN_WIND_TRANSITION_TIME = 1;

    //todo: check the layers

    // the leaf layer - should not collide with anything
    public static int LEAF_LAYER =  Layer.BACKGROUND + 201;

    // the layer at which the tree is
    public static int TREE_LAYER = Layer.DEFAULT;

    // the possible leaf dimensions
    public static final int[] LEAF_DIM = new int[]{13, 15, 17};
    // the leafs will overlap obe another by this factor
    public static final double LEAF_OVERLAP_FACTOR = 0.25;
    // factor to be multiplied by leaf size when increasing the angle when leafs are placed
    public static final double LEAF_ANGLE_CHANGE_FACTOR = 0.3;

    public static final int LEAF_SPROUT_PROBABILITY_RANGE = 10;
    public static final int LEAF_SPROUT_PROBABILITY = 4;



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
    public static int LEAF_FADEOUT_TIME = 10;

    // the velocity on x dimension while the leaf falls
    public static float LEAF_MAX_X_VELOCITY = 10;

    // the transition time for the leaf going back and forth on x dimension while it falls
    public static float LEAF_X_TRANSITION_TIME = 2;

    // the leaf size (will be a square)
    public static final int LEAF_SIZE = 15;

    // how long the leaf waits before it start blowing in the wind
    public static final int WIND_WAIT_TIME = 5;
    //todo: make sure in all of the configurations everything is used
    //todo: change leafs to leaves
}
