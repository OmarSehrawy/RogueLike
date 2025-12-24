package RogueLike;

import java.awt.*;

public enum Tile {
    WALL('#',Color.WHITE),
    FLOOR('.',Color.GRAY),
    STAIRS('^',Color.YELLOW);
    public final char glyph;
    public final Color color;
    Tile(char glyph,Color color) {
        this.glyph = glyph;
        this.color = color;
    }
}