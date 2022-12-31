package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;

import static danogl.components.Transition.TransitionType.TRANSITION_BACK_AND_FORTH;
import static danogl.components.Transition.TransitionType.TRANSITION_LOOP;

public class Leaf extends GameObject {
    private static final int leafSize = 50;
    public Leaf(Vector2 topLeftCorner, GameObjectCollection gameObjects,
                RectangleRenderable leafRenderebale) {
        super(topLeftCorner, new Vector2(leafSize, leafSize), leafRenderebale);
        gameObjects.addGameObject(this);
        this.renderer().setRenderableAngle((float) ((float) (5 / 6) * Math.PI));
        this.setTag("leaf");
        moveLeaves();
        makeLeavesFall();
    }

    private void makeLeavesFall() {
        Random rand = new Random();

        float lifeCycle = rand.nextInt();
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
        Transition<Float> transition = new Transition<Float>(this,
                angleConsumer, (float) (Math.PI * ((float) 5/6)), (float) (Math.PI* (float) 2), Transition.LINEAR_INTERPOLATOR_FLOAT,
                1, TRANSITION_BACK_AND_FORTH,
                null);


    }

}
