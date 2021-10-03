package luciferdisciple.exorcistrpg;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

/**
 *
 * @author Lucifer Disciple <piotr.momot420@gmail.com>
 */
abstract public class GameWindow {
    
    private Game game;
    
    public GameWindow(Game game) {
        this.game = game;
    }
    
    protected void closeThisWindow() {
        this.game.closeForegoundWindow();
    }
    
    protected void openWindow(GameWindow gameWindow) {
        this.game.openWindowInForeground(gameWindow);
    }
    
    abstract public void update(long timeDelta);
    abstract public void draw(Screen screen);
    abstract public void receiveInput(KeyStroke key);
}
