package luciferdisciple.exorcistrpg;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

/**
 *
 * @author Lucifer Disciple <piotr.momot420@gmail.com>
 */
public class MainMenu implements GameState {
    
    enum MainMenuItem {
        NewGame("New Game"),
        Controls("Controls"),
        Quit("Quit");
        
        private final String name;
        
        MainMenuItem(String name) {
            this.name = name;
        }
        
        public String nameToDisplay() {
            return " " + this.name + " ";
        }
    }
    
    private int currentItemIndex;
    private final Screen screen;
    private final int ITEM_VERTICAL_OFFSET = 3;
    
    public MainMenu(Screen screen) {
        this.screen = screen;
        this.currentItemIndex = 0;
    }
    
    @Override
    public void draw() {
        screen.clear();
        TerminalPosition cursorPosition = new TerminalPosition(10, 5);
        TextGraphics textGraphics = screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.RED);
        
        MainMenuItem currentItem = getCurrentItem();
        for (MainMenuItem item : MainMenuItem.values()) {
            if (item == currentItem) {
                textGraphics.putString(
                    cursorPosition,
                    item.nameToDisplay(),
                    SGR.REVERSE,
                    SGR.BOLD
                );
            }
            else {
                textGraphics.putString(cursorPosition, item.nameToDisplay(), SGR.BOLD);
            }
            cursorPosition = cursorPosition.withRelativeRow(ITEM_VERTICAL_OFFSET);
        }
    }

    @Override
    public void handleInput(KeyStroke key) {
        switch (key.getKeyType()) {
            case ArrowDown:
                selectNextItem();
                break;
            case ArrowUp:
                selectPreviousItem();
                break;
            default:
                break;
        }
    }

    @Override
    public GameState getNextState() {
        return this;
    }
    
    private MainMenuItem getCurrentItem() {
        return MainMenuItem.values()[this.currentItemIndex];
    }
    
    private void selectNextItem() {
        int lastIndex = MainMenuItem.values().length - 1;
        if (this.currentItemIndex == lastIndex)
            return;
        this.currentItemIndex++;
    }
    
    private void selectPreviousItem() {
        if (this.currentItemIndex == 0)
            return;
        this.currentItemIndex--;
    }
}
