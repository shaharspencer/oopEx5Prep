package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;

import static danogl.components.Transition.TransitionType.TRANSITION_BACK_AND_FORTH;
import static danogl.components.Transition.TransitionType.TRANSITION_LOOP;

public class Leaf extends GameObject {
    private static final int FALL_VELOCITY = 20;

//    private static
    private static final int LEAF_SIZE = 50;
    private static final int MAX_LEAF_FALL_TIME = 10;
    private final Vector2 topLeftCorner;
    private final Vector2 windowDimensions;

    public Leaf(Vector2 topLeftCorner, GameObjectCollection gameObjects,
                RectangleRenderable leafRenderebale, Vector2 windowDimensions) {
        super(topLeftCorner, new Vector2(LEAF_SIZE, LEAF_SIZE), leafRenderebale);
        this.topLeftCorner = topLeftCorner;
        this.windowDimensions = windowDimensions;
        gameObjects.addGameObject(this);
        this.renderer().setRenderableAngle((float) ((float) (5 / 6) * Math.PI));
        this.setTag("leaf");
        moveLeaves();
        makeLeavesFall();
    }

    private void makeLeavesFall() {
        Random rand = new Random();
        float waitTimeCycle = rand.nextInt(MAX_LEAF_FALL_TIME);
        new ScheduledTask(
                this,
                waitTimeCycle,
                true,
                this::duringAndAfterFall);

    }



    private void duringAndAfterFall(){
        Consumer<Float> fallingLeafCycle = (Float m)->{};
        new Transition<Float>(this, fallingLeafCycle, topLeftCorner.y(), windowDimensions.y(),
                Transition.LINEAR_INTERPOLATOR_FLOAT, 5, TRANSITION_LOOP, null);
       this.transform().setVelocityY(FALL_VELOCITY);
        this.renderer().fadeOut(FALL_VELOCITY * (float) 2 /3, this::afterFadeOut);

    }


    private void afterFadeOut(){

        new ScheduledTask(
                this,
                5,
                false,
                this::returnToOrigTopLeftCorner);
        // wait x seconds

    }

    private void returnToOrigTopLeftCorner(){
        this.setTopLeftCorner(topLeftCorner);
    }

    private void setLeafColor(Color color){
        RectangleRenderable re = new RectangleRenderable(color);
        this.renderer().setRenderable(re);
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

    private void updateLeafAngle(){

        Consumer<Float> angleConsumer = (Float angle)-> {this.renderer().setRenderableAngle(angle);
            this.setDimensions(this.getDimensions());
        };
        new Transition<Float>(this,
                angleConsumer, (float) (Math.PI * ((float) 5/6)), (float) (Math.PI* (float) 2), Transition.LINEAR_INTERPOLATOR_FLOAT,
                1, TRANSITION_BACK_AND_FORTH,
                null);


    }

}
