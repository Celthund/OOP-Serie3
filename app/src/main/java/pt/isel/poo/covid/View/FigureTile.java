package pt.isel.poo.covid.View;

import android.graphics.Canvas;
import android.graphics.Paint;

import pt.isel.poo.tile.Img;
import pt.isel.poo.tile.Tile;

public abstract class FigureTile implements Tile {
    Paint paint = new Paint();
    Img figureImg;

    @Override
    public void draw(Canvas canvas, int side) {
        figureImg.draw(canvas, side,side, paint);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
