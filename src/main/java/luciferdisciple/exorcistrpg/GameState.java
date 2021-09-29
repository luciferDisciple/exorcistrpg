package luciferdisciple.exorcistrpg;

import com.googlecode.lanterna.input.KeyStroke;

/**
 *
 * @author Lucifer Disciple <piotr.momot420@gmail.com>
 */
public interface GameState {
    public void draw();
    public GameState handleInput(KeyStroke key);
}
