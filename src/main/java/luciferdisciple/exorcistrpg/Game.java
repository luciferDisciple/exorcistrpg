package luciferdisciple.exorcistrpg;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

/**
 *
 * @author Lucifer Disciple <piotr.momot420@gmail.com>
 */
public class Game {
    private GameState gameState;
    private final Screen screen;
    private boolean exitRequested;
    
    public Game(Screen screen) {
        this.screen = screen;
        this.gameState = new MainMenu(this, screen);
    }
    
    public void draw() {
        this.gameState.draw();
    }
    
    public void handleInput(KeyStroke key) {
        this.gameState = this.gameState.handleInput(key);
    }
    
    public boolean isExitRequested() {
        return exitRequested;
    }

    void requestExit() {
        exitRequested = true;
    }
}
