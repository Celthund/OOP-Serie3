package pt.isel.poo.covid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import pt.isel.poo.covid.View.GameView;
import pt.isel.poo.covid.model.GameModel;
import pt.isel.poo.tile.TilePanel;

public class MainActivity extends Activity {
    final int MOVE_PERIOD = 500;
    GameModel gameModel;
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TilePanel tilePanel = (TilePanel)(findViewById(R.id.panel));
        try {
            InputStream inFile = getResources().getAssets().open("levels.txt");
            Scanner in = new Scanner(inFile);
            gameView = new GameView(this, tilePanel);
            gameModel = new GameModel(gameView, in);
            gameModel.update();
        } catch (IOException e) {
            e.printStackTrace();
        }
        findViewById(R.id.leftArrow).setOnClickListener(v -> gameModel.movePlayer(GameModel.Direction.LEFT));
        findViewById(R.id.rightArrow).setOnClickListener(v -> gameModel.movePlayer(GameModel.Direction.RIGHT));


        tilePanel.setHeartbeatListener(MOVE_PERIOD, (beat, time) -> {
            TextView level = findViewById(R.id.levelNumber);
            level.setText(String.valueOf(gameModel.getNumberLevel()));
            TextView virusCounter = findViewById(R.id.virusCounter);
            virusCounter.setText(String.valueOf(gameModel.getVirusCounter()));
            if (gameModel.isGameOver)
                MainActivity.this.finish();
            gameModel.onBeat();
        });
    }

}
