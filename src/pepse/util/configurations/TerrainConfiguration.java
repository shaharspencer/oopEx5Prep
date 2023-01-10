package pepse.util.configurations;

import danogl.collisions.Layer;
import pepse.world.Block;

import java.awt.*;

public class TerrainConfiguration {
    public static final int GROUND_LAYER = Layer.STATIC_OBJECTS;
    public static final double X_NOISE_FACTOR = 0.01;
    public static final int BASIC_HEIGHT = Block.SIZE * 12;

    public static final String GROUND_TAG = "ground";

    // number of blocks we want the avatar the clash with
    public static final int TOP_BLOCK_FACTOR = 3;
    public static final Color[] WINTER_COLORS = new Color[]{new Color(208, 208, 208),
            Color.WHITE,
            new Color(239, 245, 246),
            new Color(239, 238, 231)};
    ;

    // the tag for the topmost block
    public static String TOP_BLOCK_TAG = "top_block";

    public static String LOW_BLOCK_TAG = "lower_block";


    // creating collecetion of colors the block can have
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static Color other = new Color(200, 130, 100);
    public static Color otherOther = new Color(210, 123, 74);
    private static Color another = new Color(250, 130, 50);

    // defines the colors the block can use
    public static Color[] blockColors = {BASE_GROUND_COLOR, other, another, otherOther};


    // define layers for
    public static int getTopBlockLayer() {
        return TOP_BLOCKS_LAYER;
    }

    public static int DEFAULT_BLOCKS_LAYER = Layer.STATIC_OBJECTS;
    public static int TOP_BLOCKS_LAYER = DEFAULT_BLOCKS_LAYER - 1;

    public static int getDefaultBlocksLayer() {
        return DEFAULT_BLOCKS_LAYER;
    }

    private Layer l = Layer.
}
