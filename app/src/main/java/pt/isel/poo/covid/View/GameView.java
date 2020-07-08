package pt.isel.poo.covid.View;

import android.content.Context;

import java.util.HashMap;

import pt.isel.poo.covid.model.Floor;
import pt.isel.poo.covid.model.Level;
import pt.isel.poo.covid.model.ModelListener;
import pt.isel.poo.covid.model.Player;
import pt.isel.poo.covid.model.Trash;
import pt.isel.poo.covid.model.Virus;
import pt.isel.poo.tile.Tile;
import pt.isel.poo.tile.TilePanel;

public class GameView implements ModelListener {
    TilePanel tilePanel;
    HashMap<Class<?>, Tile> modelToView;

    public GameView(Context ctx, TilePanel tilePanel) {
        this.tilePanel = tilePanel;
        modelToView = new HashMap<>();
        modelToView.put(Player.class, new PlayerView(ctx));
        modelToView.put(Floor.class, new FloorView(ctx));
        modelToView.put(Trash.class, new TrashView(ctx));
        modelToView.put(Virus.class, new VirusView(ctx));
    }

    public void setPlayerDead(boolean playerDead){
        PlayerView playerView = (PlayerView) modelToView.get(Player.class);
        if (playerView != null)
            playerView.playerAlive = playerDead;
    }


    /**
     * Updates the current view with level received.
     * @param level Level to be used as updated.
     */
    @Override
    public void update(Level level) {
        tilePanel.setSize(level.getColumn(), level.getLine());
        for (int i = 0; i < level.getLine(); i++) {
            for (int j = 0; j < level.getColumn(); j++) {
                if (level.get(i, j) == null) {
                    tilePanel.setTile(j, i, modelToView.get(null));
                } else {
                    tilePanel.setTile(j, i, modelToView.get(level.get(i, j).getClass()));
                }

            }
        }
    }


}
