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


    /**
     * Handles the creation of the Activity.
     * Create gameModel and gameView and adds functionality to the buttons.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TilePanel tilePanel = findViewById(R.id.panel);
        AssetManager assets = getResources().getAssets();
        gameView = new GameView(this, tilePanel);
        gameModel = new GameModel(gameView, assets);
        if (savedInstanceState != null && savedInstanceState.getBoolean("SavedState")) {
            loadGameLevel("tmp.txt");
        } else {
            gameModel.loadLevelFromAssets(1);
        }
        gameModel.update();
        findViewById(R.id.leftArrow).setOnClickListener(v -> gameModel.movePlayer(GameModel.Direction.LEFT));
        findViewById(R.id.rightArrow).setOnClickListener(v -> gameModel.movePlayer(GameModel.Direction.RIGHT));
        findViewById(R.id.saveButton).setOnClickListener(v->saveGameModel("level.txt"));
        findViewById(R.id.loadButton).setOnClickListener(v->loadGameLevel("level.txt"));
        tilePanel.setHeartbeatListener(MOVE_PERIOD, (beat, time) -> {
            if (!gameModel.isPlayerAlive())
                gameView.setPlayerDead(false);
            if (gameModel.isGameOver)
                MainActivity.this.finish();
            gameModel.updateIfLevelIsFinished();
            TextView level = findViewById(R.id.levelNumber);
            level.setText(String.valueOf(gameModel.getNumberLevel()));
            TextView virusCounter = findViewById(R.id.virusCounter);
            virusCounter.setText(String.valueOf(gameModel.getVirusCounter()));
        });
    }

    /**
     * Save the current Game Model to a file.
     * @param file name of the file to be saved.
     */
    private void saveGameModel(String file) {
        try (PrintWriter to = new PrintWriter(openFileOutput(file, MODE_PRIVATE))){
            gameModel.save(to);
        } catch (IOException exc){
            Log.e("SaveButton", "Error trying to open the file.");
        }
    }

    /**
     * Load the level from a file.
     * @param file name of the file to be loaded.
     */
    private void loadGameLevel(String file){
        try (Scanner from = new Scanner(openFileInput(file))){
            gameModel.loadLevel(from);
        } catch (IOException exc){
            Log.e("SaveButton", "Error trying to open the file.");
        }
    }

    /**
     * Handles the save of the current state.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        saveGameModel("tmp.txt");
        savedInstanceState.putBoolean("SavedState", true);
    }

}
