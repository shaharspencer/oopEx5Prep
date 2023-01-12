package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.configurations.TreeConfiguration;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

import static danogl.components.Transition.TransitionType.TRANSITION_BACK_AND_FORTH;
import static pepse.util.configurations.TreeConfiguration.MAX_LEAF_DEATH_TIME;

/**
 * Class of leafs, each instance is a leaf in the game
 */
public class Leaf extends GameObject {

    //######## private fields ########
    private final GameObjectCollection gameObjects;
    private final Vector2 initialCenter;
    private final int leafDim;
    private final Color fallColor;
    private final Color winterColor;
    private final Random rand;
    private final Color summerColor;
    private Transition<Float> horizontalTransition;
    private Transition<Float> wind_transition;
    private Transition<Float> size_transition;

    //######## public methods ########

    /**
     * constructor for the leaf
     *
     * @param topLeftCorner   top left corner for leaf
     * @param gameObjects     add leaf to these
     * @param leafRenderebale renderable for leaf
     * @param leafDim         dimensions of leaf
     * @param seed            game seed
     */
    public Leaf(Vector2 topLeftCorner, GameObjectCollection gameObjects,
                RectangleRenderable leafRenderebale, int leafDim, int seed, Color summerColor) {
        super(topLeftCorner, new Vector2(leafDim, leafDim), leafRenderebale);
        this.leafDim = leafDim;
        this.rand = new Random(Objects.hash(this.getCenter(), seed));
        this.summerColor = summerColor;
        this.initialCenter = this.getCenter();
        this.gameObjects = gameObjects;
        this.fallColor =
                TreeConfiguration.LEAF_COLORS_FALL[rand.nextInt(TreeConfiguration.LEAF_COLORS_FALL.length)];

        this.winterColor =
                TreeConfiguration.LEAF_COLORS_WINTER[rand.nextInt(TreeConfiguration.LEAF_COLORS_WINTER.length)];
        addToGameAndInitialize();

        moveLeaves();

        initializeLifeCycle();
    }

    /**
     * upon collsion set the velocity to zero
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.removeComponent(this.horizontalTransition);
        this.removeComponent(this.wind_transition);
        this.removeComponent(this.size_transition);
        super.update(0);
        this.setVelocity(Vector2.ZERO);
    }

    /**
     * Changes the leaf color according to season
     *
     * @param season new season
     */
    public void changeColor(int season) {
        Color newColor;
        switch (season) {
            case (1):
                newColor = fallColor;
                break;
            case (2):
                newColor = winterColor;
                break;
            default:
                newColor = summerColor;
        }
        this.setLeafColor(newColor);
    }

    //######## private methods ########

    /**
     * adds leaf game object to game and sets its tag
     */
    private void addToGameAndInitialize() {
        gameObjects.addGameObject(this, TreeConfiguration.LEAF_LAYER);
        this.setTag(TreeConfiguration.LEAF_TAG);
    }

    /**
     * begins life cycle: chooses a wait time and begins to make leaf fall
     */
    private void initializeLifeCycle() {
        float waitTime = rand.nextInt(TreeConfiguration.MAX_LEAF_FALL_TIME);
        new ScheduledTask(this, waitTime, false, this::makeLeavesFall);
    }

    /**
     * starts the fall of the leaf and the fadeout
     * starts a horizontal transition for during the fall
     */
    private void makeLeavesFall() {

        this.renderer().fadeOut(TreeConfiguration.LEAF_FADEOUT_TIME, this::afterLeavesFall);
        this.transform().setVelocityY(TreeConfiguration.LEAF_FALL_VELOCITY);

        this.horizontalTransition = new Transition<>(this, (speed) ->
                this.transform().setVelocityX(speed),
                TreeConfiguration.LEAF_MAX_X_VELOCITY, -TreeConfiguration.LEAF_MAX_X_VELOCITY,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                TreeConfiguration.LEAF_X_TRANSITION_TIME, TRANSITION_BACK_AND_FORTH,
                null);
    }


    /***
     * called after the fadeout is finished, sets object velocity to zero, returns leaf to original spot
     * after a waitTime
     */
    private void afterLeavesFall() {
        this.setVelocity(Vector2.ZERO);
        float waitTimeCycle = rand.nextInt(MAX_LEAF_DEATH_TIME);
        new ScheduledTask(
                this,
                waitTimeCycle,
                false,
                this::returnToOriginalPlacement);
    }

    /**
     * returns leaf to original placement (by center), sets velocity and fadeout to 0
     */
    private void returnToOriginalPlacement() {
        this.setCenter(initialCenter);
        this.setVelocity(Vector2.ZERO);
        this.renderer().fadeIn(0);
        initializeLifeCycle();
    }


    /**
     * moves leafs in wind, starts moving them after a random waitTime
     */
    private void moveLeaves() {
        float waitTime = rand.nextInt(TreeConfiguration.WIND_WAIT_TIME);
        new ScheduledTask(this, waitTime, false, this::moveBackAndForth);
    }

    /**
     * moves the leaves back and forth in the wind
     */
    private void moveBackAndForth() {

        //move from side to side
        this.wind_transition = new Transition<>(this, (angle) ->
                this.renderer().setRenderableAngle(angle),
                TreeConfiguration.LEAF_SIDE_OF_WIND_MOVEMENT *
                        TreeConfiguration.LEAF_SIDE_MOVEMENT_FACTOR,
                -1 * TreeConfiguration.LEAF_SIDE_OF_WIND_MOVEMENT *
                        TreeConfiguration.LEAF_SIDE_MOVEMENT_FACTOR,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                TreeConfiguration.LEAF_WIND_MOVEMENT_TRANSITION_TIME,
                TRANSITION_BACK_AND_FORTH,
                null);


        // change size
        this.size_transition = new Transition<>(this, (x) ->
                this.setDimensions(Vector2.ONES.mult(x)),
                (float) this.leafDim * TreeConfiguration.LEAF_CHANGE_SIZE_INITIAL_SIZE_FACTOR,
                (float) this.leafDim * TreeConfiguration.LEAF_CHANGE_SIZE_FINALE_SIZE_FACTOR,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                TreeConfiguration.LEAF_CHANGE_SIZE_IN_WIND_TRANSITION_TIME,
                TRANSITION_BACK_AND_FORTH,
                null);
    }

    private void setLeafColor(Color color) {
        RectangleRenderable re = new RectangleRenderable(color);
        this.renderer().setRenderable(re);
    }
}
