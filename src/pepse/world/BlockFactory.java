package pepse.world;

import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A factory for instances of Block
 */
public class BlockFactory {
    //renderable for creating the bricks
    private Renderable blockRenderable;

    /**
     * Constructor of a BlockFactory instance.
     * @param renderable used to create the instance of Block.
     */
    public BlockFactory(Renderable renderable) {
        this.blockRenderable = renderable;
    }

    /**
     * This is the method that
     * @param topLeftCorner The location of the top-left corner of the created block, in window coordinates
     *                      (pixels).
     * @return a Block instance located at topLeftCorner.
     */
    public Block generateBlock(Vector2 topLeftCorner) {
        return new Block(topLeftCorner, blockRenderable);
    }


}
