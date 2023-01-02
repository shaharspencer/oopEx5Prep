package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.util.TreeConfiguration;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;

import static danogl.components.Transition.TransitionType.*;

public class Leaf extends GameObject {
    private static final int FALL_VELOCITY = 20;

//    private static
    public static final int LEAF_SIZE = 50;
    private static final int MAX_LEAF_FALL_TIME = 1;
    private final Vector2 topLeftCorner;
    private final GameObjectCollection gameObjects;


    private Transition<Float> leafTransition;

    public Leaf(Vector2 topLeftCorner, GameObjectCollection gameObjects,
                RectangleRenderable leafRenderebale, int leafDim) {
        super(topLeftCorner, new Vector2(leafDim, leafDim), leafRenderebale);
        this.topLeftCorner = topLeftCorner;
        this.gameObjects = gameObjects;

        gameObjects.addGameObject(this, TreeConfiguration.LEAF_LAYER);


        this.renderer().setRenderableAngle((float) ((float) (5 / 6) * Math.PI));
        this.setTag(TreeConfiguration.LEAF_TAG);

//        moveLeaves();
//        makeLeavesFall();
    }

//    public static
//
//    private void makeLeavesFall() {
//        this.setTopLeftCorner(topLeftCorner);
//        Random rand = new Random();
//        float waitTimeCycle = rand.nextInt(MAX_LEAF_FALL_TIME);
//        new ScheduledTask(
//                this,
//                waitTimeCycle,
//                true,
//                this::duringAndAfterFall);
//    }
//
//
//
//    private void duringAndAfterFall(){
//        Consumer<Float> fallingLeafCycle = (Float m)->{this.transform().setVelocityY(FALL_VELOCITY);};
//        this.leafTransition = new Transition<Float>(this, fallingLeafCycle, (float) FALL_VELOCITY,
//                (float) FALL_VELOCITY,
//                Transition.LINEAR_INTERPOLATOR_FLOAT, 5, TRANSITION_ONCE, null);
//       this.transform().setVelocityY(FALL_VELOCITY);
//       // upon
////       this.renderer().fadeOut(FALL_VELOCITY * (float) 2 /3);
//    }
//
//    @Override
//    public boolean shouldCollideWith(GameObject other) {
//        return (other.getTag() == TOP_BLOCK_TAG);
//    }
//
//    private void afterFadeOut(){
//
//        new ScheduledTask(
//                this,
//                3,
//                false,
//                this::returnToOrigTopLeftCorner);
//        // wait x seconds
//
//    }
//
//    private void returnToOrigTopLeftCorner(){
//        makeLeavesFall();
//
//
//    }
//
//    private void setLeafColor(Color color){
//        RectangleRenderable re = new RectangleRenderable(color);
//        this.renderer().setRenderable(re);
//    }
//
//
//
//    private void moveLeaves() {
//        Random rand = new Random();
//        float waitTime = rand.nextFloat() * 2;
//        new ScheduledTask(
//                this,
//                waitTime,
//                false,
//                this::updateLeafAngle);
//    }
//
//    private void setCollisionLayers(){
//        for (int i = 0; i < layersToCollideWith.length; i++){
//            gameObjects.layers().shouldLayersCollide(layer, layersToCollideWith[i], true);
//        }
//
//
//    }
//
////    shou
//
//    private void updateLeafAngle(){
//
//        Consumer<Float> angleConsumer = (Float angle)-> {this.renderer().setRenderableAngle(angle);
//            this.setDimensions(this.getDimensions());
//        };
//        this.leafTransition = new Transition<Float>(this,
//                angleConsumer, (float) (Math.PI * ((float) 5/6)), (float) (Math.PI* (float) 2), Transition.LINEAR_INTERPOLATOR_FLOAT,
//                1, TRANSITION_ONCE,
//                null);
//
//
//    }
//
//    /**
//     *
//     */
//    @Override
//    public void onCollisionEnter(GameObject other, Collision collision) {
//        super.onCollisionEnter(other, collision);
//        if (leafTransition != null){
//            this.removeComponent(leafTransition);
//        }
//        leafTransition = null;
//
//    }
}
