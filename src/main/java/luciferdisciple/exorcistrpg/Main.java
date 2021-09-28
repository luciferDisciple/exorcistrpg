/*
 * The MIT License
 *
 * Copyright 2021 Lucifer Disciple <piotr.momot420@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package luciferdisciple.exorcistrpg;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;

/**
 *
 * @author Lucifer Disciple <piotr.momot420@gmail.com>
 */
public class Main {

    public static void main(String[] args) {
        Screen screen = null;

        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.setCursorPosition(null); // hide the cursor
            screen.startScreen();
            screen.refresh();
            boolean exitRequested = false;
            Game game = new Game(screen);
            game.draw();
            screen.refresh();
            
            // game loop begining
            while (!exitRequested) {
                KeyStroke key = screen.pollInput();
                if (key == null)
                    continue;
                else if (key.getKeyType() == KeyType.Escape)
                    exitRequested = true;
                else
                    game.handleInput(key);
                    game.draw();
                    screen.refresh();
            }
            // game loop end
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (screen != null) {
                try {
                    screen.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void putStringCentered(TextGraphics textGraphics, String s) {
        TerminalSize termSize = textGraphics.getSize();
        int rows = termSize.getRows();
        int cols = termSize.getColumns();
        int row = rows / 2;
        int col = (cols - s.length()) / 2;
        textGraphics.putString(col, row, s);
    }
}

interface GameState {
    public void draw();
    public void handleInput(KeyStroke key);
    public GameState getNextState();
}

class MainMenu implements GameState {

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

class Game {
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
