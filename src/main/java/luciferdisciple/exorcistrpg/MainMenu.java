package luciferdisciple.exorcistrpg;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import java.util.function.Function;

/**
 *
 * @author Lucifer Disciple <piotr.momot420@gmail.com>
 */
public class MainMenu implements GameState {
    
    private final int ITEM_VERTICAL_OFFSET = 3;
    
    private final Game context;
    private final Screen screen;
    private int currentItemIndex = 0;    
    private MainMenuItem[] menuItems;
    
    public MainMenu(Game context, Screen screen) {
        this.screen = screen;
        this.context = context;
        this.currentItemIndex = 0;
        this.menuItems = new MainMenuItem[]{
            new MainMenuItem("Nowa Gra", () -> this),
            new MainMenuItem("Jak Grać?", () -> this),
            new MainMenuItem("Wyjście", () -> new ExitGame(context))
        };
    }
    
    @Override
    public void draw() {
        screen.clear();
        TerminalPosition cursorPosition = new TerminalPosition(10, 5);
        TextGraphics textGraphics = screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.RED);
        
        MainMenuItem currentItem = getCurrentItem();
        for (MainMenuItem item : menuItems) {
            String label = " " + item.getLabel() + " ";
            if (item == currentItem) {
                textGraphics.putString(
                    cursorPosition,
                    label,
                    SGR.REVERSE,
                    SGR.BOLD
                );
            }
            else {
                textGraphics.putString(cursorPosition, label, SGR.BOLD);
            }
            cursorPosition = cursorPosition.withRelativeRow(ITEM_VERTICAL_OFFSET);
        }
    }

    @Override
    public GameState handleInput(KeyStroke key) {
        GameState nextGameState = this;
        switch (key.getKeyType()) {
            case ArrowDown:
                selectNextItem();
                break;
            case ArrowUp:
                selectPreviousItem();
                break;
            case Enter:
                nextGameState = getCurrentItem().confirm();
                break;
            default:
                break;
        }
        return nextGameState;
    }
    
    private MainMenuItem getCurrentItem() {
        return this.menuItems[this.currentItemIndex];
    }
    
    private void selectNextItem() {
        int lastIndex = this.menuItems.length - 1;
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

@FunctionalInterface
interface Supplier<T> {
    T get();
}

class MainMenuItem {
    private final String label;
    private final Supplier<GameState> gameStateSupplier;
    
    public MainMenuItem(String label, Supplier<GameState> gameStateSupplier) {
        this.label = label;
        this.gameStateSupplier = gameStateSupplier;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public GameState confirm() {
        return this.gameStateSupplier.get();
    }
}
