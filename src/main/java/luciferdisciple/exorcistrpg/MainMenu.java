package luciferdisciple.exorcistrpg;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import static java.lang.Math.max;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lucifer Disciple <piotr.momot420@gmail.com>
 */
public class MainMenu extends InteractiveApplication {
    
    private final int ITEM_VERTICAL_GAP = 2;
    private final int TITLE_BOTTOM_MARGIN = 4;
    private final String screenTitle = "EGZORCYSTA";
    
    private int currentItemIndex = 0;    
    private MainMenuItem[] menuItems;
    
    public MainMenu(Terminal terminal) {
        super(terminal);
        this.menuItems = new MainMenuItem[]{
            new MainMenuItem("Nowa Gra", this::noOp),
            new MainMenuItem("Jak Grać?", this::noOp),
            new MainMenuItem("Wyjście", super::stop)
        };
    }

    @Override
    protected void initialize() {
        
    }

    @Override
    protected void update(long timeDelta) {
        // It updates only when it receives keyboard input.
        // It uoesn't care about passing time
    }
    
    @Override
    protected void draw(Screen screen) {
        Rectangle termViewport = new Rectangle(0, 0,
            new RectangleDimensions(
                screen.getTerminalSize().getColumns(),
                screen.getTerminalSize().getRows())
        );
        RectangleDimensions menuWindowDimensions = getMenuBoundingBoxDimensions();
        Rectangle menuWindowBoundingBox =
            new Rectangle(menuWindowDimensions)
                .centeredIn(termViewport);
        
        TerminalPosition cursor = new TerminalPosition(
                menuWindowBoundingBox.col(),
                menuWindowBoundingBox.row()
        );
        
        TextGraphics screeTitleTextStyle = screen.newTextGraphics()
            .setForegroundColor(TextColor.ANSI.BLUE_BRIGHT)
            .enableModifiers(SGR.BOLD);
        
        TextGraphics menuItemTextStyle = screen.newTextGraphics()
            .setForegroundColor(TextColor.ANSI.RED)
            .enableModifiers(SGR.BOLD);
        
        TextGraphics highlightedMenuItemTextStyle = screen.newTextGraphics()
            .setForegroundColor(TextColor.ANSI.RED)
            .enableModifiers(SGR.REVERSE, SGR.BOLD);
        
        putLineCenteredHorizontaly(screeTitleTextStyle, cursor, termViewport, screenTitle);
        cursor = cursor.withRelativeRow(1 + TITLE_BOTTOM_MARGIN);
        
        MainMenuItem currentItem = getCurrentItem();
        for (MainMenuItem item : this.menuItems) {
            String label = " " + item.getLabel() + " ";
            TextGraphics style = menuItemTextStyle;
            if (item == currentItem)
                style = highlightedMenuItemTextStyle;
            putLineCenteredHorizontaly(style, cursor, termViewport, label);
            cursor = cursor.withRelativeRow(ITEM_VERTICAL_GAP + 1);
        }
    }

    @Override
    protected void receiveInput(KeyStroke key) {
        switch (key.getKeyType()) {
            case ArrowDown:
                highlightNextMenuItem();
                break;
            case ArrowUp:
                highlightPreviousMainMenuItem();
                break;
            case Enter:
                getCurrentItem().executeAction();
                break;
            default:
                break;
        }
    }
    
    private MainMenuItem getCurrentItem() {
        return this.menuItems[this.currentItemIndex];
    }
    
    private void highlightNextMenuItem() {
        int lastIndex = this.menuItems.length - 1;
        if (this.currentItemIndex == lastIndex)
            return;
        this.currentItemIndex++;
    }
    
    private void highlightPreviousMainMenuItem() {
        if (this.currentItemIndex == 0)
            return;
        this.currentItemIndex--;
    }
    
    private RectangleDimensions cachedMenuBoundingBoxDimensions;
    private RectangleDimensions getMenuBoundingBoxDimensions() {
        if (this.cachedMenuBoundingBoxDimensions == null)
            this.cachedMenuBoundingBoxDimensions = computeMenuBoundingBoxDimensions();
        return this.cachedMenuBoundingBoxDimensions;
    }
    
    private RectangleDimensions computeMenuBoundingBoxDimensions() {
        List<String> lines = new ArrayList<String>();
        lines.add(this.screenTitle);
        for (int i = 0; i < TITLE_BOTTOM_MARGIN; ++i)
            lines.add("");
        
        lines.add(this.menuItems[0].getLabel());
        for (int i = 1; i < this.menuItems.length; i++) {
            for (int j = 0; j < ITEM_VERTICAL_GAP; j++)
                lines.add("");
            lines.add(this.menuItems[i].getLabel());
        }
        
        int longestLineLength = 0;
        for (var line : lines)
            longestLineLength = max(line.length(), longestLineLength);
        
        return new RectangleDimensions(longestLineLength, lines.size());
    }
    
    private void putLineCenteredHorizontaly(TextGraphics textGraphics, TerminalPosition cursor, Rectangle container, String text)
    {
        int startingColumn = (container.dimensions().width() -  text.length()) / 2;
        textGraphics.putString(cursor.withColumn(startingColumn), text);
    }
    
    private void noOp() {
        // no operation
    }
}

class MainMenuItem {
    private final String label;
    private final Runnable callback;
    
    public MainMenuItem(String label, Runnable callback) {
        this.label = label;
        this.callback = callback;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void executeAction() {
        this.callback.run();
    }
}

record RectangleDimensions(int width, int height) {}

record Rectangle(int row, int col, RectangleDimensions dimensions) {
    
    public Rectangle(RectangleDimensions dimensions) {
        this(0, 0, dimensions);
    }
    
    public Rectangle centeredIn(Rectangle container) {
        return new Rectangle(
            (container.dimensions.height() - this.dimensions.height()) / 2,
            (container.dimensions.width() - this.dimensions.width()) / 2,
            this.dimensions);
    }
}
