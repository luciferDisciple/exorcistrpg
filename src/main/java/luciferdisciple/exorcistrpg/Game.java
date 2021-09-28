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
    
    public Game(Screen screen) {
        this.screen = screen;
        this.gameState = new MainMenu(screen);
    }
    
    public void draw() {
        this.gameState.draw();
    }
    
    public void handleInput(KeyStroke key) {
        this.gameState.handleInput(key);
    } 
}
