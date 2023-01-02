package pepse.world.trees;

import java.awt.*;
import java.util.Random;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.TreeConfiguration;

public class LeafFactory {

    private final GameObjectCollection gameObjects;

    public LeafFactory(GameObjectCollection gameObjects){

        this.gameObjects = gameObjects;
    }

    public Leaf createLeaf(Vector2 topLeftCorner){
        // choose leaf dimension
        Random rand = new Random();
        int leafDimensionIndx = rand.nextInt(TreeConfiguration.LEAF_DIM.length);
        int leafDimension = TreeConfiguration.LEAF_DIM[leafDimensionIndx];

        int colorIndx = rand.nextInt(TreeConfiguration.LEAF_COLORS.length);
        Color leafColor = TreeConfiguration.LEAF_COLORS[colorIndx];
        return new Leaf(topLeftCorner, gameObjects, new RectangleRenderable(leafColor),
                leafDimension);

    }
}
