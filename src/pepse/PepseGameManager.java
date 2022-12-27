package pepse;

import danogl.GameManager;
import danogl.util.Vector2;

public class PepseGameManager extends GameManager {

    static Vector2 windowSize = new Vector2(700, 500);

    PepseGameManager(){
        super("", windowSize);
    }

    /*
    @Override
        public void initializeGame(danogl.gui.ImageReader imageReader, danogl.gui.SoundReader
            soundReader,
                         danogl.gui.UserInputListener inputListener,
                        danogl.gui.WindowController windowController){

    }
     */

    public static void main(String[] args){
        new PepseGameManager().run();
    }
}
