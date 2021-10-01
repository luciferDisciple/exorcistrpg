package luciferdisciple.exorcistrpg;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
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
    
    public InteractiveApplication(Screen screen) {
        this.screen = screen;
    }
    
    public void run() throws IOException {
        initialize();
        this.timeOfLastUpdateInMiliseconds = System.currentTimeMillis();
        while (!this.isOver) {
            long timeDelta = System.currentTimeMillis() - this.timeOfLastUpdateInMiliseconds;
            KeyStroke inputKey = screen.pollInput();
            if (inputKey != null)
                receiveInput(inputKey);
            if (timeDelta < FRAMETIME)
                continue;
            update(timeDelta);
            draw(this.screen);
            screen.refresh();
            this.timeOfLastUpdateInMiliseconds = System.currentTimeMillis();
        }
    }
    
    protected final void stop() {
        this.isOver = true;
    }
    
    abstract protected void initialize();
    abstract protected void draw(Screen screen);
    abstract protected void update(long timeDelta);
    abstract protected void receiveInput(KeyStroke keyStroke);
}
