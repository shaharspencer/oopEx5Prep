package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.TerrainConfiguration;
import pepse.util.TreeConfiguration;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;

import static danogl.components.Transition.TransitionType.TRANSITION_BACK_AND_FORTH;
import static danogl.components.Transition.TransitionType.TRANSITION_ONCE;
import static pepse.util.TerrainConfiguration.TOP_BLOCK_TAG;
import static pepse.util.TreeConfiguration.MAX_LEAF_DEATH_TIME;


public class Leaf extends GameObject {
    private static final int FALL_VELOCITY = 20;

    //    private static
    public static final int LEAF_SIZE = 50;

    private final Vector2 topLeftCorner;
    private final GameObjectCollection gameObjects;


    private Transition<Float> leafTransition;
    private Transition<Float> xTransition;

    public Leaf(Vector2 topLeftCorner, GameObjectCollection gameObjects,
                RectangleRenderable leafRenderebale, int leafDim) {
        super(topLeftCorner, new Vector2(leafDim, leafDim), leafRenderebale);
        this.topLeftCorner = topLeftCorner;
        this.gameObjects = gameObjects;

        gameObjects.addGameObject(this, TreeConfiguration.LEAF_LAYER);


        this.renderer().setRenderableAngle((float) ((float) (5 / 6) * Math.PI));
        this.setTag(TreeConfiguration.LEAF_TAG);

        gameObjects.layers().shouldLayersCollide(TreeConfiguration.LEAF_LAYER, TerrainConfiguration.BLOCK_LAYER, true);

        moveLeaves();
        makeLeavesFall();

    }

    /**
     * begins the cycle of the leaf life
     * returns leaf to original placement, waits for a random life time
     * and begins the leaf falling
     */

    private void makeLeavesFall() {

        Random rand = new Random();

        float waitTimeCycle = rand.nextInt(TreeConfiguration.MAX_LEAF_FALL_TIME);

        new ScheduledTask(
                this,
                waitTimeCycle,
                false,
                this::duringAndAfterFall);
    }


    /**
     * sets a velocity to the leaf
     * and begins a fadeout
     * calls a function for the end of the fadeout: afterleavesFall
     */
    private void duringAndAfterFall()
    {
        if (xTransition != null){
            this.addComponent(xTransition);
        }

        this.renderer().fadeOut(TreeConfiguration.LEAF_FADEOUT_TIME, this::afterLeavesFall);
        this.transform().setVelocityY(TreeConfiguration.LEAF_FALL_VELOCITY);
        Consumer<Float> velocityConsumer = (Float velocity)-> this.transform().setVelocityX(velocity);
        this.xTransition = new Transition<Float>(this,
                velocityConsumer,
                (float)
                        -TreeConfiguration.LEAF_MAX_X_VELOCITY,
                (float) TreeConfiguration.LEAF_MAX_X_VELOCITY,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                TreeConfiguration.LEAF_X_TRANSITION_TIME, TRANSITION_BACK_AND_FORTH,
                null);

    }


    /***
     * called after the fadeout is finished
     * sets object velocity to zero, returns leaf to original spot
     * and
     */
    private void afterLeavesFall() {

        this.removeComponent(xTransition);

        Random rand = new Random();
        float waitTimeCycle = rand.nextInt(MAX_LEAF_DEATH_TIME);


        new ScheduledTask(
                this,
                waitTimeCycle,
                false,
                this::returnToOriginalPlacement);
    }


    /**
     * upon a collision with an object, sets velocity to zero - aka stops the leaf
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.transform().setVelocityY(0);
        if (xTransition != null){
            this.removeComponent(xTransition);

        }
    }


    private void returnToOriginalPlacement(){
        this.transform().setVelocityY(0);
        this.renderer().fadeIn(0);
        this.setTopLeftCorner(topLeftCorner);
        makeLeavesFall();
    }



    private void moveLeaves() {
        Random rand = new Random();
        float waitTime = rand.nextFloat() * 2;
        new ScheduledTask(
                this,
                waitTime,
                false,
                this::updateLeafAngle);
    }

    private void setLeafColor(Color color){
        RectangleRenderable re = new RectangleRenderable(color);
        this.renderer().setRenderable(re);
    }

    private void updateLeafAngle(){

        Consumer<Float> angleConsumer = (Float angle)-> {this.renderer().setRenderableAngle(angle);
            this.setDimensions(this.getDimensions());
        };
        new Transition<Float>(this,
                angleConsumer, (float) (Math.PI * ((float) 5/6)), (float) (Math.PI* (float) 2),
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                1, TRANSITION_ONCE,
                null);


    }

    @Override
    public boolean shouldCollideWith(GameObject other){
        return other.getTag().equals(TOP_BLOCK_TAG);
    }

}
