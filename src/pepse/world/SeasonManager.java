package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.util.Vector2;



public class SeasonManager {
    private int season = 0;
    private boolean didSeasonChange = false;

    /**
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

    private void changeSeason(){
        season = (season +1) % 4;
        didSeasonChange = true;
    }

    public int getSeason(){
        return season;
    }

    public boolean getDidSeasonChange(){
        return didSeasonChange;
    }

    public void turnOffDidSeasonChange(){
        didSeasonChange = false;
    }
}
