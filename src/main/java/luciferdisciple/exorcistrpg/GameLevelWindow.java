package luciferdisciple.exorcistrpg;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;

/**
 *
 * @author Lucifer Disciple <piotr.momot420@gmail.com>
 */
public class GameLevelWindow extends GameWindow {

    private final World world;
    private final PlayerCharacter player = new PlayerCharacter(1, 23);
    
    public GameLevelWindow(Game game) {
        super(game);
        this.world = new World(this.worldEncodedAsTextLines);
    }

    @Override
    public void draw(Screen screen) {
        Rectangle screenRectangle = new Rectangle(
            screen.getTerminalSize().getColumns(),
            screen.getTerminalSize().getRows()
        );
        Point playerWorldPosition = new Point(this.player.getColumn(), this.player.getRow());
        Rectangle viewport = new Rectangle(screenRectangle);
        viewport = newRectangleCenteredAtPoint(viewport, playerWorldPosition);
        
        // don't let the viewport (camera) move past the left or top world
        // boundry:
        if (viewport.getX() < 0)
            viewport.x = 0;
        if (viewport.getY() < 0)
            viewport.y = 0;
        
        Point worldBottomRightVertex = new Point(
            this.world.getWidth(),
            this.world.getHeight()
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
        for (Point screenLocation : allPoints(screenRectangle)) {
            Point worldLocation = worldCoordsFromScreenCoords(viewport, screenLocation);
            staticLevelElementGraphics.putString(
                screenLocation.x,
                screenLocation.y,
                this.world.getLevelElement(worldLocation).getGlyph()
            );
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
    
    private Iterable<Point> allPoints(Rectangle rectangle) {
        return new Iterable<Point>() {
            @Override
            public Iterator<Point> iterator() {
                return new Iterator<Point>() {
                        private int currentX, currentY, destinationX, destinationY;

                        {
                            currentX = rectangle.x;
                            currentY = rectangle.y;
                            destinationX = (int) rectangle.getMaxX();
                            destinationY = (int) rectangle.getMaxY();
                        }

                        @Override
                        public boolean hasNext() {
                            return currentY < destinationY;
                        }

                        @Override
                        public Point next() {
                            Point currentPoint = new Point(currentX, currentY);
                            currentX++;
                            if (currentX > destinationX)
                                moveToNextRow();
                            return currentPoint;
                        }

                        private void moveToNextRow() {
                            currentY++;
                            currentX = rectangle.x;
                        }
                    };
                }
            };
    }
}
        
class LevelElement {  
    
    public static final LevelElement EMPTY = new LevelElement(" ");
    
    private static final LevelElement wallVertical = new LevelElement("┃");
    private static final LevelElement wallHorizontal = new LevelElement("━");
    private static final LevelElement wallLeftUp = new LevelElement("┛");
    private static final LevelElement wallRightUp = new LevelElement("┗");
    private static final LevelElement wallLeftDown = new LevelElement("┓");
    private static final LevelElement wallRightDown = new LevelElement("┏");
    private static final LevelElement ground = new LevelElement(".");
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
        if (glyph.equals(" ")) return EMPTY;
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
    
    public PlayerCharacter(int col, int row) {
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

class World {
    
    private LevelElement[][] staticLevelElements;
    
    public World(String[] worldEncodedAsLinesOfText) {
        this.staticLevelElements = decodeWorld(worldEncodedAsLinesOfText);
    }
    
    public LevelElement getLevelElement(Point location) {
        return getLevelElement(location.x, location.y);
    }
    
    public LevelElement getLevelElement(int x, int y) {
        boolean indexOutOfBounds = (x >= getWidth()) || (y >= getHeight());
        if (indexOutOfBounds)
            return LevelElement.EMPTY;
        LevelElement arrayItemAtRequestedIndex = this.staticLevelElements[x][y];
        if (arrayItemAtRequestedIndex == null)
            return LevelElement.EMPTY;
        return arrayItemAtRequestedIndex;
    }
    
    public int getWidth() {
        return this.staticLevelElements.length;
    }
    
    public int getHeight() {
        return this.staticLevelElements[0].length;
    }
    
    private LevelElement[][] decodeWorld(String[] worldEncodedAsLinesOfText) {
        int linesCount = worldEncodedAsLinesOfText.length;
        int lengthOfLongestLine = worldEncodedAsLinesOfText[0].length();
        for (String line : worldEncodedAsLinesOfText) {
            if (line.length() > lengthOfLongestLine)
                lengthOfLongestLine = line.length();
        }
        
        int width  = lengthOfLongestLine;
        int height = linesCount;
        
        LevelElement[][] decodedStaticLevelElements = new LevelElement[width][height];
        
        for (int lineNumber = 0; lineNumber < linesCount; lineNumber++) {
            String line = worldEncodedAsLinesOfText[lineNumber];
            // https://stackoverflow.com/a/63204504/13168106
            String[] glyphs = line.split("(?<=.)");
            for (int col = 0; col < glyphs.length; col++) {
                decodedStaticLevelElements[col][lineNumber] = LevelElement.fromGlyph(glyphs[col]);
            }
        }
        
        return decodedStaticLevelElements;
    }
}
