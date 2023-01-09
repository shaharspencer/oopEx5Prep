package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.configurations.TerrainConfiguration;

import java.awt.*;
import java.util.Random;

/**
 * A factory for instances of Block
 */
public class BlockFactory {
    //######## private fields ########
    private Renderable[] renderables = new Renderable[TerrainConfiguration.blockColors.length];

    //######## public methods ########

    /**
     * Constructor of a BlockFactory instance.
     */
    public BlockFactory(){
        int i = 0;
        for (Color color: TerrainConfiguration.blockColors){
            renderables[i] = new RectangleRenderable(
                    ColorSupplier.approximateColor(color));
            i++;
        }
    }


    /**
     * This is the method that creates a block, using a random integer to choose a renderable
     * and returns a block.
     * @param topLeftCorner The location of the top-left corner of the created block, in window coordinates
     *                      (pixels).
     * @param rand random object to use for this block (according to the game seed and x coordinate of the
     *             block)
     * @return a Block instance located at topLeftCorner.
     */
    public Block generateBlock(Vector2 topLeftCorner, Random rand) {

        int colorIndex = rand.nextInt(this.renderables.length);
        Renderable renderable = this.renderables[colorIndex];
        return new Block(topLeftCorner, renderable);
    }


}
