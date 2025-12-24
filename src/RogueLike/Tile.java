package RogueLike;

import java.awt.*;

public enum Tile {
    WALL('#',Color.WHITE),
    FLOOR('.',Color.GRAY),
    STAIRSDOWN('+',Color.YELLOW),
    STAIRSUP('^',Color.YELLOW);
    public char glyph;
    public Color color;
    Tile(char glyph,Color color) {
        this.glyph = glyph;
        this.color = color;
    }
}