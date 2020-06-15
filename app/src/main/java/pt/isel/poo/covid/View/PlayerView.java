package pt.isel.poo.covid.View;

import android.content.Context;
import android.graphics.Canvas;

import pt.isel.poo.covid.R;
import pt.isel.poo.tile.Img;

public class PlayerView extends FigureTile {
    public boolean playerAlive = true;
    private Img deadPlayer;

    PlayerView(Context ctx) {
        figureImg = new Img(ctx,R.drawable.nurse);
        deadPlayer = new Img(ctx,R.drawable.dead);
    }

    @Override
    public void draw(Canvas canvas, int side) {
        figureImg.draw(canvas, side,side, paint);
        if(!playerAlive)
            deadPlayer.draw(canvas, side,side, paint);
    }
}
