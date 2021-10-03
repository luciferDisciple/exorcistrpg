package luciferdisciple.exorcistrpg;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author Lucifer Disciple <piotr.momot420@gmail.com>
 */
final class Game extends InteractiveApplication {

    private Deque<GameWindow> gameWindowsStack = new ArrayDeque<GameWindow>();
    
    public Game(Terminal terminal) throws IOException {
        super(terminal);
        openWindowInForeground(new MainMenu(this));
    }
    
    @Override
    protected void initialize() {}

    @Override
    protected void draw(Screen screen) {
        getActiveScreen().draw(screen);
    }

    @Override
    protected void update(long timeDelta) {
        getActiveScreen().update(timeDelta);
    }

    @Override
    protected void receiveInput(KeyStroke keyStroke) {
        getActiveScreen().receiveInput(keyStroke);
    }
    
    private GameWindow getActiveScreen() {
        return this.gameWindowsStack.peek();
    }
    
    public void openWindowInForeground(GameWindow gameWindow) {
        this.gameWindowsStack.push(gameWindow);
        // clear the graphics left on the screen buffer by the last
        // foreground window
        requestScreenClear();
    }
    
    public void closeForegoundWindow() {
        this.gameWindowsStack.pop();
        if (this.gameWindowsStack.isEmpty())
            this.stop();
        else
            // clear the graphics left on the screen buffer by the last
            // foreground window
            requestScreenClear();
    }
}
