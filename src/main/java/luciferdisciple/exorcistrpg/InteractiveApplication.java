package luciferdisciple.exorcistrpg;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;

/**
 *
 * @author Lucifer Disciple <piotr.momot420@gmail.com>
 */
abstract public class InteractiveApplication {
    private static final long FRAMERATE = 30;
    private static final long FRAMETIME = 1000 / FRAMERATE;
    
    private final Screen screen;
    private long timeOfLastUpdateInMiliseconds;
    private boolean isOver;
    
    public InteractiveApplication(Terminal terminal) throws IOException {
        this.screen = new TerminalScreen(terminal);
    }
    
    public void run() throws IOException {
        initialize();
        this.screen.startScreen();
        this.screen.setCursorPosition(null);
        this.timeOfLastUpdateInMiliseconds = System.currentTimeMillis();
        while (!this.isOver) {
            long timeDelta = System.currentTimeMillis() - this.timeOfLastUpdateInMiliseconds;
            KeyStroke inputKey = this.screen.pollInput();
            if (inputKey != null)
                receiveInput(inputKey);
            if (timeDelta < FRAMETIME)
                continue;
            update(timeDelta);
            draw(this.screen);
            screen.refresh();
            this.timeOfLastUpdateInMiliseconds = System.currentTimeMillis();
        }
        this.screen.stopScreen();
    }
    
    protected final void stop() {
        this.isOver = true;
    }
    
    protected final void requestScreenClear() {
        this.screen.clear();
    }
    
    abstract protected void initialize();
    abstract protected void draw(Screen screen);
    abstract protected void update(long timeDelta);
    abstract protected void receiveInput(KeyStroke keyStroke);
}
