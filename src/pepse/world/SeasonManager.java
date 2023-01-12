package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.util.Vector2;


/**
 * creates the season that changes over time and holds information regarding the season
 */
public class SeasonManager {
    private int season = 0;
    private boolean didSeasonChange = false;

    /**
     * creates the game object that represents the season and changes over time
     *
     * @param gameObjects
     * @param layer
     * @param seasonLength the duration of each season in seconds
     * @return
     */
    public GameObject create(GameObjectCollection gameObjects, int layer, float seasonLength) {
        GameObject seasonObject = new GameObject(Vector2.ZERO, Vector2.ZERO, null);
        gameObjects.addGameObject(seasonObject, layer);
        seasonObject.setTag("season_manager");

        new ScheduledTask(seasonObject, seasonLength, true, this::changeSeason);

        return seasonObject;
    }

    /**
     * update the season to the next season
     */
    private void changeSeason() {
        season = (season + 1) % 4;
        didSeasonChange = true;
    }

    /**
     * getter for the current season value
     *
     * @return int of the current season
     */
    public int getSeason() {
        return season;
    }

    /**
     * getter to see if the season has changed
     *
     * @return
     */
    public boolean getDidSeasonChange() {
        return didSeasonChange;
    }

    /**
     * Sets the value indicating if the season changed to false
     */
    public void turnOffDidSeasonChange() {
        didSeasonChange = false;
    }
}
