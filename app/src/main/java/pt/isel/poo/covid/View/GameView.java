package pt.isel.poo.covid.View;

import android.content.Context;

import java.util.HashMap;

import pt.isel.poo.covid.model.Element;
import pt.isel.poo.covid.model.ModelListener;
import pt.isel.poo.tile.Tile;
import pt.isel.poo.tile.TilePanel;

public class GameView implements ModelListener {
    TilePanel tilePanel;
    HashMap<Class, Tile> modelToView;

    public GameView(Context ctx) {
        tilePanel = new TilePanel(ctx);
        modelToView = new HashMap<>();
        modelToView.put(GameView.class, new PlayerView(ctx));
    }

    @Override
    public void update(int x, int y, Element e) {
        if (e == null) {
            tilePanel.setTile(x, y, null);
        } else {
            tilePanel.setTile(x, y, modelToView.get(e.getClass()));
        }
    }


}
