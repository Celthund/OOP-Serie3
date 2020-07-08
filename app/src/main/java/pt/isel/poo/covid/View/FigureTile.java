package pt.isel.poo.covid.View;

import android.graphics.Canvas;
import android.graphics.Paint;

import pt.isel.poo.tile.Img;
import pt.isel.poo.tile.Tile;

/**
 * Base class for every Figure in view.
 */
public abstract class FigureTile implements Tile {
    Paint paint = new Paint();
    Img figureImg;

    /**
     * Handles the drawing of the figure.
     * @param canvas To draw the tile
     * @param side   The width of tile in pixels
     */
    @Override
    public void draw(Canvas canvas, int side) {
        figureImg.draw(canvas, side,side, paint);
    }

    /**
     * Makes the Tile not selectable.
     */
    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
