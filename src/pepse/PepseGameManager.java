package pepse;

import danogl.GameManager;

public class PepseGameManager extends GameManager {

    PepseGameManager(){
        super();
    }

    @Override
    public void initializeGame(danogl.gui.ImageReader imageReader, danogl.gui.SoundReader
            soundReader,
                         danogl.gui.UserInputListener inputListener,
                        danogl.gui.WindowController windowController){

    }
    public static void main(String[] args){
        new PepseGameManager().run();
    }
}
