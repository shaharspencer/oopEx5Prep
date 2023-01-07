package pepse.util;

import danogl.collisions.Layer;

import java.awt.*;

public class TerrainConfiguration {
    // number of blocks we want the avatar the clash with
    public static final int TOP_BLOCK_FACTOR = 3;

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
    public static int TOP_BLOCKS_LAYER = Layer.STATIC_OBJECTS;
    public static int getTopBlockLayer(){
        return TOP_BLOCKS_LAYER;
    }


    public static int DEFAULT_BLOCKS_LAYER = TOP_BLOCKS_LAYER -1;

    public static int getDefaultBlocksLayer(){
        return DEFAULT_BLOCKS_LAYER;
    }

}
