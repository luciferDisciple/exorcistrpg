package luciferdisciple.exorcistrpg;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

/**
 *
 * @author Lucifer Disciple <piotr.momot420@gmail.com>
 */
public class DungeonCrawlerGameLevel extends InteractiveApplication {

    private LevelElement[][] world;
    private final PlayerCharacter player = new PlayerCharacter(1, 1);
    
    
    public DungeonCrawlerGameLevel(Terminal terminal) {
        super(terminal);
        generateWorld();
    }

    @Override
    protected void initialize() {
        
    }

    @Override
    protected void draw(Screen screen) {
        TextGraphics staticLevelElementGraphics = screen.newTextGraphics();
        TerminalSize viewportSize = screen.getTerminalSize();
        for (int row = 0; row < viewportSize.getRows(); row++) {
            for (int col = 0; col < viewportSize.getColumns(); col++) {
                staticLevelElementGraphics.putString(col, row, this.world[row][col].getGlyph());
            }
        }
        
        TextGraphics playerCharacterStyle = screen.newTextGraphics()
            .setForegroundColor(TextColor.ANSI.GREEN_BRIGHT)
            .enableModifiers(SGR.BOLD);
        
        playerCharacterStyle.putString(this.player.getColumn(),
            this.player.getRow(), this.player.getGlyph());
    }

    @Override
    protected void update(long timeDelta) {
        
    }

    @Override
    protected void receiveInput(KeyStroke keyStroke) {
        switch(keyStroke.getKeyType()) {
            case Escape:
                stop();
                break;
            case ArrowUp:
                this.player.moveUp();
                break;
            case ArrowDown:
                this.player.moveDown();
                break;
            case ArrowLeft:
                this.player.moveLeft();
                break;
            case ArrowRight:
                this.player.moveRight();
                break;
            default:
                break;
        }
    }
    
    private void generateWorld() {
        int world_height = this.worldEncodedAsTextLines.length;
        int world_width  = this.worldEncodedAsTextLines[0].length();
        this.world = new LevelElement[world_height][world_width];
        for (int row = 0; row < world_height; row++) {
            // https://stackoverflow.com/a/63204504/13168106
            String[] glyphs = worldEncodedAsTextLines[row].split("(?<=.)");
            for (int col = 0; col < world_width; col++) {
                this.world[row][col] = LevelElement.fromGlyph(glyphs[col]);
            }
        }
    }
    

    final private String[] worldEncodedAsTextLines = {
	"┏━━━━━━━━━━━━┓                                                                        ┏━━━━━━━━━━━━┓",
	"┃............┃                                                                        ┃............┃",
	"┃............┃                                                                        ┃............┃",
	"┃............┃                                                                        ┃............┃",
	"┃............┃                                                                        ┃............┃",
	"┗━━━━┓..┏━━━━┛                                                                        ┗━━━━┓..┏━━━━┛",
	"     ┃..┃                                                                                  ┃..┃     ",
	"┏━━━━┛..┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛..┃     ",
	"┃.............................................................................................┃     ",
	"┃......................................┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛     ",
	"┃......................................┃                                                            ",
	"┃......................................┃                                                            ",
	"┃......................................┃                                                            ",
	"┃......................................┃                                                            ",
	"┃......................................┃                                                            ",
	"┗━━━━━━━━━━━━━━━━━━━━━━━━━━━┓..┏━━━━━━━┛                                                            ",
	"                            ┃..┃                                                                    ",
	"┏━━━━━━━━━━━━━━━━━━━━━━━━━━━┛..┗━━━━━━━┓                                                            ",
	"┃......................................┃                                                            ",
	"┃..┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓..┃                                                            ",
	"┃..┃                                ┃..┃                                                            ",
	"┃..┃  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━┓  ┃..┃                                                            ",
	"┃..┃  ┃..........................┃  ┃..┃                                                            ",
	"┃..┃  ┃..........................┃  ┃..┃                                                            ",
	"┃..┃  ┃..........................┃  ┃..┃                                                            ",
	"┃..┗━━┛..........................┗━━┛..┃                                                            ",
	"┃......................................┃                                                            ",
	"┃..┏━━┓..........................┏━━┓..┃                                                            ",
	"┃..┃  ┃..........................┃  ┃..┃                                                            ",
	"┃..┃  ┃..........................┃  ┃..┃                                                            ",
	"┃..┃  ┗━━━━━━━━━━━┓..┏━━━━━━━━━━━┛  ┃..┃                                                            ",
	"┃..┃              ┃  ┃              ┃..┃                                                            ",
	"┃..┗━━━━━━━━━━━━━━┛..┗━━━━━━━━━━━━━━┛..┃                                                            ",
	"┃......................................┃                                                            ",
	"┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛                                                            "
    };
}

class LevelElement {  
    
    private static final LevelElement wallVertical = new LevelElement("┃");
    private static final LevelElement wallHorizontal = new LevelElement("━");
    private static final LevelElement wallLeftUp = new LevelElement("┛");
    private static final LevelElement wallRightUp = new LevelElement("┗");
    private static final LevelElement wallLeftDown = new LevelElement("┓");
    private static final LevelElement wallRightDown = new LevelElement("┏");
    private static final LevelElement ground = new LevelElement(".");
    private static final LevelElement emptySpace = new LevelElement(" ");
    private static final LevelElement unknown = new LevelElement("?");
    
    private final String glyph;
    
    public static LevelElement fromGlyph(String glyph) {
        if (glyph.equals("┃")) return wallVertical;
        if (glyph.equals("━")) return wallHorizontal;
        if (glyph.equals("┛")) return wallLeftUp;
        if (glyph.equals("┗")) return wallRightUp;
        if (glyph.equals("┓")) return wallLeftDown;
        if (glyph.equals("┏")) return wallRightDown;
        if (glyph.equals(".")) return ground;
        if (glyph.equals(" ")) return emptySpace;
        return unknown;
    }
    
    private LevelElement(String glyph) {
        this.glyph = glyph;
    }
    
    public String getGlyph() {
        return this.glyph;
    }
}

class PlayerCharacter {
    
    private int row;
    private int col;
    
    public PlayerCharacter(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public String getGlyph() {return "@";}
    public int getColumn() {return this.col;}
    public int getRow() {return this.row;}
    public void moveUp() {this.row--;}
    public void moveDown() {this.row++;}
    public void moveLeft() {this.col--;}
    public void moveRight() {this.col++;}
}
