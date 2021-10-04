package luciferdisciple.exorcistrpg;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author Lucifer Disciple <piotr.momot420@gmail.com>
 */
public class GameLevelWindow extends GameWindow {

    private LevelElement[][] world;
    private final PlayerCharacter player = new PlayerCharacter(1, 1);
    
    
    public GameLevelWindow(Game game) {
        super(game);
        generateWorld();
    }

    @Override
    public void draw(Screen screen) {        
        TerminalSize viewportSize = screen.getTerminalSize();
        Point playerWorldPosition = new Point(this.player.getColumn(), this.player.getRow());
        Rectangle viewport = new Rectangle(viewportSize.getColumns(), viewportSize.getRows());
        viewport = newRectangleCenteredAtPoint(viewport, playerWorldPosition);
        
        // don't let the viewport (camera) move past the left or top world
        // boundry:
        if (viewport.getX() < 0)
            viewport.x = 0;
        if (viewport.getY() < 0)
            viewport.y = 0;
        
        Point worldBottomRightVertex = new Point(
            world[0].length,
            world.length
        );
        
        // don't let the viewport (camera) move past the right or bottom world
        // boundry:
        if (viewport.getMaxX() > worldBottomRightVertex.x) {
            viewport.translate(worldBottomRightVertex.x - (int) viewport.getMaxX(), 0);
        }
        if (viewport.getMaxY() > worldBottomRightVertex.y) {
            viewport.translate(0, worldBottomRightVertex.y - (int) viewport.getMaxY());
        }
        
        // draw the world:
        TextGraphics staticLevelElementGraphics = screen.newTextGraphics();
        
        for (int row = 0; row < viewport.height; row++) {
            for (int col = 0; col < viewport.width; col++) {
                Point worldLocation = worldCoordsFromScreenCoords(viewport, new Point(col, row));
                staticLevelElementGraphics.putString(
                    col,
                    row,
                    this.world[worldLocation.y][worldLocation.x].getGlyph()
                );
            }
        }
        
        // draw the player character:
        
        TextGraphics playerCharacterStyle = screen.newTextGraphics()
            .setForegroundColor(TextColor.ANSI.GREEN_BRIGHT)
            .enableModifiers(SGR.BOLD);
        
        Point playerScreenPosition = screenCoordsFromWorldCoords(viewport, playerWorldPosition);
        playerCharacterStyle.putString(
            playerScreenPosition.x,
            playerScreenPosition.y,
            this.player.getGlyph()
        );
    }

    @Override
    public void update(long timeDelta) {
        
    }

    @Override
    public void receiveInput(KeyStroke keyStroke) {
        switch(keyStroke.getKeyType()) {
            case Escape:
                closeThisWindow();
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
    
    private Rectangle newRectangleCenteredAtPoint(Rectangle srcRectangle, Point dstCenter) {
        Rectangle dstRectangle = new Rectangle(srcRectangle);
        dstRectangle.translate(
            dstCenter.x - (int) srcRectangle.getCenterX(),
            dstCenter.y - (int) srcRectangle.getCenterY()
        );
        return dstRectangle;
    }
    
    private Point screenCoordsFromWorldCoords(Rectangle viewport, Point worldPoint) {
        return new Point(worldPoint.x - viewport.x, worldPoint.y - viewport.y);
    }
    
    private Point worldCoordsFromScreenCoords(Rectangle viewport, Point screenPoint) {
        return new Point(screenPoint.x + viewport.x, screenPoint.y + viewport.y);
    }
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
