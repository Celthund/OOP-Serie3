package pt.isel.poo.covid;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import pt.isel.poo.covid.View.GameView;
import pt.isel.poo.covid.model.GameModel;
import pt.isel.poo.tile.TilePanel;

public class MainActivity extends Activity {
    final int MOVE_PERIOD = 1000;
    GameModel gameModel;
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TilePanel tilePanel = (TilePanel)(findViewById(R.id.panel));
        AssetManager assets = getResources().getAssets();
        gameView = new GameView(this, tilePanel);
        gameModel = new GameModel(gameView, assets);
        gameModel.update();
        findViewById(R.id.leftArrow).setOnClickListener(v -> gameModel.movePlayer(GameModel.Direction.LEFT));
        findViewById(R.id.rightArrow).setOnClickListener(v -> gameModel.movePlayer(GameModel.Direction.RIGHT));
        findViewById(R.id.saveButton).setOnClickListener(v->{
            try (PrintWriter to = new PrintWriter(openFileOutput("level.txt", MODE_PRIVATE))){
                gameModel.save(to);
            } catch (IOException exc){
                Log.e("SaveButton", "Error trying to open the file.");
            }
        });
        findViewById(R.id.loadButton).setOnClickListener(v->{
            try (Scanner from = new Scanner(openFileInput("level.txt"))){
                gameModel.load(from);
            } catch (IOException exc){
                Log.e("SaveButton", "Error trying to open the file.");
            }
        });



        tilePanel.setHeartbeatListener(MOVE_PERIOD, (beat, time) -> {
            if (!gameModel.isPlayerAlive())
                gameView.setPlayerDead(false);
            if (gameModel.isGameOver)
                MainActivity.this.finish();
            gameModel.onBeat();
            TextView level = findViewById(R.id.levelNumber);
            level.setText(String.valueOf(gameModel.getNumberLevel()));
            TextView virusCounter = findViewById(R.id.virusCounter);
            virusCounter.setText(String.valueOf(gameModel.getVirusCounter()));
        });
    }

}
