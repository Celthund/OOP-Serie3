package pt.isel.poo.covid.View;

import android.content.Context;

import pt.isel.poo.covid.R;
import pt.isel.poo.tile.Img;

public class WallView extends FigureTile {
    WallView(Context ctx) {
        figureImg = new Img(ctx,R.drawable.wall);
    }
}
