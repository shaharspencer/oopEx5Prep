package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.configurations.ColorSupplier;
import pepse.util.configurations.TerrainConfiguration;

import java.awt.*;
import java.util.Random;

/**
 * A factory for instances of Block
 */
public class BlockFactory {
    Renderable[] renderables = new Renderable[TerrainConfiguration.blockColors.length];
    //renderable for creating the bricks
    private Renderable blockRenderable;

    Random rand = new Random();

    /**
     * Constructor of a BlockFactory instance.
     */
    public BlockFactory(){
        this.blockRenderable = null;

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
     * @return a Block instance located at topLeftCorner.
     */
    public Block generateBlock(Vector2 topLeftCorner) {

        int colorIndex = rand.nextInt(this.renderables.length);
        Renderable renderable = this.renderables[colorIndex];
        return new Block(topLeftCorner, renderable);
    }


}
