package pepse.util.configurations;

import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
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
    public static final Color STUMP_COLOR = new Color(94, 60, 39);
    public static final Renderable STUMP_RENDERABLE = new RectangleRenderable(STUMP_COLOR);
    public static final String TREE_TAG = "tree_stump";
    public static final int LEAF_SIDE_OF_WIND_MOVEMENT = 1;
    public static final Color[] LEAF_COLORS_FALL = new Color[]{new Color(208, 135, 26),
            new Color(208, 199, 26),
            new Color(208, 96, 26)};
    public static final Color[] LEAF_COLORS_WINTER = new Color[]{new Color(228, 210, 231),
            Color.WHITE,
            new Color(195, 226, 234)};
    public static final Vector2 FLOWER_SIZE = Vector2.of(15,15);
    public static final String FLOWER_IMAGE_PATH = "./assets/flower.jpeg";

    /* leaf definitions */
    public static String LEAF_TAG = "leaf";
    public static final float LEAF_SIDE_MOVEMENT_FACTOR = 10f;
    public static final float LEAF_WIND_MOVEMENT_TRANSITION_TIME = 2.5f;
    public static final float LEAF_CHANGE_SIZE_INITIAL_SIZE_FACTOR = 1.5f;
    public static final float LEAF_CHANGE_SIZE_FINALE_SIZE_FACTOR = 1.05f;
    public static final int LEAF_CHANGE_SIZE_IN_WIND_TRANSITION_TIME = 1;

    public static int TREE_LAYER = Layer.DEFAULT;
    public static int LEAF_LAYER = TREE_LAYER +1;

    // the possible leaf dimensions
    public static final int[] LEAF_DIM = new int[]{13, 15, 17};
    // the leafs will overlap obe another by this factor
    public static final double LEAF_OVERLAP_FACTOR = 0.3;
    // factor to be multiplied by leaf size when increasing the angle when leafs are placed
    public static final double LEAF_ANGLE_CHANGE_FACTOR = 0.5;

    public static final int LEAF_SPROUT_PROBABILITY_RANGE = 12;
    public static final int LEAF_SPROUT_PROBABILITY = 3;


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

}
